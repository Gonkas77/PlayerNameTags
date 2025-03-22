package me.gonkas.playernametags.commands;

import me.gonkas.playernametags.PlayerNameTags;
import me.gonkas.playernametags.handlers.ConfigHandler;
import me.gonkas.playernametags.util.ConfigVarType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("get", "preset", "reset", "set");
    private static final List<String> OPTIONS = List.of(
            "enable-colors",
            "enable-formatting",
            "enable-plugin",
            "max-name-length",
            "max-prefix-length",
            "max-suffix-length",
            "valid-name-characters"
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        // Even if the plugin is disabled, attempting to enable it through the '/config' command is possible.
        // '/config preset enable-plugin ENABLED', '/config reset enable-plugin', and '/config set enable-plugin true' will all load the plugin.
        if (!PlayerNameTags.PLUGINISLOADED) {
            if (args[1].equals("enable-plugin") && ((args[0].equals("preset") && args[2].equals("ENABLED")) || args[0].equals("reset") || (args[0].equals("set") && args[2].equals("true")))) {
                sender.sendMessage("§aEnabling PlayerNameTags!");
                PlayerNameTags.load();
            } else {sender.sendMessage("§cPlugin PlayerNameTags is disabled! Enable using '/config reset enable-plugin', '/config set enable-plugin true', or '/config preset enable-plugin ENABLED'.");}
            return true;
        }

        if (args.length == 0 || !SUBCOMMANDS.contains(args[0])) {sender.sendMessage("§cInvalid command usage! Use '/config <get|preset|reset|set> <option>'."); return true;}
        if (args.length == 1 || !OPTIONS.contains(args[1])) {sender.sendMessage("§cInvalid sub-command usage! Use '/config " + args[0] + "<option>" + (args[0].equals("reset") ? "" : "<" + (args[0].equals("set") ? "value" : args[0]) + ">") + "."); return true;}

        // Although I usually order switch statements alphabetically, the 'preset' branch was so much more complex than the others that I put it at the end.
        switch (args[0]) {

            case "get" -> {
                switch (args[1]) {
                    case "enable-colors" -> sender.sendMessage(ConfigHandler.getAllowColors() ? "§aColors are enabled." : "§cColors are disabled.");
                    case "enable-formatting" -> sender.sendMessage(ConfigHandler.getAllowFormatting() ? "§aFormatting is enabled." : "§cFormatting is disabled.");
                    case "enable-plugin" -> sender.sendMessage("§aPlugin is enabled.");
                    case "max-name-length" -> sender.sendMessage("§aMaximum name length is " + ConfigHandler.getMaxNameLength() + ".");
                    case "max-prefix-length" -> sender.sendMessage("§aMaximum prefix length is " + ConfigHandler.getMaxPrefixLength() + ".");
                    case "max-suffix-length" -> sender.sendMessage("§aMaximum suffix length is " + ConfigHandler.getMaxSuffixLength() + ".");
                    case "valid-name-characters" -> sender.sendMessage("§aValid text characters are \"" + ConfigHandler.getValidChars() + "\".");
                } return true;
            }

            case "reset" -> {

                if (args[1].equals("enable-plugin")) {sender.sendMessage("§ePlugin is already enabled!"); return true;}

                sender.sendMessage("§aReset '" + args[1] + "' to '" + switch (args[1]) {
                    case "enable-colors" -> ConfigHandler.resetAllowColors();
                    case "enable-formatting" -> ConfigHandler.resetAllowFormatting();
                    case "max-name-length" -> ConfigHandler.resetMaxNameLength();
                    case "max-prefix-length" -> ConfigHandler.resetMaxPrefixLength();
                    case "max-suffix-length" -> ConfigHandler.resetMaxSuffixLength();
                    case "valid-name-characters" -> ConfigHandler.resetValidChars();
                    default -> ""; // Impossible to reach at this point in the code.
                } + "'.");
                return true;
            }

            case "set" -> {

                switch (ConfigVarType.getType(args[2])) {

                    case BOOLEAN -> {
                        switch (args[1]) {
                            case "enable-colors" -> ConfigHandler.setAllowColors(Boolean.parseBoolean(args[2]));
                            case "enable-formatting" -> ConfigHandler.setAllowFormatting(Boolean.parseBoolean(args[2]));
                            case "valid-name-characters" -> ConfigHandler.setValidChars(args[2]);
                            default -> sender.sendMessage("§cInvalid value given! Match the options or value type displayed during command usage.");
                        }
                    }

                    case INTEGER -> {
                        switch (args[1]) {
                            case "max-name-length" -> ConfigHandler.setMaxNameLength(Integer.parseInt(args[2]));
                            case "max-prefix-length" -> ConfigHandler.setMaxPrefixLength(Integer.parseInt(args[2]));
                            case "max-suffix-length" -> ConfigHandler.setMaxSuffixLength(Integer.parseInt(args[2]));
                            default -> sender.sendMessage("§cInvalid value given! Match the options or value type displayed during command usage.");
                        }
                    }

                    case STRING -> {
                        if (args[1].equals("valid-name-characters")) ConfigHandler.setValidChars(args[2]);
                        else {sender.sendMessage("§cInvalid value given! Match the options or value type displayed during command usage.");}
                    }
                }

                sender.sendMessage("§aSet '" + args[1] + "' to '" + args[2] + "'.");
                return true;
            }

            case "preset" -> {

            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
