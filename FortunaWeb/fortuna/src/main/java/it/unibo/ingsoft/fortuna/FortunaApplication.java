package it.unibo.ingsoft.fortuna;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

// NOTA: Per come funziona Spring, cerca classi con mapping di url (tipo /home, /ordina, ecc.)
// nello stesso package della classe con @SpringBootApplication
// se si vuol estendere la ricerca ad altri package, c'è una nota apposita
@SpringBootApplication
public class FortunaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FortunaApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	// 	return args -> {

	// 		System.out.println("Let's inspect the beans provided by Spring Boot:");

	// 		String[] beanNames = ctx.getBeanDefinitionNames();
	// 		Arrays.sort(beanNames);
	// 		for (String beanName : beanNames) {
	// 			System.out.println(beanName);
	// 		}

	// 	};
	// }
}
