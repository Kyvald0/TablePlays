package org.pythonchik.tableplays.managers;

import org.bukkit.Material;
import org.pythonchik.tableplays.TablePlays;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;

public class ValuesManager {

    public static HashMap<String, HashMap<Material, Integer>> variants = new HashMap<>();
    public static HashMap<Material, Integer> chips = new HashMap<>();

    public static List<Float> getItemHitbox(ItemStack stack) {
        // width and height in that order
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
        return TablePlays.config.getInt("items." + type + ".basecmd", 1);
    }

    public static int getMaxStack(String type) {
        return TablePlays.config.getInt("items." + type + ".max_stack", 1);
    }

    public static String getSaveType(int items) {
        if (TablePlays.config.getBoolean("allow_files", false)) {
            if (items > TablePlays.config.getInt("file_threshold", 36)) return "uuid";
        }
        return "data";
    }

    public static int getPitch(String type) {
        return TablePlays.config.getInt("items." + type + ".pitch", 90);
    }

    public static int getPitch(ItemStack stack) {
        if (stack == null || stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.Item.getValue())) {
            return 0;
        }
        String type = stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        if (type == null) {
            return 0;
        }
        return getPitch(type);
    }

    public static void generateChipVariants() {
        HashMap<Material, Integer> map = new HashMap<>();
        if (TablePlays.config.contains("items") && TablePlays.config.contains("items.chip") && TablePlays.config.contains("items.chip.variants")) {
            ConfigurationSection config = TablePlays.config.getConfigurationSection("items.chip.variants");
            for (String key : config.getKeys(false)) {
                if (Material.getMaterial(key) != null) map.put(Material.getMaterial(key), config.getInt(key));
                else System.out.println("Error while trying to get material for: " + key);
            }
        }
        chips = map;
    }

    public static HashMap<Material, Integer> getVariants(String type) {
        if (variants.containsKey(type)) return variants.get(type);
        // nope, does not have a key.
        HashMap<Material, Integer> map = new HashMap<>();
        if (TablePlays.config.contains("items") && TablePlays.config.contains("items." + type) && TablePlays.config.contains("items." + type + ".variants")) {
            ConfigurationSection config = TablePlays.config.getConfigurationSection("items." + type + ".variants");
            for (String key : config.getKeys(false)) {
                if (Material.getMaterial(key) != null) map.put(Material.getMaterial(key), config.getInt(key));
                else System.out.println("Error while trying to get material for: " + key);
            }
        }
        variants.put(type, map);
        return map;
    }

    public static HashMap<Material, Integer> getChipVariants() {
        return ValuesManager.chips;
    }
}
