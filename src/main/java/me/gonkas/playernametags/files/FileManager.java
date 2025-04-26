package me.gonkas.playernametags.files;

import me.gonkas.playernametags.PlayerNameTags;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static void init() {for (File dir : Files.DEFAULT_DIRS) {if (!dir.exists()) dir.mkdirs();}}

    public static void createNewFile(File file) {
        if (file.exists()) return;
        try {file.getParentFile().mkdirs(); file.createNewFile();}
        catch (IOException e) {PlayerNameTags.consoleError("Unable to create file '%s' at '%s'!", file.getName(), file.getAbsolutePath());}
    }
}
