package org.pythonchik.tableplays.managers;

import org.pythonchik.tableplays.bigdata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class RecipeManager {
    public static void init(Plugin plugin){
        //cards52(plugin);
        //cards36(plugin);
        //cards54(plugin);
        dice(plugin);
        //checkers(plugin);
        //board(plugin);
        //chess(plugin);
        //domino(plugin);
    }
    private static void cards52(Plugin plugin){
        ItemStack final_item = Util.getItemsFromBase64(bigdata.get52bundle()).getFirst();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards52bundle"), final_item);
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
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards54bundle"),Util.getItemsFromBase64(bigdata.get54bundle()).getFirst());
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
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards56bundle"),Util.getItemsFromBase64(bigdata.get36bundle()).getFirst());
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
        //TODO make craft configurable? how?
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
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"dominobundle"),Util.getItemsFromBase64(bigdata.getDominoBundle()).getFirst());
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
}
