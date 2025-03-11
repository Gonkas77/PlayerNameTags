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

public class GameMasterCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("add", "list", "remove");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0 || args.length > 2) {commandSender.sendMessage("§cThis command requires at least 1 and at most 2 arguments!"); return true;}
        if (!SUBCOMMANDS.contains(args[0])) {commandSender.sendMessage("§cInvalid sub-command! Use 'add/remove/list'."); return true;}

        if (args[0].equals("list")) {
            StringBuilder builder = new StringBuilder();
            ConfigHandler.getGameMasters().forEach(p -> builder.append("\n - ").append(p.getName()));
            commandSender.sendMessage("§aList of Game Masters:" + builder);
            return true;
        }

        if (args.length == 1) {commandSender.sendMessage("Missing argument <player>!"); return true;}

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {commandSender.sendMessage("§cPlayer not found!"); return true;}

        if (args[0].equals("add")) {
            ConfigHandler.addGameMaster(target);
            commandSender.sendMessage("Set " + NameTagHandler.getName(target) + " as a Game Master.");
        } else {
            ConfigHandler.removeGameMaster(target);
            commandSender.sendMessage(NameTagHandler.getName(target) + " is no longer a Game Master.");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) return SUBCOMMANDS;
        if (args.length == 2) {
            if (args[1].equals("add")) return Bukkit.getOnlinePlayers().stream().filter(p -> !ConfigHandler.isGameMaster(p)).map(Player::getName).toList();
            else Bukkit.getOnlinePlayers().stream().filter(ConfigHandler::isGameMaster).map(Player::getName).toList();
        } return List.of();
    }
}
