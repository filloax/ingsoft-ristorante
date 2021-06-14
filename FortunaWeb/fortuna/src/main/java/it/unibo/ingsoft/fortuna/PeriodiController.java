package it.unibo.ingsoft.fortuna;

import it.unibo.ingsoft.fortuna.model.attivazione.InsiemePeriodiDisattivazione;


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
}
