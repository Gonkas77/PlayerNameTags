package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import me.gonkas.playernametags.util.Strings;
import me.gonkas.playernametags.util.TextType;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NameTagCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("get", "reset", "set", "toggle");
    private static final List<String> COMPONENTS = List.of("name", "prefix", "suffix");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        // Making sure a sub-command and a component from the sub-command were chosen.
        if (args.length == 0 || !SUBCOMMANDS.contains(args[0])) {sender.sendMessage("§cInvalid command usage! Use '/nametag <get|reset|set|toggle> <name|prefix|suffix>'."); return true;}
        if (!args[0].equals("toggle") && (args.length == 1 || !COMPONENTS.contains(args[1]))) {
            if (args[0].equals("set")) {sender.sendMessage("§cInvalid sub-command usage! Use '/nametag set <name|prefix|suffix> <player> <text>'.");}
            else {sender.sendMessage("§cInvalid sub-command usage! Use '/nametag " + args[0] + " <name|prefix|suffix> [player]'.");}
            return true;
        }

        int arg_count = 2;
        if (args[0].equals("toggle")) {arg_count = 1;}

        // Getting the player target
        Player target;
        if (args.length == arg_count) {
            if (sender instanceof ConsoleCommandSender) {sender.sendMessage("§cConsole can not be a player target! Use '/nametag " + args[0] + " " + args[1] + "<player>'."); return true;}
            if (args[0].equals("set")) {sender.sendMessage("§cThis sub-command requires a player argument! Use '/nametag set " + args[1] + " <player> <text>'."); return true;}
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayerExact(args[arg_count]);
            if (target == null) {sender.sendMessage("§cPlayer not found!"); return true;}
        }

        TextType text_type = null;
        if (!args[0].equals("toggle")) {text_type = TextType.valueOf(args[1].toUpperCase());}

        // Executing the sub-command
        switch (args[0]) {
            case "get" -> {

                String toGet = switch (text_type) {
                    case NAME -> NameTagHandler.getName(target);
                    case PREFIX -> NameTagHandler.getPrefix(target);
                    case SUFFIX -> NameTagHandler.getSuffix(target);
                };

                sender.sendMessage("§aPlayer " + target.getName() + "'s " + args[1] + " is " + toGet + ".");
            }
            case "reset" -> {

                switch (text_type) {
                    case NAME -> NameTagHandler.setName(target, "");
                    case PREFIX -> NameTagHandler.setPrefix(target, "");
                    case SUFFIX -> NameTagHandler.setSuffix(target, "");
                }

                sender.sendMessage("Reset player " + target.getName() + "'s " + args[1] + ".");
            }
            case "set" -> {

                if (args.length == 3) {sender.sendMessage("§cInvalid text argument! Use '/nametag set " + args[1] + " " + target.getName() + " <text>'."); return true;}

                StringBuilder builder = new StringBuilder(args[3]);
                for (int i = 4; i < args.length; i++) {builder.append(" ").append(args[i]);}
                String text = builder.toString();

                if (!Strings.textIsValid(text, text_type)) {sender.sendMessage("§cInvalid or no characters at all! Use only '" + ConfigHandler.getValidChars() + "' (uppercase included)."); return true;}

                switch (text_type) {
                    case NAME -> NameTagHandler.setName(target, text + "§r");
                    case PREFIX -> NameTagHandler.setPrefix(target, text + "§r");
                    case SUFFIX -> NameTagHandler.setSuffix(target, text + "§r");
                }

                sender.sendMessage("Set player " + target.getName() + "'s " + args[1] + " to " + text + "§r.");
            }
            default -> {

                NameTagHandler.toggleNameTag(target);
                sender.sendMessage(NameTagHandler.getFullName(target) + "§a's name tag is now " + (NameTagHandler.isNameTagHidden(target) ? "hidden" : "visible") + ".");

            }
        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return switch (args.length) {
            case 1 -> SUBCOMMANDS.stream().filter(n -> Strings.containsIgnoreCase(n, args[0])).toList();
            case 2 -> {if (args[0].equals("toggle")) yield Strings.matchOnlinePlayersName(args[1]); else yield COMPONENTS.stream().filter(n -> Strings.containsIgnoreCase(n, args[1])).toList();}
            case 3 -> Strings.matchOnlinePlayersName(args[2]);
            case 4 -> {if (args[0].equals("set")) yield List.of("<text>");else yield List.of();}
            default -> List.of();
        };
    }
}
