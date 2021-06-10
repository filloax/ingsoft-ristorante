package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;

@Service
public class PrenotazioneGestService {

    @Autowired
    private PrenotazioneGestRepository repo;

    public List<Prenotazione> listAll() {

        return repo.findAll();
    }

    public void save(Prenotazione prenotazione) {
        repo.save(prenotazione);
    }

    public Prenotazione get(Integer id) {
        return repo.findById(id).get();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

}
