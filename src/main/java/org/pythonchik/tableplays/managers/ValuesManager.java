package org.pythonchik.tableplays.managers;

import org.pythonchik.tableplays.TablePlays;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.List;

public class ValuesManager {

    public static List<Float> getItemHitbox(ItemStack stack) {
        if (stack == null || stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.Item.getValue())) {
            return null;
        }
        String type = stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        if (type == null) {
            return null;
        }
        ConfigurationSection config = TablePlays.config;
        if (config.contains("items") && config.contains("items." + type) && config.contains("items." + type + ".hitbox")) {
            return config.getFloatList("items." + type + ".hitbox");
        }
        return null;
    }

    public static Transformation getTransformation(ItemStack stack) {
        if (stack == null || stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.Item.getValue())) {
            return null;
        }
        String type = stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        if (type == null) {
            return null;
        }
        ConfigurationSection config = TablePlays.config;
        if (config.contains("items") && config.contains("items." + type) && config.contains("items." + type + ".transformation")) {
            List<Float> values = config.getFloatList("items." + type + ".transformation");
            return new Transformation(
                    new Vector3f(values.get(0), values.get(1), values.get(2)),
                    new AxisAngle4f(values.get(3), values.get(4),values.get(5),values.get(6)),
                    new Vector3f(values.get(7),values.get(8),values.get(9)),
                    new AxisAngle4f(values.get(10), values.get(11), values.get(12), values.get(13)));
        }
        return null;
    }

    public static int getBaseCMD(ItemStack stack) {
        if (stack == null || stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.Item.getValue())) {
            return 0;
        }
        String type = stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        if (type == null) {
            return 0;
        }
        ConfigurationSection config = TablePlays.config;
        if (config.contains("items") && config.contains("items." + type) && config.contains("items." + type + ".basecmd")) {
            if (stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER)) {
                // add variant if we have one, nullpoint exception might only be if type is not int, so manual change, that means fuck you if you manually change it
                return config.getInt("items." + type + ".basecmd", 1) + stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER);
            }
            return config.getInt("items." + type + ".basecmd", 1);
        }
        return 0;
    }
    public static int getBaseCMD(String type) {
        ConfigurationSection config = TablePlays.config;
        if (config.contains("items") && config.contains("items." + type) && config.contains("items." + type + ".basecmd")) {
            return config.getInt("items." + type + ".basecmd", 1);
        }
        return 0;
    }

    public static int getMaxStack(String type) {
        ConfigurationSection config = TablePlays.config;
        if (config.contains("items") && config.contains("items." + type) && config.contains("items." + type + ".max_stack")) {
            return config.getInt("items." + type + ".max_stack", 1);
        }
        return 1;
    }
}
