package me.gonkas.playernametags.nametag.color;

import net.kyori.adventure.text.format.TextColor;

public class Color {

    private static final int BLACK = 0x000000;
    private static final int DARK_BLUE = 0x0000aa;
    private static final int DARK_GREEN = 0x00aa00;
    private static final int DARK_AQUA = 0x00aaaa;
    private static final int DARK_RED = 0xaa0000;
    private static final int DARK_PURPLE = 0xaa00aa;
    private static final int GOLD = 0xffaa00;
    private static final int GRAY = 0xaaaaaa;
    private static final int DARK_GRAY = 0x555555;
    private static final int BLUE = 0x5555ff;
    private static final int GREEN = 0x55ff55;
    private static final int AQUA = 0x55ffff;
    private static final int RED = 0xff5555;
    private static final int LIGHT_PURPLE = 0xff55ff;
    private static final int YELLOW = 0xffff55;
    private static final int WHITE = 0xffffff;

    private final String name;
    private final TextColor color;

    Color(String name, TextColor color) {
        this.name = name;
        this.color = color;
    }

    public static Color fromHex(final String name, final String hex) {
        return new Color(name, TextColor.fromHexString(hex));
    }

    public static Color fromRGB(final String name, final int red, final int green, final int blue) {
        return new Color(name, TextColor.color(red, green, blue));
    }

    public String name() {return name;}
    public TextColor color() {return color;}
    public int value() {return color.value();}

    @Override
    public String toString() {return name + ":" + color.asHexString();}
    public String toHexString() {return color.asHexString();}

    public static String getColorCode(Color color) {
        return switch (color.color.value()) {
            case BLACK -> "§0";
            case DARK_BLUE -> "§1";
            case DARK_GREEN -> "§2";
            case DARK_AQUA -> "§3";
            case DARK_RED -> "§4";
            case DARK_PURPLE -> "§5";
            case GOLD -> "§6";
            case GRAY -> "§7";
            case DARK_GRAY -> "§8";
            case BLUE -> "§9";
            case GREEN -> "§a";
            case AQUA -> "§b";
            case RED -> "§c";
            case LIGHT_PURPLE -> "§d";
            case YELLOW -> "§e";
            case WHITE -> "§f";
            default -> null;
        };
    }
}
