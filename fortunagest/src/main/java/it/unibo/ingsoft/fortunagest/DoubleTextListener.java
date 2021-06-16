package it.unibo.ingsoft.fortunagest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class DoubleTextListener implements ChangeListener<String> {
    private final TextField campo;

    public DoubleTextListener(TextField campo) {
        this.campo = campo;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("-?\\d*\\.\\d*")) {
            String[] parts = newValue.split("\\.");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].replaceAll("[^\\d]", "");
            }
            StringBuilder sb = new StringBuilder();
            if (newValue.startsWith("-"))
                sb.append("-");
            sb.append(parts[0]);
            if (parts.length > 1) {
                sb.append(".");
                for (int i = 1; i < parts.length; i++) {
                    sb.append(parts[i]);
                }
            }
            campo.setText(sb.toString());
        }
    }
}
