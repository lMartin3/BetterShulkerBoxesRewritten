package dev.martinl.bsbrewritten.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

@Data @RequiredArgsConstructor
public class ConfigurationParser {
    private final FileConfiguration fileConfiguration;


    private Sound openSound;
    private Sound closeSound;
    private int cooldown;
    private boolean requiresPermission;
    private boolean enableReadOnly;
    private boolean enableRightClickOpen;
    private boolean enableInventoryClickOpen;

    private String prefix;
    private String inventoryName;
    private String noPermissionMessage;
    private String cooldownMessage;
    private String configReloadMessage;
    private String configReloadErrorMessage;



    public void parseConfiguration() {
        openSound = Sound.valueOf(fileConfiguration.getString("open_sound"));
        closeSound = Sound.valueOf(fileConfiguration.getString("close_sound"));
        cooldown = fileConfiguration.getInt("cooldown");
        requiresPermission = fileConfiguration.getBoolean("requires_permission");
        enableReadOnly = fileConfiguration.getBoolean("enable_read_only");
        enableRightClickOpen = fileConfiguration.getBoolean("enable_right_click_open");
        enableInventoryClickOpen = fileConfiguration.getBoolean("enable_inventory_click_open");
    }

    private String tcc(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
