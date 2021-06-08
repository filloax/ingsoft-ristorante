package it.unibo.ingsoft.fortuna;

import com.google.maps.GeoApiContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import it.unibo.ingsoft.fortuna.log.ILogManager;

// NOTA: Per come funziona Spring, cerca classi con mapping di url (tipo /home, /ordina, ecc.)
// nello stesso package della classe con @SpringBootApplication
// se si vuol estendere la ricerca ad altri package, c'è una nota apposita
@SpringBootApplication
public class FortunaApplication {

    @Value("${GEOCODING_KEY: null}")
    private String mapsApiKey;

	@Autowired
	Environment env;

	@Autowired
	ILogManager logManager;

	public static void main(String[] args) {
		SpringApplication.run(FortunaApplication.class, args);
	}

	@Bean(destroyMethod = "shutdown")
	public GeoApiContext geoApiContext() {
		if (mapsApiKey.equals("null")) {
			logManager.scriviMessaggio("API Key per Google Maps non impostata su variabile ambiente GEOCODING_KEY, servizio ZonaConsegna non funzionerà.");
			System.err.println("API Key per Google Maps non impostata su variabile ambiente GEOCODING_KEY, servizio ZonaConsegna non funzionerà.");
			return null;
		}

		return new GeoApiContext.Builder()
			.apiKey(mapsApiKey)
			.build();
	}

}
