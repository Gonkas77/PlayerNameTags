package me.gonkas.playernametags.handlers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static me.gonkas.playernametags.PlayerNameTags.CONFIG;

public class ConfigHandler {

    private static String VALIDCHARS;
    private static int MAXNAMELENGTH;
    private static int MAXPREFIXLENGTH;
    private static int MAXSUFFIXLENGTH;
    private static boolean ALLOWCOLORS;
    private static List<OfflinePlayer> GAMEMASTERS;

    private static final String VALIDCHARSPATH = "valid-name-characters";
    private static final String MAXNAMELENGTHPATH = "max-name-length";
    private static final String MAXPREFIXLENGTHPATH = "max-prefix-length";
    private static final String MAXSUFFIXLENGTHPATH = "max-suffix-length";
    private static final String ALLOWCOLORSPATH = "enable-colors";
    private static final String GAMEMASTERSPATH = "game-masters";

    private static final String DEFAULTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890.-_ ";
    private static final int DEFAULTNAMELENGTH = 16;
    private static final int DEFAULTPREFIXLENGTH = 8;
    private static final int DEFAULTSUFFIXLENGTH = 8;
    private static final boolean DEFAULTALLOWCOLORS = true;
    private static final List<String> DEFAULTGMS = List.of("44e86468-37f4-4b17-b2e2-b2368bb24a93");

    public static void load() {
        fixConfigPaths(CONFIG);

        VALIDCHARS = CONFIG.getString(VALIDCHARSPATH);
        MAXNAMELENGTH = CONFIG.getInt(MAXNAMELENGTHPATH);
        MAXPREFIXLENGTH = CONFIG.getInt(MAXPREFIXLENGTHPATH);
        MAXSUFFIXLENGTH = CONFIG.getInt(MAXSUFFIXLENGTHPATH);
        ALLOWCOLORS = CONFIG.getBoolean(ALLOWCOLORSPATH);
        GAMEMASTERS = CONFIG.getStringList(GAMEMASTERSPATH).stream().map(e -> Bukkit.getOfflinePlayer(UUID.fromString(e))).toList();
    }
    
    public static void unload() {
        CONFIG.set(VALIDCHARSPATH, VALIDCHARS);
        CONFIG.set(MAXNAMELENGTHPATH, MAXNAMELENGTH);
        CONFIG.set(MAXPREFIXLENGTHPATH, MAXPREFIXLENGTH);
        CONFIG.set(MAXSUFFIXLENGTHPATH, MAXSUFFIXLENGTH);
        CONFIG.set(ALLOWCOLORSPATH, ALLOWCOLORS);
        CONFIG.set(GAMEMASTERSPATH, GAMEMASTERS.stream().map(e -> e.getUniqueId().toString()).toList());
    }

    // Fixes any used config paths that are broken or missing.
    private static void fixConfigPaths(FileConfiguration config) {
        if (pathIsString(config, VALIDCHARSPATH)) config.set(VALIDCHARSPATH, DEFAULTCHARS);
        if (pathIsInteger(config, MAXNAMELENGTHPATH)) config.set(MAXNAMELENGTHPATH, DEFAULTNAMELENGTH);
        if (pathIsInteger(config, MAXPREFIXLENGTHPATH)) config.set(MAXPREFIXLENGTHPATH, DEFAULTPREFIXLENGTH);
        if (pathIsInteger(config, MAXSUFFIXLENGTHPATH)) config.set(MAXSUFFIXLENGTHPATH, DEFAULTSUFFIXLENGTH);
        if (pathIsBoolean(config, ALLOWCOLORSPATH)) config.set(ALLOWCOLORSPATH, DEFAULTALLOWCOLORS);
        if (pathIsStringList(config, GAMEMASTERSPATH)) config.set(GAMEMASTERSPATH, DEFAULTGMS);
    }

    private static boolean pathIsString(FileConfiguration config, String path) {return config.contains(path) && config.get(path) instanceof String;}
    private static boolean pathIsInteger(FileConfiguration config, String path) {return config.contains(path) && config.get(path) instanceof Integer;}
    private static boolean pathIsBoolean(FileConfiguration config, String path) {return config.contains(path) && config.get(path) instanceof Boolean;}
    private static boolean pathIsStringList(FileConfiguration config, String path) {return config.contains(path) && config.get(path) instanceof List<?> && config.get(path).getClass() == String.class;}

    public static String getValidChars() {return VALIDCHARS;}
    public static void setValidChars(String chars) {VALIDCHARS = chars;}
    public static boolean hasInvalidChars(String chars) {
        for (char c : chars.toLowerCase().toCharArray()) {if (!ConfigHandler.getValidChars().contains(String.valueOf(c)) && c != '§') {return true;}}
        return false;
    }

    public static int getMaxNameLength() {return MAXNAMELENGTH;}
    public static void setMaxNameLength(int num) {MAXNAMELENGTH = num;}

    public static int getMaxPrefixLength() {return MAXPREFIXLENGTH;}
    public static void setMaxPrefixLength(int num) {MAXPREFIXLENGTH = num;}

    public static int getMaxSuffixLength() {return MAXSUFFIXLENGTH;}
    public static void setMaxSuffixLength(int num) {MAXSUFFIXLENGTH = num;}

    public static boolean getAllowColors() {return ALLOWCOLORS;}
    public static void setAllowColors(boolean value) {ALLOWCOLORS = value;}

    public static List<OfflinePlayer> getGameMasters() {return GAMEMASTERS;}
    public static void addGameMaster(Player player) {if (!isGameMaster(player)) GAMEMASTERS.add(player);}
    public static void removeGameMaster(Player player) {GAMEMASTERS.remove(player);}
    public static boolean isGameMaster(Player player) {return GAMEMASTERS.contains(player);}
}
