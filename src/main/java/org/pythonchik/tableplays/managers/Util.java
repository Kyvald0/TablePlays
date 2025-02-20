package org.pythonchik.tableplays.managers;

import org.pythonchik.tableplays.TablePlays;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.EnumSet;

public class Util {

    public enum ActionTag {
        NONE_HAND(1),
        MAIN_HAND(2),
        LEFT_HAND(4),
        BOTH_HAND(8),
        ON_BLOCK(16),
        ON_AIR(32),
        ON_ITEM(64),
        WITH_SHIFT(128);

        private final int value;

        ActionTag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        // Convert a bitwise combination back to an EnumSet
        public static EnumSet<ActionTag> fromValue(int Value) {
            EnumSet<ActionTag> set = EnumSet.noneOf(ActionTag.class);
            for (ActionTag tag : ActionTag.values()) {
                if ((Value & tag.value) != 0) { // Check if the bit is set
                    set.add(tag);
                }
            }
            return set;
        }

        // Combine multiple ActionTags into a single bitwise value
        public static int combine(ActionTag... tags) {
            int result = 0;
            for (ActionTag tag : tags) {
                result |= tag.value; // Bitwise OR to combine
            }
            return result;
        }

    }

    public static class ActionTagSet {
        public static ActionTagSet fromEnum(EnumSet<ActionTag> enumSet) {
            ActionTagSet toReturn = new ActionTagSet();
            for (ActionTag tag : enumSet) {
                toReturn.add(tag);
            }
            return toReturn;
        }

        private int value;

        public ActionTagSet() {
            this.value = 0;
        }

        public ActionTagSet(int initialValue) {
            this.value = initialValue;
        }

        public void add(ActionTag tag) {
            this.value |= tag.getValue();
        }

        public void remove(ActionTag tag) {
            this.value &= ~tag.getValue();
        }

        public boolean contains(ActionTag tag) {
            return (this.value & tag.getValue()) != 0;
        }

        public boolean containsAny(ActionTag... tags) {
            //TODO implement BTrees to improve 0.0001 MS spent on this function
            //(I'm not doing this shit, it's a joke)
            for (ActionTag tag : tags) {
                if (contains(tag)) return true;
            }
            return false;
        }

        public int getInt() {
            return this.value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum ItemTags {
        Item(new NamespacedKey(TablePlays.getPlugin(), "item")), // boolean, used only in .has
        Entity(new NamespacedKey(TablePlays.getPlugin(), "entity")), // boolean, used only in .has
        Type(new NamespacedKey(TablePlays.getPlugin(), "type")), // string, type of the item/entity
        Actions(new NamespacedKey(TablePlays.getPlugin(), "actions")), //string, actions described in another file
        Modifiers(new NamespacedKey(TablePlays.getPlugin(), "modifiers")), //string, all modifiers applied to the actions

        //all next probably redundent as I have type now
        Card(new NamespacedKey(TablePlays.getPlugin(), "card")),
        Board(new NamespacedKey(TablePlays.getPlugin(), "board")),
        Bundle(new NamespacedKey(TablePlays.getPlugin(), "bundle")),
        Checker(new NamespacedKey(TablePlays.getPlugin(), "checker")),
        ChessPiece(new NamespacedKey(TablePlays.getPlugin(), "chessp")),
        Domino(new NamespacedKey(TablePlays.getPlugin(), "domino")),
        Chip(new NamespacedKey(TablePlays.getPlugin(), "chip")),
        Square(new NamespacedKey(TablePlays.getPlugin(), "square")),
        //up to this point

        // all below are bools with no effect from the value
        Flippable(new NamespacedKey(TablePlays.getPlugin(), "flippable")), // if present - item may be flipped upside down
        Rotatable(new NamespacedKey(TablePlays.getPlugin(), "rotatable")), // if present - item may be rotated 90 degrees
        Base(new NamespacedKey(TablePlays.getPlugin(), "base")),
        Hcard(new NamespacedKey(TablePlays.getPlugin(), "hcard")),
        RandomFace(new NamespacedKey(TablePlays.getPlugin(), "random_face")),

        Bundle_domino(new NamespacedKey(TablePlays.getPlugin(), "bundle_domino")),
        Bundle_cards(new NamespacedKey(TablePlays.getPlugin(), "bundle_card")),
        Bundle_chess(new NamespacedKey(TablePlays.getPlugin(), "bundle_chess")),
        Bundle_checkers(new NamespacedKey(TablePlays.getPlugin(), "bundle_checker")),
        Bundle_randO(new NamespacedKey(TablePlays.getPlugin(), "bundle_rando")),
        Bundle_items(new NamespacedKey(TablePlays.getPlugin(), "bundle_items")),
        Bundle_Stype(new NamespacedKey(TablePlays.getPlugin(), "bundle_stype")),
        Bundle_max_count(new NamespacedKey(TablePlays.getPlugin(), "bundle_maxcount"));

        private final NamespacedKey value;

        ItemTags(NamespacedKey value) {
            this.value = value;
        }

        public NamespacedKey getValue() {
            return value;
        }

    }

    public static ArrayList<ItemStack> getItemsFromBase64(String baseString) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(baseString));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            ArrayList<ItemStack> returning_stacks = new ArrayList<>();
            for (int i = 0; i < size;i++){
                returning_stacks.add((ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return returning_stacks;
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    public static String convertItemsToBase64(ArrayList<ItemStack> itemStacks) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(itemStacks.size());
            for (ItemStack stack : itemStacks) {
                dataOutput.writeObject(stack);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception ignored) {}
        return "Errore";
    }

    public static Location getBlockEyeLoc(Player player) {
        Vector direction = player.getEyeLocation().getDirection();
        Location eyeloc = player.getEyeLocation();
        for (double i = 0; i < player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(); i += 0.002) {
            eyeloc.add(direction.clone().multiply(0.002));
            if (eyeloc.getBlock().getType() != Material.AIR) {break;}
        }
        eyeloc.setY(eyeloc.getY() + 0.005); //moving up a bit, to not clip into anything
        return eyeloc;
    }

    public static ArrayList<String> getModifiers(ItemStack stack, String action) {
        String[] modifs = (stack.getItemMeta().getPersistentDataContainer().get(ItemTags.Modifiers.getValue(), PersistentDataType.STRING)).split(",");
        ArrayList<String> modifiers = new ArrayList<>();
        for (String modif : modifs) {
            String[] split_mod = modif.split(":");
            if (split_mod.length >= 2 && split_mod[0].equals(action)){
                modifiers.add(split_mod[1]);
            }
        }
        return modifiers;
    }
}
