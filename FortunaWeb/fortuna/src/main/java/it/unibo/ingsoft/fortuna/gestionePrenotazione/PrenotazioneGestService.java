package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.util.List;
import javax.transaction.Transactional;
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

    public List<Prenotazione> listInAttesa() {

        return repo.findInAttesa();
    }

    public List<Prenotazione> listAccettati() {

        return repo.findAccettati();
    }

    public void save(Prenotazione prenotazione) {
        repo.save(prenotazione);
    }

    public Prenotazione get(Integer id) {
        return repo.findById(id).get();
    }


    // Definire il servizio Transactional per "validare" l'operazione a hybernate/jpa, senza di
    // questo il metodo non ha permesso a eseguire aggiornamenti alle tabelle, provocando eccezioni
    @Transactional
    public int accetta(Integer id) {
        return repo.accetta(id);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }



}
