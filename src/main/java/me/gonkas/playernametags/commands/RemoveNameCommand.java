package me.gonkas.playernametags.commands;

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

public class RemoveNameCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Player target = args.length == 0 ? (Player) commandSender : Bukkit.getPlayerExact(args[0]);
        if (target == null) {commandSender.sendMessage("§cPlayer not found!"); return false;}

        NameTagHandler.removeNameTag(target);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().contains((args[0]).toLowerCase())).toList();
    }
}
