package it.unibo.ingsoft.fortunagest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GestionePrenotazioniController extends StageController {
    @FXML
    private ListView<PrenotazioneDati> prenotazioniInAttesaList;
    private ObservableList<PrenotazioneDati> observableInAttesaList = FXCollections.observableArrayList();

    @FXML
    private ListView<PrenotazioneDati> prenotazioniAccettateList;
    private ObservableList<PrenotazioneDati> observableAccettateList = FXCollections.observableArrayList();

    @FXML
    private ListView<PeriodoDisabilitazioneDati> disabilitazioniPrenotazioniList;
    private ObservableList<PeriodoDisabilitazioneDati> observableDisabilitazioneList = FXCollections
            .observableArrayList();

    public void initialize() {

        RestTemplate template = new RestTemplate();
        String UrlInAttesa = "http://localhost:8080/gest-prenotazioni/attesa";

        ResponseEntity<PrenotazioneDati[]> response = template.getForEntity(UrlInAttesa, PrenotazioneDati[].class);
        PrenotazioneDati[] prenotazioniInAttesa = response.getBody();
        observableInAttesaList.addAll(prenotazioniInAttesa);
        prenotazioniInAttesaList.setItems(observableInAttesaList);

        String UrlAccettate = "http://localhost:8080/gest-prenotazioni/accettati";
        response = template.getForEntity(UrlAccettate, PrenotazioneDati[].class);
        PrenotazioneDati[] prenotazioniAccettate = response.getBody();
        observableAccettateList.addAll(prenotazioniAccettate);
        prenotazioniAccettateList.setItems(observableAccettateList);

        String UrlDisabilitazioni = "http://localhost:8080/disa-prenotazioni";

        ResponseEntity<PeriodoDisabilitazioneDati[]> responseDisabilitazioni = template.getForEntity(UrlDisabilitazioni,
                PeriodoDisabilitazioneDati[].class);

        PeriodoDisabilitazioneDati[] disabilitazioni = responseDisabilitazioni.getBody();
        observableDisabilitazioneList.addAll(disabilitazioni);
        disabilitazioniPrenotazioniList.setItems(observableDisabilitazioneList);
    }

}
