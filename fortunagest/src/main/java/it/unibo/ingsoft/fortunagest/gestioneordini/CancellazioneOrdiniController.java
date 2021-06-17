package it.unibo.ingsoft.fortunagest.gestioneordini;

import java.io.IOException;
import java.time.LocalDateTime;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CancellazioneOrdiniController extends StageController {

    @FXML
    private TableView<DatiOrdine> tableview;

    @FXML
    private TableColumn<DatiOrdine, String> colNominativo;
    @FXML
    private TableColumn<DatiOrdine, LocalDateTime> colDataOra;
    @FXML
    private TableColumn<DatiOrdine, String> colTelefono;
    @FXML
    private TableColumn<DatiOrdine, String> colIndirizzo;
    @FXML
    private TableColumn<DatiOrdine, Integer> colId;

    @FXML
    private TextField textfield;

    @FXML
    private Button btnCancella;

    private RestTemplate template;

    public void initialize() {
        template = new RestTemplate();

        String UrlAccettate = "http://localhost:8080/gest/ordini/accettati";
        ResponseEntity<DatiOrdine[]> response = template.exchange(UrlAccettate, HttpMethod.GET,
                new HttpEntity<DatiOrdine[]>(AuthSingleton.getInstance().getAuthHeaders()), DatiOrdine[].class);

        DatiOrdine[] prenotazioniAccettate = response.getBody();

        colNominativo.setCellValueFactory(new PropertyValueFactory<DatiOrdine, String>("nominativo"));
        colDataOra.setCellValueFactory(new PropertyValueFactory<DatiOrdine, LocalDateTime>("dataOra"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<DatiOrdine, String>("telefono"));
        colIndirizzo.setCellValueFactory(new PropertyValueFactory<DatiOrdine, String>("indirizzo"));
        colId.setCellValueFactory(new PropertyValueFactory<DatiOrdine, Integer>("idRichiesta"));

        tableview.getItems().setAll(prenotazioniAccettate);

    }

    public void rifiutaPrenotazione(ActionEvent event) throws IOException {
        String resourceUrl = "http://localhost:8080/gest/ordini" + '/' + textfield.getText();

        template.exchange(resourceUrl, HttpMethod.DELETE,
                new HttpEntity<Void>(AuthSingleton.getInstance().getAuthHeaders()), Void.class);

    }

}
