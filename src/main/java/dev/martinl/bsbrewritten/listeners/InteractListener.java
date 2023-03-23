package dev.martinl.bsbrewritten.listeners;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.manager.SlotType;
import dev.martinl.bsbrewritten.util.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class InteractListener implements Listener {
    private final BSBRewritten instance;

    public InteractListener(BSBRewritten instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(!instance.getBSBConfig().isEnableRightClickOpen()) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if(is.getAmount() != 1) return; // Do not open if stacked: compatible stacking plugin
        if (!MaterialUtil.isShulkerBox(is.getType())) return;
        //todo permissions, cooldown, worldguard area perms, etc
        if (BSBRewritten.getWorldGuardManager() != null) {
            for (String regionID : instance.getBSBConfig().getRegionList()) {
                if (instance.getBSBConfig().isBlacklistRegions()) {
                    if (BSBRewritten.getWorldGuardManager().isInRegion(e.getPlayer(), regionID)) return;
                } else {
                    if (!BSBRewritten.getWorldGuardManager().isInRegion(e.getPlayer(), regionID)) return;
                }
            }
        }
        BlockStateMeta bsm = (BlockStateMeta) is.getItemMeta();
        assert bsm != null;
        instance.getShulkerManager().openShulkerBoxInventory(e.getPlayer(), is, SlotType.HOTBAR, e.getPlayer().getInventory().getHeldItemSlot());
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if(!instance.getBSBConfig().isEnableInventoryClickOpen()) return;
        Player player = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();
        if (e.getClick() != ClickType.RIGHT) return;
        if (clickedInventory == null) return;
        if (clickedInventory.getType() != InventoryType.PLAYER) {
            return;
        }

        ItemStack clicked = e.getCurrentItem();
        if(clicked != null && clicked.getAmount() != 1) return; // Do not open if stacked: compatible stacking plugin
        boolean isShulker = clicked!=null && MaterialUtil.isShulkerBox(clicked.getType());
        if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING) {
            if(!isShulker) {
                return;
            }
        }
        if(!isShulker) return;
        e.setCancelled(true);
        ItemStack oldItem = clicked.clone();
        // Run the handler in 1 tick to not desync the inventory.
        Bukkit.getScheduler().runTask(instance, () -> {
            ItemStack currentItem = clickedInventory.getItem(e.getSlot());
            // Make sure the item has not changed since the click.
            if (oldItem.equals(currentItem)) {
                instance.getShulkerManager().openShulkerBoxInventory(player, currentItem, SlotType.INVENTORY, e.getRawSlot());
            }
        });
    }
}
