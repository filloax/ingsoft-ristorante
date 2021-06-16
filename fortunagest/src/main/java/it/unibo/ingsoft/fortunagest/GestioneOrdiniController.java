package it.unibo.ingsoft.fortunagest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GestioneOrdiniController extends StageController {
    @FXML
    private ListView<OrdineDati> ordiniInAttesaList;
    private ObservableList<OrdineDati> observableInAttesaList = FXCollections.observableArrayList();

    @FXML
    private ListView<OrdineDati> ordiniAccettatiList;
    private ObservableList<OrdineDati> observableAccettatiList = FXCollections.observableArrayList();

    @FXML
    private ListView<PeriodoDisabilitazioneDati> disabilitazioniOrdiniList;
    private ObservableList<PeriodoDisabilitazioneDati> observableDisabilitazioneList = FXCollections
            .observableArrayList();

    public void initialize() {

        RestTemplate template = new RestTemplate();
        String UrlInAttesa = "http://localhost:8080/gest/ordini/attesa";

        ResponseEntity<OrdineDati[]> response = template.exchange(UrlInAttesa, HttpMethod.GET,
                new HttpEntity<OrdineDati[]>(AuthSingleton.getInstance().getAuthHeaders()), OrdineDati[].class);

        OrdineDati[] ordiniInAttesa = response.getBody();
        observableInAttesaList.addAll(ordiniInAttesa);
        ordiniInAttesaList.setItems(observableInAttesaList);
        ordiniInAttesaList.refresh();

        String UrlAccettate = "http://localhost:8080/gest/ordini/accettati";
        response = template.exchange(UrlAccettate, HttpMethod.GET,
                new HttpEntity<OrdineDati[]>(AuthSingleton.getInstance().getAuthHeaders()), OrdineDati[].class);

        OrdineDati[] ordiniAccettati = response.getBody();
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
