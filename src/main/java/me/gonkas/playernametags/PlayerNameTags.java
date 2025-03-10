package me.gonkas.playernametags;

import me.gonkas.playernametags.commands.LockChatCommand;
import me.gonkas.playernametags.commands.NameCommand;
import me.gonkas.playernametags.commands.RemoveNameCommand;
import me.gonkas.playernametags.commands.UnlockChatCommand;
import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.CustomHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;

public final class PlayerNameTags extends JavaPlugin {

    public static ConsoleCommandSender CONSOLE;
    private static final String PLUGINPREFIX = "[PlayerNameTags/";
    private static final String ERRORPREFIX = "§4" + PLUGINPREFIX + "ERROR] ";
    private static final String INFOPREFIX = "§f" + PLUGINPREFIX + "INFO] ";
    private static final String WARNPREFIX = "§e" + PLUGINPREFIX + "WARN] ";

    public static FileConfiguration CONFIG;
    public static File NAMEFILE;

    public static ScoreboardManager SCOREBOARDMANAGER;
    public static Scoreboard MAINSCOREBOARD;
    public static Team TEAM;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();

        CONFIG = getConfig();
        CONSOLE = Bukkit.getConsoleSender();

        NAMEFILE = new File("plugins/PlayerNameTags/player_names.yml");
        try {NAMEFILE.createNewFile();} catch (IOException e) {consoleError("Unable to create player name file."); throw new RuntimeException(e);}

        SCOREBOARDMANAGER = Bukkit.getScoreboardManager();
        MAINSCOREBOARD = SCOREBOARDMANAGER.getMainScoreboard();

        TEAM = MAINSCOREBOARD.getTeam("PlayerNameTags");
        if (TEAM == null) {TEAM = MAINSCOREBOARD.registerNewTeam("PlayerNameTags");}
        TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        TEAM.setCanSeeFriendlyInvisibles(false);

        Bukkit.getPluginManager().registerEvents(new NameTagHandler(), this);
        Bukkit.getPluginManager().registerEvents(new CustomHandler(), this);

        getCommand("name").setExecutor(new NameCommand());
        getCommand("removename").setExecutor(new RemoveNameCommand());
        getCommand("lockchat").setExecutor(new LockChatCommand());
        getCommand("unlockchat").setExecutor(new UnlockChatCommand());

        ConfigHandler.load();
        NameTagHandler.load();

        consoleInfo("Loaded successfully.");
    }

    @Override
    public void onDisable() {
        NameTagHandler.unload();
    }

    public static void consoleError(String message, String... args) {CONSOLE.sendMessage(String.format(ERRORPREFIX + message, (Object[]) args));}
    public static void consoleInfo(String message, String... args) {CONSOLE.sendMessage(String.format(INFOPREFIX + message, (Object[]) args));}
    public static void consoleWarn(String message, String... args) {CONSOLE.sendMessage(String.format(WARNPREFIX + message, (Object[]) args));}
}
