package dev.martinl.bsbrewritten;

import dev.martinl.bsbrewritten.commands.MainCommand;
import dev.martinl.bsbrewritten.listeners.InteractListener;
import dev.martinl.bsbrewritten.listeners.InventoryCloseListener;
import dev.martinl.bsbrewritten.listeners.PlayerJoinListener;
import dev.martinl.bsbrewritten.manager.ShulkerManager;
import dev.martinl.bsbrewritten.util.ConfigurationParser;
import dev.martinl.bsbrewritten.util.Metrics;
import dev.martinl.bsbrewritten.util.UpdateChecker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class BSBRewritten extends JavaPlugin {
    private ShulkerManager shulkerManager;
    private ConfigurationParser configurationParser;
    private UpdateChecker updateChecker;
    @Setter
    private boolean lockFeatures = false;


    @Override
    public void onEnable() {
        shulkerManager = new ShulkerManager(this);
        loadAndParseConfig();
        new InteractListener(this);
        new InventoryCloseListener(this);
        new PlayerJoinListener(this);
        new MainCommand(this);

        updateChecker = new UpdateChecker(this, 58837);
        updateChecker.setupVulnerableVersionCheck(!configurationParser.isDisableVulnerableVersionProtection());


        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if (updateChecker.checkForUpdates()) {
                for (String msg : updateChecker.getUpdateMessages()) {
                    Bukkit.getConsoleSender().sendMessage(msg);
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[BSB] " + ChatColor.GRAY + "You are running the latest BetterShulkerBoxes version.");
            }
        });

        if (configurationParser.isEnableStatistics()) {
            new Metrics(this, 6076);
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[BSB] Statistics have been disabled, please consider enabling them to help plugin development.");
        }

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
        if (configurationParser.getOldVersionField() != null && !configurationParser.getOldVersionField().isEmpty()) {
            File oldFile = new File(getDataFolder(), "config.yml");
            if (!oldFile.exists()) {
                return;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[BSB] Old configuration detected! (" + configurationParser.getOldVersionField() + ") File will be renamed to config-old.yml and a new config file will be created.");
            oldFile.renameTo(new File(getDataFolder(), "config-old.yml"));
            reloadConfig();
            saveDefaultConfig();
            configurationParser = new ConfigurationParser(this.getConfig());
            configurationParser.parseConfiguration();
        }
    }

    public int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
    }


}
