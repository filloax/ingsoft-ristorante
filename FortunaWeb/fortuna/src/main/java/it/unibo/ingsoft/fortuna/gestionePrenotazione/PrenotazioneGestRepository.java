package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;

public interface PrenotazioneGestRepository extends JpaRepository<Prenotazione, Integer> {

    // JPQL  sintassi simile ad SQL
    // JPQL non funziona , mettiamo query native
    @Query(value = "SELECT * FROM prenotazioni p WHERE p.accettato IS NULL", nativeQuery = true)
    List<Prenotazione> findInAttesa();

    @Query(value = "SELECT * FROM prenotazioni p WHERE p.accettato = 'Y'", nativeQuery = true)
    List<Prenotazione> findAccettati();
    
}
