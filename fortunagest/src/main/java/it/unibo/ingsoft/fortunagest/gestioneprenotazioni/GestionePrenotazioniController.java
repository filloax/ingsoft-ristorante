package it.unibo.ingsoft.fortunagest.gestioneprenotazioni;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.PeriodoDisabilitazioneDati;
import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import it.unibo.ingsoft.fortunagest.model.PrenotazioneDati;
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
        String UrlInAttesa = "http://localhost:8080/gest/prenotazioni/attesa";
        
        ResponseEntity<PrenotazioneDati[]> response =
        template.exchange(UrlInAttesa, HttpMethod.GET, new HttpEntity<PrenotazioneDati[]>(AuthSingleton.getInstance().getAuthHeaders()), PrenotazioneDati[].class);
        PrenotazioneDati[] prenotazioniInAttesa = response.getBody();
        observableInAttesaList.addAll(prenotazioniInAttesa);
        prenotazioniInAttesaList.setItems(observableInAttesaList);

        
        String UrlAccettate = "http://localhost:8080/gest/prenotazioni/accettati";
        response =
        template.exchange(UrlAccettate, HttpMethod.GET, new HttpEntity<PrenotazioneDati[]>(AuthSingleton.getInstance().getAuthHeaders()), PrenotazioneDati[].class);
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
