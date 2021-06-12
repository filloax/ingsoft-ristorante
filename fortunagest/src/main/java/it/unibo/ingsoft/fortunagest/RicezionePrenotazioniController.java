package it.unibo.ingsoft.fortunagest;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class RicezionePrenotazioniController extends StageController{
    
    private PrenotazioneDati[] prenotazioniInAttesa;

    @FXML
    private TextArea textarea;
    private int index=0;

    public void initialize(){

        RestTemplate template = new RestTemplate();
        String UrlInAttesa = "http://localhost:8080/gest-prenotazioni/attesa";
        
        ResponseEntity<PrenotazioneDati[]> response =
        template.getForEntity(UrlInAttesa, PrenotazioneDati[].class);
        PrenotazioneDati[] prenotazioniInAttesa = response.getBody();

        if(index <= prenotazioniInAttesa.length-1)
            textarea.setText(prenotazioniInAttesa[index].toString());



    }
    


    public void accettaPrenotazione(ActionEvent event) throws IOException{
       
    }

    public void rifiutaPrenotazione(ActionEvent event) throws IOException{
       
    }
}
