package me.gonkas.playernametags.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigPaths {

    private static final ArrayList<String> PATHS = new ArrayList<>(74);

    // Categories
    private static final String NAMETAG = "nametag.";
    private static final String NAME = NAMETAG + "name.";
    private static final String PREFIX = NAMETAG + "prefix.";
    private static final String SUFFIX = NAMETAG + "suffix.";
    private static final String TITLE = NAMETAG + "title.";
    private static final String SUBTITLE = NAMETAG + "subtitle.";

    private static final String COLORS = "colors.";
    private static final String DECORATIONS = "decorations.";
    private static final String TEXT = "text.";

    // Common
    private static final String ENABLED = "enabled";

    private static final String ALLOWNONMINECRAFTCOLORS = "allow-non-minecraft-colors";
    private static final String MAXCOLORS = "max-colors";
    private static final String BANNEDCOLORS = "banned-colors";
    private static final String BANNEDCOLORCOMBOS = "banned-color-combos";

    private static final String MAXDECORATIONS = "max-decorations";
    private static final String BANNEDDECORATIONS = "banned-decorations";
    private static final String BANNEDDECORATIONCOMBOS = "banned-decoration-combos";

    private static final String PERMITTEDCHARS = "permitted-chars";
    private static final String MAXLENGTH = "max-length";


    // Paths -----------------------------------------------------------------------------------------------------------

    public static final String ENABLE_PLUGIN = "enable-plugin";
    public static final String VERSION = "version";


    public static final String NAMETAG_ENABLED = NAMETAG + ENABLED;
    public static final String NAMETAG_DISPLAY_IN_PLAYER_LIST = NAMETAG + "display-in-player-list";
    public static final String NAMETAG_DISPLAY_IN_CHAT = NAMETAG + "display-in-chat";


    public static final String NAME_ENABLED_COLORS = NAME + COLORS + ENABLED;
    public static final String NAME_ALLOW_NON_MINECRAFT_COLORS = NAME + COLORS + ALLOWNONMINECRAFTCOLORS;
    public static final String NAME_MAX_COLORS = NAME + COLORS + MAXCOLORS;
    public static final String NAME_BANNED_COLORS = NAME + COLORS + BANNEDCOLORS;
    public static final String NAME_BANNED_COLOR_COMBOS = NAME + COLORS + BANNEDCOLORCOMBOS;

    public static final String NAME_ENABLED_DECORATIONS = NAME + DECORATIONS + ENABLED;
    public static final String NAME_MAX_DECORATIONS = NAME + DECORATIONS + MAXDECORATIONS;
    public static final String NAME_BANNED_DECORATIONS = NAME + DECORATIONS + BANNEDDECORATIONS;
    public static final String NAME_BANNED_DECORATION_COMBOS = NAME + DECORATIONS + BANNEDDECORATIONCOMBOS;

    public static final String NAME_PERMITTED_CHARS = NAME + TEXT + PERMITTEDCHARS;
    public static final String NAME_MAX_LENGTH = NAME + TEXT + MAXLENGTH;


    public static final String PREFIX_ENABLED = PREFIX + ENABLED;

    public static final String PREFIX_ENABLED_COLORS = PREFIX + COLORS + ENABLED;
    public static final String PREFIX_ALLOW_NON_MINECRAFT_COLORS = PREFIX + COLORS + ALLOWNONMINECRAFTCOLORS;
    public static final String PREFIX_MAX_COLORS = PREFIX + COLORS + MAXCOLORS;
    public static final String PREFIX_BANNED_COLORS = PREFIX + COLORS + BANNEDCOLORS;
    public static final String PREFIX_BANNED_COLOR_COMBOS = PREFIX + COLORS + BANNEDCOLORCOMBOS;

    public static final String PREFIX_ENABLED_DECORATIONS = PREFIX + DECORATIONS + ENABLED;
    public static final String PREFIX_MAX_DECORATIONS = PREFIX + DECORATIONS + MAXDECORATIONS;
    public static final String PREFIX_BANNED_DECORATIONS = PREFIX + DECORATIONS + BANNEDDECORATIONS;
    public static final String PREFIX_BANNED_DECORATION_COMBOS = PREFIX + DECORATIONS + BANNEDDECORATIONCOMBOS;

    public static final String PREFIX_PERMITTED_CHARS = PREFIX + TEXT + PERMITTEDCHARS;
    public static final String PREFIX_MAX_LENGTH = PREFIX + TEXT + MAXLENGTH;


    public static final String SUFFIX_ENABLED = SUFFIX + ENABLED;

    public static final String SUFFIX_ENABLED_COLORS = SUFFIX + COLORS + ENABLED;
    public static final String SUFFIX_ALLOW_NON_MINECRAFT_COLORS = SUFFIX + COLORS + ALLOWNONMINECRAFTCOLORS;
    public static final String SUFFIX_MAX_COLORS = SUFFIX + COLORS + MAXCOLORS;
    public static final String SUFFIX_BANNED_COLORS = SUFFIX + COLORS + BANNEDCOLORS;
    public static final String SUFFIX_BANNED_COLOR_COMBOS = SUFFIX + COLORS + BANNEDCOLORCOMBOS;

    public static final String SUFFIX_ENABLED_DECORATIONS = SUFFIX + DECORATIONS + ENABLED;
    public static final String SUFFIX_MAX_DECORATIONS = SUFFIX + DECORATIONS + MAXDECORATIONS;
    public static final String SUFFIX_BANNED_DECORATIONS = SUFFIX + DECORATIONS + BANNEDDECORATIONS;
    public static final String SUFFIX_BANNED_DECORATION_COMBOS = SUFFIX + DECORATIONS + BANNEDDECORATIONCOMBOS;

    public static final String SUFFIX_PERMITTED_CHARS = SUFFIX + TEXT + PERMITTEDCHARS;
    public static final String SUFFIX_MAX_LENGTH = SUFFIX + TEXT + MAXLENGTH;


    public static final String TITLE_ENABLED = TITLE + ENABLED;

    public static final String TITLE_ENABLED_COLORS = TITLE + COLORS + ENABLED;
    public static final String TITLE_ALLOW_NON_MINECRAFT_COLORS = TITLE + COLORS + ALLOWNONMINECRAFTCOLORS;
    public static final String TITLE_MAX_COLORS = TITLE + COLORS + MAXCOLORS;
    public static final String TITLE_BANNED_COLORS = TITLE + COLORS + BANNEDCOLORS;
    public static final String TITLE_BANNED_COLOR_COMBOS = TITLE + COLORS + BANNEDCOLORCOMBOS;

    public static final String TITLE_ENABLED_DECORATIONS = TITLE + DECORATIONS + ENABLED;
    public static final String TITLE_MAX_DECORATIONS = TITLE + DECORATIONS + MAXDECORATIONS;
    public static final String TITLE_BANNED_DECORATIONS = TITLE + DECORATIONS + BANNEDDECORATIONS;
    public static final String TITLE_BANNED_DECORATION_COMBOS = TITLE + DECORATIONS + BANNEDDECORATIONCOMBOS;

    public static final String TITLE_PERMITTED_CHARS = TITLE + TEXT + PERMITTEDCHARS;
    public static final String TITLE_MAX_LENGTH = TITLE + TEXT + MAXLENGTH;


    public static final String SUBTITLE_ENABLED = SUBTITLE + ENABLED;

    public static final String SUBTITLE_ENABLED_COLORS = SUBTITLE + COLORS + ENABLED;
    public static final String SUBTITLE_ALLOW_NON_MINECRAFT_COLORS = SUBTITLE + COLORS + ALLOWNONMINECRAFTCOLORS;
    public static final String SUBTITLE_MAX_COLORS = SUBTITLE + COLORS + MAXCOLORS;
    public static final String SUBTITLE_BANNED_COLORS = SUBTITLE + COLORS + BANNEDCOLORS;
    public static final String SUBTITLE_BANNED_COLOR_COMBOS = SUBTITLE + COLORS + BANNEDCOLORCOMBOS;

    public static final String SUBTITLE_ENABLED_DECORATIONS = SUBTITLE + DECORATIONS + ENABLED;
    public static final String SUBTITLE_MAX_DECORATIONS = SUBTITLE + DECORATIONS + MAXDECORATIONS;
    public static final String SUBTITLE_BANNED_DECORATIONS = SUBTITLE + DECORATIONS + BANNEDDECORATIONS;
    public static final String SUBTITLE_BANNED_DECORATION_COMBOS = SUBTITLE + DECORATIONS + BANNEDDECORATIONCOMBOS;

    public static final String SUBTITLE_PERMITTED_CHARS = SUBTITLE + TEXT + PERMITTEDCHARS;
    public static final String SUBTITLE_MAX_LENGTH = SUBTITLE + TEXT + MAXLENGTH;


    // -----------------------------------------------------------------------------------------------------------------


    // Essentially functions the same way an enum class' valueOf() method does.
    // This will be used to check if an inputted path in the /pntconfig command is an existing path.
    // For better user readability/QoL the field names do not perfectly match their String value.
    public static String valueOf(String name) {
        name = name.replaceAll("-", "_").toUpperCase();
        try {return ConfigPaths.class.getDeclaredField(name).get(null).toString();}
        catch (NoSuchFieldException | IllegalAccessException e) {return "";}
    }

    public static List<String> getAllPaths() {
        if (PATHS.isEmpty()) {PATHS.addAll(Arrays.stream(ConfigPaths.class.getDeclaredFields()).map(f -> f.getName().replaceAll("_", "-").toLowerCase()).toList());}
        return PATHS;
    }

    public static ConfigVarType getVarType(String path) throws IllegalArgumentException {
        if (!getAllPaths().contains(path)) throw new IllegalArgumentException("Path given is not a valid Config path.");

        if (path.contains(PERMITTEDCHARS)) return ConfigVarType.STRING;
        if (path.contains(VERSION) || path.contains(MAXCOLORS) || path.contains(MAXDECORATIONS) || path.contains(MAXLENGTH)) return ConfigVarType.INTEGER;
        if (path.contains(ENABLE_PLUGIN) || path.contains(ENABLED) || path.contains(NAMETAG_DISPLAY_IN_PLAYER_LIST) || path.contains(NAMETAG_DISPLAY_IN_CHAT) || path.contains(ALLOWNONMINECRAFTCOLORS)) return ConfigVarType.BOOLEAN;
        return ConfigVarType.STRINGLIST;
    }
}
