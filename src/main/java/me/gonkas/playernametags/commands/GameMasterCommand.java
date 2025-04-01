package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.PlayerNameTags;
import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import me.gonkas.playernametags.util.Strings;
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

    private static final List<String> SUBCOMMANDS = List.of("add", "hide", "list", "remove", "reveal");
    private static final String GAMEMASTERPREFIX = "§4§lGM";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!PlayerNameTags.PLUGINISLOADED) {sender.sendMessage("§cPlugin PlayerNameTags is disabled! Enable using '/pntconfig reset enable-plugin', '/pntconfig set enable-plugin true', or '/pntconfig preset enable-plugin ENABLED'.");return true;}

        if (args.length == 0 || args.length > 2) {sender.sendMessage("§cThis command requires at least 1 and at most 2 arguments!"); return true;}
        if (!SUBCOMMANDS.contains(args[0])) {sender.sendMessage("§cInvalid sub-command! Use 'add/remove/list'."); return true;}

        if (args[0].equals("list")) {
            StringBuilder builder = new StringBuilder();
            ConfigHandler.getGameMasters().forEach(p -> builder.append("\n - ").append(p.getName()));
            sender.sendMessage("§aList of Game Masters:" + builder);
            return true;
        }

        if (args.length == 1) {sender.sendMessage("Missing argument <player>!"); return true;}

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {sender.sendMessage("§cPlayer not found!"); return true;}

        switch (args[0]) {
            case "add" -> {
                ConfigHandler.addGameMaster(target);
                sender.sendMessage("Set " + NameTagHandler.getName(target) + " as a Game Master.");
            }
            case "hide" -> {
                if (!ConfigHandler.isGameMaster(target)) {sender.sendMessage("§cPlayer is not a Game Master!"); return true;}
                if (isGameMasterHidden(target)) {sender.sendMessage("§cGame Master is already hidden!"); return true;}
                NameTagHandler.setPrefix(target, "");
                NameTagHandler.setName(target, NameTagHandler.getTrueName(target));
                sender.sendMessage("§cHid Game Master " + NameTagHandler.getFullName(target) + "§c.");
            }
            case "remove" -> {
                ConfigHandler.removeGameMaster(target);
                NameTagHandler.setPrefix(target, "");
                NameTagHandler.setName(target, NameTagHandler.getTrueName(target));
                sender.sendMessage(NameTagHandler.getName(target) + " is no longer a Game Master.");
            }
            case "reveal" -> {
                if (!ConfigHandler.isGameMaster(target)) {sender.sendMessage("§cPlayer is not a Game Master!"); return true;}
                if (!isGameMasterHidden(target)) {sender.sendMessage("§cPlayer is already revealed as a Game Master!"); return true;}
                NameTagHandler.setPrefix(target, GAMEMASTERPREFIX);
                NameTagHandler.setName(target, "§c" + NameTagHandler.getTrueName(target));
                sender.sendMessage("§cRevealed Game Master " + NameTagHandler.getFullName(target) + "§c.");
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) return SUBCOMMANDS;
        if (args.length == 2) {
            return switch (args[0]) {
                case "add" -> Strings.matchOnlinePlayersName(args[1], p -> !ConfigHandler.isGameMaster(p));
                case "hide" -> Strings.matchOnlinePlayersName(args[1], p -> ConfigHandler.isGameMaster(p) && !isGameMasterHidden(p));
                case "remove" -> Strings.matchOnlinePlayersName(args[1], ConfigHandler::isGameMaster);
                case "reveal" -> Strings.matchOnlinePlayersName(args[1], p -> ConfigHandler.isGameMaster(p) && isGameMasterHidden(p));
                default -> List.of();
            };
        } return List.of();
    }

    private static boolean isGameMasterHidden(Player player) {
        return !NameTagHandler.getPrefix(player).equals(GAMEMASTERPREFIX);
    }
}
