package me.pythonchik.tableplays;

import org.bukkit.command.TabCompleter;
import me.pythonchik.tableplays.managers.ItemCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("gimme")) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.getInventory().addItem(ItemCreator.getDice());
                    player.getInventory().addItem(ItemCreator.getBoard());
                    player.getInventory().addItem(ItemCreator.getCheckers());
                    player.getInventory().addItem(ItemCreator.getChess());
                    player.getInventory().addItem(ItemCreator.get36bundle());
                    player.getInventory().addItem(ItemCreator.get52bundle());
                    player.getInventory().addItem(ItemCreator.get54bundle());
                    player.getInventory().addItem(ItemCreator.getDomino());
                    for (int i=0; i <= 7; i++) {
                        player.getInventory().addItem(ItemCreator.getChip(i));
                    }
                    for (int i=10; i <= 18; i++) {
                        player.getInventory().addItem(ItemCreator.getChipBundle(i));
                    }
                    player.sendMessage("[TablePlays] Done.");
                } else if (args.length == 1) {
                    switch (args[0]) {
                        case "dice":
                            player.getInventory().addItem(ItemCreator.getDice());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        case "board":
                            player.getInventory().addItem(ItemCreator.getBoard());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        case "checkers":
                            player.getInventory().addItem(ItemCreator.getCheckers());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        case "chess":
                            player.getInventory().addItem(ItemCreator.getChess());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        case "domino":
                            player.getInventory().addItem(ItemCreator.getDomino());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        case "card36":
                            player.getInventory().addItem(ItemCreator.get36bundle());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        case "card52":
                            player.getInventory().addItem(ItemCreator.get52bundle());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        case "card54":
                            player.getInventory().addItem(ItemCreator.get54bundle());
                            player.sendMessage("[TablePlays] Done.");
                            break;
                        default:
                            player.sendMessage("[TablePlays] Not done. If you want to give yourself every item then type \"/gimme\" with no arguments");
                            break;
                    }

                } else if (args[0].equals("chips") && args[1].matches("[0-9]*")) {
                    player.getInventory().addItem(ItemCreator.getChip(Integer.parseInt(args[1])));
                    player.sendMessage("[TablePlays] Done.");
                } else if (args[0].equals("chip_bundles") && args[1].matches("[0-9]*")) {
                    player.getInventory().addItem(ItemCreator.getChipBundle(Integer.parseInt(args[1]) + 10));
                    player.sendMessage("[TablePlays] Done.");
                } else {
                    player.sendMessage("[TablePlays] I don't know what you were trying to do. If you want to give yourself every item then type \"/gimme\" with no arguments");
                }
            } else {
                TablePlays.instance.reload();
                sender.sendMessage("Reload complete. (maybe?)");
            }
        } else {
            TablePlays.instance.reload();
            TablePlays.getPlugin().getLogger().info("Reload complete. (hopefully)");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("gimme")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                completions.add("dice");
                completions.add("board");
                completions.add("domino");
                completions.add("chess");
                completions.add("checkers");
                completions.add("card36");
                completions.add("card52");
                completions.add("card54");
                completions.add("chips");
                completions.add("chip_bundles");
                completions.add("<no arguments>");
                return completions;
            } else if (args.length == 2 && args[0].equals("chips")) {
                List<String> completions = new ArrayList<>();
                completions.add("<sub-type-number>");
                return completions;
            } else if (args.length == 2 && args[0].equals("chip_bundles")) {
                List<String> completions = new ArrayList<>();
                completions.add("<sub-type-number>");
                return completions;
            }
        }
        return List.of();
    }
}
