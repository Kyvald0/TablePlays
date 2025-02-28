package org.pythonchik.tableplays.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ItemCreator {
    // new iteration of big data, better and not so big :(

    public static ItemStack getDice() {
        ItemStack dice = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta meta = dice.getItemMeta();
        String type = Util.ItemTypes.Dice.getValue();
        if (meta != null){
            meta.getPersistentDataContainer().set(Util.ItemTags.Item.getValue(), PersistentDataType.STRING, type);
            meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "18:PLACE_MAIN,20:PLACE_LEFT,146:PLACE_MAIN,148:PLACE_LEFT,194:PLACE_TOP_MAIN,196:PLACE_TOP_LEFT,65:PICK_UP,66:PICK_UP,68:PICK_UP");
            meta.getPersistentDataContainer().set(Util.ItemTags.Modifiers.getValue(), PersistentDataType.STRING, "18:RCMDP6,20:RCMDP6,146:RCMDP6,148:RCMDP6,194:RCMDP6,196:RCMDP6,65:RESETCMD,66:RESETCMD,68:RESETCMD,196:ALIGN"); // ,66:ALIGN

            dice.setItemMeta(meta);

            meta.setCustomModelData(ValuesManager.getBaseCMD(dice));
            meta.setMaxStackSize(ValuesManager.getMaxStack(type));
            //more values form config?

            translationManager manager = translationManager.getInstance();

            meta.setLore(manager.getLore(dice));
            meta.setDisplayName(manager.getName(dice));

        }

        dice.setItemMeta(meta);
        return dice;
    }

    public static ItemStack get36bundle() {
        ItemStack bundle = CardCreator.getEmpty36bundle();
        BundleManager.saveItemsToBundle(bundle, CardCreator.get36Deck());
        return bundle;
    }

    public static ItemStack get52bundle() {
        ItemStack bundle = CardCreator.getEmpty52bundle();
        BundleManager.saveItemsToBundle(bundle, CardCreator.get52Deck());
        return bundle;
    }

    public static ItemStack get54bundle() {
        ItemStack bundle = CardCreator.getEmpty54bundle();
        BundleManager.saveItemsToBundle(bundle, CardCreator.get54Deck());
        return bundle;
    }

    public static ItemStack getChip(int subType) {
        ItemStack chip = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta meta = chip.getItemMeta();
        String type = Util.ItemTypes.Chip.getValue();
        if (meta != null){
            meta.getPersistentDataContainer().set(Util.ItemTags.Item.getValue(), PersistentDataType.STRING, type);
            meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "65:PICK_UP,193:PICK_UP,18:PLACE_MAIN,146:PLACE_MAIN,66:PLACE_TOP_MAIN,194:PLACE_TOP_MAIN,20:PLACE_LEFT,148:PLACE_LEFT,68:PLACE_TOP_LEFT,196:PLACE_TOP_LEFT");
            meta.getPersistentDataContainer().set(Util.ItemTags.Modifiers.getValue(), PersistentDataType.STRING, "18:RANDYAW,18:CGRID4,146:CGRID4,66:ALIGN,66:RANDYAW,194:ALIGN,20:RANDYAW,20:CGRID4,148:CGRID4,68:ALIGN,68:RANDYAW,196:ALIGN"); // ,66:ALIGN
            meta.getPersistentDataContainer().set(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER, subType);
            chip.setItemMeta(meta);
            meta.setCustomModelData(ValuesManager.getBaseCMD(chip));
            meta.setMaxStackSize(ValuesManager.getMaxStack(type));
            //more values form config?

            translationManager manager = translationManager.getInstance();

            meta.setLore(manager.getLore(chip));
            meta.setDisplayName(manager.getName(chip));

        }

        chip.setItemMeta(meta);
        return chip;
    }

    public static ItemStack getChipBundle(int subType) {
        return null;
    }


    public static ItemStack getEmptyBundle(int size) {
        ItemStack bundle = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta meta = bundle.getItemMeta();
        String type = Util.ItemTypes.Bundle.getValue();
        if (meta != null){
            meta.getPersistentDataContainer().set(Util.ItemTags.Item.getValue(), PersistentDataType.STRING, type);
            meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "65:PICK_UP,193:PICK_UP,18:GET_FROM_BUNDLE_MAIN,146:PLACE_MAIN,34:GET_FROM_BUNDLE_MAIN,162:GET_FROM_BUNDLE_MAIN,194:PLACE_TOP_MAIN,20:GET_FROM_BUNDLE_LEFT,148:PLACE_LEFT,36:GET_FROM_BUNDLE_LEFT,164:GET_FROM_BUNDLE_LEFT,68:PUT_FROM_GROUND,68:GET_FROM_BUNDLE_LEFT,196:PUT_FROM_GROUND,196:PLACE_LEFT,24:PUT_FROM_MAIN,24:GET_FROM_BUNDLE_MAIN,152:PUT_FROM_MAIN,152:GET_FROM_BUNDLE_MAIN,40:PUT_FROM_MAIN,168:PUT_FROM_MAIN,72:PUT_FROM_MAIN,72:PUT_FROM_GROUND,200:PUT_FROM_MAIN,200:PUT_FROM_GROUND");
            meta.getPersistentDataContainer().set(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER, 0);
            meta.getPersistentDataContainer().set(Util.ItemTags.Bundle.getValue(), PersistentDataType.STRING, Util.ItemTypes.Card.getValue());
            String saveType = ValuesManager.getSaveType(36); // magical 36 because it's a 36 card deck
            meta.getPersistentDataContainer().set(Util.ItemTags.BundleMeta.getValue(), PersistentDataType.STRING, saveType + ";36;random"); // save in data, as its the smallest deck, 36 cards max, get randomly.
            if (saveType.equals("data")) {
                meta.getPersistentDataContainer().set(Util.ItemTags.BundleData.getValue(), PersistentDataType.STRING, ""); // generate new bundle
            } else {
                meta.getPersistentDataContainer().set(Util.ItemTags.BundleData.getValue(), PersistentDataType.STRING, UUID.randomUUID().toString()); // generate new bundle
            }
            bundle.setItemMeta(meta);

            meta.setCustomModelData(ValuesManager.getBaseCMD(bundle));
            meta.setMaxStackSize(ValuesManager.getMaxStack(type));

            translationManager manager = translationManager.getInstance();
            meta.setLore(manager.getLore(bundle));
            meta.setDisplayName(manager.getName(bundle));
        }
        bundle.setItemMeta(meta);
        return bundle;
    }

}
