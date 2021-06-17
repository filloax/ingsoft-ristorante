package it.unibo.ingsoft.fortunagest.gestioneordini;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.PeriodoDisabilitazioneDati;
import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import it.unibo.ingsoft.fortunagest.model.DatiOrdine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GestioneOrdiniController extends StageController {
    @FXML
    private ListView<DatiOrdine> ordiniInAttesaList;
    private ObservableList<DatiOrdine> observableInAttesaList = FXCollections.observableArrayList();

    @FXML
    private ListView<DatiOrdine> ordiniAccettatiList;
    private ObservableList<DatiOrdine> observableAccettatiList = FXCollections.observableArrayList();

    @FXML
    private ListView<PeriodoDisabilitazioneDati> disabilitazioniOrdiniList;
    private ObservableList<PeriodoDisabilitazioneDati> observableDisabilitazioneList = FXCollections
            .observableArrayList();

    public void initialize() {

        RestTemplate template = new RestTemplate();
        String UrlInAttesa = "http://localhost:8080/gest/ordini/attesa";

        ResponseEntity<DatiOrdine[]> response = template.exchange(UrlInAttesa, HttpMethod.GET,
                new HttpEntity<DatiOrdine[]>(AuthSingleton.getInstance().getAuthHeaders()), DatiOrdine[].class);

        DatiOrdine[] ordiniInAttesa = response.getBody();
        observableInAttesaList.addAll(ordiniInAttesa);
        ordiniInAttesaList.setItems(observableInAttesaList);
        ordiniInAttesaList.refresh();

        String UrlAccettate = "http://localhost:8080/gest/ordini/accettati";
        response = template.exchange(UrlAccettate, HttpMethod.GET,
                new HttpEntity<DatiOrdine[]>(AuthSingleton.getInstance().getAuthHeaders()), DatiOrdine[].class);

        DatiOrdine[] ordiniAccettati = response.getBody();
        observableAccettatiList.addAll(ordiniAccettati);
        ordiniAccettatiList.setItems(observableAccettatiList);

        String UrlDisabilitazioni = "http://localhost:8080/disa-ordini";

        ResponseEntity<PeriodoDisabilitazioneDati[]> responseDisabilitazioni = template.getForEntity(UrlDisabilitazioni,
                PeriodoDisabilitazioneDati[].class);

        PeriodoDisabilitazioneDati[] disabilitazioni = responseDisabilitazioni.getBody();
        observableDisabilitazioneList.addAll(disabilitazioni);
        disabilitazioniOrdiniList.setItems(observableDisabilitazioneList);
    }
}
