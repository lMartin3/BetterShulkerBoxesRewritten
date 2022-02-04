package dev.martinl.bsbrewritten.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShulkerManager {
    private final BSBRewritten instance;
    private HashMap<Inventory, ItemStack> openShulkerInventories = new HashMap<>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public ShulkerManager(BSBRewritten instance) {
        this.instance = instance;
    }


    public void openShulkerBoxInventory(Player player, ItemStack shulkerStack) {
        BlockStateMeta bsm = (BlockStateMeta) shulkerStack.getItemMeta();
        assert bsm != null;
        ShulkerBox shulker = (ShulkerBox) bsm.getBlockState();
        Inventory inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, "asd");
        inventory.setContents(shulker.getInventory().getContents());
        player.openInventory(inventory);
        openShulkerInventories.put(inventory, shulkerStack);
    }

    public void closeShulkerBox(Player player, Inventory inventory) {
        if(!openShulkerInventories.containsKey(inventory)) return;
        ItemStack correspondingStack = openShulkerInventories.get(inventory);


        boolean found = false;
        for(ItemStack is : player.getInventory().getContents()) {
            if(is!=null&&is.equals(correspondingStack)) {
                found = true;
            }
        }
        if(!found) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING! Player " + player.getName() + " closed a shulkerbox and changes were not saved!");
        }
        BlockStateMeta cMeta = (BlockStateMeta) correspondingStack.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) cMeta.getBlockState();
        shulker.getInventory().setContents(inventory.getContents());
        cMeta.setBlockState(shulker);
        correspondingStack.setItemMeta(cMeta);
        Bukkit.broadcastMessage("End");
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


    public void closeAllInventories() {
        for(Inventory inventory : openShulkerInventories.keySet()) {
            for(HumanEntity player : inventory.getViewers()) {
                player.closeInventory();
            }
        }
    }


}
