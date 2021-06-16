package it.unibo.ingsoft.fortuna;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;

public interface PeriodoDisattivazioneRepository extends JpaRepository<PeriodoDisattivazione, Integer> {

}
