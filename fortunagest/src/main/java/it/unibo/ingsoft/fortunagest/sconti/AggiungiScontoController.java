package it.unibo.ingsoft.fortunagest.sconti;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.DoubleTextListener;
import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.model.DatiSconto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AggiungiScontoController extends StageController {
    @FXML
    private DatePicker startDate;
    @FXML
    private TextField startTime;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField endTime;
    @FXML
    private TextField numeroProdotto;
    @FXML
    private TextField spesaMinima;
    @FXML
    private TextField quantita;
    @FXML
    private CheckBox isPercent;

    public void initialize() {
        // force the field to be numeric only
        numeroProdotto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    numeroProdotto.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        quantita.textProperty().addListener(new DoubleTextListener(quantita));
        spesaMinima.textProperty().addListener(new DoubleTextListener(spesaMinima));

        Function<TextField, ChangeListener<String>> timeListener = (campo) -> new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*:\\d*")) {
                    String[] parts = newValue.split(":");
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].replaceAll("[^\\d]", "");
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(parts[0]);
                    if (parts.length > 1) {
                        sb.append(":");
                        for (int i = 1; i < parts.length; i++) {
                            sb.append(parts[i]);
                        }
                    }
                    campo.setText(sb.toString());
                }
            }
        };

        startTime.textProperty().addListener(timeListener.apply(startTime));
        endTime.textProperty().addListener(timeListener.apply(endTime));
    }

    private boolean checkData() {
        return startTime.getText().matches("\\d\\d:\\d\\d") && endTime.getText().matches("\\d\\d:\\d\\d")
                && endDate.getValue() != null && startDate.getValue() != null
                && endDate.getValue().isAfter(startDate.getValue()) && !quantita.getText().isBlank();
    }

    public void aggiungiSconto(ActionEvent event) throws IOException {
        if (!checkData())
            return;

        LocalTime startTimeValue = LocalTime.parse(startTime.getText(), DateTimeFormatter.ISO_TIME);
        LocalTime endTimeValue = LocalTime.parse(endTime.getText(), DateTimeFormatter.ISO_TIME);

        DatiSconto datiSconto = new DatiSconto();
        datiSconto.setStart(LocalDateTime.of(startDate.getValue(), startTimeValue));
        datiSconto.setEnd(LocalDateTime.of(endDate.getValue(), endTimeValue));
        datiSconto.setQuantita(Double.parseDouble(quantita.getText()));
        datiSconto.setPercent(isPercent.isSelected());
        if (!spesaMinima.getText().isBlank())
            datiSconto.setPrezzoMin(Double.parseDouble(spesaMinima.getText()));
        if (!numeroProdotto.getText().isBlank())
            datiSconto.setPrezzoMin(Integer.parseInt(numeroProdotto.getText()));

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest-sconti/";

        template.put(url, datiSconto);

        switchToGestioneSconti(event);
    }
}
