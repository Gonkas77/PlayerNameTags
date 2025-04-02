package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.PlayerNameTags;
import me.gonkas.playernametags.config.ConfigHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import me.gonkas.playernametags.util.Strings;
import me.gonkas.playernametags.nametag.text.TextType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NameTagCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("copy", "get", "reset", "set", "toggle");
    private static final List<String> COMPONENTS = List.of("name", "prefix", "suffix");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!PlayerNameTags.PLUGINISLOADED) {sender.sendMessage("§cPlugin PlayerNameTags is disabled! Enable using '/pntconfig reset enable-plugin', '/pntconfig set enable-plugin true', or '/pntconfig preset enable-plugin ENABLED'.");return true;}

        // Making sure a sub-command and a component from the sub-command were chosen.
        if (args.length == 0 || !SUBCOMMANDS.contains(args[0])) {sender.sendMessage("§cInvalid command usage! Use '/nametag <copy|get|reset|set|toggle> <name|prefix|suffix>'."); return true;}
        if (!args[0].equals("toggle") && (args.length == 1 || !COMPONENTS.contains(args[1]))) {
            if (args[0].equals("set")) {sender.sendMessage("§cInvalid sub-command usage! Use '/nametag set <name|prefix|suffix> <player> <text>'.");}
            else if (args[0].equals("copy")) {sender.sendMessage("§cInvalid sub-command usage! Use '/nametag copy <name|prefix|suffix> <source_player> <target_player>'.");}
            else {sender.sendMessage("§cInvalid sub-command usage! Use '/nametag " + args[0] + " <name|prefix|suffix> [player]'.");}
            return true;
        }

        int arg_count = 2;
        if (args[0].equals("toggle")) {arg_count = 1;}

        TextType text_type = null;
        if (!args[0].equals("toggle")) {text_type = TextType.valueOf(args[1].toUpperCase());}

        // Essentially does 'get' and 'set' for <source_player> and <target_player> respectively with source_player's get result being target_player's set statement.
        if (args[0].equals("copy")) {

            String text = getText(sender, args[2], text_type);

            if (text.isEmpty()) return true;
            if (args.length < 4) {sender.sendMessage("§cThis sub-command requires at least 3 arguments! Use '/nametag copy " + args[1] + " <source_player> <target_player>'."); return true;}

            args[0] = "set";
            args[2] = args[3];
            args[3] = text;
        }

        // Getting the player target
        Player target;
        if (args.length == arg_count) {
            if (sender instanceof ConsoleCommandSender) {sender.sendMessage("§cConsole can not be a player target! Use '/nametag " + args[0] + " " + args[1] + "<player>'."); return true;}
            if (args[0].equals("set")) {sender.sendMessage("§cThis sub-command requires a player argument! Use '/nametag set " + args[1] + " <player> <text>'."); return true;}
            target = (Player) sender;
        } else {target = Bukkit.getPlayerExact(args[arg_count]);}


        // Executing the sub-command
        switch (args[0]) {
            case "get" -> {
                String text = getText(sender, target, args.length >= 3 ? args[2] : "", text_type);
                if (text.isEmpty()) return true;

                sender.sendMessage("§aPlayer " + (target != null ? target.getName() : args[2]) + "'s " + args[1] + " is " + text + "§a.");
            }
            case "reset" -> {

                if (target != null) {
                    switch (text_type) {
                        case NAME -> NameTagHandler.setName(target.getPlayer(), target.getName());
                        case PREFIX -> NameTagHandler.setPrefix(target.getPlayer(), "");
                        case SUFFIX -> NameTagHandler.setSuffix(target.getPlayer(), "");
                    }
                } else {
                    switch (text_type) {
                        case NAME -> NameTagHandler.setName(args[2], args[2]);
                        case PREFIX -> NameTagHandler.setPrefix(args[2], "");
                        case SUFFIX -> NameTagHandler.setSuffix(args[2], "");
                    }
                }

                sender.sendMessage("Reset player " + (target != null ? target.getName() : args[2]) + "'s " + args[1] + ".");
            }
            case "set" -> {

                if (args.length == 3) {sender.sendMessage("§cInvalid text argument! Use '/nametag set " + args[1] + " " + (target != null ? target.getName() : args[2]) + " <text>'."); return true;}

                StringBuilder builder = new StringBuilder(args[3]);
                for (int i = 4; i < args.length; i++) {builder.append(" ").append(args[i]);}
                String text = Strings.formatText(builder.toString(), text_type);

                if (text == null) {sender.sendMessage("§cInvalid or no characters at all! Use only '" + ConfigHandler.getValidChars() + "'."); return true;}

                if (target != null) {
                    switch (text_type) {
                        case NAME -> NameTagHandler.setName(target.getPlayer(), text + "§r");
                        case PREFIX -> NameTagHandler.setPrefix(target.getPlayer(), text + "§r");
                        case SUFFIX -> NameTagHandler.setSuffix(target.getPlayer(), text + "§r");
                    }
                } else {
                    switch (text_type) {
                        case NAME -> NameTagHandler.setName(args[2], text + "§r");
                        case PREFIX -> NameTagHandler.setPrefix(args[2], text + "§r");
                        case SUFFIX -> NameTagHandler.setSuffix(args[2], text + "§r");
                    }
                }

                sender.sendMessage("Set player " + (target != null ? target.getName() : args[2]) + "'s " + args[1] + " to " + text + "§r.");
            }
            default -> {

                if (target != null) {
                    NameTagHandler.toggleNameTag(target.getPlayer());
                    sender.sendMessage("§a" + NameTagHandler.getFullName(target.getPlayer()) + "'s name tag is now " + (NameTagHandler.isNameTagToggled(target.getPlayer()) ? "hidden" : "visible") + ".");
                } else {
                    NameTagHandler.setHidden(args[1], !NameTagHandler.isHidden(args[1]));
                    sender.sendMessage("§a" + NameTagHandler.getFullName(args[1]) + "'s name tag will be " + (NameTagHandler.isHidden(args[1]) ? "hidden" : "visible") + " upon login.");
                }
            }
        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return switch (args.length) {
            case 1 -> SUBCOMMANDS.stream().filter(n -> Strings.containsIgnoreCase(n, args[0])).toList();
            case 2 -> {if (args[0].equals("toggle")) yield Strings.matchOnlinePlayersName(args[1]); else yield COMPONENTS.stream().filter(n -> Strings.containsIgnoreCase(n, args[1])).toList();}
            case 3 -> Strings.matchOnlinePlayersName(args[2]);
            case 4 -> {if (args[0].equals("copy")) yield Strings.matchOnlinePlayersName(args[3]); else if (args[0].equals("set")) yield List.of("<text>"); else yield List.of();}
            default -> List.of();
        };
    }

    private static String getText(CommandSender sender, String player_name, TextType text_type) {

        Player target = Bukkit.getPlayerExact(player_name);
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(player_name);

        if (target == null && player == null) {sender.sendMessage("§cPlayer is offline and not cached!"); return "";}

        return switch (text_type) {
            case NAME -> {if (target != null) yield NameTagHandler.getName(target.getPlayer()); else yield NameTagHandler.getFromFile(player, TextType.NAME);}
            case PREFIX -> {if (target != null) yield NameTagHandler.getPrefix(target.getPlayer()); else yield NameTagHandler.getFromFile(player, TextType.PREFIX);}
            case SUFFIX -> {if (target != null) yield NameTagHandler.getSuffix(target.getPlayer()); else yield NameTagHandler.getFromFile(player, TextType.SUFFIX);}
        };
    }

    private static String getText(CommandSender sender, Player target, String player_name, TextType text_type) {
        
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(player_name.isEmpty() ? target.getName() : player_name);
        if (target == null && player == null) {sender.sendMessage("§cPlayer is offline and not cached!"); return "";}

        return switch (text_type) {
            case NAME -> {if (target != null) yield NameTagHandler.getName(target.getPlayer()); else yield NameTagHandler.getFromFile(player, TextType.NAME);}
            case PREFIX -> {if (target != null) yield NameTagHandler.getPrefix(target.getPlayer()); else yield NameTagHandler.getFromFile(player, TextType.PREFIX);}
            case SUFFIX -> {if (target != null) yield NameTagHandler.getSuffix(target.getPlayer()); else yield NameTagHandler.getFromFile(player, TextType.SUFFIX);}
        };
    }
}
