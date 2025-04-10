package me.gonkas.playernametags.config;

import org.bukkit.configuration.file.FileConfiguration;

import static me.gonkas.playernametags.PlayerNameTags.CONFIG;

public class ConfigPresets {

    private static final FileConfiguration DEFAULT = (FileConfiguration) CONFIG.getDefaults();
    private static final FileConfiguration CHAOS = getChaos();

    private static FileConfiguration getChaos() {
        assert DEFAULT != null;
        DEFAULT.set(ConfigPaths.SUFFIX_ENABLED, true);
        DEFAULT.set(ConfigPaths.SUBTITLE_ENABLED, true);
        return DEFAULT;
    }


}
