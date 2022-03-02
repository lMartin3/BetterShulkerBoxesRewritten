package dev.martinl.bsbrewritten.listeners;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.manager.ShulkerOpenData;
import dev.martinl.bsbrewritten.util.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

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
        instance.getShulkerManager().closeShulkerBox(player, inventory, Optional.empty());
    }

    //todo view-mode only
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack droppedItem = e.getItemDrop().getItemStack();
        if(!MaterialUtil.isShulkerBox(droppedItem.getType())) return; //check if the dropped item is a shulker box
        if(e.getPlayer().getOpenInventory().getType()!=InventoryType.SHULKER_BOX) return; //check if the open inventory is one from a shulker box
        if(e.getPlayer().getOpenInventory().getTopInventory().getLocation()!=null) return; //check if the shulker is a block
        if(!instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId())) return; //check if the inventory belongs to BSB
        ItemStack corresponding = instance.getShulkerManager().getCorrespondingStack(e.getPlayer().getOpenInventory().getTopInventory());
        if(corresponding==null) {
            Bukkit.broadcastMessage("Corresponding is null!");
            return;
        } else if(!corresponding.equals(droppedItem)) {
            Bukkit.broadcastMessage("Corresponding is not equal to the dropped item");
            Bukkit.broadcastMessage(corresponding.getType().toString());
            return;
        }
        //Bukkit.broadcastMessage("Should work!");
        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(e.getPlayer().getOpenInventory().getType()!=InventoryType.SHULKER_BOX) return; //check if the open inventory is one from a shulker box
        if(e.getPlayer().getOpenInventory().getTopInventory().getLocation()!=null) return; //check if the shulker is a block
        if(!instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId())) return; //check if the inventory belongs to BSB
        ShulkerOpenData sod = instance.getShulkerManager().getShulkerOpenData(e.getPlayer().getOpenInventory().getTopInventory());
        if(sod==null||e.getTo()==null) return;
        if(sod.getOpenLocation().distance(e.getTo())>1) {
            instance.getShulkerManager().closeShulkerBox(e.getPlayer(), e.getPlayer().getOpenInventory().getTopInventory(), Optional.empty());
        }
    }


    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(e.getPlayer().getOpenInventory().getType()!=InventoryType.SHULKER_BOX) return; //check if the open inventory is one from a shulker box
        if(e.getPlayer().getOpenInventory().getTopInventory().getLocation()!=null) return; //check if the shulker is a block
        if(!instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId())) return; //check if the inventory belongs to BSB
        instance.getShulkerManager().closeShulkerBox(e.getPlayer(), e.getPlayer().getOpenInventory().getTopInventory(), Optional.empty());
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(player.getOpenInventory().getType()!=InventoryType.SHULKER_BOX) return; //check if the open inventory is one from a shulker box
        if(player.getOpenInventory().getTopInventory().getLocation()!=null) return; //check if the shulker is a block
        if(!instance.getShulkerManager().doesPlayerHaveShulkerOpen(player.getUniqueId())) return; //check if the inventory belongs to BSB

        /*
        *   This should prevent some kind of exploit in which a player closes an inventory
        *   locally, the packet is not sent, and then tries to dye the shulker to try and
        *   duplicate items
        *
        */
        if(MaterialUtil.isShulkerBox(e.getRecipe().getResult().getType())) {
            e.setCancelled(true);
        }
        instance.getShulkerManager().closeShulkerBox(player, player.getOpenInventory().getTopInventory(), Optional.empty());
    }


    /*
        This should prevent someone from trying to duplicate items by closing an inventory locally and then opening
        another one. The server should not allow the player to open another inventory before closing the one that's
        already open, but this is just to be safe.
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if(instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId())&&!instance.getShulkerManager().isShulkerInventory(e.getInventory())
        &&e.getPlayer().getOpenInventory().getTopInventory().getType()==InventoryType.SHULKER_BOX) {
            instance.getShulkerManager().closeShulkerBox((Player) e.getPlayer(), e.getPlayer().getOpenInventory().getTopInventory(), Optional.empty());
        }
    }


    /*
    * This should prevent players from moving shulkers around in their inventories while they have a BSB shulker open
    * */
    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();
        if(clickedInventory==null) return;

        ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem==null||!MaterialUtil.isShulkerBox(clickedItem.getType())) return;

        Inventory topInventory = player.getOpenInventory().getTopInventory();
        ItemStack correspondingStack = instance.getShulkerManager().getCorrespondingStack(topInventory);
        if(correspondingStack==null) return;

        if(correspondingStack.equals(clickedItem)) {
            Bukkit.broadcastMessage("Click on open shulker detected!");
            e.setCancelled(true);
        }
    }
}
