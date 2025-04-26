package me.gonkas.playernametags.nametag.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Color;

public class Text {

    static Builder builder(String string) {return new Builder(string);}
    static Builder builder(Text text) {return new Builder(text);}

    String text;
    Color color;
    TextFormat format;

    String getText() {return text;}
    Color getColor() {return color;}
    TextFormat getFormat() {return format;}

    Text(String text, Color color, TextFormat format) {
        this.text = text;
        this.color = color;
        this.format = format;
    }

    public String toString() {
        StringBuilder string = new StringBuilder(text);
    }

    private static class Builder {

        String text;
        Color color;
        TextFormat format;

        Builder(String text) {
            this.text = text;
        }
        Builder(Text text) {
            this.text = text.getText();
            this.color = text.getColor();
            this.format = text.getFormat();
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }
        public Builder appendText(String text) {
            this.text += text;
            return this;
        }
        public Builder insertText(String text, int index) {
            this.text = (new StringBuilder(this.text)).insert(index, text).toString();
            return this;
        }
        public Builder replaceText(String target, String replacement) {
            this.text = this.text.replace(target, replacement);
            return this;
        }

        public Builder setColor(Color color) {
            this.color = color;
            return this;
        }
        public Builder setFormat(TextFormat format) {
            this.format = format;
            return this;
        }

        public Text build() {
            return new Text(text, color, format);
        }
    }
}
