package it.unibo.ingsoft.fortuna.prenotazione;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.PeriodiController;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;
import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;

@Component
public class PrenotazioneController extends Controller implements IPrenotazioneController {
    private PeriodiController periodiDisattivazione;

    public PrenotazioneController() {
        periodiDisattivazione = PeriodiController.getInstance();
    }

    @Override
    public List<PeriodoDisattivazione> getPeriodiDisattivati() {
        return periodiDisattivazione.getPeriodi().getPeriodi().stream()
            .filter(periodo -> periodo.getTipo() == TipoDisattivazione.PRENOTAZIONE)
            .collect(Collectors.toList());
    }

    @Override
    public String creaPrenotazione(String nome, LocalDateTime dataOra, String telefono, int numeroPersone) {
        boolean dataValida = getPeriodiDisattivati().stream()
            .noneMatch(periodo -> (periodo.getInizio().isBefore(dataOra) || periodo.getInizio().equals(dataOra)) && periodo.getFine().isAfter(dataOra));
        if (!dataValida)
            return "err-data";

        Prenotazione prenotazione = new Prenotazione(nome, dataOra, telefono, numeroPersone);

        // TODO: inserisci in DB

        return "success";
    }
    
}
