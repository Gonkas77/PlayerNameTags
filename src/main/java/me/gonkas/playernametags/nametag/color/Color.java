package me.gonkas.playernametags.nametag.color;

import net.kyori.adventure.text.format.TextColor;

public class Color {

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

    public String toHexString() {return color.asHexString();}
    public int toRGBValue() {return color.value();}

    @Override
    public String toString() {return name + ":" + color.asHexString();}
}
