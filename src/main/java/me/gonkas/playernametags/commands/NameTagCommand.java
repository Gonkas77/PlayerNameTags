package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NameTagCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("get", "reset", "set");
    private static final List<String> COMPONENTS = List.of("name", "prefix", "suffix");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        // Making sure a sub-command and a component from the sub-command were chosen.
        if (args.length == 0 || !SUBCOMMANDS.contains(args[0])) {sender.sendMessage("§cInvalid command usage! Use '/nametag <get|reset|set>'."); return true;}
        if (args.length == 1 || !COMPONENTS.contains(args[1])) {
            if (args[0].equals("set")) {sender.sendMessage("§cInvalid sub-command usage! Use '/nametag set <name|prefix|suffix> <player> <text>'.");}
            else {sender.sendMessage("§cInvalid sub-command usage! Use '/nametag " + args[0] + " <name|prefix|suffix> [player]'.");}
            return true;
        }


        // Getting the player target
        Player target;
        if (args.length == 2) {
            if (sender instanceof ConsoleCommandSender) {sender.sendMessage("§cConsole can not be a player target! Use '/nametag " + args[0] + " " + args[1] + "<player>'."); return true;}
            if (args[0].equals("set")) {sender.sendMessage("§cThis sub-command requires a player argument! Use '/nametag set " + args[1] + " <player> <text>'."); return true;}
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayerExact(args[2]);
            if (target == null) {sender.sendMessage("§cPlayer not found!"); return true;}
        }


        // Executing the sub-command
        if (args[0].equals("get")) {

            String toGet = switch (args[1]) {
                case "name" -> NameTagHandler.getName(target);
                case "prefix" -> NameTagHandler.getPrefix(target);
                case "suffix" -> NameTagHandler.getSuffix(target);
                default -> null; // It is impossible at this point in the code to have 'args[1]' not be one of the options.
            };

            sender.sendMessage("§aPlayer " + target.getName() + "'s " + args[1] + " is " + toGet + ".");

        } else if (args[0].equals("reset")) {

            switch (args[1]) {
                case "name" -> NameTagHandler.setName(target, "");
                case "prefix" -> NameTagHandler.setPrefix(target, "");
                case "suffix" -> NameTagHandler.setSuffix(target, "");
            }

            sender.sendMessage("Reset player " + target.getName() + "'s " + args[1] + ".");

        } else {

            if (args.length == 3) {sender.sendMessage("§cInvalid text argument! Use '/nametag set " + args[1] + " " + target.getName() + " <text>'."); return true;}

            StringBuilder builder = new StringBuilder(args[3]);
            for (int i=4; i < args.length; i++) {builder.append(" ").append(args[i]);}
            String text = builder.toString();

            if (text.isEmpty()) {sender.sendMessage("§cText must have at least one character!");}

            // Essentially replaces the '§' character for Minecraft message/name formatting with the '&' character.
            // Users can still put '&' in their name by adding a backslash '\' before the '&' character. See 'https://minecraft.wiki/w/Formatting_codes' for more info.
            String format_chars = "0123456789abcdefklmnor";
            int text_length = text.length();

            for (int i=0; i < text.length(); i++) {
                if (text.charAt(i) != '&' || i == text.length() - 1) continue;
                if (!format_chars.contains(String.valueOf(text.charAt(i+1)))) continue;

                if (i == 0) text = new StringBuilder(text).replace(0, 1, "§").toString();
                else if (text.charAt(i-1) != '\\') {
                    text = new StringBuilder(text).replace(i, i + 1, "§").toString();
                }
            }

            if (!isValidName(text)) {
                sender.sendMessage("§cInvalid characters detected! Use only '" + ConfigHandler.getValidChars() + "' (uppercase included).");
                return true;
            }

            switch (args[1]) {
                case "name" -> NameTagHandler.setName(target, args[3]);
                case "prefix" -> NameTagHandler.setPrefix(target, args[3]);
                case "suffix" -> NameTagHandler.setSuffix(target, args[3]);
            }

            sender.sendMessage("Set player " + target.getName() + "'s " + args[1] + " to " + args[3] + ".");

        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().contains(args[0].toLowerCase())).toList();}
        if (args.length <= 2) {return List.of("<name>");}
        return List.of();
    }

    private static boolean isValidName(String name) {
        for (char c : name.toLowerCase().toCharArray()) {if (!ConfigHandler.getValidChars().contains(String.valueOf(c)) && c != '§') {return false;}}
        return true;
    }
}
