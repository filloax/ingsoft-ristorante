package it.unibo.ingsoft.fortunagest;

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private final String applicationTitle;

    public StageListener(@Value("spring.application.ui.title") String applicationTitle) {
        this.applicationTitle = applicationTitle;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            // File newfile = new File("." + File.separator + "newFile.txt");
            // newfile.createNewFile();
            Stage window = event.getStage();
            window.setTitle(this.applicationTitle);
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

            Scene scene1 = new Scene(root);
            Styler.style(scene1);

            window.setScene(scene1);
            window.show();

            // Button btn = new Button();
            // btn.setText("Say 'Hello World'");
            // btn.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {

            //     @Override
            //     public void handle(ActionEvent event) {
            //         System.out.println("Hello World!");
            //         RestTemplate template = new RestTemplate();
            //         String fooResourceUrl = "http://localhost:8080/gest/prenotazioni";
            //         ResponseEntity<String> response = template.getForEntity(fooResourceUrl+"/1", String.class);
            //         System.out.println(response.getStatusCode());
            //         System.out.println("JSON");
            //         System.out.println(response.getBody());
            //         try {
            //             ObjectMapper mapper = new ObjectMapper();
            //             JsonNode root;
            //             root = mapper.readTree(response.getBody());
            //             JsonNode nominativo = root.path("nominativo");

            //         } catch (JsonMappingException e) {
            //             // TODO Auto-generated catch block
            //             e.printStackTrace();
            //         } catch (JsonProcessingException e) {
            //             // TODO Auto-generated catch block
            //             e.printStackTrace();
            //         }
            //     }
            // });

            // StackPane root = new StackPane();
            // root.getChildren().add(btn);
            // window.setScene(new Scene(root, 300, 250));
            // window.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
