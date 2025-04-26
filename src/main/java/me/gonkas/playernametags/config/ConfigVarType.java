package me.gonkas.playernametags.config;

import java.util.Arrays;

public enum ConfigVarType {
    BOOLEAN,
    INTEGER,
    STRING,
    STRINGLIST;

    public static Class<?> toClass(ConfigVarType varType) {
        return switch (varType) {
            case BOOLEAN -> Boolean.class;
            case INTEGER -> Integer.class;
            case STRING -> String.class;
            case STRINGLIST -> Arrays.stream(new String[0]).toList().getClass();
        };
    }
}
