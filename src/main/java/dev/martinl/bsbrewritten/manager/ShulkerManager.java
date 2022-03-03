package dev.martinl.bsbrewritten.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.BSBPermission;
import dev.martinl.bsbrewritten.util.TimeFormatter;
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

import java.util.*;

public class ShulkerManager {
    private final BSBRewritten instance;
    private HashMap<Inventory, ShulkerOpenData> openShulkerInventories = new HashMap<>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private HashMap<UUID, Long> lastOpened = new HashMap<>();


    public ShulkerManager(BSBRewritten instance) {
        this.instance = instance;
    }


    public void openShulkerBoxInventory(Player player, ItemStack shulkerStack) {

        //permission check
        if(instance.getConfigurationParser().isRequiresPermission()) {
            if(!player.hasPermission(BSBPermission.OPEN_SHULKER.toString())) {
                player.sendMessage(ChatColor.RED + "No permission");
                return;
            }
        }

        // Cooldown check
        int cooldown = getPlayerCooldown(player.getUniqueId());
        if(cooldown>0) {
            int[] formatted = TimeFormatter.formatToMinutesAndSeconds(cooldown);
            player.sendMessage(instance.getConfigurationParser().getPrefix() + instance.getConfigurationParser().getCooldownMessage()
                    .replace("%minutes%", String.valueOf(formatted[0])).replace("%seconds%", String.valueOf(formatted[1])));
            return;
        }

        // Check end

        lastOpened.put(player.getUniqueId(), System.currentTimeMillis());

        BlockStateMeta bsm = (BlockStateMeta) shulkerStack.getItemMeta();
        assert bsm != null;
        ShulkerBox shulker = (ShulkerBox) bsm.getBlockState();
        Inventory inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, formatShulkerPlaceholder(instance.getConfigurationParser().getInventoryName(), shulkerStack));
        inventory.setContents(shulker.getInventory().getContents());
        player.openInventory(inventory);
        ItemStack clone = shulkerStack.clone();
        openShulkerInventories.put(inventory, new ShulkerOpenData(clone, player.getLocation()));


        String msgToSend = formatShulkerPlaceholder(instance.getConfigurationParser().getOpenMessage(), shulkerStack);
        if(!msgToSend.isEmpty()) {
            player.sendMessage(instance.getConfigurationParser().getPrefix() + msgToSend);
        }
        Sound toPlay = instance.getConfigurationParser().getOpenSound();
        if(toPlay!=null) player.playSound(player.getLocation(), toPlay, 1f, 1f);
    }

    public ItemStack closeShulkerBox(Player player, Inventory inventory, Optional<ItemStack> useStack) {
        player.getOpenInventory().getTopInventory();
        if(!openShulkerInventories.containsKey(inventory)) return null;

        ItemStack stackClone = openShulkerInventories.get(inventory).getItemStack();
        if(useStack.isPresent()) {
            stackClone = useStack.get();
        }

        openShulkerInventories.remove(inventory);
        if(player.getOpenInventory().getTopInventory().getType() == InventoryType.SHULKER_BOX) {
            player.closeInventory();
        }



        ItemStack target = stackClone;
        boolean found = false;
        for(ItemStack is : player.getInventory().getContents()) {
            if(is!=null&&is.equals(stackClone)) {
                found = true;
                target = is;
                break;
            }
        }
        if(!found) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING! Player " + player.getName() + " closed a shulkerbox and changes were not saved!");
        }

        BlockStateMeta cMeta = (BlockStateMeta) target.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) cMeta.getBlockState();
        shulker.getInventory().setContents(inventory.getContents());
        cMeta.setBlockState(shulker);
        target.setItemMeta(cMeta);


        String msgToSend = formatShulkerPlaceholder(instance.getConfigurationParser().getCloseMessage(), target);
        if(!msgToSend.isEmpty()) {
            player.sendMessage(instance.getConfigurationParser().getPrefix() + msgToSend);
        }
        Sound toPlay = instance.getConfigurationParser().getCloseSound();
        if(toPlay!=null) player.playSound(player.getLocation(), toPlay, 1f, 1f);
        return target;
    }

    public boolean isShulkerInventory(Inventory inv) {
        return openShulkerInventories.containsKey(inv);
    }

    public boolean doesPlayerHaveShulkerOpen(UUID uuid) {
        for(Inventory inv : openShulkerInventories.keySet()) {
            for(HumanEntity he : inv.getViewers()) {
                if(he.getUniqueId().equals(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getCorrespondingStack(Inventory inv) {
        ShulkerOpenData sod = openShulkerInventories.getOrDefault(inv, null);
        if(sod==null) return null;
        return sod.getItemStack();
    }

    public ShulkerOpenData getShulkerOpenData(Inventory inv) {
        return openShulkerInventories.getOrDefault(inv, null);
    }


    public void closeAllInventories(boolean disableCall) {
        HashMap<HumanEntity, Inventory> playersToCloseInventory = new HashMap<>();
        for(Inventory inventory : openShulkerInventories.keySet()) {
            for(HumanEntity he : inventory.getViewers()) {
                playersToCloseInventory.put(he, inventory);
            }
        }
        for(Map.Entry<HumanEntity, Inventory> entry : playersToCloseInventory.entrySet()) {
            Player player = (Player) entry.getKey();
            player.closeInventory();
            if(disableCall) {
                closeShulkerBox(player, entry.getValue(), Optional.empty());
            }
        };

    }

    private int getPlayerCooldown(UUID uuid) {
        if(!lastOpened.containsKey(uuid)) return 0;
        long timePassed = System.currentTimeMillis() - lastOpened.getOrDefault(uuid, 0L);
        return (int) Math.max(0, instance.getConfigurationParser().getCooldown()-timePassed);
    }

    private String formatShulkerPlaceholder(String message, ItemStack shulker) {
        if(message.isEmpty()) return message;
        if(!message.contains("%shulker_name%")) return message;
        if(shulker==null) return message.replace("%shulker_name%", "invalid");
        if(shulker.getItemMeta()==null||!shulker.getItemMeta().hasDisplayName()) return message.replace("%shulker_name%", InventoryType.SHULKER_BOX.getDefaultTitle());
        return message.replace("%shulker_name%", shulker.getItemMeta().getDisplayName());
    }


}
