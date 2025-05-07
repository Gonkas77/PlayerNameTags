package me.gonkas.playernametags.config;

import org.bukkit.configuration.Configuration;

import static me.gonkas.playernametags.PlayerNameTags.CONFIG;

public class ConfigPresets {

    private static final Configuration DEFAULT = CONFIG.getDefaults();
    private static final Configuration CHAOS = getChaos();

    private static Configuration getChaos() {
        assert DEFAULT != null;
        DEFAULT.set(ConfigPaths.SUFFIX_ENABLED, true);
        DEFAULT.set(ConfigPaths.SUBTITLE_ENABLED, true);
        return DEFAULT;
    }


}
