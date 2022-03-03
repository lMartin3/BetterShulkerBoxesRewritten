package dev.martinl.bsbrewritten;

import dev.martinl.bsbrewritten.commands.MainCommand;
import dev.martinl.bsbrewritten.listeners.InteractListener;
import dev.martinl.bsbrewritten.listeners.InventoryCloseListener;
import dev.martinl.bsbrewritten.manager.ShulkerManager;
import dev.martinl.bsbrewritten.util.ConfigurationParser;
import dev.martinl.bsbrewritten.util.UpdateChecker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

@Getter
public class BSBRewritten extends JavaPlugin {
    private ShulkerManager shulkerManager;
    private ConfigurationParser configurationParser;
    private UpdateChecker updateChecker;


    @Override
    public void onEnable() {
        shulkerManager = new ShulkerManager(this);
        loadAndParseConfig();
        InteractListener interactListener = new InteractListener(this);
        InventoryCloseListener inventoryCloseListener = new InventoryCloseListener(this);
        MainCommand mainCommand = new MainCommand(this);

        updateChecker = new UpdateChecker(this, 58837);

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if (updateChecker.checkForUpdates()) {
                for(String msg : updateChecker.getUpdateMessages()) {
                    Bukkit.getConsoleSender().sendMessage(msg);
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[BSB] " + ChatColor.GRAY + "You are running the latest BetterShulkerBoxes version.");
            }
        });

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "BetterShulkerBoxes version " +
                ChatColor.YELLOW + this.getDescription().getVersion()
                + ChatColor.AQUA + " loaded! (" + this.getServer().getVersion() + " | " + this.getServer().getBukkitVersion() + ") - Made by Rektb (lMartin3#1975)");
    }

    @Override
    public void onDisable() {
        shulkerManager.closeAllInventories(true);
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "BetterShulkerBoxes disabled");
    }

    public void loadAndParseConfig() {
        saveDefaultConfig();
        saveConfig();
        configurationParser = new ConfigurationParser(this.getConfig());
        configurationParser.parseConfiguration();
    }

    public int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
    }


}
