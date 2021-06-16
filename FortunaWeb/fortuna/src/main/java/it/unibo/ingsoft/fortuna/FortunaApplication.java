package it.unibo.ingsoft.fortuna;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.maps.GeoApiContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;

import it.unibo.ingsoft.fortuna.log.ILogManager;

// NOTA: Per come funziona Spring, cerca classi con mapping di url (tipo /home, /ordina, ecc.)
// nello stesso package della classe con @SpringBootApplication
// se si vuol estendere la ricerca ad altri package, c'è una nota apposita
@SpringBootApplication
public class FortunaApplication {
	@Autowired
	Environment env;
	@Autowired
	ILogManager logManager;
	@Autowired
	ConfigProps cfg;

	public static void main(String[] args) {
		SpringApplication.run(FortunaApplication.class, args);

		initFolders();
	}

	private static void initFolders() {
		String rootPath = new FileSystemResource("").getFile().getAbsolutePath();
		Path config = Paths.get(rootPath, "config");

		try {
			if (!Files.isDirectory(config))
				Files.createDirectory(config);
		} catch(IOException e) {
			e.printStackTrace();
			System.err.println("Errore a creare le cartelle: " + e.getMessage());
			System.exit(-1);
		}
	}
	
	@Bean(destroyMethod = "shutdown")
	public GeoApiContext geoApiContext() {
		if (cfg.getKeys().getGoogleGeocoding() == null) {
			logManager.scriviMessaggio("API Key per Google Maps non impostata su variabile ambiente GEOCODING_KEY, servizio ZonaConsegna non funzionerà.");
			System.err.println("API Key per Google Maps non impostata su variabile ambiente GEOCODING_KEY, servizio ZonaConsegna non funzionerà.");
			return null;
		}

		return new GeoApiContext.Builder()
			.apiKey(cfg.getKeys().getGoogleGeocoding())
			.build();
	}
}
