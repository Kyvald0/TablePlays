package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.pythonchik.tableplays.managers.Util;
import org.pythonchik.tableplays.managers.ValuesManager;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PushModifier implements BaseModifier {
    @Override
    public boolean apply(ModifierContext context, String modifier, List<String> allModifiers) {
        AtomicBoolean flag = new AtomicBoolean(false);
        context.getLocation().ifPresent(spawn_loc -> {
            context.getItemStack().ifPresent(stack -> {
                List<Float> hitbox = ValuesManager.getItemHitbox(stack);
                boolean adjusted;
                int maxIterations = 100; // I doubt you will have 100 items stacked on top of each other and you will click on the bottom one.
                do {
                    Collection<Entity> nearbyEntities = spawn_loc.getWorld().getNearbyEntities(spawn_loc, hitbox.get(0), hitbox.get(1), hitbox.get(0));
                    adjusted = false;
                    double currentY = spawn_loc.getY();
                    double highestEntityTop = -Double.MAX_VALUE;

                    for (Entity entity : nearbyEntities) {
                        if (entity.getPersistentDataContainer().has(Util.ItemTags.Entity.getValue())) {
                            Entity vehicle = entity.getVehicle();
                            if (vehicle != null && vehicle.getType().equals(EntityType.ITEM_DISPLAY) && vehicle.getPersistentDataContainer().has(Util.ItemTags.Entity.getValue()) && ((ItemDisplay) vehicle).getItemStack() != null) {
                                List<Float> entityHitbox = ValuesManager.getItemHitbox(((ItemDisplay) vehicle).getItemStack());
                                double entityTop = entity.getLocation().getY() + entityHitbox.get(1);
                                if (entityTop > currentY) {
                                    highestEntityTop = Math.max(highestEntityTop, entityTop);
                                }
                            }
                        }
                    }
                    if (highestEntityTop != -Double.MAX_VALUE) {
                        spawn_loc.setY(highestEntityTop);
                        adjusted = true;
                    } else {
                        spawn_loc.setY(currentY + 0.001);
                        break;
                    }
                } while (adjusted && maxIterations-- > 0);

                flag.set(true);
            });
        });
        return flag.get();
    }
}
