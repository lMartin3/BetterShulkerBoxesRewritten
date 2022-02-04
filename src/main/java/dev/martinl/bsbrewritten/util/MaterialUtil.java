package dev.martinl.bsbrewritten.util;

import org.bukkit.Material;

public class MaterialUtil {
    public static boolean isShulkerBox(Material mat) {
        return mat.toString().endsWith("SHULKER_BOX");
    }
}
