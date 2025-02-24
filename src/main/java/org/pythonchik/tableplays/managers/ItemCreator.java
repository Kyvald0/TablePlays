package org.pythonchik.tableplays.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class ItemCreator {
    // new iteration of big data, but better and not so big :(

    public static ItemStack getDice() {
        ItemStack dice = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta meta = dice.getItemMeta();
        String type = Util.ItemTypes.Dice.getValue();
        if (meta != null){
            meta.getPersistentDataContainer().set(Util.ItemTags.Item.getValue(), PersistentDataType.STRING, type);
            meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "18:PLACE_MAIN,20:PLACE_LEFT,146:PLACE_MAIN,148:PLACE_LEFT,194:PLACE_TOP_MAIN,196:PLACE_TOP_LEFT,65:PICK_UP,66:PICK_UP,68:PICK_UP");
            meta.getPersistentDataContainer().set(Util.ItemTags.Modifiers.getValue(), PersistentDataType.STRING, "18:RCMDP6,20:RCMDP6,146:RCMDP6,148:RCMDP6,194:RCMDP6,196:RCMDP6,65:RESETCMD,66:RESETCMD,68:RESETCMD,196:ALIGN"); // ,66:ALIGN
            meta.setCustomModelData(ValuesManager.getBaseCMD(type));
            meta.setMaxStackSize(ValuesManager.getMaxStack(type));
            //more values form config?

            translationManager manager = translationManager.getInstance();

            meta.setLore(manager.getLore(type));
            meta.setDisplayName(manager.getName(type));

        }

        dice.setItemMeta(meta);
        return dice;
    }

    public static ItemStack getEmpty36bundle() {
        ItemStack bundle = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta meta = bundle.getItemMeta();
        String type = Util.ItemTypes.Bundle.getValue();
        if (meta != null){
            meta.getPersistentDataContainer().set(Util.ItemTags.Item.getValue(), PersistentDataType.STRING, type);
            //TODO this
            //meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "65:PICK_UP,193:PICK_UP,18:GET_FROM_BUNDLE_MAIN,146:PLACE_MAIN,34:GET_FROM_BUNDLE_MAIN,162:GET_FROM_BUNDLE_MAIN,66:GET_FROM_BUNDLE_MAIN,194:GET_FROM_BUNDLE_MAIN,20:GET_FROM_BUNDLE_LEFT,148:PLACE_LEFT,36:GET_FROM_BUNDLE_LEFT,164:GET_FROM_BUNDLE_LEFT,68:PLACE_TOP_LEFT,196:GET_FROM_BUNDLE_LEFT,24:PUT_FROM_MAIN,152:PLACE_LEFT,40:PUT_FROM_MAIN,168:PUT_FROM_MAIN,72:PUT_FROM_MAIN,72:PUT_FROM_GROUND,200:PUT_FROM_MAIN,200:PUT_FROM_GROUND");
            meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "65:PICK_UP,40:PUT_FROM_MAIN,146:PLACE_MAIN");
            meta.getPersistentDataContainer().set(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER, 0);
            meta.getPersistentDataContainer().set(Util.ItemTags.Bundle.getValue(), PersistentDataType.STRING, Util.ItemTypes.Card.getValue());
            meta.getPersistentDataContainer().set(Util.ItemTags.BundleMeta.getValue(), PersistentDataType.STRING, "data;36;stack"); // save in server, 8 max items, get as stack. TODO take from config, maybe someone is stupid to not save on a server
            meta.getPersistentDataContainer().set(Util.ItemTags.BundleData.getValue(), PersistentDataType.STRING, Util.convertItemsToBase64(new ArrayList<>())); // generate new bundle
            //meta.getPersistentDataContainer().set(Util.ItemTags.Modifiers.getValue(), PersistentDataType.STRING, "18:RCMDP6,20:RCMDP6,146:RCMDP6,148:RCMDP6,194:RCMDP6,196:RCMDP6,65:RESETCMD,66:RESETCMD,68:RESETCMD,196:ALIGN"); // ,66:ALIGN

            bundle.setItemMeta(meta);

            meta.setCustomModelData(ValuesManager.getBaseCMD(bundle));
            meta.setMaxStackSize(ValuesManager.getMaxStack(type));

            translationManager manager = translationManager.getInstance();
            meta.setLore(manager.getLore(bundle));
            meta.setDisplayName(manager.getName(bundle));
        }
        //TODO fill with cards
        bundle.setItemMeta(meta);
        return bundle;
    }


    public static ItemStack getCard(int subType) {
        ItemStack card = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta meta = card.getItemMeta();
        String type = Util.ItemTypes.Card.getValue();
        if (meta != null) {
            meta.getPersistentDataContainer().set(Util.ItemTags.Item.getValue(), PersistentDataType.STRING, type);
            meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "65:PICK_UP,18:PLACE_MAIN,146:PLACE_MAIN,66:PLACE_TOP_MAIN,194:PLACE_TOP_MAIN,20:PLACE_LEFT,148:PLACE_LEFT,68:PLACE_TOP_LEFT,196:PLACE_TOP_LEFT");
            meta.getPersistentDataContainer().set(Util.ItemTags.Modifiers.getValue(), PersistentDataType.STRING, "193:FLIP,146:FLIP,194:FLIP,148:FLIP,196:FLIP");
            meta.getPersistentDataContainer().set(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER, subType);
            //set 1, to use tags later.
            card.setItemMeta(meta);

            meta.setCustomModelData(ValuesManager.getBaseCMD(card));
            meta.setMaxStackSize(ValuesManager.getMaxStack(type));

            translationManager manager = translationManager.getInstance();
            meta.setLore(manager.getLore(card));
            meta.setDisplayName(manager.getName(card));
        }
        card.setItemMeta(meta);
        return card;
    }

}
