package it.unibo.ingsoft.fortunagest.gestioneprenotazioni;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import it.unibo.ingsoft.fortunagest.model.PrenotazioneDati;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class RicezionePrenotazioniController extends StageController {

    private static String rootUrl = "http://localhost:8080/gest/prenotazioni";

    private PrenotazioneDati[] prenotazioniInAttesa;

    private RestTemplate template;

    @FXML
    private TextArea textarea;
    private int index = 0;

    @FXML
    private Button btnAccetta;
    @FXML
    private Button btnRifiuta;
    

    public void initialize() {

        template = new RestTemplate();
        String url = rootUrl + "/attesa";

        ResponseEntity<PrenotazioneDati[]> response = template.exchange(url, 
            HttpMethod.GET, 
            new HttpEntity<PrenotazioneDati[]>(AuthSingleton.getInstance().getAuthHeaders()), 
            PrenotazioneDati[].class);
        prenotazioniInAttesa = response.getBody();

        this.mostraProssimaPrenotazione();


    }


    private void mostraProssimaPrenotazione() {

        if (index <= prenotazioniInAttesa.length - 1)
            textarea.setText(prenotazioniInAttesa[index].toString());
        else{
            textarea.setText("Non ci sono più altre Prenotazioni in Attesa");
            btnAccetta.setDisable(true);
            btnRifiuta.setDisable(true);
        }
    }

    public void accettaPrenotazione(ActionEvent event) throws IOException {

        String resourceUrl = rootUrl + '/' + prenotazioniInAttesa[index].getIdRichiesta();
        template.exchange(resourceUrl, HttpMethod.PUT, new HttpEntity<Void>(AuthSingleton.getInstance().getAuthHeaders()), Void.class);

        index++;

        this.mostraProssimaPrenotazione();

    }

    public void rifiutaPrenotazione(ActionEvent event) throws IOException {
        String resourceUrl = rootUrl + '/' + prenotazioniInAttesa[index].getIdRichiesta();

        template.exchange(resourceUrl, HttpMethod.DELETE, new HttpEntity<Void>(AuthSingleton.getInstance().getAuthHeaders()), Void.class);

        index++;

        this.mostraProssimaPrenotazione();


    }
}
