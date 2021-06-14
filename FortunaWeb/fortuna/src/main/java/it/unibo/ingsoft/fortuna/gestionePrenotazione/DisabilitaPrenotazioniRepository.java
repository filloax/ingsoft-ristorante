package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;

public interface DisabilitaPrenotazioniRepository extends JpaRepository<PeriodoDisattivazione, Integer> {

}
