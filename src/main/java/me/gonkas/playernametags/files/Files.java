package me.gonkas.playernametags.files;

import org.bukkit.OfflinePlayer;

import java.io.File;

public class Files {

    public static final File PLUGIN_DIR = new File("plugins/PlayerNameTags");
    public static final File BACKUPS_DIR = new File(PLUGIN_DIR, "backups");

    public static final File NAMETAGS_DIR = new File(PLUGIN_DIR, "nametags");
    public static final File NAMETAG_BACKUPS_DIR = new File(BACKUPS_DIR, "nametags");

    public static final File COLORS_DIR = new File(PLUGIN_DIR, "colors");
    public static final File COLOR_BACKUPS_DIR = new File(BACKUPS_DIR, "colors");

    public static final File LOGS_DIR = new File(PLUGIN_DIR, "logs");

    public static final File[] DEFAULT_DIRS = new File[]{
            PLUGIN_DIR,
            BACKUPS_DIR,
            NAMETAGS_DIR,
            NAMETAG_BACKUPS_DIR,
            COLORS_DIR,
            COLOR_BACKUPS_DIR,
            LOGS_DIR
    };

    public static final File CONFIG_FILE = new File(PLUGIN_DIR, "config.yml");

    private static File getPlayerFile(File directory, OfflinePlayer player) {
        File file = new File(directory, player.getUniqueId() + ".yml");
        FileManager.createNewFile(file);
        return file;
    }

    public static File getNameTagFile(OfflinePlayer player) {return getPlayerFile(NAMETAGS_DIR, player);}
    public static File getColorsFile(OfflinePlayer player) {return getPlayerFile(COLORS_DIR, player);}
}
