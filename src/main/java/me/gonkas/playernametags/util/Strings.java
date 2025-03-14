package me.gonkas.playernametags.util;

import me.gonkas.playernametags.PlayerNameTags;
import me.gonkas.playernametags.handlers.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Predicate;

public class Strings {

    // Same as String.contains(s) but ignores case.
    public static boolean containsIgnoreCase(String match, String string) {return match.toLowerCase().contains(string.toLowerCase());}

    // Returns a List containing the names of all online players whose names could contain 'string'.
    public static List<String> matchOnlinePlayersName(String string) {return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> containsIgnoreCase(p, string)).toList();}

    // Same as matchOnlinePlayersName() but it filters the player list using param 'filter'.
    public static List<String> matchOnlinePlayersName(String string, Predicate<? super Player> filter) {return Bukkit.getOnlinePlayers().stream().filter(filter).map(Player::getName).filter(p -> containsIgnoreCase(p, string)).toList();}

    public static boolean textIsValid(String text, TextType type) {
        if (text == null || text.isEmpty()) return false;

        // Essentially replaces the '§' character for Minecraft message/name formatting with the '&' character. See 'https://minecraft.wiki/w/Formatting_codes' for more info.
        // Users can still put '&' in their name by adding a backslash '\' before the '&' character.
        String format_chars = "0123456789abcdefklmnor";
        int text_length = text.length();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != '&' || i == text.length() - 1) continue;
            if (!format_chars.contains(String.valueOf(text.charAt(i + 1)))) continue;

            if (i == 0) text = new StringBuilder(text).replace(0, 1, "§").toString();
            else if (text.charAt(i - 1) != '\\') {text = new StringBuilder(text).replace(i, i + 1, "§").toString();}
            text_length -= 2;
        }

        if (ConfigHandler.hasInvalidChars(text)) return false;

        return switch (type) {
            case NAME -> text_length <= ConfigHandler.getMaxNameLength();
            case PREFIX -> text_length <= ConfigHandler.getMaxPrefixLength();
            case SUFFIX -> text_length <= ConfigHandler.getMaxSuffixLength();
        };
    }
}
