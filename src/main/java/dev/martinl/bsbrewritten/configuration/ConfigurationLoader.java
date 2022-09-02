package dev.martinl.bsbrewritten.configuration;

import dev.martinl.bsbrewritten.util.StringUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

@RequiredArgsConstructor
public class ConfigurationLoader<T extends IDeepCloneable> {
    private final Plugin plugin;
    private final String configName;
    private final T defaultConfig;

    @Getter
    private T configData;

    private File configFile;
    private CustomYamlConfiguration bukkitPluginConfig;

    public void loadConfiguration() {
        configData = (T) defaultConfig.clone();

        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        configFile = new File(plugin.getDataFolder(), configName);
        try {
            if(!configFile.exists()) {
                configFile.createNewFile();
            }
            writeMissingConfigFields();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        readConfiguration();
    }

    public void writeMissingConfigFields() throws IOException {
        bukkitPluginConfig = new CustomYamlConfiguration();
        for(Field field : defaultConfig.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                bukkitPluginConfig.set(StringUtil.convertToSnakeCase(field.getName()), ParamParser.serialize(field.get(defaultConfig)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        bukkitPluginConfig.save(configFile);
    }

    private void readConfiguration() {
        bukkitPluginConfig = CustomYamlConfiguration.loadConfiguration(configFile);
        for(Field field : configData.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = ParamParser.deserialize(bukkitPluginConfig.get(StringUtil.convertToSnakeCase(field.getName())), field);
                field.set(configData, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
