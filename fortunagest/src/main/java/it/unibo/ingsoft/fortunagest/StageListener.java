package it.unibo.ingsoft.fortunagest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private final String applicationTitle;

    public StageListener(@Value("spring.application.ui.title") String applicationTitle) {
        this.applicationTitle = applicationTitle;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            stage.setTitle(this.applicationTitle);
            Button btn = new Button();
            btn.setText("Say 'Hello World'");
            btn.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                    RestTemplate template = new RestTemplate();
                    String fooResourceUrl = "http://localhost:8080/gest-prenotazioni";
                    ResponseEntity<String> response = template.getForEntity(fooResourceUrl+"/1", String.class);
                    System.out.println(response.getStatusCode());
                    System.out.println("JSON");
                    System.out.println(response.getBody());
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root;
                        root = mapper.readTree(response.getBody());
                        JsonNode nominativo = root.path("nominativo");

                    } catch (JsonMappingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

            StackPane root = new StackPane();
            root.getChildren().add(btn);
            stage.setScene(new Scene(root, 300, 250));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
