package it.unibo.ingsoft.fortunagest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


@Component
public class StageListener implements ApplicationListener<StageReadyEvent>{

    private final String applicationTitle;

    public StageListener(@Value("spring.application.ui.title")String applicationTitle){
        this.applicationTitle = applicationTitle;
    } 
    
    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        // TODO Auto-generated method stub
     try {
        Stage stage = event.getStage();
        stage.setTitle(this.applicationTitle);
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
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
