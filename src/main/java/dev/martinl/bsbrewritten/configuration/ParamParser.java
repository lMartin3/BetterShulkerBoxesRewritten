package dev.martinl.bsbrewritten.configuration;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.List;

public class ParamParser {
    private static final List<Class<?>> yamlClasses = List.of(String.class, Boolean.class, Integer.class, Float.class, Double.class, Short.class, Long.class,
            boolean.class, int.class, float.class, short.class, double.class, long.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object deserialize(Object input, Class targetFieldType) {
        Bukkit.getConsoleSender().sendMessage("Deserializing: " + targetFieldType.getName() + " | " + targetFieldType.isEnum());
        if(targetFieldType.isEnum()) {
            return Enum.valueOf(targetFieldType, (String) input);
        } else if(yamlClasses.contains(targetFieldType)) {
            return input;
        }

        Bukkit.getConsoleSender().sendMessage("Warning: Could not deserialize field of type " + targetFieldType.getName() + " correctly!");
        return input;
    }

    public static Object serialize(Object input) {
        if(input.getClass().isEnum()) {
            return input.toString();
        } else if(yamlClasses.contains(input.getClass())) {
            return input;
        }
        return input;
    }
}
