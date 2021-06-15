package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.AbstractService;
import it.unibo.ingsoft.fortuna.PeriodiController;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;

@Service
public class DisabilitaOrdineService extends AbstractService {
    @Autowired
    private PeriodiController periodiController;

    public DisabilitaOrdineService() {
    }

    @PostConstruct
    private void init() {
        // periodiController = PeriodiController.getInstance();
        System.out.println("\n[POST-CONSTRUCT] DisabilitaOrdineService\n");
    }

    public List<PeriodoDisattivazione> listaPeriodiDisattivazione() {
        return periodiController.getPeriodi().getPeriodi().stream()
                .filter(periodo -> periodo.getTipo() != TipoDisattivazione.PRENOTAZIONE).collect(Collectors.toList());
    }

    public List<PeriodoDisattivazione> listaPeriodiDisattivazione(LocalDateTime time) {
        return periodiController.getPeriodi().getPeriodi().stream()
                .filter(periodo -> periodo.getTipo() != TipoDisattivazione.PRENOTAZIONE && periodo.contieneTempo(time))
                .collect(Collectors.toList());
    }

    public void disabilitaOrdini(LocalDateTime inizio, LocalDateTime fine, TipoDisattivazione tipo) {
        PeriodoDisattivazione periodo = new PeriodoDisattivazione().inizio(inizio).fine(fine);
        periodo.setTipo(tipo);

        periodiController.add(periodo);
    }

    public void disabilitaOrdini(LocalDateTime inizio, LocalDateTime fine, Prodotto prodotto) {
        PeriodoDisattivazione periodo = new PeriodoDisattivazione().inizio(inizio).fine(fine)
                .tipo(TipoDisattivazione.PRODOTTO).prodotto(prodotto);
        periodiController.add(periodo);
    }

    public void riabilitaOrdini(Integer id) {
        periodiController.remove(id);
    }
}
