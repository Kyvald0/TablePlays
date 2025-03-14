package me.pythonchik.tableplays.managers.modifiers;

import org.bukkit.Location;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BLGRIDModifier implements BaseModifier {

    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        //modifier looks like this: BLGRID1
        // its the same as CGRID, but not Center Grid, rather BottomLeft Grid (Dont know if its always bottom left, but hey!)
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getLocation().ifPresent(spawn_loc -> {
            context.getVector().ifPresent(clicked_pos -> {
                int divisions = Integer.parseInt(modifier.substring(6));
                if (divisions <= 0) return;
                Location blockLocation = spawn_loc.getBlock().getLocation();
                double regionSize = 1.0 / divisions;
                spawn_loc.setX(blockLocation.getX() + regionSize * ((int) (clicked_pos.getX() / regionSize)));
                spawn_loc.setZ(blockLocation.getZ() + regionSize * ((int) (clicked_pos.getZ() / regionSize)));
                flag.set(true);
            });
        });
        return flag.get();
    }
}
