package dev.martinl.bsbrewritten.listeners;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.MaterialUtil;
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

    @EventHandler(ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e) {
        if(!instance.getBSBConfig().isEnableRightClickOpen()) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (!MaterialUtil.isShulkerBox(is.getType())) return;
        //todo permissions, cooldown, worldguard area perms, etc
        BlockStateMeta bsm = (BlockStateMeta) is.getItemMeta();
        assert bsm != null;
        instance.getShulkerManager().openShulkerBoxInventory(e.getPlayer(), is);
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
        boolean isShulker = clicked!=null && MaterialUtil.isShulkerBox(clicked.getType());
        if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING) {
            if(!isShulker) {
                return;
            }
        }
        if(!isShulker) return;
        e.setCancelled(true);
        instance.getShulkerManager().openShulkerBoxInventory(player, clicked);

    }
}
