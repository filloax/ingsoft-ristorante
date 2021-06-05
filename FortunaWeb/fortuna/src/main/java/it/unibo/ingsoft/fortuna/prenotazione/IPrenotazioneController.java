package it.unibo.ingsoft.fortuna.prenotazione;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;

public interface IPrenotazioneController {
    List<PeriodoDisattivazione> getPeriodiDisattivati();
    String creaPrenotazione(HttpServletRequest request, String nome, LocalDateTime dataOra, String telefono, int numeroPersone);
}
