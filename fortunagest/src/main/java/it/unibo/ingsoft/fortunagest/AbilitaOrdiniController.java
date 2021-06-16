package it.unibo.ingsoft.fortunagest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AbilitaOrdiniController extends StageController {
    @FXML
    private ListView<PeriodoDisabilitazioneDati> disabilitazionePrenotazioneList;
    private ObservableList<PeriodoDisabilitazioneDati> observableDisabilitazioneList = FXCollections
            .observableArrayList();
    private String UrlDisabilitazioni = "http://localhost:8080/disa-ordini";
    private RestTemplate template = new RestTemplate();
    @FXML
    private TextField idCancellazione;
    @FXML
    private DatePicker dataInizio;
    @FXML
    private DatePicker dataFine;
    @FXML
    private TextField oraInizio;
    @FXML
    private TextField oraFine;
    @FXML
    private TextField minutiInizio;
    @FXML
    private TextField minutiFine;
    @FXML
    private ComboBox<String> tipoOrdinazione;

    public void initialize() {
        updateList();
        tipoOrdinazione.getItems().setAll("ORDINAZ_ASPORTO", "ORDINAZ_DOMICILIO", "ORDINAZ_TAVOLO");
        tipoOrdinazione.getSelectionModel().selectFirst();
    }

    private void updateList() {
        ResponseEntity<PeriodoDisabilitazioneDati[]> responseDisabilitazioni = template.getForEntity(UrlDisabilitazioni,
                PeriodoDisabilitazioneDati[].class);

        PeriodoDisabilitazioneDati[] disabilitazioni = responseDisabilitazioni.getBody();
        observableDisabilitazioneList.setAll(disabilitazioni);
        disabilitazionePrenotazioneList.setItems(observableDisabilitazioneList);
        disabilitazionePrenotazioneList.refresh();

    }

    public void riabilitaPeriodo(ActionEvent event) throws IOException {
        String resourceUrl = UrlDisabilitazioni + '/' + idCancellazione.getText();
        template.delete(resourceUrl);
        updateList();
    }

    public void disabilitaPeriodo(ActionEvent event) throws IOException {
        PeriodoDisabilitazioneDati periodo = new PeriodoDisabilitazioneDati();

        LocalDateTime inizio = LocalDateTime.of(dataInizio.getValue(),
                LocalTime.of(Integer.parseInt(oraInizio.getText()), Integer.parseInt(minutiInizio.getText())));

        LocalDateTime fine = LocalDateTime.of(dataFine.getValue(),
                LocalTime.of(Integer.parseInt(oraFine.getText()), Integer.parseInt(minutiFine.getText())));

        periodo.setInizio(inizio);
        periodo.setFine(fine);
        periodo.setTipo(tipoOrdinazione.getValue());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<String>(periodo.toString(), headers);

        template.postForEntity(UrlDisabilitazioni, request, Void.class);
        updateList();

    }
}
