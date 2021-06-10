package it.unibo.ingsoft.fortuna.model.attivazione;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import it.unibo.ingsoft.fortuna.model.*;

public class InsiemePeriodiDisattivazione {
    private ArrayList<PeriodoDisattivazione> periodi;


    public InsiemePeriodiDisattivazione() {
        this.periodi = new ArrayList<PeriodoDisattivazione>();
    }

    /**
     * Controlla se un certo tipo di richiesta è attivo.
     * @param tempo Tempo in cui controllare
     * @param tipo Tipo da controllare
     * @return true se è attivo, false se è disattivato
     */
    public boolean controlla(LocalDateTime tempo, TipoDisattivazione tipo) {
        for (PeriodoDisattivazione periodo : periodi) {
            if (isDisattivatoPeriodoTipo(periodo, tempo, tipo)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Controlla se un certo prodotto è attivo.
     * Modifica rispetto a progetto: avere tipo come argomento sarebbe ridondante, essendo per prodotto sempre
     * PRODOTTO il tipo
     * @param tempo Tempo in cui controllare
     * @param prodotto Prodotto da controllare
     * @return true se è attivo, false se è disattivato
     */
    public boolean controlla(LocalDateTime tempo, Prodotto prodotto) {
        for (PeriodoDisattivazione periodo : periodi) {
            if (isDisattivatoPeriodoProdotto(periodo, tempo, prodotto)) {
                return false;
            }
        }
        return true;
    }

    public Set<TipoDisattivazione> getTipiDisattivati(LocalDateTime tempo) {
        HashSet<TipoDisattivazione> out = new HashSet<>();
        for (PeriodoDisattivazione periodo : periodi) {
            if (timeBetween(tempo, periodo)) {
                out.add(periodo.getTipo());
            }
        }
        return out;
    }

    public Set<Prodotto> getProdottiDisattivati(LocalDateTime tempo) {
        HashSet<Prodotto> out = new HashSet<>();
        for (PeriodoDisattivazione periodo : periodi) {
            if (isDisattivatoPeriodoTipo(periodo, tempo, TipoDisattivazione.PRODOTTO)) {
                out.add(periodo.getProdotto());
            }
        }
        return out;
    }

    private boolean timeBetween(LocalDateTime tempo, PeriodoDisattivazione periodo) {
        return (tempo.isAfter(periodo.getInizio()) || tempo.isEqual(periodo.getInizio())) 
        && tempo.isBefore(periodo.getFine());
    }

    private boolean isDisattivatoPeriodoTipo(PeriodoDisattivazione periodo, LocalDateTime tempo, TipoDisattivazione tipo) {
        return periodo.getTipo().equals(tipo)
        && timeBetween(tempo, periodo);
    }

    private boolean isDisattivatoPeriodoProdotto(PeriodoDisattivazione periodo, LocalDateTime tempo, Prodotto prodotto) {
        return isDisattivatoPeriodoTipo(periodo, tempo, TipoDisattivazione.PRODOTTO)
        && periodo.getProdotto().equals(prodotto);
    }


    public void add(PeriodoDisattivazione periodo) {
        periodi.add(periodo);
    }

    public boolean remove(PeriodoDisattivazione periodo) {
        return periodi.remove(periodo);
    }

    public PeriodoDisattivazione get(int i) {
        return periodi.get(i);
    }

    public ArrayList<PeriodoDisattivazione> getPeriodi() {
        return this.periodi;
    }
}
