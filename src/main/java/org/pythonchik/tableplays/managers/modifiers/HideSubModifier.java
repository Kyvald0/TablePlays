package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.tableplays.managers.Util;
import org.pythonchik.tableplays.managers.ValuesManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HideSubModifier implements BaseModifier {
    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        // HIDESUB70
        // HIDESUB# with int - what should it be replaced with
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getItemStack().ifPresent(stack -> {
            if (stack.getItemMeta() != null && stack.getItemMeta().getPersistentDataContainer().has(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER)) {
                ItemMeta meta = stack.getItemMeta();

                meta.getPersistentDataContainer().set(Util.ItemTags.ExtraData.getValue(), PersistentDataType.STRING, "subtype:" + stack.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER));
                meta.getPersistentDataContainer().set(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER, Integer.parseInt(modifier.substring(7)));

                stack.setItemMeta(meta);

                meta.setCustomModelData(ValuesManager.getBaseCMD(stack));
                stack.setItemMeta(meta);
                flag.set(true);
            }
        });
        return flag.get();
    }
}

