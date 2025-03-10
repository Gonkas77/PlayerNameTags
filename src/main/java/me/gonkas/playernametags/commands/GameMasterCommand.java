package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.handlers.ConfigHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GameMasterCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0 || args.length > 2) {commandSender.sendMessage("§cThis command requires at least 1 and at most 2 arguments!"); return true;}
        if (!(args[0].equals("add") || args[0].equals("remove") || args[0].equals("list"))) {commandSender.sendMessage("§cInvalid sub-command! Use 'add/remove/list'."); return true;}

        if (args[0].equals("list")) {
            StringBuilder builder = new StringBuilder();
            ConfigHandler.getGameMasters().forEach(p -> builder.append("\n - ").append(p.getName()));
            commandSender.sendMessage("§aList of Game Masters:" + builder);
            return true;
        }

        if (args[0].equals("add")) {

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
