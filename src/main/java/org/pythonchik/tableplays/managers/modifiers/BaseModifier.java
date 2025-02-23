package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface BaseModifier {
    public abstract void apply(ModifierContext context, String modifier, List<String> allModifiers);
}
