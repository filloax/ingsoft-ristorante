package it.unibo.ingsoft.fortunagest.gestioneordini;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import it.unibo.ingsoft.fortunagest.model.DatiOrdine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class RicezioneOrdiniController extends StageController {

    private static String rootUrl = "http://localhost:8080/gest/ordini";

    private DatiOrdine[] ordiniInAttesa;

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
        String UrlInAttesa = rootUrl + "/attesa";

        ResponseEntity<DatiOrdine[]> response = template.exchange(UrlInAttesa, HttpMethod.GET,
                new HttpEntity<DatiOrdine[]>(AuthSingleton.getInstance().getAuthHeaders()), DatiOrdine[].class);

        ordiniInAttesa = response.getBody();
        this.mostraProssimoOrdine();

    }

    private void mostraProssimoOrdine() {

        if (index <= ordiniInAttesa.length - 1)
            textarea.setText(ordiniInAttesa[index].toString());
        else {
            textarea.setText("Non ci sono più altri Ordini in Attesa");
            btnAccetta.setDisable(true);
            btnRifiuta.setDisable(true);
        }
    }

    public void accettaOrdine(ActionEvent event) throws IOException {

        String resourceUrl = rootUrl + '/' + ordiniInAttesa[index].getIdRichiesta();
        template.exchange(resourceUrl, HttpMethod.PUT,
                new HttpEntity<Void>(AuthSingleton.getInstance().getAuthHeaders()), Void.class);

        index++;

        this.mostraProssimoOrdine();

    }

    public void rifiutaOrdine(ActionEvent event) throws IOException {
        String resourceUrl = rootUrl + '/' + ordiniInAttesa[index].getIdRichiesta();

        template.exchange(resourceUrl, HttpMethod.DELETE,
                new HttpEntity<Void>(AuthSingleton.getInstance().getAuthHeaders()), Void.class);

        index++;

        this.mostraProssimoOrdine();

    }

}
