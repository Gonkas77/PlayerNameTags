package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NameCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0) {commandSender.sendMessage("§cThis command requires at least one argument! Use '/name <player> <name>'."); return false;}

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {commandSender.sendMessage("§cPlayer not found!"); return false;}

        StringBuilder builder = new StringBuilder(args[1]);
        for (int i=2; i < args.length; i++) {
            builder.append(" ").append(args[i]);
            if (builder.length() > 24) {commandSender.sendMessage("§cNames can have at most 24 characters!"); return false;}
        }

        String name = builder.toString();
        if (name.isEmpty()) {commandSender.sendMessage("§cNames must have at least one character!");}
        if (!isValidName(name)) {
            commandSender.sendMessage("§cInvalid characters or no letters detected! Use only '" + ConfigHandler.getValidChars() + "' (uppercase included) and at least one letter.");
            return false;
        }

        NameTagHandler.createNameTag(target, name);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().contains((args[0]).toLowerCase())).toList();}
        if (args.length <= 2) {return List.of("<name>");}
        return List.of();
    }

    private static boolean isValidName(String name) {
        for (char c : name.toLowerCase().toCharArray()) {
            if (!ConfigHandler.getValidChars().contains(String.valueOf(c))) {return false;}
        } return true;
    }
}
