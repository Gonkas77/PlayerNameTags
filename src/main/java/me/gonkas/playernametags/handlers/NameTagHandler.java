package me.gonkas.playernametags.handlers;

import me.gonkas.playernametags.PlayerNameTags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;
import org.intellij.lang.annotations.RegExp;

import java.io.IOException;
import java.util.HashMap;

import static me.gonkas.playernametags.PlayerNameTags.NAMEFILE;
import static me.gonkas.playernametags.PlayerNameTags.TEAM;

public class NameTagHandler implements Listener {

    private static HashMap<Player, String> PLAYERNAMES = new HashMap<>(0);
    private static HashMap<Player, ArmorStand> PLAYERSTANDS = new HashMap<>(0);

    public static void load() {
        Bukkit.getOnlinePlayers().forEach(NameTagHandler::loadPlayer);
    }

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        loadPlayer(player);
        if (event.joinMessage() != null) {
            @RegExp String name = PLAYERNAMES.get(player) == null ? player.getName() : PLAYERNAMES.get(player);
            event.joinMessage(event.joinMessage().replaceText(
                    TextReplacementConfig.builder()
                            .match(name)
                            .replacement(PLAYERNAMES.get(player))
                            .build()));
        }
    }

    public static void loadPlayer(Player player) {
        YamlConfiguration names = YamlConfiguration.loadConfiguration(NAMEFILE);
        String name = names.getString(player.getUniqueId().toString());
        if (name == null || name.isEmpty()) return;

        TEAM.addPlayer(player);
        player.playerListName(Component.text(name));
        player.displayName(Component.text(name));

        createNameTag(player, name);
        PlayerNameTags.consoleInfo("Creating Name Tag for player '%s'.", player.getName());
    }

    public static void createNameTag(Player player, String name) {
        if (PLAYERSTANDS.containsKey(player)) {updateNameTag(player, name); return;}

        ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        stand.customName(Component.text(name));
        stand.setCustomNameVisible(true);
        stand.setVisible(false);
        stand.setGravity(false);

        stand.getAttribute(Attribute.SCALE).setBaseValue(0.0625);

        if (!player.addPassenger(stand)) {PlayerNameTags.consoleWarn("Unable to anchor armor stand onto player '%s'.", player.getName());}

        PLAYERSTANDS.put(player, stand);
        PLAYERNAMES.put(player, name);

        PlayerNameTags.consoleInfo("Successfully created Name Tag for player '%s'.", player.getName());
    }

    public static void updateNameTag(Player player, String name) {
        PLAYERSTANDS.get(player).customName(Component.text(name));
        PLAYERNAMES.replace(player, name);

        player.playerListName(Component.text(name));
        player.displayName(Component.text(name));

        PlayerNameTags.consoleInfo("Updated name tag for player '%s' to '%s'.", player.getName(), name);
    }

    public static void removeNameTag(Player player) {
        PLAYERNAMES.replace(player, "");
        unloadPlayer(player);
    }

    public static void unload() {
        Bukkit.getOnlinePlayers().forEach(NameTagHandler::unloadPlayer);
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (event.quitMessage() != null) {
            @RegExp String name = PLAYERNAMES.get(player) == null ? player.getName() : PLAYERNAMES.get(player);
            event.quitMessage(event.quitMessage().replaceText(
                    TextReplacementConfig.builder()
                            .match(name)
                            .replacement(PLAYERNAMES.get(player))
                            .build()));
        }
        unloadPlayer(player);
    }

    public static void unloadPlayer(Player player) {
        YamlConfiguration names = YamlConfiguration.loadConfiguration(NAMEFILE);
        String name = PLAYERNAMES.get(player);
        if (name == null) return;

        if (name.isEmpty()) {PlayerNameTags.consoleInfo("Attempting to remove player name for '%s'.", player.getName());}
        else {PlayerNameTags.consoleInfo("Attempting to update player name for '%s'.", player.getName());}

        names.set(player.getUniqueId().toString(), name);
        try {names.save(NAMEFILE);}
        catch (IOException e) {PlayerNameTags.consoleError("Unable to update player name file!");}
        finally {PlayerNameTags.consoleInfo("Successfully updated player name file.");}

        PLAYERNAMES.remove(player);
        PLAYERSTANDS.get(player).remove();
        PLAYERSTANDS.remove(player);

        TEAM.removePlayer(player);

        PlayerNameTags.consoleInfo("Deleted name tag for player '%s'.", player.getName());
    }

    @EventHandler
    public static void onPlayerCrouch(PlayerToggleSneakEvent event) {
        if (event.isCancelled()) return;
        if (PLAYERSTANDS.containsKey(event.getPlayer())) PLAYERSTANDS.get(event.getPlayer()).setCustomNameVisible(event.getPlayer().isSneaking() && !event.getPlayer().isFlying());
    }

    @EventHandler
    public static void onSpectatorMode(PlayerGameModeChangeEvent event) {
        if (PLAYERSTANDS.containsKey(event.getPlayer())) PLAYERSTANDS.get(event.getPlayer()).setCustomNameVisible(event.getNewGameMode() != GameMode.SPECTATOR);
    }

    @EventHandler
    public static void onPlayerInvis(EntityPotionEffectEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (!PLAYERSTANDS.containsKey((Player) event.getEntity()) || event.getModifiedType() != PotionEffectType.INVISIBILITY) return;

        if (event.getAction() == EntityPotionEffectEvent.Action.ADDED) PLAYERSTANDS.get((Player) event.getEntity()).setCustomNameVisible(false);
        else if (event.getAction() != EntityPotionEffectEvent.Action.CHANGED) PLAYERSTANDS.get((Player) event.getEntity()).setCustomNameVisible(true);
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event) {
        if (event.deathMessage() != null) {
            event.deathMessage(event.deathMessage().replaceText(
                    TextReplacementConfig.builder()
                            .match(getName(event.getPlayer()))
                            .replacement(PLAYERNAMES.get(event.getPlayer()))
                            .build()));
        }
    }

    @EventHandler
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        event.getPlayer().addPassenger(PLAYERSTANDS.get(event.getPlayer()));
    }

    public static String getName(Player player) {
        return PLAYERNAMES.get(player) == null ? player.getName() : PLAYERNAMES.get(player);
    }
}
