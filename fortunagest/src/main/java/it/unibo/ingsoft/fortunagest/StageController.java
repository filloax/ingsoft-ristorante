package it.unibo.ingsoft.fortunagest;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class StageController {
    private Stage stage;
    private Parent root;
    private Scene scene;

    public void switchToHomeTitolare(ActionEvent event) throws IOException{
        switchToResource(event, "/HomeTitolare.fxml");
    }

    public void switchToAbilitaPrenotazioni(ActionEvent event) throws IOException{
        switchToResource(event, "/AbilitaPrenotazioni.fxml");
    }
   

    public void switchToGestionePrenotazioni(ActionEvent event) throws IOException{
        switchToResource(event, "/GestionePrenotazioni.fxml");
    }

    public void switchToRicezionePrenotazioni(ActionEvent event) throws IOException{
        switchToResource(event, "/RicezionePrenotazioni.fxml");
    }
    public void switchToCancellazionePrenotazioni(ActionEvent event) throws IOException{
        switchToResource(event, "/CancellazionePrenotazioni.fxml");
    }


    public void switchToGestioneSconti(ActionEvent event) throws IOException{
        switchToResource(event, "/GestioneSconti.fxml");
    }

    public void switchToAggiuntaSconti(ActionEvent event) throws IOException {
        switchToResource(event, "/AggiuntaSconto.fxml");
    }


    public void switchToGestioneProdotti(ActionEvent event) throws IOException{
        switchToResource(event, "/GestioneProdotti.fxml");
    }

    public void switchToAggiuntaProdotti(ActionEvent event) throws IOException {
        switchToResource(event, "/AggiuntaProdotto.fxml");
    }


    public void switchToGestioneZoneConsegna(ActionEvent event) throws IOException{
        switchToResource(event, "/GestioneZoneConsegna.fxml");
    }

    public void switchToAggiuntaZoneConsegna(ActionEvent event) throws IOException {
        switchToResource(event, "/AggiuntaZonaConsegna.fxml");
    }


    private void switchToResource(ActionEvent event, String path) throws IOException {
        // il path relativo di default per getResource a quanto pare è  src/resources
        // quindi mettendo /nomefile.fxml mi va a cercare il file nella cartella resources
        root = FXMLLoader.load(getClass().getResource(path));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        Styler.style(scene);
        
        stage.setScene(scene);
        stage.show();
    }
}
