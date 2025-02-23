package org.pythonchik.tableplays.managers.modifiers;

import java.util.List;

public class AlignModifier implements BaseModifier{
    @Override
    public void apply(ModifierContext context, String modifier, List<String> allModifiers) {
        context.getVector().ifPresent(clicked_pos -> {
            clicked_pos.setZ(0);
            clicked_pos.setX(0);
        });
    }
}
