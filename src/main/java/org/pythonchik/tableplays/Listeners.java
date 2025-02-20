package org.pythonchik.tableplays;

import org.pythonchik.tableplays.managers.Util;
import org.pythonchik.tableplays.managers.Util.*;
import org.pythonchik.tableplays.managers.ValuesManager;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Listeners implements Listener {

    @EventHandler
    public void OnItemUse(PlayerStatisticIncrementEvent event) {
        // it's not even the right statistic - get out of here!
        if (!event.getStatistic().equals(Statistic.USE_ITEM) || event.getMaterial() == null || !event.getMaterial().equals(Material.WARPED_FUNGUS_ON_A_STICK)) {
            return;
        }
        Player player = event.getPlayer();
        ActionTagSet currentTag = new ActionTagSet();
        ItemStack mainStack = player.getInventory().getItemInMainHand();
        ItemStack offStack = player.getInventory().getItemInOffHand();

        //from what hand are we doing this?
        if (mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())
            && !(offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))) {
            currentTag.add(ActionTag.MAIN_HAND);
        } else if (!(mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))
                && offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())) {
            currentTag.add(ActionTag.LEFT_HAND);
        } else {
            currentTag.add(ActionTag.BOTH_HAND);
        }
        // we did not use any item, rather a regular warped fungus on a stick. lol who uses it?
        if (!currentTag.containsAny(ActionTag.MAIN_HAND, ActionTag.LEFT_HAND, ActionTag.BOTH_HAND)) {
            return;
        }
        //to not mess with actual uses of warped fungus
        if (player.getStatistic(Statistic.USE_ITEM, Material.WARPED_FUNGUS_ON_A_STICK) > 0) player.decrementStatistic(Statistic.USE_ITEM, Material.WARPED_FUNGUS_ON_A_STICK);

        // what did he click?
        if (player.getTargetBlockExact((int) player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(), FluidCollisionMode.NEVER) != null) {
            currentTag.add(ActionTag.ON_BLOCK);
        } else {
            currentTag.add(ActionTag.ON_AIR);
        }
        //is he holding shift?
        if (player.isSneaking()) {
            currentTag.add(ActionTag.WITH_SHIFT);
        }

        //now that we have all active tags I really need to handle only edge cases, like 2 items that _should_ work together, and leave the rest to the function I'm yet to create

        //now that we have handled the edge cases - throw this shit into this function ->
        handle_action(player, currentTag, currentTag.contains(ActionTag.MAIN_HAND) ? mainStack : null, currentTag.contains(ActionTag.LEFT_HAND) ? offStack : null, null);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        //We are not interested in non-my-interactions
        if (!event.getRightClicked().getType().equals(EntityType.INTERACTION) || !event.getRightClicked().getPersistentDataContainer().has(ItemTags.Entity.getValue()) || event.getRightClicked().getVehicle() == null || !(event.getRightClicked().getVehicle().getType() == EntityType.ITEM_DISPLAY) || !event.getRightClicked().getVehicle().getPersistentDataContainer().has(ItemTags.Entity.getValue())) {
            return;
        }

        Player player = event.getPlayer();
        ActionTagSet currentTag = new ActionTagSet();
        ItemStack mainStack = player.getInventory().getItemInMainHand();
        ItemStack offStack = player.getInventory().getItemInOffHand();
        if (mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())
                && !(offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))) {
            currentTag.add(ActionTag.MAIN_HAND);
        } else if (!(mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))
                && offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())) {
            currentTag.add(ActionTag.LEFT_HAND);
        } else if (!(mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))
                && !(offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))) {
            currentTag.add(ActionTag.NONE_HAND);
        } else {
            currentTag.add(ActionTag.BOTH_HAND);
        }
        currentTag.add(ActionTag.ON_ITEM);
        if (player.isSneaking()) {
            currentTag.add(ActionTag.WITH_SHIFT);
        }

        //now that we have all active tags I really need to handle only edge cases, like 2 items that _should_ work together, and leave the rest to the function I'm yet to create

        //now that we have handled the edge cases - throw this shit into this function ->
        handle_action(player, currentTag, currentTag.contains(ActionTag.MAIN_HAND) ? mainStack : null, currentTag.contains(ActionTag.LEFT_HAND) ? offStack : null, (Interaction) event.getRightClicked());
    }

    public static void handle_action(Player player, ActionTagSet currentTag, ItemStack mainStack, ItemStack offStack, Interaction interaction) {
        //this should be impossible, but we can never know
        if (mainStack == null && offStack == null && interaction == null) {
            return;
        }
        ItemStack groundStack = null;
        if (interaction != null && interaction.getVehicle() != null && interaction.getPersistentDataContainer().has(ItemTags.Entity.getValue()) && interaction.getVehicle().getType().equals(EntityType.ITEM_DISPLAY) && ((ItemDisplay) interaction.getVehicle()).getItemStack() != null) {
            groundStack = ((ItemDisplay) interaction.getVehicle()).getItemStack();
        }
        // priority - groud - main - off
        ArrayList<ActionTagSet> requested_actions = new ArrayList<>();
        String working_action = "";
        // data format = "int:action,int:action,int:action" executed from left to right by priority.
        // for example for bundle that might be = "134:PICK_UP_ITEM,292:PUT_DOWN"
        // that means that if we have action 134(on item + left hand) then we do action PICK_UP, then, if fails, we check for 292(with shift, on block, main hand) and if yes - place down bundle

        if (groundStack != null && groundStack.getItemMeta() != null && groundStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Actions.getValue())) {
            String[] data = (groundStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Actions.getValue(), PersistentDataType.STRING)).split(","); // ignore null execution on split, can not be(well, it might if Actions tag will be not a string, but who cares
            for (String currentCheck : data) {
                String[] split = currentCheck.split(":");
                if (split.length > 1 && split[0].equals(currentTag.toString())) { // split is in the correct format, and we are doing the correct action
                    ArrayList<String> modifiers = Util.getModifiers(groundStack, split[1]);
                    executeAction(player, split[1], modifiers, mainStack, offStack, interaction);
                    return;
                }
            }
        }

        if (mainStack != null && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Actions.getValue())) {
            String[] data = (mainStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Actions.getValue(), PersistentDataType.STRING)).split(","); // ignore null execution on split, can not be(well, it might if Actions tag will be not a string, but who cares
            for (String currentCheck : data) {
                String[] split = currentCheck.split(":");
                if (split.length > 1 && split[0].equals(currentTag.toString())) { // split is in the correct format, and we are doing the correct action
                    ArrayList<String> modifiers = Util.getModifiers(mainStack, split[1]);
                    executeAction(player, split[1], modifiers, mainStack, offStack, interaction);
                    return;
                }
            }
        }

        if (offStack != null && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Actions.getValue())) {
            String[] data = (offStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Actions.getValue(), PersistentDataType.STRING)).split(","); // ignore null execution on split, can not be(well, it might if Actions tag will be not a string, but who cares
            for (String currentCheck : data) {
                String[] split = currentCheck.split(":");
                if (split.length > 1 && split[0].equals(currentTag.toString())) { // split is in the correct format, and we are doing the correct action
                    ArrayList<String> modifiers = Util.getModifiers(offStack, split[1]);
                    executeAction(player, split[1], modifiers, mainStack, offStack, interaction);
                    return;
                }
            }
        }
    }
    //make a lot of functions to handle actions with card, maybe transport here also some params, maybe in hashmap format, but how to get the obj?

    public static boolean executeAction(Player player, String action, ArrayList<String> modifiers, ItemStack mainStack, ItemStack offStack, Interaction interaction) {
        switch (action) {
            //place X down, main hand
            case "PLACE_MAIN" -> {
                Location toPlace = Util.getBlockEyeLoc(player);
                Interaction spawned_interaction = player.getWorld().spawn(toPlace, Interaction.class);
                ItemDisplay display = player.getWorld().spawn(toPlace, ItemDisplay.class);
                spawned_interaction.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                display.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                List<Float> hitbox = ValuesManager.getItemHitbox(mainStack); // should return 2 values, width and height, in that order
                if (hitbox != null) {
                    spawned_interaction.setInteractionWidth(hitbox.getFirst());
                    spawned_interaction.setInteractionHeight(hitbox.getLast());
                }
                //item handling
                ItemStack single_item = mainStack.clone();

                single_item = applyItemModifiers(single_item, modifiers);

                single_item.setAmount(1);
                display.setItemStack(single_item);

                if (player.getInventory().getItemInMainHand().getAmount() == 1) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
                }

                //correct size and shit for the model, data driven
                display.setTransformation(ValuesManager.getTransformation(single_item));

                //make them aligned to the ground
                display.setRotation(display.getLocation().getYaw(),0);

                applyInteractionModifiers(spawned_interaction, modifiers);

                display.addPassenger(spawned_interaction);
                break;
            }
            //place X down, left hand
            case "PLACE_LEFT" -> {
                Location toPlace = Util.getBlockEyeLoc(player);
                Interaction spawned_interaction = player.getWorld().spawn(toPlace, Interaction.class);
                ItemDisplay display = player.getWorld().spawn(toPlace, ItemDisplay.class);
                spawned_interaction.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                display.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                List<Float> hitbox = ValuesManager.getItemHitbox(offStack); // should return 2 values, width and height, in that order
                if (hitbox != null) {
                    spawned_interaction.setInteractionWidth(hitbox.getFirst());
                    spawned_interaction.setInteractionHeight(hitbox.getLast());
                }
                //item handling
                ItemStack single_item = offStack.clone();

                single_item = applyItemModifiers(single_item, modifiers);

                single_item.setAmount(1);
                display.setItemStack(single_item);

                if (player.getInventory().getItemInOffHand().getAmount() == 1) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount()-1);
                }

                //correct size and shit for the model, data driven
                display.setTransformation(ValuesManager.getTransformation(single_item));

                //make item aligned to the ground
                display.setRotation(display.getLocation().getYaw(),0);
                
                applyInteractionModifiers(spawned_interaction, modifiers);

                display.addPassenger(spawned_interaction);
                break;
            }
            // place X on top of item, main hand
            case "PLACE_TOP_MAIN" -> {

            }
            //TODO make more modes

            // pick up from the ground
            case "PICK_UP" -> {
                ItemDisplay display = (ItemDisplay) interaction.getVehicle();
                ItemStack groundStack = display.getItemStack();
                groundStack = applyItemModifiers(groundStack, modifiers);
                player.getInventory().addItem(groundStack);
                interaction.remove();
                display.remove();
                break;
            }
            default -> {
                return true;
            }
        }
        // in reality never used as switch covers all cases and always returns something
        return true;

    }

    public static ItemStack applyItemModifiers(ItemStack stack, ArrayList<String> modifiers) {
        for (String modifier : modifiers) {
            if (modifier.toUpperCase().matches("RCMDP[123456789]\\d*")) {
                int bound = Integer.parseInt(modifier.substring(5));
                int number = new Random().nextInt(0, bound);
                ItemMeta meta = stack.getItemMeta();
                meta.setCustomModelData(meta.getCustomModelData()+number);
                stack.setItemMeta(meta);
            } else if (modifier.toUpperCase().equals("RESETCMD")) {
                ItemMeta meta = stack.getItemMeta();
                meta.setCustomModelData(TablePlays.config.getInt("items." + meta.getPersistentDataContainer().get(ItemTags.Type.getValue(), PersistentDataType.STRING) + ".basecmd"));
                stack.setItemMeta(meta);
            }
        }
        return stack;
    }

    public static Interaction applyInteractionModifiers(Interaction interaction, ArrayList<String> modifiers) {
        //TODO this
        for (String modifier : modifiers) {

        }
        return interaction;
    }


}
