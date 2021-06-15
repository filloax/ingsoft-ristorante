package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.AbstractService;
import it.unibo.ingsoft.fortuna.PeriodiController;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;

@Service
public class DisabilitaPrenotazioneService extends AbstractService implements IDisabilitazionePrenotazioni {
    private PeriodiController periodiController;

    public DisabilitaPrenotazioneService() {
    }

    @PostConstruct
    private void init() {
        periodiController = PeriodiController.getInstance();
    }

    public List<PeriodoDisattivazione> listaPeriodiDisattivazione() {
        return periodiController.getPeriodi().getPeriodi().stream()
            .filter(periodo -> periodo.getTipo() == TipoDisattivazione.PRENOTAZIONE)
            .collect(Collectors.toList());
    }

    public List<PeriodoDisattivazione> listaPeriodiDisattivazione(LocalDateTime time) {
        return periodiController.getPeriodi().getPeriodi().stream()
            .filter(periodo -> periodo.getTipo() == TipoDisattivazione.PRENOTAZIONE && periodo.contieneTempo(time))
            .collect(Collectors.toList());
    }

    public void disabilitaPrenotazioni(LocalDateTime inizio, LocalDateTime fine) {
        PeriodoDisattivazione periodo = new PeriodoDisattivazione().inizio(inizio).fine(fine);
        periodo.setTipo(TipoDisattivazione.PRENOTAZIONE);

        periodiController.add(periodo);
    }

    public void riabilitaPrenotazioni(Integer id) {
        periodiController.remove(id);
    }
}
