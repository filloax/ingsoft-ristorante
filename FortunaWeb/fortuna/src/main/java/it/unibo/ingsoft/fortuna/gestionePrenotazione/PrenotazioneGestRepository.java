package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;

public interface PrenotazioneGestRepository extends JpaRepository<Prenotazione, Integer> {

}
