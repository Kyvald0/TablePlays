package org.pythonchik.tableplays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class RecipeManager {
    public static void init(Plugin plugin){
        cards52(plugin);
        cards36(plugin);
        cards54(plugin);
        //dice(plugin);
        checkers(plugin);
        board(plugin);
        chess(plugin);
    }
    private static void cards52(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards52bundle"),TablePlays.getItemsFromBase64(bigdata.get52bundle()).getFirst());
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
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards54bundle"),TablePlays.getItemsFromBase64(bigdata.get54bundle()).getFirst());
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
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"cards56bundle"),TablePlays.getItemsFromBase64(bigdata.get36bundle()).getFirst());
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
    /*
    private static void dice(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"dice"),TablePlays.getItemsFromBase64(bigdata.getDice()).getFirst());
        recipe.shape(
                " W ",
                "ABA",
                " b "
        );
        recipe.setIngredient('W', Material.WHITE_DYE);
        recipe.setIngredient('A',Material.AMETHYST_SHARD);
        recipe.setIngredient('B', Material.BONE_BLOCK);
        recipe.setIngredient('b',Material.BLACK_DYE);
        Bukkit.addRecipe(recipe);
    }
     */
    private static void checkers(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"checkersbundle"),TablePlays.getItemsFromBase64(bigdata.getCheckersBundle()).getFirst());
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
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"board"),TablePlays.getItemsFromBase64(bigdata.getBoard()).getFirst());
        recipe.shape(
                "BS",
                "SB"
        );
        recipe.setIngredient('B', Material.BIRCH_SLAB);
        recipe.setIngredient('S',Material.DARK_OAK_SLAB);
        Bukkit.addRecipe(recipe);
    }
    private static void chess(Plugin plugin){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin,"chessbundle"),TablePlays.getItemsFromBase64(bigdata.getChessBundle()).getFirst());
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
}
