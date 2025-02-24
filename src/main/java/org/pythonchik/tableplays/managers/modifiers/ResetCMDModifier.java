package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.inventory.meta.ItemMeta;
import org.pythonchik.tableplays.managers.ValuesManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResetCMDModifier implements BaseModifier {

    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getItemStack().ifPresent(stack -> {
            if (stack.getItemMeta() != null) {
                ItemMeta meta = stack.getItemMeta();
                meta.setCustomModelData(ValuesManager.getBaseCMD(stack));
                stack.setItemMeta(meta);
                flag.set(true);
            }
        });
        return flag.get();
    }
}
