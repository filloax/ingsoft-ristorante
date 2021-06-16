package it.unibo.ingsoft.fortunagest.zonaconsegna;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import it.unibo.ingsoft.fortunagest.model.DatiZonaConsegnaPunti;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GestioneZoneConsegnaController extends StageController {
    @FXML
    private ListView<DatiZonaConsegnaPunti> listaZone;
    private ObservableList<DatiZonaConsegnaPunti> observableListaZone = FXCollections.observableArrayList();
    

    public void initialize() {
        listaZone.setItems(observableListaZone);
        updateList();
    }

    public void updateList() {
        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest/zone/";
        
        ResponseEntity<DatiZonaConsegnaPunti[]> response = template.exchange(url, 
            HttpMethod.GET, 
            new HttpEntity<DatiZonaConsegnaPunti[]>(AuthSingleton.getInstance().getAuthHeaders()), 
            DatiZonaConsegnaPunti[].class);
        DatiZonaConsegnaPunti[] arrayZone = response.getBody();

        observableListaZone.setAll(arrayZone);
        listaZone.refresh();
    }

    public void cancellaZona(ActionEvent event) throws IOException {
        int id = listaZone.getSelectionModel().getSelectedIndex();
        int zonaId = listaZone.getItems().get(id).getId();

        RestTemplate template = new RestTemplate();
        
        String url = "http://localhost:8080/gest/zone/" + zonaId;
        template.exchange(url, 
            HttpMethod.DELETE, 
            new HttpEntity<Object>(AuthSingleton.getInstance().getAuthHeaders()), 
            Object.class);

        updateList();
    }
}
