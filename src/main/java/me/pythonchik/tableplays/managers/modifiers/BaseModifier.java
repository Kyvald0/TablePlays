package me.pythonchik.tableplays.managers.modifiers;

import java.util.List;

public interface BaseModifier {
    boolean apply(ModifierContext context, String modifier, List<String> allModifiers);
}
