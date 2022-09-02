package dev.martinl.bsbrewritten.configuration;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import dev.martinl.bsbrewritten.BSBRewritten;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;

public class CustomYamlConfiguration extends YamlConfiguration {

    @Override
    public void save(@NotNull File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);

        String data = saveToString();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
        String text = new Scanner(BSBRewritten.getPlugin(BSBRewritten.class).getResource("configheader.txt"), "UTF-8").useDelimiter("\\A").next();
        writer.write(text);
        writer.write("\n\n");


        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    @NotNull
    public static CustomYamlConfiguration loadConfiguration(@NotNull File file) {
        Validate.notNull(file, "File cannot be null");

        CustomYamlConfiguration config = new CustomYamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        return config;
    }
}
