package me.gonkas.playernametags.util;

public enum ConfigVarType {
    BOOLEAN,
    INTEGER,
    STRING;

    public static ConfigVarType getType(String var) {
        try {Integer.parseInt(var); return INTEGER;} catch (NumberFormatException ignore) {}
        if (var.equalsIgnoreCase("true") || var.equalsIgnoreCase("false")) return BOOLEAN;
        return STRING;
    }
}
