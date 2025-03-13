package me.gonkas.playernametags;

import me.gonkas.playernametags.commands.*;
import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.CustomHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public final class PlayerNameTags extends JavaPlugin {

    public static ConsoleCommandSender CONSOLE;
    private static final String PLUGINPREFIX = "[PlayerNameTags/";
    private static final String ERRORPREFIX = "§4" + PLUGINPREFIX + "ERROR] ";
    private static final String INFOPREFIX = "§7" + PLUGINPREFIX + "INFO] ";
    private static final String WARNPREFIX = "§e" + PLUGINPREFIX + "WARN] ";

    public static File PLUGINFOLDER = new File("plugins/PlayerNameTags");
    public static File BACKUPSFOLDER = new File(PLUGINFOLDER, "backups");

    public static FileConfiguration CONFIG;
    public static File NAMEFILE;
    private static final int NAMEFILEVERSION = 1;

    public static ScoreboardManager SCOREBOARDMANAGER;
    public static Scoreboard MAINSCOREBOARD;
    public static Team TEAM;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();

        if (!PLUGINFOLDER.exists()) {PLUGINFOLDER.mkdir();}
        if (!BACKUPSFOLDER.exists()) {BACKUPSFOLDER.mkdir();}

        CONFIG = getConfig();
        CONSOLE = Bukkit.getConsoleSender();

        NAMEFILE = new File(PLUGINFOLDER, "player_names.yml");
        try {NAMEFILE.createNewFile();} catch (IOException e) {consoleError("Unable to create player name file."); throw new RuntimeException(e);}
        updateNameFileVersion(NAMEFILE);

        SCOREBOARDMANAGER = Bukkit.getScoreboardManager();
        MAINSCOREBOARD = SCOREBOARDMANAGER.getMainScoreboard();

        TEAM = MAINSCOREBOARD.getTeam("PlayerNameTags");
        if (TEAM == null) {TEAM = MAINSCOREBOARD.registerNewTeam("PlayerNameTags");}
        TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        TEAM.setCanSeeFriendlyInvisibles(false);

        Bukkit.getPluginManager().registerEvents(new NameTagHandler(), this);
        Bukkit.getPluginManager().registerEvents(new CustomHandler(), this);

        getCommand("nametag").setExecutor(new NameTagCommand());
        getCommand("lockchat").setExecutor(new LockChatCommand());
        getCommand("unlockchat").setExecutor(new UnlockChatCommand());
        getCommand("gamemaster").setExecutor(new GameMasterCommand());

        ConfigHandler.load();
        NameTagHandler.load();

        consoleInfo("Loaded successfully.");
    }

    @Override
    public void onDisable() {
        NameTagHandler.unload();
        ConfigHandler.unload();
        saveConfig();
    }

    public static void consoleError(String message, String... args) {CONSOLE.sendMessage(String.format(ERRORPREFIX + message, (Object[]) args));}
    public static void consoleInfo(String message, String... args) {CONSOLE.sendMessage(String.format(INFOPREFIX + message, (Object[]) args));}
    public static void consoleWarn(String message, String... args) {CONSOLE.sendMessage(String.format(WARNPREFIX + message, (Object[]) args));}

    private static void updateNameFileVersion(File namefile) {
        YamlConfiguration names = YamlConfiguration.loadConfiguration(namefile);
        if (names.getInt("version") == NAMEFILEVERSION) return;
        names.set("version", NAMEFILEVERSION);

        names.getValues(false).forEach((path, value) -> {
            try {UUID.fromString(path);}
            catch (IllegalArgumentException e) {return;}

            names.set(path + ".prefix", "");
            names.set(path + ".name", value instanceof String ? value : Bukkit.getOfflinePlayer(UUID.fromString(path)).getName() + "§r");
            names.set(path + ".suffix", "");
        });

        try {names.save(NAMEFILE);} catch (IOException e) {
            consoleError("Unable to update name file. Creating backup...");

            try {Files.copy(namefile.toPath(), (new File(BACKUPSFOLDER, "old_namefile").toPath()), StandardCopyOption.REPLACE_EXISTING);}
            catch (IOException ex) {consoleError("Unable to create backup! Cancelling name file update. Please update manually or delete the file."); return;}

            consoleInfo("Created backup successfully. Deleting name file...");
            if (!NAMEFILE.delete()) {consoleError("Unable to delete name file! Please delete it manually and restart the server."); return;}

            consoleInfo("Successfully deleted name file. Reloading the server...");
            Bukkit.reload();
       }
    }
}
