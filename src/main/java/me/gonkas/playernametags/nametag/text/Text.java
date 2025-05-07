package me.gonkas.playernametags.nametag.text;

import me.gonkas.playernametags.nametag.color.Color;

public class Text {

    static Builder builder(String string) {return new Builder(string);}
    static Builder builder(Text text) {return new Builder(text);}

    String text;
    Color color;
    TextStyle style;

    String getText() {return text;}
    Color getColor() {return color;}
    TextStyle getStyle() {return style;}

    Text(String text, Color color, TextStyle style) {
        this.text = text;
        this.color = color;
        this.style = style;
    }

    public String toString() {
        StringBuilder string = new StringBuilder(text);
        string.insert(0, Color.getColorCode(this.color));
        string.insert(1, TextStyle.getStyleCode(this.style));
        return string.toString();
    }

    private static class Builder {

        String text;
        Color color;
        TextStyle style;

        Builder(String text) {
            this.text = text;
        }
        Builder(Text text) {
            this.text = text.getText();
            this.color = text.getColor();
            this.style = text.getStyle();
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
        public Builder setStyle(TextStyle style) {
            this.style = style;
            return this;
        }

        public Text build() {
            return new Text(text, color, style);
        }
    }
}
