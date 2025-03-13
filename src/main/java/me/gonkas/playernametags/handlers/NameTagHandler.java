package me.gonkas.playernametags.handlers;

import me.gonkas.playernametags.PlayerNameTags;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.gonkas.playernametags.PlayerNameTags.NAMEFILE;
import static me.gonkas.playernametags.PlayerNameTags.TEAM;

public class NameTagHandler implements Listener {

    private static final HashMap<Player, ArrayList<String>> PLAYERNAMES = new HashMap<>(0);
    private static final HashMap<Player, ArmorStand> PLAYERSTANDS = new HashMap<>(0);

    public static void load() {Bukkit.getOnlinePlayers().forEach(NameTagHandler::loadPlayer);}
    public static void unload() {Bukkit.getOnlinePlayers().forEach(NameTagHandler::unloadPlayer);}

    private static void loadPlayer(Player player) {
        YamlConfiguration names = YamlConfiguration.loadConfiguration(NAMEFILE);

        String prefix_path = player.getUniqueId() + ".prefix";
        String name_path = player.getUniqueId() + ".name";
        String suffix_path = player.getUniqueId() + ".suffix";

        if (!names.contains(prefix_path)) {names.set(prefix_path, "");}
        if (!names.contains(name_path)) {names.set(name_path, player.getName() + "§r");}
        if (!names.contains(suffix_path)) {names.set(suffix_path, "");}

        String prefix = names.getString(prefix_path);
        String name = names.getString(name_path);
        String suffix = names.getString(suffix_path);

        if (prefix == null || name == null || suffix == null) {PlayerNameTags.consoleError("Unable to load player %s's nickname! Check the name file.", player.getName()); return;}

        createNameTag(player, prefix, name, suffix);
        PlayerNameTags.consoleInfo("Creating Name Tag for player '%s'.", player.getName());
    }

    private static void unloadPlayer(Player player) {
        if (!hasName(player)) return;
        YamlConfiguration names = YamlConfiguration.loadConfiguration(NAMEFILE);

        names.set(player.getUniqueId() + ".prefix", getPrefix(player));
        names.set(player.getUniqueId() + ".name", getName(player));
        names.set(player.getUniqueId() + ".suffix", getSuffix(player));

        PlayerNameTags.consoleInfo("Attempting to save player name for '%s'.", player.getName());

        try {names.save(NAMEFILE);}
        catch (IOException e) {PlayerNameTags.consoleError("Unable to save player name file!");}
        finally {PlayerNameTags.consoleInfo("Successfully saved player name file.");}

        PLAYERNAMES.remove(player);
        PLAYERSTANDS.get(player).remove();
        PLAYERSTANDS.remove(player);

        TEAM.removePlayer(player);

        PlayerNameTags.consoleInfo("Deleted name tag entity for player '%s'.", player.getName());
    }

    public static void createNameTag(Player player, String prefix, String name, String suffix) {
        if (PLAYERSTANDS.containsKey(player)) {return;}

        Component nick = Component.text(prefix + " " + name + " " + suffix);

        ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        stand.customName(nick);
        stand.setCustomNameVisible(true);
        stand.setVisible(false);
        stand.setGravity(false);

        stand.getAttribute(Attribute.SCALE).setBaseValue(0.0625);

        if (!player.addPassenger(stand)) {PlayerNameTags.consoleWarn("Unable to anchor armor stand onto player '%s'.", player.getName());}

        PLAYERSTANDS.put(player, stand);
        setFullName(player, prefix, name, suffix);

        TEAM.addPlayer(player);
        player.playerListName(nick);
        player.displayName(nick);

        PlayerNameTags.consoleInfo("Successfully created Name Tag '%s§r' for player '%s'.", name, player.getName());
    }

    public static void updateNameTag(Player player) {
        if (!PLAYERNAMES.containsKey(player)) return;

        Component name = Component.text(getFullName(player));

        PLAYERSTANDS.get(player).customName(name);
        player.playerListName(name);
        player.displayName(name);

        PlayerNameTags.consoleInfo("Updated name tag for player '%s' to '%s§r'.", player.getName(), getFullName(player));
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        loadPlayer(player);
        if (event.joinMessage() != null) {event.joinMessage(Component.text("§c" + getName(player) + "§c entered the death game."));}
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (event.quitMessage() != null) {event.quitMessage(Component.text("§c" + getName(player) + "§c left the death game."));}
        unloadPlayer(player);
    }

