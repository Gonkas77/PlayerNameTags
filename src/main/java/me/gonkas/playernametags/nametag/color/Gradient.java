package me.gonkas.playernametags.nametag.color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Gradient {

    /*
    'vertices' -> colors that have an index and a "strength" associated with them
        this color will have its position in the gradient decided by the point
        the gradient with the smallest point is the starting color
        the gradient with the biggest point is the ending color
     */

    List<Vertex> gradient = new ArrayList<>();

    Gradient(Vertex v1, Vertex v2, Vertex... vertices) {
        this.gradient.add(v1);
        this.gradient.add(v2);
        this.gradient.addAll(List.of(vertices));

        gradient.sort(Comparator.comparingInt(v -> v.index));
    }

    Gradient(List<Vertex> vertices) {
        this.gradient = vertices;
    }

    public List<Color> apply(int length) {
        return List.of();  // TODO
    }

    public List<Color> apply(String text) {return apply(text.length());}

    record Vertex(Color color, float strength, int index) {

    }
}
