package dev.martinl.bsbrewritten.listeners;

import dev.martinl.bsbrewritten.BSBRewritten;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MergeEmptyShulkersListener implements Listener {
    private final BSBRewritten instance;
    public MergeEmptyShulkersListener(BSBRewritten instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();
        if(clickedInventory==null) return;
        if(clickedInventory.getType()!= InventoryType.PLAYER) {
            Bukkit.broadcastMessage("Wrong type: " + clickedInventory.getType().toString());
            return;
        }

        if(player.getOpenInventory().getTopInventory().getType()!=InventoryType.CRAFTING) {
            Bukkit.broadcastMessage("Wrong top type: " + player.getOpenInventory().getTopInventory().getType().toString());
            return;
        }

        ItemStack clickedItem = clickedInventory.getItem(e.getSlot());
        ItemStack cursorItem = e.getCursor();
        ItemStack currentItem = e.getCurrentItem();
        ClickType clickType = e.getClick();

        Bukkit.broadcastMessage(
                "Clicked item: " + format(clickedItem) + "\n" +
                "Cursor item: " + format(cursorItem) + "\n" +
                "Current item: " + format(currentItem) + "\n" +
                        "Click type: " + clickType.toString()
        );
    }

    private String format(ItemStack is) {
        if(is==null) return "null";
        return is.getType().toString();
    }
}
