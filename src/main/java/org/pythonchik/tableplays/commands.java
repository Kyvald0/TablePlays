package org.pythonchik.tableplays;

import org.pythonchik.tableplays.managers.Util;
import org.pythonchik.tableplays.managers.Util.ItemTags;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("base64")) {
                Player player = (Player) sender;
                ItemStack stack = player.getInventory().getItemInMainHand();
                String items = Util.convertItemsToBase64(new ArrayList<>(List.of(stack)));
                player.getInventory().addItem(Util.getItemsFromBase64(items).getFirst());
            } else {
                Player player = (Player) sender;
                ItemStack stack = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
                ItemMeta meta = stack.getItemMeta();
                if (meta != null) {
                    meta.getPersistentDataContainer().set(ItemTags.Item.getValue(), PersistentDataType.BOOLEAN, true);
                    meta.getPersistentDataContainer().set(ItemTags.Type.getValue(), PersistentDataType.STRING, "dice");
                    meta.getPersistentDataContainer().set(ItemTags.Actions.getValue(), PersistentDataType.STRING, "18:PLACE_MAIN,65:PICK_UP");
                    meta.getPersistentDataContainer().set(ItemTags.Modifiers.getValue(), PersistentDataType.STRING, "PLACE_MAIN:RCMDP6,PICK_UP:RESETCMD");
                    meta.getPersistentDataContainer().copyTo(meta.getPersistentDataContainer(), true);
                    meta.setCustomModelData(33400); // get from config?
                    meta.setDisplayName("Кубик");
                }
                stack.setItemMeta(meta);
                player.getInventory().addItem(stack);
            }
        }
        return true;
    }
}
