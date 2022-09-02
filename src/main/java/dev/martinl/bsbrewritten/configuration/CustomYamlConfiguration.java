package dev.martinl.bsbrewritten.configuration;

import com.google.common.io.Files;
import dev.martinl.bsbrewritten.BSBRewritten;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;

public class CustomYamlConfiguration extends YamlConfiguration {

    @Override
    public void save(@NotNull File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);

        String data = saveToString();

        FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8, false);
        //Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
        String text = new Scanner(BSBRewritten.getPlugin(BSBRewritten.class).getResource("configheader.txt"), "UTF-8").useDelimiter("\\A").next();
        Bukkit.getConsoleSender().sendMessage(text);
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

    @Override
    public void load(@NotNull Reader reader) throws IOException, InvalidConfigurationException {
        BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);

        StringBuilder builder = new StringBuilder();

        try {
            String line;
            boolean header = false;
            while ((line = input.readLine()) != null) {
                if(line.equals("# <HEADER>")) {
                    header = true;
                    continue;
                } else if (line.equals("# </HEADER>")) {
                    header = false;
                    continue;
                }
                if(!header) {
                    builder.append(line);
                    builder.append('\n');
                }
            }
        } finally {
            input.close();
        }

        loadFromString(builder.toString());
    }
}
