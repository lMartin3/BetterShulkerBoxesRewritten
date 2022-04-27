package dev.martinl.bsbrewritten.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

@Data
@RequiredArgsConstructor
public class ConfigurationParser {
    private final FileConfiguration fileConfiguration;

    private String oldVersionField;

    private Sound openSound;
    private Sound closeSound;
    private int cooldown;
    private boolean requiresPermission;
    private boolean enableReadOnly;
    private boolean enableRightClickOpen;
    private boolean enableInventoryClickOpen;
    private boolean enableStatistics;

    private String prefix;
    private String inventoryName;
    private String openMessage;
    private String closeMessage;
    private String noPermissionMessage;
    private String cooldownMessage;


    public void parseConfiguration() {
        String openSoundName = fileConfiguration.getString("open_sound");
        String closeSoundName = fileConfiguration.getString("close_sound");

        oldVersionField = fileConfiguration.getString("version");
        openSound = (openSoundName==null||openSoundName.isEmpty()) ? null : Sound.valueOf(openSoundName);
        closeSound = (closeSoundName==null||closeSoundName.isEmpty()) ? null : Sound.valueOf(closeSoundName);
        cooldown = fileConfiguration.getInt("cooldown");
        requiresPermission = fileConfiguration.getBoolean("requires_permission");
        enableReadOnly = fileConfiguration.getBoolean("enable_read_only");
        enableRightClickOpen = fileConfiguration.getBoolean("enable_right_click_open");
        enableInventoryClickOpen = fileConfiguration.getBoolean("enable_inventory_click_open");
        enableStatistics = fileConfiguration.getBoolean("enable_statistics");

        prefix = translateCC(strFromConfig("prefix"));
        prefix = (prefix.isEmpty() ? "" : prefix + ChatColor.RESET + " ");
        openMessage = strFromConfig("open_message");
        closeMessage = strFromConfig("close_message");
        inventoryName = translateCC(strFromConfig("inventory_name"));
        noPermissionMessage = translateCC(strFromConfig("no_permission_message"));
        cooldownMessage = translateCC(strFromConfig("cooldown_message"));
    }

    private String strFromConfig(String path) {
        return translateCC(fileConfiguration.getString(path));
    }

    private String translateCC(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
