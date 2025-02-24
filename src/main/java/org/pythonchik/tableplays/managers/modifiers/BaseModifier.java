package org.pythonchik.tableplays.managers.modifiers;

import java.util.List;

public interface BaseModifier {
    public abstract boolean apply(ModifierContext context, String modifier, List<String> allModifiers);
}
