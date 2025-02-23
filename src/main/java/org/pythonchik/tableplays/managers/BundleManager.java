package org.pythonchik.tableplays.managers;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.tableplays.TablePlays;

import java.util.*;

public class BundleManager {
    //TODO test ALL of this
    public static boolean addToBundle(ItemStack bundle, ItemStack item) {
        PersistentDataContainer container = bundle.getItemMeta().getPersistentDataContainer();
        if (!isValidBundle(bundle)) return false;
        String[] types = container.get(Util.ItemTags.Bundle.getValue(), PersistentDataType.STRING).split(",");
        if (!Arrays.asList(types).contains(item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING))) {
            return false; // bundle does not accept that type
        }
        ArrayList<ItemStack> currentItems = getBundleItems(bundle);
        if (currentItems.size() >= Integer.parseInt(types[1])) return false; // max items reached
        currentItems.add(item);
        saveItemsToBundle(bundle, currentItems);
        return true;
    }
    public static ArrayList<ItemStack> getBundleItems(ItemStack bundle) {
        String data = bundle.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.BundleData.getValue(), PersistentDataType.STRING);
        if (TablePlays.data.getString(data) != null) return Util.getItemsFromBase64(TablePlays.data.getString(data));
        return Util.getItemsFromBase64(data);
    }

    public static boolean saveItemsToBundle(ItemStack bundle, ArrayList<ItemStack> items) {
        return saveItemsToBundle(bundle, Util.convertItemsToBase64(items));
    }

    public static boolean saveItemsToBundle(ItemStack bundle, String base64) {
        String type = bundle.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.BundleMeta.getValue(), PersistentDataType.STRING).split(";")[0];
        if (type.equals("uuid")) {
            TablePlays.data.set(bundle.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.BundleData.getValue(), PersistentDataType.STRING), base64);
        } else {
            bundle.getItemMeta().getPersistentDataContainer().set(Util.ItemTags.BundleData.getValue(), PersistentDataType.STRING, base64);
        }
        return true;
    }

    public static boolean isValidBundle(ItemStack bundle) {
        if (bundle == null) return false; // nothing is not a valid bundle
        if (bundle.getItemMeta() == null) return false; // item with no meta is not
        PersistentDataContainer container = bundle.getItemMeta().getPersistentDataContainer();
        if (!container.has(Util.ItemTags.Item.getValue(), PersistentDataType.STRING)) return false; // it's not my item
        if (!container.get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING).equals(Util.ItemTypes.Bundle)) return false; //it's not a bundle
        if (!container.has(Util.ItemTags.BundleMeta.getValue(), PersistentDataType.STRING)) return false; // does not have necessary meta
        if (!container.has(Util.ItemTags.BundleData.getValue(), PersistentDataType.STRING)) return false; // does not have necessary data
        if (!container.get(Util.ItemTags.BundleMeta.getValue(), PersistentDataType.STRING).matches("(data|uuid);[123456789]\\d*")) return false; // meta is in wrong format
        //TODO check more, I think I forgot something, maybe check that the save method is correct? maybe not?
        return true;
    }

}
