package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.entity.Entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RotateModifier implements BaseModifier {
    //rot#
    //rot90 - rotate item to a nearest multiple of 90 degrees
    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getInteraction().ifPresent(interaction -> {
            //interaction here is a spawned interaction, I need to get its itemDisplay, and rotate pitch to 180
            if (interaction.getVehicle() != null) {
                Entity display = interaction.getVehicle();
                int alignTo = Integer.parseInt(modifier.substring(3));
                float yaw = display.getLocation().getYaw()+0.01f; // from 0 to 360 (0.01 for the case when its already aligned to rotate still
                float nextMultiple = ((float) Math.ceil(yaw / alignTo)) * alignTo;
                if (nextMultiple > 360) {
                    nextMultiple = alignTo;
                }
                display.setRotation(nextMultiple, display.getLocation().getPitch());
                flag.set(true);
            }
        });
        return flag.get();
    }
}
