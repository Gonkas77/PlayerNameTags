package me.gonkas.playernametags.nametag.color;

import me.gonkas.playernametags.PlayerNameTags;
import me.gonkas.playernametags.files.Files;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColorManager implements Listener {

    public static final HashMap<Player, ArrayList<Color>> PLAYER_COLOR_MAP = new HashMap<>();

    public static void init() {Bukkit.getOnlinePlayers().forEach(ColorManager::loadPlayer);}
    public static void quit() {Bukkit.getOnlinePlayers().forEach(ColorManager::unloadPlayer);}

    // ----------------------------------------------------------------------------------------------------

    public static void loadPlayer(Player player) {

        File file = Files.getColorsFile(player);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        PLAYER_COLOR_MAP.merge(
                player,
                new ArrayList<>(config.getValues(false).entrySet().stream().filter(e -> {
                    if (!(e.getValue() instanceof String) || e.getKey().length() > 16) return false;
                    String hex = e.getValue().toString();

                    if (hex.length() != 7 || hex.charAt(0) != TextColor.HEX_CHARACTER) return false;
                    for (char c : hex.substring(1).toCharArray()) {if (!"0123456789abcdef".contains(String.valueOf(c))) return false;}

                    return true;
                }).map((e) -> Color.fromHex(e.getKey(), e.getValue().toString())).toList()),
                (mapped, given) -> {mapped.addAll(given); return mapped;}
        );
    }

    public static void unloadPlayer(Player player) {
        if (!PLAYER_COLOR_MAP.containsKey(player)) return;

        File file = Files.getColorsFile(player);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        PLAYER_COLOR_MAP.get(player).forEach(c -> config.set(c.name(), c.toHexString()));
        try {config.save(file);} catch (IOException e) {PlayerNameTags.consoleError("Unable to save player %s's colors file at '%s'!", player.getName(), file.getAbsolutePath());}

        PLAYER_COLOR_MAP.remove(player);
    }

    // ----------------------------------------------------------------------------------------------------

    public static void registerColor(Player player, Color color) {
        if (!PLAYER_COLOR_MAP.containsKey(player) || PLAYER_COLOR_MAP.get(player).stream().anyMatch(c -> c.name().equals(color.name()))) return;
        PLAYER_COLOR_MAP.merge(player, new ArrayList<>(List.of(color)), (mapped, given) -> {mapped.add(color); return mapped;});
    }
    public static void deleteColor(Player player, Color color) {deleteColor(player, color.name());}

    public static void registerColor(Player player, String color_name, String hex_value) {registerColor(player, Color.fromHex(color_name, hex_value));}
    public static void deleteColor(Player player, String color_name) {
        if (!PLAYER_COLOR_MAP.containsKey(player)) return;
        PLAYER_COLOR_MAP.get(player).removeIf(c -> c.name().equals(color_name));
    }

    // ----------------------------------------------------------------------------------------------------

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {loadPlayer(event.getPlayer());}

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {unloadPlayer(event.getPlayer());}
}
