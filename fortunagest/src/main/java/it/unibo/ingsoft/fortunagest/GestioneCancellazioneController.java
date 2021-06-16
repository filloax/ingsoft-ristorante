package it.unibo.ingsoft.fortunagest;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestioneCancellazioneController extends StageController {

    @FXML
    private TableView<PrenotazioneDati> tableview;

    @FXML
    private TableColumn<PrenotazioneDati, String> colNominativo;
    @FXML
    private TableColumn<PrenotazioneDati, LocalDateTime> colDataOra;
    @FXML
    private TableColumn<PrenotazioneDati, String> colTelefono;
    @FXML
    private TableColumn<PrenotazioneDati, Integer> colPersone;
    @FXML
    private TableColumn<PrenotazioneDati, Integer> colId;

    @FXML
    private TextField textfield;

    @FXML
    private Button btnCancella;

    private RestTemplate template;

    public void initialize() {
        template = new RestTemplate();


        String UrlAccettate = "http://localhost:8080/gest/prenotazioni/accettati";
        ResponseEntity<PrenotazioneDati[]> response =
                template.getForEntity(UrlAccettate, PrenotazioneDati[].class);
        PrenotazioneDati[] prenotazioniAccettate = response.getBody();

        colNominativo.setCellValueFactory(
                new PropertyValueFactory<PrenotazioneDati, String>("nominativo"));
        colDataOra.setCellValueFactory(
                new PropertyValueFactory<PrenotazioneDati, LocalDateTime>("dataOra"));
        colTelefono.setCellValueFactory(
                new PropertyValueFactory<PrenotazioneDati, String>("telefono"));
        colPersone.setCellValueFactory(
                new PropertyValueFactory<PrenotazioneDati, Integer>("numeroPersone"));
        colId.setCellValueFactory(
                new PropertyValueFactory<PrenotazioneDati, Integer>("idRichiesta"));

        tableview.getItems().setAll(prenotazioniAccettate);



    }

    public void rifiutaPrenotazione(ActionEvent event) throws IOException {
        String resourceUrl = "http://localhost:8080/gest/prenotazioni" + '/' + textfield.getText();

        template.delete(resourceUrl);
        // template.exchange(resourceUrl, HttpMethod.DELETE, null, Void.class);

    }
}
