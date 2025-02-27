package org.pythonchik.tableplays.managers.modifiers;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class RandYawModifier implements BaseModifier {

    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getLocation().ifPresent(spawn_loc -> {
            spawn_loc.setYaw(new Random().nextInt(360));
            flag.set(true);
        });
        return flag.get();
    }
}
