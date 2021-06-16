package it.unibo.ingsoft.fortunagest;

import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Styler {
    private static JMetro jMetro;

    public static void style(Scene scene) {
        if (jMetro == null)
            jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);
    }
}
