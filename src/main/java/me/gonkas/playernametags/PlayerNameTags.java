package me.gonkas.playernametags;

import me.gonkas.playernametags.commands.*;
import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.CustomHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import me.gonkas.playernametags.util.Strings;
import me.gonkas.playernametags.util.TextType;
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
import java.time.Instant;
import java.util.UUID;

public final class PlayerNameTags extends JavaPlugin {

    public static PlayerNameTags INSTANCE;
    public static boolean PLUGINISLOADED = false;

    public static ConsoleCommandSender CONSOLE;
    private static final String PLUGINPREFIX = "[PlayerNameTags/";
    private static final String ERRORPREFIX = "§c" + PLUGINPREFIX + "ERROR] ";
    private static final String INFOPREFIX = "§7" + PLUGINPREFIX + "INFO] ";
    private static final String WARNPREFIX = "§e" + PLUGINPREFIX + "WARN] ";

    public static File PLUGINFOLDER = new File("plugins/PlayerNameTags");
    public static File BACKUPSFOLDER = new File(PLUGINFOLDER, "backups");

    public static FileConfiguration CONFIG;
    public static File NAMEFILE;
    private static final int NAMEFILEVERSION = 2;

    public static ScoreboardManager SCOREBOARDMANAGER;
    public static Scoreboard MAINSCOREBOARD;
    public static Team TEAM;

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        reloadConfig();
        CONFIG = getConfig();

        if (!PLUGINFOLDER.exists()) {PLUGINFOLDER.mkdir();}
        if (!BACKUPSFOLDER.exists()) {BACKUPSFOLDER.mkdir();}

        CONSOLE = Bukkit.getConsoleSender();
        getCommand("config").setExecutor(new ConfigCommand());
        getCommand("nametag").setExecutor(new NameTagCommand());
        getCommand("lockchat").setExecutor(new LockChatCommand());
        getCommand("unlockchat").setExecutor(new UnlockChatCommand());
        getCommand("gamemaster").setExecutor(new GameMasterCommand());

        if (!CONFIG.getBoolean("enable-plugin")) {consoleError("Plugin is disabled! Use '/config enable-plugin true' to enable the plugin!"); return;}

        load();
    }

    public static void load() {
        if (PLUGINISLOADED) return;

        consoleInfo("Loading plugin...");

        ConfigHandler.load();

        NAMEFILE = new File(PLUGINFOLDER, "player_names.yml");
        try {NAMEFILE.createNewFile();} catch (IOException e) {consoleError("Unable to create player name file."); throw new RuntimeException(e);}
        updateNameFileVersion(NAMEFILE);

        SCOREBOARDMANAGER = Bukkit.getScoreboardManager();
        MAINSCOREBOARD = SCOREBOARDMANAGER.getMainScoreboard();

        TEAM = MAINSCOREBOARD.getTeam("PlayerNameTags");
        if (TEAM == null) {TEAM = MAINSCOREBOARD.registerNewTeam("PlayerNameTags");}
        Bukkit.getOnlinePlayers().forEach(p -> TEAM.addPlayer(p));
        TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        TEAM.setCanSeeFriendlyInvisibles(false);

        NameTagHandler.load();

        Bukkit.getPluginManager().registerEvents(new NameTagHandler(), INSTANCE);
        Bukkit.getPluginManager().registerEvents(new CustomHandler(), INSTANCE);

        consoleInfo("Loaded successfully.");
        PLUGINISLOADED = true;
    }

    @Override
    public void onDisable() {
        if (!PLUGINISLOADED) return;

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

        names.getValues(false).forEach((path, v) -> {
            try {UUID.fromString(path);}
            catch (IllegalArgumentException e) {return;}

            if (!names.contains(path + ".prefix") || !Strings.textIsValid(names.getString(path + ".prefix"), TextType.PREFIX)) names.set(path + ".prefix", "");
            if (!names.contains(path + ".name") || !Strings.textIsValid(names.getString(path + ".name"), TextType.NAME)) {names.set(path + ".name", Strings.textIsValid(names.getString(path), TextType.NAME) ? names.getString(path) : Bukkit.getOfflinePlayer(UUID.fromString(path)).getName() + "§r");}
            if (!names.contains(path + ".suffix") || !Strings.textIsValid(names.getString(path + ".suffix"), TextType.SUFFIX)) names.set(path + ".suffix", "");
            if (!names.contains(path + ".hidden") || !(names.get(path + ".hidden") instanceof Boolean)) names.set(path + ".hidden", false);
        });

        try {names.save(NAMEFILE);} catch (IOException e) {
            consoleError("Unable to update name file. Creating backup...");

            String filename = "namefile_" + Instant.now().toString().replace('T', '_').split("\\.")[0].replaceAll(":", ".");
            try {Files.copy(namefile.toPath(), (new File(BACKUPSFOLDER, filename).toPath()), StandardCopyOption.REPLACE_EXISTING);}
            catch (IOException ex) {consoleError("Unable to create backup! Cancelling name file update. Please update manually or delete the file."); return;}

            consoleInfo("Created backup successfully. Deleting name file...");
            if (!NAMEFILE.delete()) {consoleError("Unable to delete name file! Please delete it manually and restart the server."); return;}

            consoleInfo("Successfully deleted name file. Reloading the server...");
            Bukkit.reload();
       }
    }
}
