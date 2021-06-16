package cc.fascinated.gravestones.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 15/06/2021
 */

public class ItemUtils {

    public static String itemToString(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return null;
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("stack", item);
        return configuration.saveToString();
    }

    public static ItemStack stringToItem(String s) {
        if (s == null)
            return null;
        try {
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.loadFromString(s);
            return configuration.getItemStack("stack");
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
