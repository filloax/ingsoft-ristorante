package it.unibo.ingsoft.fortunagest.prodotti;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.model.DatiProdotto;
import it.unibo.ingsoft.fortunagest.model.DatiSconto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AggiungiProdottoController extends StageController {
    @FXML
    private TextField numero;
    @FXML
    private TextField nome;
    @FXML
    private TextArea descrizione;
    @FXML
    private TextField prezzo;

    public void initialize() {
        numero.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numero.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Function<TextField, ChangeListener<String>> doubleListener = (campo) -> new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*\\.\\d*")) {
                    String[] parts = newValue.split("\\.");
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].replaceAll("[^\\d]", "");
                    }
                    StringBuilder sb = new StringBuilder();
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
        };

        prezzo.textProperty().addListener(doubleListener.apply(prezzo));
    }

    private boolean checkData() {
        return !numero.getText().isBlank() && !nome.getText().isBlank() && !prezzo.getText().isBlank();
    }

    public void aggiungiProdotto(ActionEvent event) throws IOException {
        if (!checkData()) return;

        DatiProdotto datiProdotto = new DatiProdotto();
        datiProdotto.setNumero(Integer.parseInt(numero.getText()));
        datiProdotto.setNome(nome.getText());
        datiProdotto.setPrezzo(Double.parseDouble(prezzo.getText()));
        datiProdotto.setDesc(descrizione.getText());

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest-prodotti/";
        
        template.put(url, datiProdotto);

        switchToGestioneProdotti(event);
    }
}
