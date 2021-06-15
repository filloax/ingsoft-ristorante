package it.unibo.ingsoft.fortunagest.prodotti;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.model.DatiProdotto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GestioneProdottiController extends StageController {
    @FXML
    private ListView<DatiProdotto> listaProdotti;
    private ObservableList<DatiProdotto> observableListaProdotti = FXCollections.observableArrayList();
    

    public void initialize() {
        listaProdotti.setItems(observableListaProdotti);
        updateList();
    }

    public void updateList() {
        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest-prodotti/";
        
        ResponseEntity<DatiProdotto[]> response =
        template.getForEntity(url, DatiProdotto[].class);
        DatiProdotto[] arrayProdotti = response.getBody();

        observableListaProdotti.setAll(arrayProdotti);
        listaProdotti.refresh();
    }

    public void cancellaProdotto(ActionEvent event) throws IOException {
        int id = listaProdotti.getSelectionModel().getSelectedIndex();
        int numero = listaProdotti.getItems().get(id).getNumero();

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest-prodotti/" + numero;
        template.delete(url);

        updateList();
    }
}
