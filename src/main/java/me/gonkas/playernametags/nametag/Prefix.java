package me.gonkas.playernametags.nametag;

import me.gonkas.playernametags.nametag.text.Text;

import java.util.ArrayList;

public class Prefix {

    ArrayList<Text> texts;
    int length;

    Prefix(ArrayList<Text> texts) {

    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        texts.forEach(string::append);
        return string.toString();
    }
}
