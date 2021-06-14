package it.unibo.ingsoft.fortuna.prenotazione;

import java.time.LocalDateTime;
import java.util.List;

import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;

public interface IPrenotazioneService {
    List<PeriodoDisattivazione> getPeriodiDisattivati();
    String creaPrenotazione(String nome, LocalDateTime dataOra, String telefono, int numeroPersone);
}
