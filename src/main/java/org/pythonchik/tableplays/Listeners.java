package org.pythonchik.tableplays;

import org.bukkit.*;
import org.bukkit.util.Vector;
import org.pythonchik.tableplays.managers.BundleManager;
import org.pythonchik.tableplays.managers.ModifierManager;
import org.pythonchik.tableplays.managers.Util;
import org.pythonchik.tableplays.managers.Util.*;
import org.pythonchik.tableplays.managers.ValuesManager;
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
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.tableplays.managers.modifiers.ModifierContext;

import java.util.*;

public class Listeners implements Listener {

    private static HashMap<Player, Long> ItemUses = new HashMap<>();

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

        //prevent double-calling from holding an item in 2 hands
        if (System.currentTimeMillis() - ItemUses.getOrDefault(player, 0L) < 1000/Bukkit.getServer().getServerTickManager().getTickRate()) {
            event.setCancelled(true);
            return;
        }

        //from what hand are we doing this?
        if (mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())
            && !(offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))) {
            currentTag.add(ActionTag.MAIN_HAND);
        } else if (!(mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()))
                && offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())) {
            currentTag.add(ActionTag.LEFT_HAND);
        } else if (mainStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())
                && offStack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())) {
            currentTag.add(ActionTag.BOTH_HAND);
        }

        // we did not use any item, rather a regular warped fungus on a stick. lol who uses it?
        if (!currentTag.containsAny(ActionTag.MAIN_HAND, ActionTag.LEFT_HAND, ActionTag.BOTH_HAND)) {
            return;
        }

        ItemUses.put(player, System.currentTimeMillis()); // save that we have triggered event

        //to not mess with actual uses of warped fungus
        player.setStatistic(event.getStatistic(), event.getMaterial(), event.getPreviousValue()); // for some reason does not work as intended, just does not.

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
        handle_action(player, currentTag, currentTag.containsAny(ActionTag.MAIN_HAND, ActionTag.BOTH_HAND) ? mainStack : null, currentTag.containsAny(ActionTag.LEFT_HAND, ActionTag.BOTH_HAND) ? offStack : null, null);
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
        handle_action(player, currentTag, currentTag.contains(ActionTag.MAIN_HAND) ? mainStack : null, currentTag.contains(ActionTag.LEFT_HAND) ? offStack : null, (Interaction) event.getRightClicked(), event.getClickedPosition());
    }

    public static void handle_action(Player player, ActionTagSet currentTag, ItemStack mainStack, ItemStack offStack, Interaction interaction, Object... args) {
        //this should be impossible, but we can never know
        if (mainStack == null && offStack == null && interaction == null) {
            return;
        }
        ItemStack groundStack = null;
        if (interaction != null && interaction.getVehicle() != null && interaction.getPersistentDataContainer().has(ItemTags.Entity.getValue()) && interaction.getVehicle().getType().equals(EntityType.ITEM_DISPLAY) && ((ItemDisplay) interaction.getVehicle()).getItemStack() != null) {
            groundStack = ((ItemDisplay) interaction.getVehicle()).getItemStack();
        }
        // priority - groud - main - off
        // data format = "int:action,int:action,int:action" executed from left to right by priority.
        // for example for bundle that might be = "134:PICK_UP_ITEM,292:PUT_DOWN"
        // that means that if we have action 134(on item + left hand) then we do action PICK_UP, then, if fails, we check for 292(with shift, on block, main hand) and if yes - place down bundle
        if (groundStack != null && groundStack.getItemMeta() != null && groundStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Actions.getValue())) {
            String[] data = (groundStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Actions.getValue(), PersistentDataType.STRING)).split(","); // ignore null execution on split, can not be(well, it might if Actions tag will be not a string, but who cares
            for (String currentCheck : data) {
                String[] split = currentCheck.split(":");
                if (split.length > 1 && split[0].equals(currentTag.toString())) { // split is in the correct format, and we are doing the correct action
                    ArrayList<String> modifiers = Util.getModifiers(groundStack, currentTag.toString());
                    // if we return false, e.g. do not continue -> then do not continue
                    if (!executeAction(player, split[1], modifiers, mainStack, offStack, interaction, args)) return;
                }
            }
        }

        if (mainStack != null && mainStack.getItemMeta() != null && mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Actions.getValue())) {
            String[] data = (mainStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Actions.getValue(), PersistentDataType.STRING)).split(","); // ignore null execution on split, can not be(well, it might if Actions tag will be not a string, but who cares
            for (String currentCheck : data) {
                String[] split = currentCheck.split(":");
                if (split.length > 1 && split[0].equals(currentTag.toString())) { // split is in the correct format, and we are doing the correct action
                    ArrayList<String> modifiers = Util.getModifiers(mainStack, currentTag.toString());
                    // if we return false, e.g. do not continue -> then do not continue
                    if (!executeAction(player, split[1], modifiers, mainStack, offStack, interaction, args)) return;
                }
            }
        }
        if (offStack != null && offStack.getItemMeta() != null && offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Actions.getValue())) {
            String[] data = (offStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Actions.getValue(), PersistentDataType.STRING)).split(","); // ignore null execution on split, can not be(well, it might if Actions tag will be not a string, but who cares
            for (String currentCheck : data) {
                String[] split = currentCheck.split(":");
                if (split.length > 1 && split[0].equals(currentTag.toString())) { // split is in the correct format, and we are doing the correct action
                    ArrayList<String> modifiers = Util.getModifiers(offStack, currentTag.toString());
                    // if we return false, e.g. do not continue -> then do not continue
                    if (!executeAction(player, split[1], modifiers, mainStack, offStack, interaction, args)) return;
                }
            }
        }
    }
    //make a lot of functions to handle actions with card, maybe transport here also some params, maybe in hashmap format, but how to get the obj?

    public static boolean executeAction(Player player, String action, ArrayList<String> modifiers, ItemStack mainStack, ItemStack offStack, Interaction interaction, Object... args) {
        switch (action.toUpperCase()) {
            //place X down, main hand
            case "PLACE_MAIN" -> {
                Location toPlace = Util.getBlockEyeLoc(player);

                ModifierContext context = new ModifierContext(player, null, null, toPlace, null);
                ModifierManager.applyModifiers(context, modifiers);

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

                context = new ModifierContext(player, single_item, spawned_interaction, toPlace, null);
                ModifierManager.applyModifiers(context, modifiers);

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
                display.setRotation(display.getLocation().getYaw(),90);

                display.addPassenger(spawned_interaction);

                context = new ModifierContext(player, single_item, spawned_interaction, toPlace, null);
                ModifierManager.applyModifiers(context, modifiers);
                return false;
            }
            //place X down, left hand
            case "PLACE_LEFT" -> {
                Location toPlace = Util.getBlockEyeLoc(player);
                //modify the location of spawn if needed
                ModifierContext context = new ModifierContext(player, null, null, toPlace, null);
                ModifierManager.applyModifiers(context, modifiers);

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

                //modify item if needed
                context = new ModifierContext(player, single_item, spawned_interaction, null, null);
                ModifierManager.applyModifiers(context, modifiers);

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
                display.setRotation(display.getLocation().getYaw(),90);

                display.addPassenger(spawned_interaction);
                //final apply if not already
                context = new ModifierContext(player, single_item, spawned_interaction, toPlace, null);
                ModifierManager.applyModifiers(context, modifiers);

                return false;
            }
            // place X on top of an item, main hand
            case "PLACE_TOP_MAIN" -> {
                if (args.length == 0 || interaction == null) {
                    return true;
                }
                Vector clicked_pos = (Vector) args[0];
                clicked_pos.setY(interaction.getInteractionHeight()+0.001); // offset for not clipping
                Location spawn_loc = interaction.getLocation();
                spawn_loc.setYaw(player.getLocation().getYaw());

                ModifierContext context = new ModifierContext(player, null, null, spawn_loc, clicked_pos);
                ModifierManager.applyModifiers(context, modifiers);

                spawn_loc.add(clicked_pos);
                Interaction spawned_interaction = player.getWorld().spawn(spawn_loc, Interaction.class);
                ItemDisplay display = player.getWorld().spawn(spawn_loc, ItemDisplay.class);
                spawned_interaction.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                display.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                List<Float> hitbox = ValuesManager.getItemHitbox(mainStack); // should return 2 values, width and height, in that order
                if (hitbox != null) {
                    spawned_interaction.setInteractionWidth(hitbox.getFirst());
                    spawned_interaction.setInteractionHeight(hitbox.getLast());
                }
                //item handling
                ItemStack single_item = mainStack.clone();

                context = new ModifierContext(player, single_item, spawned_interaction, spawn_loc, clicked_pos);
                ModifierManager.applyModifiers(context, modifiers);

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
                display.setRotation(display.getLocation().getYaw(),90);

                display.addPassenger(spawned_interaction);
                //final-check to apply everything if needed
                context = new ModifierContext(player, single_item, spawned_interaction, spawn_loc, clicked_pos);
                ModifierManager.applyModifiers(context, modifiers);

                return false;
            }
            // place X on top of an item, left hand
            case "PLACE_TOP_LEFT" -> {
                if (args.length == 0 || interaction == null) {
                    return true;
                }
                Vector clicked_pos = (Vector) args[0];
                clicked_pos.setY(interaction.getInteractionHeight()+0.001); // offset for not clipping
                Location spawn_loc = interaction.getLocation();
                spawn_loc.setYaw(player.getLocation().getYaw());

                ModifierContext context = new ModifierContext(player, null, null, spawn_loc, clicked_pos);
                ModifierManager.applyModifiers(context, modifiers);

                spawn_loc.add(clicked_pos);
                Interaction spawned_interaction = player.getWorld().spawn(spawn_loc, Interaction.class);
                ItemDisplay display = player.getWorld().spawn(spawn_loc, ItemDisplay.class);
                spawned_interaction.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                display.getPersistentDataContainer().set(ItemTags.Entity.getValue(), PersistentDataType.BOOLEAN, true);
                List<Float> hitbox = ValuesManager.getItemHitbox(offStack); // should return 2 values, width and height, in that order
                if (hitbox != null) {
                    spawned_interaction.setInteractionWidth(hitbox.getFirst());
                    spawned_interaction.setInteractionHeight(hitbox.getLast());
                }
                //item handling
                ItemStack single_item = offStack.clone();

                context = new ModifierContext(player, single_item, spawned_interaction, spawn_loc, clicked_pos);
                ModifierManager.applyModifiers(context, modifiers);

                single_item.setAmount(1);
                display.setItemStack(single_item);

                if (player.getInventory().getItemInOffHand().getAmount() == 1) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount()-1);
                }

                //correct size and shit for the model, data driven
                display.setTransformation(ValuesManager.getTransformation(single_item));

                //make them aligned to the ground
                display.setRotation(display.getLocation().getYaw(),90);

                display.addPassenger(spawned_interaction);

                context = new ModifierContext(player, single_item, spawned_interaction, spawn_loc, clicked_pos);
                ModifierManager.applyModifiers(context, modifiers);

                return false;
            }
            // suck to bundle in off hand from the main hand
            case "PUT_FROM_MAIN" -> {
                //assumption is - used in bundle to requested to put the item in bundle, the bundle is always in off hand, and the item is in main hand
                if (offStack == null || offStack.getItemMeta() == null || !offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()) || !offStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Item.getValue(), PersistentDataType.STRING).equals(ItemTypes.Bundle.getValue()))  {
                    return true;
                }
                if (mainStack == null || mainStack.getItemMeta() == null || !mainStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())) {
                    return true;
                }
                ItemStack single_item = mainStack.clone();
                single_item.setAmount(1);

                ModifierContext context = new ModifierContext(player, single_item, null, null, null);
                ModifierManager.applyModifiers(context, modifiers);

                boolean result = BundleManager.addToBundle(offStack, single_item);
                if (result) {
                    mainStack.setAmount(mainStack.getAmount()-1);
                    return false;
                }

                return true;
            }
            // suck to bundle in the off hand from the ground
            case "PUT_FROM_GROUND" -> {
                //assumption is - used in bundle to requested to put the item in bundle, the bundle is always in off hand, and the item is in the ground
                if (offStack == null || offStack.getItemMeta() == null || !offStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue()) || offStack.getItemMeta().getPersistentDataContainer().get(ItemTags.Item.getValue(), PersistentDataType.STRING).equals(ItemTypes.Bundle.getValue()))  {
                    return true;
                }
                if (interaction == null) {
                    return true;
                }
                ItemStack groundStack =  ((ItemDisplay) interaction.getVehicle()).getItemStack();
                if (groundStack == null || groundStack.getItemMeta() == null || !groundStack.getItemMeta().getPersistentDataContainer().has(ItemTags.Item.getValue())) {
                    return true;
                }
                ItemStack single_item = groundStack.clone();
                single_item.setAmount(1);

                ModifierContext context = new ModifierContext(player, single_item, null, null, null);
                ModifierManager.applyModifiers(context, modifiers);

                boolean result = BundleManager.addToBundle(offStack, single_item);
                if (result) {
                    if (groundStack.getAmount() == 1) {
                        // no items left, just kill this thing
                        interaction.getVehicle().remove();
                        interaction.remove();
                    } else {
                        groundStack.setAmount(groundStack.getAmount()-1);
                        ((ItemDisplay)interaction.getVehicle()).setItemStack(groundStack);
                    }
                    return false;
                }

                return true;
            }
            // get from the bundle in main
            case "GET_FROM_BUNDLE_MAIN" -> {
                if (!BundleManager.isValidBundle(mainStack)) {
                    return true;
                }
                ArrayList<ItemStack> items = BundleManager.getBundleItems(mainStack);
                if (items.isEmpty()) return true;
                String order = mainStack.getItemMeta().getPersistentDataContainer().get(ItemTags.BundleMeta.getValue(), PersistentDataType.STRING).split(";")[2]; // literal 'random', 'stack', or 'queue'
                ItemStack toAdd;
                int index;
                if (order.equals("random")) {
                    index = new Random(0).nextInt(0, items.size()-1);
                    toAdd = items.get(index);
                } else if (order.equals("stack")) {
                    index = items.size()-1;
                    toAdd = items.getLast();
                } else {
                    index = 0;
                    toAdd = items.getFirst();
                }
                items.remove(index);
                BundleManager.saveItemsToBundle(mainStack, items);
                HashMap<Integer, ItemStack> left = player.getInventory().addItem(toAdd);
                if (!left.isEmpty()) {
                    for (ItemStack leftover : left.values()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), leftover);
                    }
                }
                return false;
            }
            // get from the bundle in main
            case "GET_FROM_BUNDLE_LEFT" -> {
                if (!BundleManager.isValidBundle(offStack)) {
                    return true;
                }
                ArrayList<ItemStack> items = BundleManager.getBundleItems(offStack);
                if (items.isEmpty()) return true;
                String order = offStack.getItemMeta().getPersistentDataContainer().get(ItemTags.BundleMeta.getValue(), PersistentDataType.STRING).split(";")[2]; // literal 'random', 'stack', or 'queue'
                ItemStack toAdd;
                int index;
                if (order.equals("random")) {
                    index = new Random(0).nextInt(0, items.size()-1);
                    toAdd = items.get(index);
                } else if (order.equals("stack")) {
                    index = items.size()-1;
                    toAdd = items.getLast();
                } else {
                    index = 0;
                    toAdd = items.getFirst();
                }
                items.remove(index);
                BundleManager.saveItemsToBundle(offStack, items);
                HashMap<Integer, ItemStack> left = player.getInventory().addItem(toAdd);
                if (!left.isEmpty()) {
                    for (ItemStack leftover : left.values()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), leftover);
                    }
                }
                return false;
            }
            // pick up from the ground
            case "PICK_UP" -> {
                ItemDisplay display = (ItemDisplay) interaction.getVehicle();
                ItemStack groundStack = display.getItemStack();

                ModifierContext context = new ModifierContext(player, groundStack, null, null, null);
                ModifierManager.applyModifiers(context, modifiers);

                HashMap<Integer, ItemStack> left = player.getInventory().addItem(groundStack);
                if (!left.isEmpty()) {
                    for (ItemStack leftover : left.values()) {
                        player.getWorld().dropItemNaturally(interaction.getLocation(), leftover);
                    }
                }
                interaction.remove();
                display.remove();
                return false;
            }
            //TODO make `nothing` action, to only apply modifiers(e.g. flip)
            //TODO make more modes

            default -> {
                return true;
            }
        }
    }

}
