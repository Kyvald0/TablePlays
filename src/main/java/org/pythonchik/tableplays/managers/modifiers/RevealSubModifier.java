package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.tableplays.managers.Util;
import org.pythonchik.tableplays.managers.ValuesManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RevealSubModifier implements BaseModifier {

    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        // REVSUB
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getItemStack().ifPresent(old_stack -> {
            ItemStack stack = old_stack.clone();
            if (stack.getItemMeta() != null && stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.ExtraData.getValue(), PersistentDataType.STRING)) {

                ItemMeta meta = stack.getItemMeta();
                String[] extras = meta.getPersistentDataContainer().get(Util.ItemTags.ExtraData.getValue(), PersistentDataType.STRING).split(";");

                for (String extra : extras) {
                    String[] split = extra.split(":");
                    if (split[0].equals("subtype") && split.length >= 2) {
                        // put subtype back
                        meta.getPersistentDataContainer().set(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER, Integer.parseInt(split[1]));
                        break;
                    }
                }


                stack.setItemMeta(meta);
                meta.setCustomModelData(ValuesManager.getBaseCMD(stack));
                stack.setItemMeta(meta);
                if (context.getInteraction().isPresent() && context.getInteraction().get().getVehicle() != null && context.getInteraction().get().getVehicle().getType().equals(EntityType.ITEM_DISPLAY)) {
                    ((ItemDisplay) context.getInteraction().get().getVehicle()).setItemStack(stack);
                    flag.set(true);
                } else {
                    old_stack = stack;
                }
            }
        });
        return flag.get();
    }
}
