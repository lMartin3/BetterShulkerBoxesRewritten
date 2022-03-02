package dev.martinl.bsbrewritten.util;

import dev.martinl.bsbrewritten.BSBRewritten;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfiguration {
    private BSBRewritten instance;
    public String configName;
    public String filename;
    public String fileType;
    public FileConfiguration configFile;
    public File file;

    public CustomConfiguration(BSBRewritten pluginInstance, String configName, String fileType) {
        this.instance = pluginInstance;
        this.configName = configName;
        this.filename = configName + "." + fileType;
        this.fileType = fileType;
    }

    public void setup() {
        if (!instance.getDataFolder().exists()) {
            boolean ignore = instance.getDataFolder().mkdir();
        }
        file = new File(instance.getDataFolder(), filename);
        if (!file.exists()) {
            instance.saveResource(configName + "." + fileType, false);

            try {
                boolean ignore = file.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "File " + filename + " could not be created. Check write permissions.");
            }


        }
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getData() {
        return configFile;
    }

    public void saveData() {
        try {
            configFile.save(file);

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "File " + filename + " could not be modified. Check write permissions.");
        }
    }

    public void reloadData() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }

}
