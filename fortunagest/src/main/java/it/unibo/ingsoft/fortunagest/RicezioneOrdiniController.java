package it.unibo.ingsoft.fortunagest;

import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class RicezioneOrdiniController extends StageController {

    private static String rootUrl = "http://localhost:8080/gest-ordini";

    private OrdineDati[] ordiniInAttesa;

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

        ResponseEntity<OrdineDati[]> response = template.getForEntity(UrlInAttesa, OrdineDati[].class);
        ordiniInAttesa = response.getBody();

        this.mostraProssimaPrenotazione();

    }

    private void mostraProssimaPrenotazione() {

        if (index <= ordiniInAttesa.length - 1)
            textarea.setText(ordiniInAttesa[index].toString());
        else {
            textarea.setText("Non ci sono più altre Prenotazioni in Attesa");
            btnAccetta.setDisable(true);
            btnRifiuta.setDisable(true);
        }
    }

    public void accettaPrenotazione(ActionEvent event) throws IOException {

        String resourceUrl = rootUrl + '/' + ordiniInAttesa[index].getIdRichiesta();
        template.exchange(resourceUrl, HttpMethod.PUT, null, Void.class);

        index++;

        this.mostraProssimaPrenotazione();

    }

    public void rifiutaPrenotazione(ActionEvent event) throws IOException {
        String resourceUrl = rootUrl + '/' + ordiniInAttesa[index].getIdRichiesta();

        template.delete(resourceUrl);
        // template.exchange(resourceUrl, HttpMethod.DELETE, null, Void.class);

        index++;

        this.mostraProssimaPrenotazione();

    }

}
