package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pythonchik.tableplays.managers.ValuesManager;

import java.util.List;

public class ResetCMDModifier implements BaseModifier {

    @Override
    public void apply(ModifierContext context, String modifier, List<String> allModifiers) {
        context.getItemStack().ifPresent(stack -> {
            if (stack.getItemMeta() != null) {
                ItemMeta meta = stack.getItemMeta();
                meta.setCustomModelData(ValuesManager.getBaseCMD(stack));
                stack.setItemMeta(meta);
            }
        });
    }
}
