package dev.martinl.bsbrewritten.manager.chestsort;

import de.jeff_media.chestsort.api.ChestSortAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ChestSortManagerImpl implements IChestSortManager {
    @Override
    public void sort(Player player, Inventory shulkerInventory) {
        //noinspection ConstantConditions
        if(ChestSortAPI.hasSortingEnabled(player)) {
            ChestSortAPI.sortInventory(shulkerInventory);
        }
    }

    @Override
    public void setSortable(Inventory shulkerInventory) {
        ChestSortAPI.setSortable(shulkerInventory);
    }
}
