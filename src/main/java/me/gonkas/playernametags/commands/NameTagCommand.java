package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.handlers.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NameTagCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("get", "reset", "set");
    private static final List<String> COMPONENTS = List.of("name", "prefix", "suffix");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0 || !SUBCOMMANDS.contains(args[0])) {commandSender.sendMessage("§cInvalid command usage! Use '/nametag <get|reset|set>'."); return true;}

        switch (args[0]) {
            case "get":
                if (args.length == 1) {commandSender.sendMessage("§cThis sub-command requires player argument! Use '/nametag <get> <player>'."); return true;}

                break;
            case "reset":
                break;
            case "set":
                break;
        }


//        if (args.length == 0) {commandSender.sendMessage("§cThis command requires at least one argument! Use '/name <player> <name>'."); return true;}
//
//        Player target = Bukkit.getPlayerExact(args[0]);
//        if (target == null) {commandSender.sendMessage("§cPlayer not found!"); return true;}
//
//        StringBuilder builder = new StringBuilder(args[1]);
//        for (int i=2; i < args.length; i++) {builder.append(" ").append(args[i]);}
//        String name = builder.toString();
//
//        if (name.isEmpty()) {commandSender.sendMessage("§cNames must have at least one character!");}
//        if (builder.length() > ConfigHandler.getMaxNameLength()) {commandSender.sendMessage("§cNames can have at most " + ConfigHandler.getMaxNameLength() + " characters!"); return true;}
//
//        // Essentially replaces the '§' character for Minecraft message/name formatting with the '&' character.
//        // Users can still put '&' in their name by adding a backslash '\' before the '&' character. See 'https://minecraft.wiki/w/Formatting_codes' for more info.
//        String format_chars = "0123456789abcdefklmnor";
//        for (int i=0; i < name.length(); i++) {
//            if (name.charAt(i) != '&' || i == name.length() - 1) continue;
//            if (!format_chars.contains(String.valueOf(name.charAt(i+1)))) continue;
//
//            if (i == 0) name = new StringBuilder(name).replace(0, 1, "§").toString();
//            else if (name.charAt(i-1) != '\\') name = new StringBuilder(name).replace(i, i+1, "§").toString();
//        }
//
//        if (!isValidName(name)) {
//            commandSender.sendMessage("§cInvalid characters detected! Use only '" + ConfigHandler.getValidChars() + "' (uppercase included).");
//            return true;
//        }
//
//        NameTagHandler.createNameTag(target, name);
//        return true;
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
