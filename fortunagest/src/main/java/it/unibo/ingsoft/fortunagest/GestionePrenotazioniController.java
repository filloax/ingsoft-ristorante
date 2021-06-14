package it.unibo.ingsoft.fortunagest;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GestionePrenotazioniController extends StageController {
    @FXML
    private ListView<PrenotazioneDati> prenotazioniInAttesaList;
    private ObservableList<PrenotazioneDati> observableInAttesaList = FXCollections.observableArrayList();
    
    @FXML
    private ListView<PrenotazioneDati> prenotazioniAccettateList;
    private ObservableList<PrenotazioneDati> observableAccettateList = FXCollections.observableArrayList();


    public void initialize() {

        RestTemplate template = new RestTemplate();
        String UrlInAttesa = "http://localhost:8080/gest-prenotazioni/attesa";
        
        ResponseEntity<PrenotazioneDati[]> response =
        template.getForEntity(UrlInAttesa, PrenotazioneDati[].class);
        PrenotazioneDati[] prenotazioniInAttesa = response.getBody();
        observableInAttesaList.addAll(prenotazioniInAttesa);
        prenotazioniInAttesaList.setItems(observableInAttesaList);
        

        
        String UrlAccettate = "http://localhost:8080/gest-prenotazioni/accettati";
        response =
                template.getForEntity(UrlAccettate, PrenotazioneDati[].class);
        PrenotazioneDati[] prenotazioniAccettate = response.getBody();
        observableAccettateList.addAll(prenotazioniAccettate);
        prenotazioniAccettateList.setItems(observableAccettateList);

    }

   
}
