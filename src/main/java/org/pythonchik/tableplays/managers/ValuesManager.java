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
        if (stack == null || stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.Type.getValue())) {
            return null;
        }
        String type = stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Type.getValue(), PersistentDataType.STRING);
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
        if (stack == null || stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.Type.getValue())) {
            return null;
        }
        String type = stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Type.getValue(), PersistentDataType.STRING);
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
}
