package it.unibo.ingsoft.fortunagest.prodotti;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.DoubleTextListener;
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

        prezzo.textProperty().addListener(new DoubleTextListener(prezzo));
    }

    private boolean checkData() {
        return !numero.getText().isBlank() && !nome.getText().isBlank() && !prezzo.getText().isBlank() 
            && Double.parseDouble(prezzo.getText()) >= 0;
    }

    public void aggiungiProdotto(ActionEvent event) throws IOException {
        if (!checkData()) return;

        DatiProdotto datiProdotto = new DatiProdotto();
        datiProdotto.setNumero(Integer.parseInt(numero.getText()));
        datiProdotto.setNome(nome.getText());
        datiProdotto.setPrezzo(Double.parseDouble(prezzo.getText()));
        datiProdotto.setDesc(descrizione.getText());

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest/prodotti/";
        
        template.put(url, datiProdotto);

        switchToGestioneProdotti(event);
    }
}
