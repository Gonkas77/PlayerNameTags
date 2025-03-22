package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.handlers.CustomHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnlockChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        CustomHandler.unlockChat();
        sender.sendMessage("§aChat is now unlocked. Everyone can see and use chat.");
        return true;
    }
}
