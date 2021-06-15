package it.unibo.ingsoft.fortuna;

import it.unibo.ingsoft.fortuna.model.attivazione.InsiemePeriodiDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;


public class PeriodiController {
    private InsiemePeriodiDisattivazione periodi;

    private static PeriodiController instance;

    public static PeriodiController getInstance() {
        if (instance == null)
            instance = new PeriodiController();

        return instance;
    }

    public PeriodiController() {
        periodi = new InsiemePeriodiDisattivazione();
    }

    public InsiemePeriodiDisattivazione getPeriodi() {
        return periodi;
    }

    public void add(PeriodoDisattivazione periodo) {
        // aggiungi a DB
        periodi.add(periodo);
    }

    public void remove(Integer id) {
        // rimuovi da DB
        periodi.removeById(id);
    }
}
