package dev.martinl.bsbrewritten.manager.chestsort;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IChestSortManager {
    void sort(Player player, Inventory shulkerInventory);

    void setSortable(Inventory shulkerInventory);
}
