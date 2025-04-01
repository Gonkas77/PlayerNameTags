package me.gonkas.playernametags.handlers;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.ServerOperator;

import java.util.List;

public class CustomHandler implements Listener {

    private static boolean ALLOWPLAYERCHAT = true;
    private static final List<String> BANNEDCOMMANDS = List.of("me", "msg", "teammsg", "tell", "tm", "w");

    public static void lockChat() {ALLOWPLAYERCHAT = false;}
    public static void unlockChat() {ALLOWPLAYERCHAT = true;}

    @EventHandler
    public static void onPlayerItemFrameInteract(PlayerItemFrameChangeEvent event) {

        // If the player is not in creative and not an operator, prevent removal action.
        if (event.getAction() == PlayerItemFrameChangeEvent.ItemFrameChangeAction.REMOVE && !event.getPlayer().isOp() && event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    @EventHandler
    public static void onPlayerChat(AsyncChatEvent event) {
        if (ALLOWPLAYERCHAT) return;

        event.setCancelled(true);
        if (event.getPlayer().isOp() || ConfigHandler.isGameMaster(event.getPlayer())) {
            Bukkit.getOnlinePlayers().stream().filter(p -> p.isOp() || p == event.getPlayer()).forEach(
                    p -> p.sendMessage(Component.text("<" + NameTagHandler.getFullName(event.getPlayer()) + "> ").append(event.message())));
        }
    }

    @EventHandler
    public static void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (ALLOWPLAYERCHAT) return;
        if (event.getPlayer().isOp()) return;

        String[] args = event.getMessage().substring(1).split(" ")[0].split(":");
        String command = args.length == 1 ? args[0] : args[1];

        if (BANNEDCOMMANDS.contains(command)) event.setCancelled(true);
        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(p -> p.sendMessage("§e" + NameTagHandler.getName(event.getPlayer()) + " attempted to /msg someone."));
    }
}
