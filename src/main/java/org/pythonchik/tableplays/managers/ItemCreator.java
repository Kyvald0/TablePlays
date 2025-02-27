package org.pythonchik.tableplays.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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



}
