package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.PeriodiController;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;

@Service
public class DisabilitaPrenotazioneService {

    @Autowired
    private DisabilitaPrenotazioniRepository repo;

    private PeriodiController periodiController;

    public DisabilitaPrenotazioneService() {

        // il repo non è ancora pronto, shit
    }

    @PostConstruct
    private void init() {
        // TODO è giusto inizializzare il singlethon delle disabilitazioni così??
        periodiController = PeriodiController.getInstance();
        periodiController.getPeriodi().setAll(this.listAll());

    }

    public List<PeriodoDisattivazione> listAll() {
        return repo.findAll();
    }

    public List<PeriodoDisattivazione> getPeriodi() {
        return periodiController.getPeriodi().getPeriodi();
    }

    public List<PeriodoDisattivazione> getDisabilitazionePrenotazioni() {
        ArrayList<PeriodoDisattivazione> disabilitaPrenotazioni = new ArrayList<PeriodoDisattivazione>();
        for (PeriodoDisattivazione item : periodiController.getPeriodi().getPeriodi()) {
            if (item.getTipo().equals(TipoDisattivazione.PRENOTAZIONE))
                disabilitaPrenotazioni.add(item);
        }

        return disabilitaPrenotazioni;
    }

    public void save(PeriodoDisattivazione periodo) {
        repo.save(periodo);
        this.periodiController.getPeriodi().add(periodo);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
        this.periodiController.getPeriodi().removeById(id);
    }

    public PeriodoDisattivazione get(Integer id) {
        return repo.getById(id);
    }

}
