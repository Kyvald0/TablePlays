package org.pythonchik.tableplays.managers.modifiers;

import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Optional;

public class ModifierContext {
    private final ItemStack itemStack; // stack in question
    private final Interaction interaction; // interaction of an item
    private final Player player; // player who made me do this (me:( )
    private final Location location; // clicked location on the interaction
    private final Vector clicked_pos;

    public ModifierContext(Player player, ItemStack itemStack, Interaction interaction, Location location, Vector clicked_pos) {
        this.itemStack = itemStack;
        this.interaction = interaction;
        this.player = player;
        this.location = location;
        this.clicked_pos = clicked_pos;
    }

    public Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(itemStack);
    }

    public Optional<Interaction> getInteraction() {
        return Optional.ofNullable(interaction);
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    public Optional<Vector> getVector() {return Optional.ofNullable(clicked_pos);}

    @Override
    public String toString() {
        return "ModifierContext{" +
                "player=" + (player != null ? player.getName() : "null") + ", " +
                "itemStack=" + (itemStack != null ? itemStack.toString() : "null") + ", " +
                "interaction=" + (interaction != null ? interaction.toString() : "null") + ", " +
                "location=" + (location != null ? location.toString() : "null") + ", " +
                "clicked_pos=" + (clicked_pos != null ? clicked_pos.toString() : "null") +
                '}';
    }

}