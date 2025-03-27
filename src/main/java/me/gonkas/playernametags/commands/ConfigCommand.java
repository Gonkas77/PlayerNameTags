package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.PlayerNameTags;
import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.handlers.NameTagHandler;
import me.gonkas.playernametags.util.ConfigVarType;
import me.gonkas.playernametags.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class ConfigCommand implements CommandExecutor, TabCompleter {

    private static final String ENABLECOLORS = "enable-colors";
    private static final String ENABLEFORMATTING = "enable-formatting";
    private static final String ENABLEPLUGIN = "enable-plugin";
    private static final String MAXNAMELENGTH = "max-name-length";
    private static final String MAXPREFIXLENGTH = "max-prefix-length";
    private static final String MAXSUFFIXLENGTH = "max-suffix-length";
    private static final String VALIDNAMECHARACTERS = "valid-name-characters";

    private static final List<String> SUBCOMMANDS = List.of("get", "preset", "reset", "set");
    private static final List<String> OPTIONS = List.of(
            ENABLECOLORS,
            ENABLEFORMATTING,
            ENABLEPLUGIN,
            MAXNAMELENGTH,
            MAXPREFIXLENGTH,
            MAXSUFFIXLENGTH,
            VALIDNAMECHARACTERS
    );

    private static final List<String> BOOLEANPRESETS = List.of("ENABLED", "DISABLED", "DEFAULT");
    private static final List<String> TEXTLENGTHPRESETS = List.of("DEFAULT", "MINECRAFT_LENGTH", "HALF_MINECRAFT_LENGTH", "UNLIMITED");
    private static final List<String> CHARSPRESETS = List.of(
            "DEFAULT",
            "MINECRAFT",
            "DEFAULT_NO_SPACES",
            "DEFAULT_LOWERCASE",
            "DEFAULT_UPPERCASE",
            "DEFAULT_NO_LETTERS",
            "LETTERS_ONLY",
            "LETTERS_ONLY_LOWERCASE",
            "LETTERS_ONLY_UPPERCASE",
            "NUMBERS_ONLY",
            "SPECIAL_CHARACTERS_ONLY"
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        // Even if the plugin is disabled, attempting to enable it through the '/pntconfig' command is possible.
        // '/pntconfig preset enable-plugin ENABLED', '/pntconfig reset enable-plugin', and '/pntconfig set enable-plugin true' will all load the plugin.
        if (!PlayerNameTags.PLUGINISLOADED) {
            if (args[1].equals(ENABLEPLUGIN) && ((args[0].equals("preset") && args[2].equals("ENABLED")) || args[0].equals("reset") || (args[0].equals("set") && args[2].equals("true")))) {
                sender.sendMessage("§aEnabling PlayerNameTags!");
                PlayerNameTags.enable();
            } else {sender.sendMessage("§cPlugin PlayerNameTags is disabled! Enable using '/pntconfig reset enable-plugin', '/pntconfig set enable-plugin true', or '/pntconfig preset enable-plugin ENABLED'.");}
            return true;
        }

        if (args.length == 0 || !SUBCOMMANDS.contains(args[0])) {sender.sendMessage("§cInvalid command usage! Use '/pntconfig <get|preset|reset|set> <option>'."); return true;}
        if (args.length == 1 || !OPTIONS.contains(args[1])) {sender.sendMessage("§cInvalid sub-command usage! Use '/pntconfig " + args[0] + "<option>" + (args[0].equals("reset") ? "" : "<" + (args[0].equals("set") ? "value" : args[0]) + ">") + "."); return true;}

        switch (args[0]) {

            case "get" -> {
                switch (args[1]) {
                    case ENABLECOLORS -> sender.sendMessage(ConfigHandler.getAllowColors() ? "§aColors are enabled." : "§cColors are disabled.");
                    case ENABLEFORMATTING -> sender.sendMessage(ConfigHandler.getAllowFormatting() ? "§aFormatting is enabled." : "§cFormatting is disabled.");
                    case ENABLEPLUGIN -> sender.sendMessage("§aPlugin is enabled.");
                    case MAXNAMELENGTH -> sender.sendMessage("§aMaximum name length is " + (ConfigHandler.getMaxNameLength() == Integer.MAX_VALUE ? "unlimited" : ConfigHandler.getMaxNameLength()) + ".");
                    case MAXPREFIXLENGTH -> sender.sendMessage("§aMaximum prefix length is " + (ConfigHandler.getMaxPrefixLength() == Integer.MAX_VALUE ? "unlimited" : ConfigHandler.getMaxPrefixLength()) + ".");
                    case MAXSUFFIXLENGTH -> sender.sendMessage("§aMaximum suffix length is " + (ConfigHandler.getMaxSuffixLength() == Integer.MAX_VALUE ? "unlimited" : ConfigHandler.getMaxSuffixLength()) + ".");
                    case VALIDNAMECHARACTERS -> sender.sendMessage("§aValid text characters are \"" + ConfigHandler.getValidChars() + "\".");
                }
            }

            case "preset" -> {

                if (args.length == 2) {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}

                switch (args[1]) {

                    case ENABLECOLORS -> {
                        switch (args[2]) {
                            case "ENABLED","DEFAULT" -> ConfigHandler.setAllowColors(true);
                            case "DISABLED" -> ConfigHandler.setAllowColors(false);
                            default -> {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}
                        }
                    }

                    case ENABLEFORMATTING -> {
                        switch (args[2]) {
                            case "ENABLED","DEFAULT" -> ConfigHandler.setAllowFormatting(true);
                            case "DISABLED" -> ConfigHandler.setAllowFormatting(false);
                            default -> {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}
                        }
                    }

                    case ENABLEPLUGIN -> {
                        switch (args[2]) {
                            case "ENABLED","DEFAULT" -> {sender.sendMessage("§aPlugin already enabled!"); return true;}
                            case "DISABLED" -> PlayerNameTags.disable();
                            default -> {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}
                        }
                    }

                    case MAXNAMELENGTH -> {
                        switch (args[2]) {
                            case "DEFAULT" -> ConfigHandler.setMaxNameLength(24);
                            case "MINECRAFT_LENGTH" -> ConfigHandler.setMaxNameLength(16);
                            case "HALF_MINECRAFT_LENGTH" -> ConfigHandler.setMaxNameLength(8);
                            case "UNLIMITED" -> ConfigHandler.setMaxNameLength(Integer.MAX_VALUE);
                            default -> {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}
                        }
                    }

                    case MAXPREFIXLENGTH -> {
                        switch (args[2]) {
                            case "DEFAULT" -> ConfigHandler.setMaxPrefixLength(24);
                            case "MINECRAFT_LENGTH" -> ConfigHandler.setMaxPrefixLength(16);
                            case "HALF_MINECRAFT_LENGTH" -> ConfigHandler.setMaxPrefixLength(8);
                            case "UNLIMITED" -> ConfigHandler.setMaxPrefixLength(Integer.MAX_VALUE);
                            default -> {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}
                        }
                    }

                    case MAXSUFFIXLENGTH -> {
                        switch (args[2]) {
                            case "DEFAULT" -> ConfigHandler.setMaxSuffixLength(24);
                            case "MINECRAFT_LENGTH" -> ConfigHandler.setMaxSuffixLength(16);
                            case "HALF_MINECRAFT_LENGTH" -> ConfigHandler.setMaxSuffixLength(8);
                            case "UNLIMITED" -> ConfigHandler.setMaxSuffixLength(Integer.MAX_VALUE);
                            default -> {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}
                        }
                    }

                    case VALIDNAMECHARACTERS -> {
                        switch (args[2]) {
                            case "DEFAULT" -> ConfigHandler.setValidChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.-_ ");
                            case "MINECRAFT" -> ConfigHandler.setValidChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_");
                            case "DEFAULT_NO_SPACES" -> ConfigHandler.setValidChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.-_");
                            case "DEFAULT_LOWERCASE" -> ConfigHandler.setValidChars("abcdefghijklmnopqrstuvwxyz1234567890.-_ ");
                            case "DEFAULT_UPPERCASE" -> ConfigHandler.setValidChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.-_ ");
                            case "DEFAULT_NO_LETTERS" -> ConfigHandler.setValidChars("1234567890.-_ ");
                            case "LETTERS_ONLY" -> ConfigHandler.setValidChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
                            case "LETTERS_ONLY_LOWERCASE" -> ConfigHandler.setValidChars("abcdefghijklmnopqrstuvwxyz");
                            case "LETTERS_ONLY_UPPERCASE" -> ConfigHandler.setValidChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                            case "NUMBERS_ONLY" -> ConfigHandler.setValidChars("1234567890");
                            case "SPECIAL_CHARACTERS_ONLY" -> ConfigHandler.setValidChars(".-_ ");
                            default -> {sender.sendMessage("§cInvalid preset given! Match the presets displayed during command usage."); return true;}
                        }
                    }
                } sender.sendMessage("Set " + args[1] + " to preset '" + args[2].toUpperCase() + "'.");
            }

            case "reset" -> {

                if (args[1].equals(ENABLEPLUGIN)) {sender.sendMessage("§ePlugin is already enabled!"); return true;}

                sender.sendMessage("Reset '" + args[1] + "' to '" + switch (args[1]) {
                    case ENABLECOLORS -> ConfigHandler.resetAllowColors();
                    case ENABLEFORMATTING -> ConfigHandler.resetAllowFormatting();
                    case MAXNAMELENGTH -> ConfigHandler.resetMaxNameLength();
                    case MAXPREFIXLENGTH -> ConfigHandler.resetMaxPrefixLength();
                    case MAXSUFFIXLENGTH -> ConfigHandler.resetMaxSuffixLength();
                    case VALIDNAMECHARACTERS -> ConfigHandler.resetValidChars();
                    default -> ""; // Impossible to reach at this point in the code.
                } + "'.");
            }

            case "set" -> {

                if (args.length == 2) {sender.sendMessage("§cInvalid value given! Match the options or value type displayed during command usage."); return true;}

                switch (ConfigVarType.getType(args[2])) {

                    case BOOLEAN -> {
                        switch (args[1]) {
                            case ENABLECOLORS -> ConfigHandler.setAllowColors(Boolean.parseBoolean(args[2]));
                            case ENABLEFORMATTING -> ConfigHandler.setAllowFormatting(Boolean.parseBoolean(args[2]));
                            case ENABLEPLUGIN -> PlayerNameTags.disable();
                            case VALIDNAMECHARACTERS -> ConfigHandler.setValidChars(args[2]);
                            default -> sender.sendMessage("§cInvalid value given! Match the options or value type displayed during command usage.");
                        }
                    }

                    case INTEGER -> {
                        switch (args[1]) {
                            case MAXNAMELENGTH -> ConfigHandler.setMaxNameLength(Integer.parseInt(args[2]));
                            case MAXPREFIXLENGTH -> ConfigHandler.setMaxPrefixLength(Integer.parseInt(args[2]));
                            case MAXSUFFIXLENGTH -> ConfigHandler.setMaxSuffixLength(Integer.parseInt(args[2]));
                            default -> sender.sendMessage("§cInvalid value given! Match the options or value type displayed during command usage.");
                        }
                    }

                    case STRING -> {
                        if (args[1].equals(VALIDNAMECHARACTERS)) ConfigHandler.setValidChars(args[2]);
                        else {sender.sendMessage("§cInvalid value given! Match the options or value type displayed during command usage.");}
                    }
                } sender.sendMessage("Set '" + args[1] + "' to '" + args[2] + "'.");
            }
        }

        // Force update all name tags to follow new config rules.
        if (!args[0].equals("get")) {Bukkit.getOnlinePlayers().forEach(NameTagHandler::updateNameTag);}
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return switch (args.length) {
            case 1 -> SUBCOMMANDS.stream().filter(n -> Strings.containsIgnoreCase(n, args[0])).toList();
            case 2 -> OPTIONS.stream().filter(n -> Strings.containsIgnoreCase(n, args[1])).toList();
            case 3 -> {
                if (args[0].equals("preset")) {
                    yield switch (args[1]) {
                        case ENABLECOLORS, ENABLEFORMATTING, ENABLEPLUGIN -> BOOLEANPRESETS.stream().filter(n -> Strings.containsIgnoreCase(n, args[2])).toList();
                        case MAXNAMELENGTH, MAXPREFIXLENGTH, MAXSUFFIXLENGTH -> TEXTLENGTHPRESETS.stream().filter(n -> Strings.containsIgnoreCase(n, args[2])).toList();
                        case VALIDNAMECHARACTERS -> CHARSPRESETS.stream().filter(n -> Strings.containsIgnoreCase(n, args[2])).toList();
                        default -> List.of();
                    };
                } else if (args[0].equals("set")) {
                    yield switch (args[1]) {
                        case ENABLECOLORS, ENABLEFORMATTING, ENABLEPLUGIN -> Stream.of("true", "false").filter(n -> Strings.containsIgnoreCase(n, args[2])).toList();
                        case MAXNAMELENGTH, MAXPREFIXLENGTH, MAXSUFFIXLENGTH -> List.of("<length>");
                        case VALIDNAMECHARACTERS -> List.of("<characters>");
                        default -> List.of();
                    };
                } else {yield List.of();}
            }
            default -> List.of();
        };
    }
}
