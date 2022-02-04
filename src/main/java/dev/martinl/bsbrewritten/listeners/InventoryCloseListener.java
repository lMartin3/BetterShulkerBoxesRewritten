package dev.martinl.bsbrewritten.listeners;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {
    private final BSBRewritten instance;

    public InventoryCloseListener(BSBRewritten instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Inventory inventory = e.getInventory();
        if(inventory.getType()!= InventoryType.SHULKER_BOX) return; //validate inventory type
        if(inventory.getHolder()!=null||e.getInventory().getLocation()!=null) return; //check that the shulker inventory is not a block inventory
        if(!instance.getShulkerManager().isShulkerInventory(inventory)) return; //check that the inventory belongs to BSB
        Bukkit.broadcastMessage("Is shulker inventory: " + instance.getShulkerManager().isShulkerInventory(inventory));
        instance.getShulkerManager().closeShulkerBox(player, inventory);
    }

    //todo view-mode only
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack droppedItem = e.getItemDrop().getItemStack();
        if(!MaterialUtil.isShulkerBox(droppedItem.getType())) return; //check if the dropped item is a shulker box
        if(e.getPlayer().getOpenInventory().getType()!=InventoryType.SHULKER_BOX) return; //check if the open inventory is one from a shulker box
        if(e.getPlayer().getOpenInventory().getTopInventory().getLocation()!=null) return; //check if the shulker is a block
        if(!instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId())) return; //check if the inventory belongs to BSB
        e.setCancelled(true);
    }
}
