package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class RandomCMDModifier implements BaseModifier {

    @Override
    public void apply(ModifierContext context, String modifier, List<String> allModifiers) {
        context.getItemStack().ifPresent(stack -> {
            if (stack.getItemMeta() != null) {
                int bound = Integer.parseInt(modifier.substring(5));
                int number = new Random().nextInt(0, bound);
                ItemMeta meta = stack.getItemMeta();
                meta.setCustomModelData(meta.getCustomModelData() + number);
                stack.setItemMeta(meta);
            }
        });
    }
}
