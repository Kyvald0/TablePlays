package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.inventory.meta.ItemMeta;
import org.pythonchik.tableplays.managers.ValuesManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShiftCMDModifier implements BaseModifier {
    //shift12332
    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getItemStack().ifPresent(stack -> {
            ItemMeta meta = stack.getItemMeta();
            int basecmd = ValuesManager.getBaseCMD(stack);
            if (basecmd == stack.getItemMeta().getCustomModelData()) {
                meta.setCustomModelData(basecmd+Integer.parseInt(modifier.substring(5)));
            } else {
                meta.setCustomModelData(basecmd);
            }
            stack.setItemMeta(meta);
            flag.set(true);
        });
        return flag.get();
    }
}
