package it.unibo.ingsoft.fortunagest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class SpringLauncher{
    
	public static void main(String[] args) {
        //	SpringApplication.run(FortunagestApplication.class, args);
            Application.launch(JavaFxApplication.class,args);
    
    }
    
}
