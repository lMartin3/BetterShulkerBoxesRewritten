package dev.martinl.bsbrewritten;

import dev.martinl.bsbrewritten.commands.MainCommand;
import dev.martinl.bsbrewritten.configuration.BSBConfig;
import dev.martinl.bsbrewritten.configuration.ConfigurationLoader;
import dev.martinl.bsbrewritten.listeners.InteractListener;
import dev.martinl.bsbrewritten.listeners.InventoryCloseListener;
import dev.martinl.bsbrewritten.listeners.PlayerJoinListener;
import dev.martinl.bsbrewritten.manager.ShulkerManager;
import dev.martinl.bsbrewritten.manager.chestsort.ChestSortManagerImpl;
import dev.martinl.bsbrewritten.util.Metrics;
import dev.martinl.bsbrewritten.updater.UpdateChecker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class BSBRewritten extends JavaPlugin {
    private static BSBRewritten instance;
    private ShulkerManager shulkerManager;
    private ConfigurationLoader<BSBConfig> configurationLoader;
    private UpdateChecker updateChecker;
    @Setter
    private boolean lockFeatures = false;


    @Override
    public void onEnable() {
        instance = this;
        shulkerManager = new ShulkerManager(this);
        configurationLoader = new ConfigurationLoader<>(this, "config.yml", new BSBConfig());
        configurationLoader.loadConfiguration();
        updateConfigVersion();
        new InteractListener(this);
        new InventoryCloseListener(this);
        new PlayerJoinListener(this);
        new MainCommand(this);

        updateChecker = new UpdateChecker(this, 58837);
        updateChecker.setupVulnerableVersionCheck(!getBSBConfig().isDisableVulnerableVersionProtection());


        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if (updateChecker.checkForUpdates()) {
                for (String msg : updateChecker.getUpdateMessages()) {
                    Bukkit.getConsoleSender().sendMessage(msg);
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[BSB] " + ChatColor.GRAY + "You are running the latest BetterShulkerBoxes version.");
            }
        });

        if(Bukkit.getPluginManager().getPlugin("ChestSort") != null) {
            if(getBSBConfig().isEnableChestSortHook()) {
                shulkerManager.setChestSortManager(new ChestSortManagerImpl());
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[BSB] BetterShulkerBoxes successfully hooked to ChestSort");
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[BSB] BetterShulkerBoxes did not hook to ChestSort because it's disabled in the config file");
            }

        }

        if (getBSBConfig().isEnableStatistics()) {
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

    public BSBConfig getBSBConfig() {
        return this.getConfigurationLoader().getConfigData();
    }

    public void updateConfigVersion() {
        if(configurationLoader.getConfigData().getConfigVersion().equals(getDescription().getVersion())) {
            configurationLoader.getConfigData().setConfigVersion(getDescription().getVersion());
            configurationLoader.saveConfiguration(); // Apply new config version

        }
    }

    public int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
    }

    public static BSBRewritten getInstance() {
        return instance;
    }


}
