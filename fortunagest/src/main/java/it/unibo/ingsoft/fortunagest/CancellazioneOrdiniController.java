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

public class CancellazioneOrdiniController extends StageController {

    @FXML
    private TableView<OrdineDati> tableview;

    @FXML
    private TableColumn<OrdineDati, String> colNominativo;
    @FXML
    private TableColumn<OrdineDati, LocalDateTime> colDataOra;
    @FXML
    private TableColumn<OrdineDati, String> colTelefono;
    @FXML
    private TableColumn<OrdineDati, String> colIndirizzo;
    @FXML
    private TableColumn<OrdineDati, Integer> colId;

    @FXML
    private TextField textfield;

    @FXML
    private Button btnCancella;

    private RestTemplate template;

    public void initialize() {
        template = new RestTemplate();

        String UrlAccettate = "http://localhost:8080/gest-ordini/accettati";
        ResponseEntity<OrdineDati[]> response = template.getForEntity(UrlAccettate, OrdineDati[].class);
        OrdineDati[] prenotazioniAccettate = response.getBody();

        colNominativo.setCellValueFactory(new PropertyValueFactory<OrdineDati, String>("nominativo"));
        colDataOra.setCellValueFactory(new PropertyValueFactory<OrdineDati, LocalDateTime>("dataOra"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<OrdineDati, String>("telefono"));
        colIndirizzo.setCellValueFactory(new PropertyValueFactory<OrdineDati, String>("indirizzo"));
        colId.setCellValueFactory(new PropertyValueFactory<OrdineDati, Integer>("idRichiesta"));

        tableview.getItems().setAll(prenotazioniAccettate);

    }

    public void rifiutaPrenotazione(ActionEvent event) throws IOException {
        String resourceUrl = "http://localhost:8080/gest-ordini" + '/' + textfield.getText();

        template.delete(resourceUrl);
        // template.exchange(resourceUrl, HttpMethod.DELETE, null, Void.class);

    }

}
