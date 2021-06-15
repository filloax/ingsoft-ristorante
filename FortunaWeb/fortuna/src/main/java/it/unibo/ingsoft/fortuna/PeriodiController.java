package it.unibo.ingsoft.fortuna;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.gestionePrenotazione.DisabilitaPrenotazioniRepository;
import it.unibo.ingsoft.fortuna.model.attivazione.InsiemePeriodiDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;


@Component
public class PeriodiController {

    @Autowired
    private DisabilitaPrenotazioniRepository repo;

    private InsiemePeriodiDisattivazione periodi;

    // private static PeriodiController instance;

    // public static PeriodiController getInstance() {
    //     System.out.println("\n\n\n[TEST] istanzaPeriodoCOntrooler\n\n\n");
    //     if (instance == null){
    //         instance = new PeriodiController();
    //         instance.init();
    //     }

    //     return instance;
    // }

    public PeriodiController() {
        System.out.println("\n[COSTRUTTORE PERIODO CONTROLLER]\n");
        periodi = new InsiemePeriodiDisattivazione();
        
    }

    @PostConstruct
    private void init() {
        System.out.println("\n[INIT] PeriodoController\n");
        List<PeriodoDisattivazione> tutti = repo.findAll();
        System.out.println(tutti);
        
        this.periodi.setAll(tutti);
        

    }

    public InsiemePeriodiDisattivazione getPeriodi() {
        return periodi;
    }

    public void add(PeriodoDisattivazione periodo) {
        // aggiungi a DB
        repo.save(periodo);
        periodi.add(periodo);
    }

    public void remove(Integer id) {
        // rimuovi da DB
        repo.deleteById(id);
        periodi.removeById(id);
    }
}
