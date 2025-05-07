package me.gonkas.playernametags.nametag.color;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Gradient {

    List<Vertex> gradient = new ArrayList<>();

    Gradient(@NotNull Vertex v1, @NotNull Vertex v2, @NotNull Vertex... vertices) {
        this.gradient.add(v1);
        this.gradient.add(v2);
        this.gradient.addAll(List.of(vertices));

        throwIfRepeatingPositions();
        gradient.sort(Comparator.comparingInt(v -> (int) v.pos*100));
    }

    Gradient(@NotNull List<Triple<TextColor, Float, Float>> triples) {
        this.gradient = triples.stream().map(t -> new Vertex(t.getLeft(), t.getMiddle(), t.getRight())).toList();

        if (triples.size() < 2) throw new IllegalArgumentException("Expected 2 or more vertices, found " + triples.size() + ".");
        throwIfRepeatingPositions();
        gradient.sort(Comparator.comparingInt(v -> (int) v.pos*100));
    }

    public @NotNull List<TextColor> getColors(int length) {
        TextColor[] result = new TextColor[length];
        result[0] = gradient.getFirst().textColor;

        for (int i=0; i < gradient.size(); i++) {
            if (i == 0) continue;
            Vertex start = gradient.get(i-1);
            Vertex end = gradient.get(i);

            int startIndex = findIndex(start);
            int endIndex = findIndex(end);
            result[findIndex(end)] = end.textColor;

            if (endIndex - startIndex == 1) continue;

            Point middle = Vertex.getMiddlePoint(start, end);
            int middleIndex = findIndex(middle);
            result[middleIndex] = middle.textColor;

            int distance = endIndex - middleIndex;
            if (endIndex - middleIndex == 1) continue;

            for (int e=1; e < distance; e++) {
                result[e+startIndex] = Point.calculateColor(start, middle, distance-1, e);
                result[e+middleIndex] = Point.calculateColor(middle, end, distance-1, e);
            }
        }

        return Arrays.asList(result);
    }

    public @NotNull Component apply(@NotNull String text) {
        List<TextColor> colors = getColors(text.length());
        Component result = Component.text("");
        for (int i=0; i < text.length(); i++) {result = result.append(Component.text(text.charAt(i)).color(colors.get(i)));}
        return result;
    }
0
    public @NotNull Component apply(@NotNull Component component) {
        if (component instanceof TextComponent result) return apply(result.content());
        else throw new IllegalArgumentException("Component given is not a TextComponent and therefore cannot have a Gradient applied to it");
    }

    public int findIndex(@NotNull Point p) {
        return (int) p.pos * gradient.size();
    }

    public static class Point {

        final @NotNull TextColor textColor;
        final float pos;

        Point(@NotNull TextColor textColor, float pos) {
            this.textColor = textColor;
            this.pos = pos;
        }

        Point(@NotNull Point point) {
            this.textColor = point.textColor;
            this.pos = point.pos;
        }

        public static TextColor calculateColor(@NotNull Point start, @NotNull Point end, int length, int index) {
            float redDiff = (float) (end.textColor.red() - start.textColor.red()) / length;
            float greenDiff = (float) (end.textColor.green() - start.textColor.green()) / length;
            float blueDiff = (float) (end.textColor.blue() - start.textColor.blue()) / length;

            return TextColor.color(start.textColor.red() + redDiff*index, start.textColor.green() + greenDiff*index, start.textColor.blue() + blueDiff*index);
        }
    }

    public static class Vertex extends Point {

        final float strength;

        Vertex(@NotNull TextColor textColor, float pos, float strength) {
            super(textColor, pos);
            this.strength = strength;
        }

        Vertex(@NotNull Point point, float strength) {
            super(point);
            this.strength = strength;
        }

        public static Point getMiddlePoint(@NotNull Vertex v1, @NotNull Vertex v2) {
            float min = Math.min(v1.pos, v2.pos);
            Vertex minVertex = min == v1.pos ? v1 : v2;
            return new Point(calcMiddleColor(v1, v2), min + Math.abs(v1.pos - v2.pos) * (minVertex.strength / (v1.strength + v2.strength)));
        }

        public static TextColor calcMiddleColor(@NotNull Vertex v1, @NotNull Vertex v2) {
            float totalStrength = v1.strength + v2.strength;
            float ratio = v1.strength / totalStrength;

            int red = (int) (v1.textColor.red() * ratio + v2.textColor.red() * (1 - ratio));
            int green = (int) (v1.textColor.green() * ratio + v2.textColor.green() * (1 - ratio));
            int blue = (int) (v1.textColor.blue() * ratio + v2.textColor.blue() * (1 - ratio));

            return TextColor.color(red, green, blue);
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private void throwIfRepeatingPositions() {
        List<Float> positions = this.gradient.stream().map(v -> v.pos).toList();
        for (int i=0; i < positions.size(); i++) {
            if (positions.contains(positions.get(i))) {throw new IllegalArgumentException(
                    "Vertex " + (i + 2) + " has the same position as vertex " + positions.indexOf(positions.get(i)) + "! Gradients' vertices must have different positions."
            );}
        }
    }
}
