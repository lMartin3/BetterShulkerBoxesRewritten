package dev.martinl.bsbrewritten.manager;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.configuration.BSBConfig;
import dev.martinl.bsbrewritten.manager.chestsort.IChestSortManager;
import dev.martinl.bsbrewritten.util.BSBPermission;
import dev.martinl.bsbrewritten.util.TimeUtils;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ShulkerManager {
    private final BSBRewritten instance;
    private HashMap<Inventory, ShulkerOpenData> openShulkerInventories = new HashMap<>();
    private HashMap<UUID, Long> lastOpened = new HashMap<>();

    @Setter
    private IChestSortManager chestSortManager = new IChestSortManager() {
        @Override
        public void sort(Player player, Inventory shulkerInventory) {

        }

        @Override
        public void setSortable(Inventory shulkerInventory) {

        }
    };


    public ShulkerManager(BSBRewritten instance) {
        this.instance = instance;
    }


    public void openShulkerBoxInventory(Player player, ItemStack shulkerStack, SlotType slotType, int rawSlot) {
        if (instance.isLockFeatures()) return;
        BSBConfig bsbConfig = instance.getBSBConfig();

        //permission check
        if (bsbConfig.isRequiresPermission() &&
                !player.hasPermission(BSBPermission.OPEN_SHULKER.toString())) {
            bsbConfig.getNoPermissionMessage().send(player);
            return;

        }

        // Cooldown check
        int cooldown = getPlayerCooldown(player.getUniqueId());
        if (cooldown > 0 && !player.hasPermission(BSBPermission.BYPASS_COOLDOWN.toString())) {
            int[] formatted = TimeUtils.formatToMinutesAndSeconds(cooldown);
            bsbConfig.getCooldownMessage().send(player, "%minutes%", String.valueOf(formatted[0]), "%seconds%", String.valueOf(formatted[1]));
            return;
        }

        // Cursor item is not keept when opening a new inventory, keep it manually.
        ItemStack cursorItem = player.getItemOnCursor();
        // It is "keept" when closing your inventory, but not in your cursor, make sure it's not duplicated.
        player.setItemOnCursor(null);

        // close the player's current inventory if they have one open
        if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING||
                openShulkerInventories.containsKey(player.getOpenInventory().getTopInventory())) {
            player.closeInventory();
        };


        // Check end

        lastOpened.put(player.getUniqueId(), System.currentTimeMillis());

        BlockStateMeta bsm = (BlockStateMeta) shulkerStack.getItemMeta();
        assert bsm != null;
        ShulkerBox shulker = (ShulkerBox) bsm.getBlockState();
        Inventory inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, formatShulkerPlaceholder(bsbConfig.getInventoryName(), shulkerStack));
        inventory.setContents(shulker.getInventory().getContents());

        // Apply sort
        chestSortManager.sort(player, inventory);

        player.openInventory(inventory);
        // Restore the cursor item
        player.setItemOnCursor(cursorItem);
        ItemStack clone = shulkerStack.clone();
        openShulkerInventories.put(inventory, new ShulkerOpenData(clone, player.getLocation(), slotType, rawSlot));

        sendSoundAndMessage(player, shulkerStack, MessageSoundComb.OPEN);
    }

    public ItemStack closeShulkerBox(Player player, Inventory inventory, Optional<ItemStack> useStack) {
        player.getOpenInventory().getTopInventory();
        if (!openShulkerInventories.containsKey(inventory)) return null;
        ShulkerOpenData shulkerOpenData = openShulkerInventories.remove(inventory);

        ItemStack stackClone = shulkerOpenData.getItemStack();
        if (useStack.isPresent()) {
            stackClone = useStack.get();
        }

        if (player.getOpenInventory().getTopInventory().getType() == InventoryType.SHULKER_BOX) {
            player.closeInventory();
        }

        ItemStack targetItem = stackClone;
        boolean found = false;
        if (shulkerOpenData.getSlotType() == SlotType.HOTBAR) {
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is.equals(stackClone)) {
                targetItem = is;
                found = true;
            }
        } else if (shulkerOpenData.getSlotType() == SlotType.INVENTORY) {
            ItemStack is = player.getInventory().getItem(shulkerOpenData.getRawSlot());
            if (is != null && is.equals(stackClone)) {
                targetItem = is;
                found = true;
            }
        }

        //Keep as fallback
        if (!found) {
            for (ItemStack is : player.getInventory().getContents()) {
                if (is != null && is.equals(stackClone)) {
                    found = true;
                    targetItem = is;
                    break;
                }
            }
        }
        if (!found) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING! Player " + player.getName() + " closed a shulkerbox and changes were not saved!");
        }

        BlockStateMeta cMeta = (BlockStateMeta) targetItem.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) cMeta.getBlockState();
        shulker.getInventory().setContents(inventory.getContents());
        cMeta.setBlockState(shulker);
        targetItem.setItemMeta(cMeta);
        player.updateInventory();
        sendSoundAndMessage(player, targetItem, MessageSoundComb.CLOSE);
        return targetItem;
    }

    public boolean isShulkerInventory(Inventory inv) {
        return openShulkerInventories.containsKey(inv);
    }

    public boolean doesPlayerHaveShulkerOpen(UUID uuid) {
        for (Inventory inv : openShulkerInventories.keySet()) {
            for (HumanEntity he : inv.getViewers()) {
                if (he.getUniqueId().equals(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getCorrespondingStack(Inventory inv) {
        ShulkerOpenData sod = openShulkerInventories.getOrDefault(inv, null);
        if (sod == null) return null;
        return sod.getItemStack();
    }

    public ShulkerOpenData getShulkerOpenData(Inventory inv) {
        return openShulkerInventories.getOrDefault(inv, null);
    }


    public void closeAllInventories(boolean disableCall) {
        HashMap<HumanEntity, Inventory> playersToCloseInventory = new HashMap<>();
        for (Inventory inventory : openShulkerInventories.keySet()) {
            for (HumanEntity he : inventory.getViewers()) {
                playersToCloseInventory.put(he, inventory);
            }
        }
        for (Map.Entry<HumanEntity, Inventory> entry : playersToCloseInventory.entrySet()) {
            Player player = (Player) entry.getKey();
            player.closeInventory();
            if (disableCall) {
                closeShulkerBox(player, entry.getValue(), Optional.empty());
            }
        }

    }

    public int getPlayerCooldown(UUID uuid) {
        if (!lastOpened.containsKey(uuid)) return 0;
        long timePassed = System.currentTimeMillis() - lastOpened.getOrDefault(uuid, 0L);
        return (int) Math.max(0, instance.getBSBConfig().getCooldown() - timePassed);
    }

    private String getShulkerPlaceholderReplacement(ItemStack shulker) {
        if (shulker == null) return "invalid";
        if (shulker.getItemMeta() == null || !shulker.getItemMeta().hasDisplayName())
            return InventoryType.SHULKER_BOX.getDefaultTitle();
        return shulker.getItemMeta().getDisplayName();
    }
    private String formatShulkerPlaceholder(String message, ItemStack shulker) {
        if (message.isEmpty()) return message;
        if (!message.contains("%shulker_name%")) return message;
        return message.replace("%shulker_name%",getShulkerPlaceholderReplacement(shulker));
    }

    private enum MessageSoundComb {
        OPEN,
        CLOSE
    }


    private void sendSoundAndMessage(Player player, ItemStack shulker, MessageSoundComb type) {
        BSBConfig cfgp = instance.getBSBConfig();
        (type == MessageSoundComb.OPEN ? cfgp.getOpenMessage() : cfgp.getCloseMessage()).send(player, "%shulker_name%", getShulkerPlaceholderReplacement(shulker));
        Sound toPlay = (type == MessageSoundComb.OPEN ? cfgp.getOpenSound() : cfgp.getCloseSound());
        if (toPlay != null) player.playSound(player.getLocation(), toPlay, 1f, 1f);
    }


}
