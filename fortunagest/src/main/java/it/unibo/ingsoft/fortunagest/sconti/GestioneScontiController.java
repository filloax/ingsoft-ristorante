package it.unibo.ingsoft.fortunagest.sconti;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import it.unibo.ingsoft.fortunagest.model.DatiSconto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GestioneScontiController extends StageController {
    @FXML
    private ListView<DatiSconto> listaSconti;
    private ObservableList<DatiSconto> observableListaSconti = FXCollections.observableArrayList();
    

    public void initialize() {
        listaSconti.setItems(observableListaSconti);
        updateList();
    }

    public void updateList() {
        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest/sconti/";
        
        ResponseEntity<DatiSconto[]> response = template.exchange(url, 
            HttpMethod.GET, 
            new HttpEntity<Object>(AuthSingleton.getInstance().getAuthHeaders()), 
            DatiSconto[].class);
        DatiSconto[] arraySconti = response.getBody();

        observableListaSconti.setAll(arraySconti);
        listaSconti.refresh();
    }

    public void cancellaSconto(ActionEvent event) throws IOException {
        int id = listaSconti.getSelectionModel().getSelectedIndex();
        int scontoId = listaSconti.getItems().get(id).getId();

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest/sconti/" + scontoId;
        template.exchange(url, 
            HttpMethod.DELETE, 
            new HttpEntity<Object>(AuthSingleton.getInstance().getAuthHeaders()), 
            Object.class);

        updateList();
    }
}
