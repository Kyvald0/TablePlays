package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CGridModifier implements BaseModifier {

    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        //modifier looks like this: CGRID4
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getLocation().ifPresent(spawn_loc -> {
            context.getVector().ifPresent(clicked_pos -> {
                int divisions = Integer.parseInt(modifier.substring(5));
                if (divisions <= 0) return;
                Location blockLocation = spawn_loc.getBlock().getLocation();
                double regionSize = 1.0 / divisions;
                spawn_loc.setX(blockLocation.getX() + regionSize * ((int) (clicked_pos.getX() / regionSize) + 0.5));
                spawn_loc.setZ(blockLocation.getZ() + regionSize * ((int) (clicked_pos.getZ() / regionSize) + 0.5));
                flag.set(true);
            });
        });
        return flag.get();
    }
}
