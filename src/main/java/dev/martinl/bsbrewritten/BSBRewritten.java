package dev.martinl.bsbrewritten;

import dev.martinl.bsbrewritten.listeners.InteractListener;
import dev.martinl.bsbrewritten.listeners.InventoryCloseListener;
import dev.martinl.bsbrewritten.listeners.MergeEmptyShulkersListener;
import dev.martinl.bsbrewritten.manager.ShulkerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BSBRewritten extends JavaPlugin {
    @Getter
    private ShulkerManager shulkerManager;

    @Override
    public void onEnable() {
        shulkerManager = new ShulkerManager(this);
        InteractListener interactListener = new InteractListener(this);
        InventoryCloseListener inventoryCloseListener = new InventoryCloseListener(this);
        MergeEmptyShulkersListener mergeEmptyShulkersListener = new MergeEmptyShulkersListener(this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "BetterShulkerBoxes version " +
                ChatColor.YELLOW + this.getDescription().getVersion()
        + ChatColor.AQUA + " loaded! (" + this.getServer().getVersion() + " | " + this.getServer().getBukkitVersion() + ")");
    }

    @Override
    public void onDisable() {
        shulkerManager.closeAllInventories();
    }


}
