package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.time.LocalDateTime;
import java.util.List;

import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;

public interface IDisabilitazionePrenotazioni {
    public List<PeriodoDisattivazione> listaPeriodiDisattivazione();
    public List<PeriodoDisattivazione> listaPeriodiDisattivazione(LocalDateTime time);
    public void disabilitaPrenotazioni(LocalDateTime inizio, LocalDateTime fine);
    public void riabilitaPrenotazioni(Integer id);
}
