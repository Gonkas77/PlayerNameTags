package me.gonkas.playernametags.handlers;

import org.bukkit.configuration.file.FileConfiguration;

import static me.gonkas.playernametags.PlayerNameTags.CONFIG;

public class ConfigHandler {

    private static String VALIDCHARS;
    private static int MAXLENGTH;

    private static final String VALIDCHARSPATH = "valid-name-characters";
    private static final String MAXLENGTHPATH = "max-name-length";

    private static final String DEFAULTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890.-_ ";
    private static final int DEFAULTLENGTH = 24;

    public static void load() {
        fixConfigPaths(CONFIG);

        VALIDCHARS = CONFIG.getString(VALIDCHARSPATH);
        MAXLENGTH = CONFIG.getInt(MAXLENGTHPATH);
    }

    // Fixes any used config paths that are broken or missing.
    public static void fixConfigPaths(FileConfiguration config) {
        if (!config.contains(VALIDCHARSPATH) || !(config.get(VALIDCHARSPATH) instanceof String)) {config.set(VALIDCHARSPATH, DEFAULTCHARS);}
        if (!config.contains(MAXLENGTHPATH) || !(config.get(MAXLENGTHPATH) instanceof Integer)) {config.set(MAXLENGTHPATH, DEFAULTLENGTH);}
    }

    public static String getValidChars() {return VALIDCHARS;}
    public static int getMaxNameLength() {return MAXLENGTH;}
}
