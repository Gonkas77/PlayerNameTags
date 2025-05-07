package me.gonkas.playernametags.nametag.text;

public enum TextStyle {
    OBFUSCATED,
    BOLD,
    STRIKTHROUGH,
    UNDERLINED,
    ITALIC;

    public static String getStyleCode(TextStyle style) {
        return switch (style) {
            case OBFUSCATED -> "§k";
            case BOLD -> "§l";
            case STRIKTHROUGH -> "§m";
            case UNDERLINED -> "§n";
            case ITALIC -> "§o";
        };
    }
}
