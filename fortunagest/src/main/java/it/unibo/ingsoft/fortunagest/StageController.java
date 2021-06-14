package it.unibo.ingsoft.fortunagest;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class StageController {
    private Stage stage;
    private Parent root;
    private Scene scene;

   
    public void switchToGestionePrenotazioni(ActionEvent event) throws IOException{
        // il path relativo di default per getResource a quanto pare è  src/resources
        // quindi mettendo /nomefile.fxml mi va a cercare il file nella cartella resources
        root = FXMLLoader.load(getClass().getResource("/GestionePrenotazioni.fxml"));
        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeTitolare(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/HomeTitolare.fxml"));
        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRicezionePrenotazioni(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/RicezionePrenotazioni.fxml"));
        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        
        stage.setScene(scene);
        stage.show();
    }
    public void switchToCancellazionePrenotazioni(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/CancellazionePrenotazioni.fxml"));
        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        
        stage.setScene(scene);
        stage.show();
    }



    

}