    @EventHandler
    public static void onPlayerCrouch(PlayerToggleSneakEvent event) {
        if (event.isCancelled() || event.getPlayer().isInvisible() || event.getPlayer().isFlying()) return;
        if (PLAYERSTANDS.containsKey(event.getPlayer())) setNameVisible(event.getPlayer(), event.getPlayer().isSneaking());
    }

    @EventHandler
    public static void onSpectatorMode(PlayerGameModeChangeEvent event) {
        if (event.isCancelled() || event.getPlayer().isInvisible()) return;
        if (PLAYERSTANDS.containsKey(event.getPlayer())) setNameVisible(event.getPlayer(), event.getNewGameMode() != GameMode.SPECTATOR);
    }

    @EventHandler
    public static void onPlayerInvis(EntityPotionEffectEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (!PLAYERSTANDS.containsKey((Player) event.getEntity()) || event.getModifiedType() != PotionEffectType.INVISIBILITY) return;

        if (event.getAction() == EntityPotionEffectEvent.Action.ADDED) setNameVisible((Player) event.getEntity(), false);
        else if (event.getAction() != EntityPotionEffectEvent.Action.CHANGED) setNameVisible((Player) event.getEntity(), true);
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (event.deathMessage() != null) {event.deathMessage(Component.text("§4" + getName(player) + "§4 died in the death game."));}
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p, Sound.ENTITY_BLAZE_DEATH, SoundCategory.MASTER, 100f, 0.5f));
        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        event.getPlayer().addPassenger(PLAYERSTANDS.get(event.getPlayer()));
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------

    public static String getPrefix(Player player) {return hasPrefix(player) ? PLAYERNAMES.get(player).getFirst() : "";}
    public static boolean hasPrefix(Player player) {return PLAYERNAMES.containsKey(player) && !PLAYERNAMES.get(player).getFirst().isEmpty();}
    public static void setPrefix(Player player, String prefix) {
        if (PLAYERNAMES.containsKey(player)) PLAYERNAMES.get(player).set(0, prefix);
        else {PLAYERNAMES.put(player, new ArrayList<>(List.of(prefix, "", "")));}
        updateNameTag(player);
    }

    public static String getName(Player player) {return hasName(player) ? PLAYERNAMES.get(player).get(1) : player.getName();}
    public static boolean hasName(Player player) {return PLAYERNAMES.containsKey(player) && !PLAYERNAMES.get(player).get(1).isEmpty();}
    public static void setName(Player player, String name) {
        if (PLAYERNAMES.containsKey(player)) PLAYERNAMES.get(player).set(1, name);
        else {PLAYERNAMES.put(player, new ArrayList<>(List.of("", name, "")));}
        updateNameTag(player);
    }

    public static String getSuffix(Player player) {return hasSuffix(player) ? PLAYERNAMES.get(player).getLast() : "";}
    public static boolean hasSuffix(Player player) {return PLAYERNAMES.containsKey(player) && !PLAYERNAMES.get(player).getLast().isEmpty();}
    public static void setSuffix(Player player, String suffix) {
        if (PLAYERNAMES.containsKey(player)) PLAYERNAMES.get(player).set(2, suffix);
        else {PLAYERNAMES.put(player, new ArrayList<>(List.of("", "", suffix)));}
        updateNameTag(player);
    }

    public static String getFullName(Player player) {
        StringBuilder fullname = new StringBuilder(getName(player));
        if (hasPrefix(player)) fullname.insert(0, getPrefix(player) + " ");
        if (hasSuffix(player)) fullname.append(" ").append(getSuffix(player));
        return fullname.toString();
    }
    public static void setFullName(Player player, String prefix, String name, String suffix) {setPrefix(player, prefix); setName(player, name); setSuffix(player, suffix);}

    public static boolean isNameVisible(Player player) {return PLAYERSTANDS.containsKey(player) ? PLAYERSTANDS.get(player).isCustomNameVisible() : player.isInvisible();}
    public static void setNameVisible(Player player, boolean bool) {if (PLAYERSTANDS.containsKey(player)) PLAYERSTANDS.get(player).setCustomNameVisible(bool);}
}
