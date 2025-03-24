package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.PlayerNameTags;
import me.gonkas.playernametags.handlers.CustomHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnlockChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!PlayerNameTags.PLUGINISLOADED) {sender.sendMessage("§cPlugin PlayerNameTags is disabled! Enable using '/pntconfig reset enable-plugin', '/pntconfig set enable-plugin true', or '/pntconfig preset enable-plugin ENABLED'.");return true;}

        CustomHandler.unlockChat();
        sender.sendMessage("§aChat is now unlocked. Everyone can see and use chat.");
        return true;
    }
}
