package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.handlers.NameTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RemoveNameCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Player target;
        if (args.length == 0) {
            if (commandSender instanceof ConsoleCommandSender) {return true;}
            else {target = (Player) commandSender;}
        } else {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {commandSender.sendMessage("§cPlayer not found!"); return true;}
        }

        NameTagHandler.removeNameTag(target);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().contains((args[0]).toLowerCase())).toList();
    }
}
