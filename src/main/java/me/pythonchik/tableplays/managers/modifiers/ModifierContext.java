package me.pythonchik.tableplays.managers.modifiers;

import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Optional;

public class ModifierContext {
    private final String action;
    private final ItemStack itemStack; // stack in question
    private final Interaction interaction; // interaction of an item
    private final Player player; // player who made me do this (me:( )
    private final Location location; // where to spawn a interaction
    private final Vector clicked_pos; // where I have clicked localy on a block or interaction
    private Interaction clicked_interaction = null; // the interaction I have cliecked
    private final String issuer;

    public ModifierContext(String action, Player player, ItemStack itemStack, Interaction interaction, Location location, Vector clicked_pos, String issuer) {
        this.action = action;
        this.itemStack = itemStack;
        this.interaction = interaction;
        this.player = player;
        this.location = location;
        this.clicked_pos = clicked_pos;
        this.issuer = issuer;
    }

    public ModifierContext(String action, Player player, ItemStack itemStack, Interaction interaction, Location location, Vector clicked_pos, String issuer, Interaction clicked_interaction) {
        this.action = action;
        this.itemStack = itemStack;
        this.interaction = interaction;
        this.player = player;
        this.location = location;
        this.clicked_pos = clicked_pos;
        this.issuer = issuer;
        this.clicked_interaction = clicked_interaction;
    }


    public Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(itemStack);
    }

    public Optional<Interaction> getInteraction() {
        return Optional.ofNullable(interaction);
    }

    public Optional<Interaction> getClickedInteraction() {
        return Optional.ofNullable(clicked_interaction);
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    public Optional<Vector> getVector() {return Optional.ofNullable(clicked_pos);}

    public String getIssuer() {return issuer;}

    @Override
    public String toString() {
        return "ModifierContext{" +
                "player=" + (player != null ? player.getName() : "null") + ", " +
                "itemStack=" + (itemStack != null ? itemStack.toString() : "null") + ", " +
                "interaction=" + (interaction != null ? interaction.toString() : "null") + ", " +
                "clicked_interaction=" + (clicked_interaction != null ? clicked_interaction.toString() : "null") + ", " +
                "location=" + (location != null ? location.toString() : "null") + ", " +
                "issuer=" + issuer + ", " +
                "clicked_pos=" + (clicked_pos != null ? clicked_pos.toString() : "null") +
                '}';
    }

}