package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.entity.Entity;

import java.util.List;

public class FlipModifier implements BaseModifier {


    @Override
    public void apply(ModifierContext context, String modifier, List<String> allModifiers) {
        context.getInteraction().ifPresent(interaction -> {
            //interaction here is a spawned interaction, I need to get its itemDisplay, and rotate yaw to 180
            if (interaction.getVehicle() != null) {
                Entity display = interaction.getVehicle();
                display.setRotation(display.getLocation().getYaw(),display.getLocation().getPitch() == 0 ? 180 : 0);
            }
        });
    }
}
