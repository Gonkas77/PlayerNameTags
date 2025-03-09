package me.gonkas.playernametags.handlers;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomHandler implements Listener {

    @EventHandler
    public static void onPlayerItemFrameInteract(PlayerItemFrameChangeEvent event) {
        if (event.getAction() == PlayerItemFrameChangeEvent.ItemFrameChangeAction.REMOVE && !event.getPlayer().isOp()) event.setCancelled(true);
    }
}
