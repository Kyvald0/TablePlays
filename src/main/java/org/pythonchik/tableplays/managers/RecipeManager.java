package org.pythonchik.tableplays.managers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.tableplays.TablePlays;
import org.pythonchik.tableplays.bigdata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class RecipeManager {
    public static void init(Plugin plugin){
        cards52(plugin);
        cards36(plugin);
        cards54(plugin);
        dice(plugin);
        //checkers(plugin);
        //board(plugin);
        //chess(plugin);
        domino(plugin);
        chips(plugin);
        chip_bundles(plugin);
    }
    private static void cards52(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards52bundle"), ItemCreator.get52bundle());
        recipe.shape(
                "PPP",
                "RSB",
                "RLB"
        );
        recipe.setIngredient('P', Material.PAPER);
        recipe.setIngredient('R',Material.RED_DYE);
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('B',Material.BLACK_DYE);
        recipe.setIngredient('L', Material.LEATHER);
        Bukkit.addRecipe(recipe);
    }

    private static void cards54(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards54bundle"), ItemCreator.get54bundle());
        recipe.shape(
                "PPP",
                "RLB",
                "RSB"
        );
        recipe.setIngredient('P', Material.PAPER);
        recipe.setIngredient('R',Material.RED_DYE);
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('B',Material.BLACK_DYE);
        recipe.setIngredient('L', Material.LEATHER);
        Bukkit.addRecipe(recipe);
    }

    private static void cards36(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards56bundle"), ItemCreator.get36bundle());
        recipe.shape(
                "PPP",
                "BSR",
                " L "
        );
        recipe.setIngredient('P', Material.PAPER);
        recipe.setIngredient('R',Material.RED_DYE);
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('B',Material.BLACK_DYE);
        recipe.setIngredient('L', Material.LEATHER);
        Bukkit.addRecipe(recipe);
    }

    private static void dice(Plugin plugin) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "dice"), ItemCreator.getDice());
        recipe.shape(
                " W ",
                "ABA",
                " b "
        );
        recipe.setIngredient('W', Material.WHITE_DYE);
        recipe.setIngredient('A', Material.AMETHYST_SHARD);
        recipe.setIngredient('B', Material.BONE_BLOCK);
        recipe.setIngredient('b', Material.BLACK_DYE);
        Bukkit.addRecipe(recipe);
    }

    private static void checkers(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"checkersbundle"),Util.getItemsFromBase64(bigdata.getCheckersBundle()).getFirst());
        recipe.shape(
                " Q ",
                "YSB",
                " L "
        );
        recipe.setIngredient('Q', Material.QUARTZ_BLOCK);
        recipe.setIngredient('B',Material.BLACK_DYE);
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('Y',Material.YELLOW_DYE);
        recipe.setIngredient('L', Material.LEATHER);
        Bukkit.addRecipe(recipe);
    }

    private static void board(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"board"),Util.getItemsFromBase64(bigdata.getBoard()).getFirst());
        recipe.shape(
                "BS",
                "SB"
        );
        recipe.setIngredient('B', Material.BIRCH_SLAB);
        recipe.setIngredient('S',Material.DARK_OAK_SLAB);
        Bukkit.addRecipe(recipe);
    }

    private static void chess(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"chessbundle"),Util.getItemsFromBase64(bigdata.getChessBundle()).getFirst());
        recipe.shape(
                "WQB",
                "WSB",
                " L "
        );
        recipe.setIngredient('Q', Material.BONE_BLOCK);
        recipe.setIngredient('B',Material.BLACK_DYE);
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('W',Material.WHITE_DYE);
        recipe.setIngredient('L', Material.LEATHER);
        Bukkit.addRecipe(recipe);
    }

    private static void domino(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"dominobundle"), ItemCreator.getDomino());
        recipe.shape(
                " M ",
                "WSB",
                " L "
        );
        recipe.setIngredient('M', Material.MANGROVE_LOG);
        recipe.setIngredient('B',Material.BLACK_DYE);
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('W',Material.WHITE_DYE);
        recipe.setIngredient('L', Material.LEATHER);
        Bukkit.addRecipe(recipe);
    }

    private static void chips(Plugin plugin) {
        HashMap<Material, Integer> variants = ValuesManager.getVariants(Util.ItemTypes.Chip.getValue());
        for (Material mat : variants.keySet()) {
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "chip_" + variants.get(mat)), ItemCreator.getChip(variants.get(mat)));
            recipe.shape(
                    " # ",
                    "#M#",
                    " # "
            );
            recipe.setIngredient('M', mat);
            recipe.setIngredient('#', Material.GOLD_NUGGET);
            Bukkit.addRecipe(recipe);
        }
    }

    private static void chip_bundles(Plugin plugin) {
        HashMap<Material, Integer> variants = ValuesManager.getVariants(Util.ItemTypes.Bundle.getValue());
        for (Material mat : variants.keySet()) {
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "cbundle_" + variants.get(mat)), ItemCreator.getChipBundle(variants.get(mat)));
            recipe.shape(
                    "#D#",
                    "#S#",
                    " L "
            );
            recipe.setIngredient('D', mat);
            recipe.setIngredient('S', Material.STRING);
            recipe.setIngredient('L', Material.LEATHER);
            recipe.setIngredient('#', Material.GOLD_NUGGET);
            Bukkit.addRecipe(recipe);
        }
    }

}
