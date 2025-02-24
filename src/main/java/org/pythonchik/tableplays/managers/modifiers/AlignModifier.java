package org.pythonchik.tableplays.managers.modifiers;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AlignModifier implements BaseModifier{
    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getVector().ifPresent(clicked_pos -> {
            clicked_pos.setZ(0);
            clicked_pos.setX(0);
            flag.set(true);
        });
        return flag.get();
    }
}
