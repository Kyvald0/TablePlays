package org.pythonchik.tableplays.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemCreator {
    // new iteration of big data, but better and not so big :(

    public static ItemStack getDice() {
        ItemStack dice = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta meta = dice.getItemMeta();
        String type = "dice";
        if (meta != null){
            meta.getPersistentDataContainer().set(Util.ItemTags.Item.getValue(), PersistentDataType.STRING, type);
            meta.getPersistentDataContainer().set(Util.ItemTags.Actions.getValue(), PersistentDataType.STRING, "18:PLACE_MAIN,20:PLACE_LEFT,146:PLACE_MAIN,148:PLACE_LEFT,194:PLACE_TOP_MAIN,196:PLACE_TOP_LEFT,65:PICK_UP,66:PICK_UP,68:PICK_UP");
            meta.getPersistentDataContainer().set(Util.ItemTags.Modifiers.getValue(), PersistentDataType.STRING, "18:RCMDP6,20:RCMDP6,146:RCMDP6,148:RCMDP6,194:RCMDP6,196:RCMDP6,65:RESETCMD,66:RESETCMD,68:RESETCMD,196:ALIGN"); // ,66:ALIGN
            meta.getPersistentDataContainer().copyTo(meta.getPersistentDataContainer(), true);
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


}
