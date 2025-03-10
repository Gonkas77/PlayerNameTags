package me.gonkas.playernametags.handlers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static me.gonkas.playernametags.PlayerNameTags.CONFIG;

public class ConfigHandler {

    private static String VALIDCHARS;
    private static int MAXLENGTH;
    private static List<OfflinePlayer> GAMEMASTERS;

    private static final String VALIDCHARSPATH = "valid-name-characters";
    private static final String MAXLENGTHPATH = "max-name-length";
    private static final String GAMEMASTERSPATH = "game-masters";

    private static final String DEFAULTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890.-_ ";
    private static final int DEFAULTLENGTH = 24;
    private static final List<String> DEFAULTGMS = List.of("44e86468-37f4-4b17-b2e2-b2368bb24a93");

    public static void load() {
        fixConfigPaths(CONFIG);

        VALIDCHARS = CONFIG.getString(VALIDCHARSPATH);
        MAXLENGTH = CONFIG.getInt(MAXLENGTHPATH);
        GAMEMASTERS = CONFIG.getStringList(GAMEMASTERSPATH).stream().map(e -> Bukkit.getOfflinePlayer(UUID.fromString(e))).toList();
    }
    
    public static void unload() {
        CONFIG.set(VALIDCHARSPATH, VALIDCHARS);
        CONFIG.set(MAXLENGTHPATH, MAXLENGTH);
        CONFIG.set(GAMEMASTERSPATH, GAMEMASTERS.stream().map(e -> e.getUniqueId().toString()));
    }

    // Fixes any used config paths that are broken or missing.
    private static void fixConfigPaths(FileConfiguration config) {
        if (!config.contains(VALIDCHARSPATH) || !(config.get(VALIDCHARSPATH) instanceof String)) {config.set(VALIDCHARSPATH, DEFAULTCHARS);}
        if (!config.contains(MAXLENGTHPATH) || !(config.get(MAXLENGTHPATH) instanceof Integer)) {config.set(MAXLENGTHPATH, DEFAULTLENGTH);}
        if (!config.contains(GAMEMASTERSPATH) || !(config.get(GAMEMASTERSPATH) instanceof List<?>)) {config.set(GAMEMASTERSPATH, DEFAULTGMS);}

    }

    public static String getValidChars() {return VALIDCHARS;}
    public static int getMaxNameLength() {return MAXLENGTH;}

    public static List<OfflinePlayer> getGameMasters() {return GAMEMASTERS;}
    public static void addGameMaster(Player player) {GAMEMASTERS.add(player);}
    public static void removeGameMaster(Player player) {GAMEMASTERS.remove(player);}
    public static boolean isGameMaster(Player player) {return GAMEMASTERS.contains(player);}
}
