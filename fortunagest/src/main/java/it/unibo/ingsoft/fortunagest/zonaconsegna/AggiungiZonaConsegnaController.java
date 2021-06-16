package it.unibo.ingsoft.fortunagest.zonaconsegna;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import it.unibo.ingsoft.fortunagest.DoubleTextListener;
import it.unibo.ingsoft.fortunagest.StageController;
import it.unibo.ingsoft.fortunagest.auth.AuthSingleton;
import it.unibo.ingsoft.fortunagest.model.DatiZonaConsegnaPunti;
import it.unibo.ingsoft.fortunagest.model.Vector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AggiungiZonaConsegnaController extends StageController {
    @FXML
    private TableView<Vector> tabellaPunti;
    private ObservableList<Vector> observableListaZone = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Vector, String> colLat;
    @FXML
    private TableColumn<Vector, String> colLong;
    @FXML
    private TextField spesaMinima;
    @FXML
    private TextField fieldLat;
    @FXML
    private TextField fieldLong;


    public void initialize() {       
        colLat.setCellValueFactory(new PropertyValueFactory<Vector, String>("x"));
        colLong.setCellValueFactory(new PropertyValueFactory<Vector, String>("y"));
        tabellaPunti.setItems(observableListaZone);

        spesaMinima.textProperty().addListener(new DoubleTextListener(spesaMinima));
        fieldLat.textProperty().addListener(new DoubleTextListener(fieldLat));
        fieldLong.textProperty().addListener(new DoubleTextListener(fieldLong));
    }

    private boolean checkData() {
        return observableListaZone.size() >= 3;
    }

    public void aggiungiPunto(ActionEvent event) throws IOException {
        if (fieldLat.getText().isEmpty() || fieldLong.getText().isEmpty()) return;

        observableListaZone.add(new Vector(Double.parseDouble(fieldLat.getText()), Double.parseDouble(fieldLong.getText())));
    }

    public void rimuoviPunto(ActionEvent event) throws IOException {

    }

    public void aggiungiZonaConsegna(ActionEvent event) throws IOException {
        if (!checkData()) return;

        DatiZonaConsegnaPunti datiZonaConsegna = new DatiZonaConsegnaPunti();
        datiZonaConsegna.setPunti(observableListaZone.stream().collect(Collectors.toList()));
        datiZonaConsegna.setPrezzoMinimo(Double.parseDouble(spesaMinima.getText()));

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest/zone/";
        
        template.exchange(url, 
            HttpMethod.PUT, 
            new HttpEntity<DatiZonaConsegnaPunti>(datiZonaConsegna, AuthSingleton.getInstance().getAuthHeaders()), 
            DatiZonaConsegnaPunti.class);

        switchToGestioneZoneConsegna(event);
    }
}
