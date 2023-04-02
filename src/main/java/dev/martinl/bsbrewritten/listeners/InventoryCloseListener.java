package dev.martinl.bsbrewritten.listeners;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.manager.ShulkerOpenData;
import dev.martinl.bsbrewritten.util.MaterialUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
        if (inventory.getType() != InventoryType.SHULKER_BOX) return; //validate inventory type
        if (inventory.getHolder() != null || e.getInventory().getLocation() != null)
            return; //check that the shulker inventory is not a block inventory
        if (!instance.getShulkerManager().isShulkerInventory(inventory))
            return; //check that the inventory belongs to BSB
        instance.getShulkerManager().closeShulkerBox(player, inventory, Optional.empty());
        player.setItemOnCursor(null); //Workaround for Shulker box caught in cursor after opening with right click in inventory
    }

    //todo view-mode only
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack droppedItem = e.getItemDrop().getItemStack();
        if (!MaterialUtil.isShulkerBox(droppedItem.getType())) return; //check if the dropped item is a shulker box
        if (e.getPlayer().getOpenInventory().getType() != InventoryType.SHULKER_BOX)
            return; //check if the open inventory is one from a shulker box
        if (e.getPlayer().getOpenInventory().getTopInventory().getLocation() != null)
            return; //check if the shulker is a block
        if (!instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId()))
            return; //check if the inventory belongs to BSB
        ItemStack corresponding = instance.getShulkerManager().getCorrespondingStack(e.getPlayer().getOpenInventory().getTopInventory());
        if (corresponding == null) {
            return;
        } else if (!corresponding.equals(droppedItem)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (instance.getBSBConfig().isDisableMovementCheck()) return;
        if (e.getPlayer().getOpenInventory().getType() != InventoryType.SHULKER_BOX)
            return; //check if the open inventory is one from a shulker box
        if (e.getPlayer().getOpenInventory().getTopInventory().getLocation() != null)
            return; //check if the shulker is a block
        if (!instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId()))
            return; //check if the inventory belongs to BSB
        ShulkerOpenData sod = instance.getShulkerManager().getShulkerOpenData(e.getPlayer().getOpenInventory().getTopInventory());
        if (sod == null || e.getTo() == null) return;
        if (sod.getOpenLocation().distance(e.getTo()) > 1) {
            instance.getShulkerManager().closeShulkerBox(e.getPlayer(), e.getPlayer().getOpenInventory().getTopInventory(), Optional.empty());
        }
    }


    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().getOpenInventory().getType() != InventoryType.SHULKER_BOX)
            return; //check if the open inventory is one from a shulker box
        if (e.getPlayer().getOpenInventory().getTopInventory().getLocation() != null)
            return; //check if the shulker is a block
        if (!instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId()))
            return; //check if the inventory belongs to BSB
        instance.getShulkerManager().closeShulkerBox(e.getPlayer(), e.getPlayer().getOpenInventory().getTopInventory(), Optional.empty());
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (player.getOpenInventory().getType() != InventoryType.SHULKER_BOX)
            return; //check if the open inventory is one from a shulker box
        if (player.getOpenInventory().getTopInventory().getLocation() != null) return; //check if the shulker is a block
        if (!instance.getShulkerManager().doesPlayerHaveShulkerOpen(player.getUniqueId()))
            return; //check if the inventory belongs to BSB

        /*
         *   This should prevent some kind of exploit in which a player closes an inventory
         *   locally, the packet is not sent, and then tries to dye the shulker to try and
         *   duplicate items
         *
         */
        if (MaterialUtil.isShulkerBox(e.getRecipe().getResult().getType())) {
            e.setCancelled(true);
        }
        instance.getShulkerManager().closeShulkerBox(player, player.getOpenInventory().getTopInventory(), Optional.empty());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (player.getOpenInventory().getType() != InventoryType.SHULKER_BOX)
            return; //check if the open inventory is one from a shulker box
        if (player.getOpenInventory().getTopInventory().getLocation() != null) return; //check if the shulker is a block
        if (!instance.getShulkerManager().doesPlayerHaveShulkerOpen(player.getUniqueId()))
            return; //check if the inventory belongs to BSB
        instance.getShulkerManager().closeShulkerBox(player, player.getOpenInventory().getTopInventory(), Optional.empty());
    }

    /*
        This should prevent someone from trying to duplicate items by closing an inventory locally and then opening
        another one. The server should not allow the player to open another inventory before closing the one that's
        already open, but this is just to be safe.
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (instance.getShulkerManager().doesPlayerHaveShulkerOpen(e.getPlayer().getUniqueId()) && !instance.getShulkerManager().isShulkerInventory(e.getInventory())
                && e.getPlayer().getOpenInventory().getTopInventory().getType() == InventoryType.SHULKER_BOX) {
            instance.getShulkerManager().closeShulkerBox((Player) e.getPlayer(), e.getPlayer().getOpenInventory().getTopInventory(), Optional.empty());
        }
    }


    /*
     * This should prevent players from moving shulkers around in their inventories while they have a BSB shulker open
     * Also this works for read only mode
     * */
    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) return;

        Inventory topInventory = player.getOpenInventory().getTopInventory();
        ItemStack correspondingStack = instance.getShulkerManager().getCorrespondingStack(topInventory);

        if (correspondingStack == null) return;

        if (instance.getBSBConfig().isEnableReadOnly()) {
            e.setCancelled(true);
        }


        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || !MaterialUtil.isShulkerBox(clickedItem.getType())) return;

        if (correspondingStack.equals(clickedItem)) {
            e.setCancelled(true);
        }
    }

    /*
     * This should prevent players from placing a BSB shulker while having a BSB shulker open
     * */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        cancelIfPlayerHasShulkerOpen(e.getPlayer(), e);
    }


    /*
     * This should prevent players from
     * */
    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent e){
        cancelIfPlayerHasShulkerOpen(e.getPlayer(), e);
    }


    private void cancelIfPlayerHasShulkerOpen(Player player, Cancellable cancellable) {
        if (player.getOpenInventory().getType() != InventoryType.SHULKER_BOX)
            return; //check if the open inventory is one from a shulker box
        if (player.getOpenInventory().getTopInventory().getLocation() != null) return; //check if the shulker is a block
        if (!instance.getShulkerManager().doesPlayerHaveShulkerOpen(player.getUniqueId()))
            return; //check if the inventory belongs to BSB
        cancellable.setCancelled(true);
    }
}
