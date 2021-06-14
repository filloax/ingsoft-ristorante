package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.AbstractService;
import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.ResourceUtilsLib;
import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;
import it.unibo.ingsoft.fortuna.sms.IInvioSms;
import it.unibo.ingsoft.fortuna.sms.SMSException;

@Service
public class PrenotazioneGestService extends AbstractService implements IGestionePrenotazioni {
    @Autowired
    private PrenotazioneGestRepository repo;

    @Autowired
    private IInvioSms sms;

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
    public int accetta(Integer id) throws DatabaseException, SMSException, IOException {
        int rowsUpdated = repo.accetta(id);

        if (rowsUpdated == 1) {
            Prenotazione prenotazione = get(id);

            String msgTemplate = ResourceUtilsLib.loadResourceToString("classpath:sms/prenot-accettata.txt");
            sms.inviaSMS(prenotazione.getTelefono(), String.format(msgTemplate, prenotazione.getDataOra(), id));
        } else {
            throw new DatabaseException("Aggiornato un numero inaspettato di righe con l'accettazione: " + id);
        }

        return rowsUpdated;
    }

    public void cancella(Integer id, String ragione) throws IOException, SMSException {
        Prenotazione prenotazione = get(id);

        repo.deleteById(id);

        String msgTemplate = ResourceUtilsLib.loadResourceToString("classpath:sms/prenot-cancellata.txt");
        sms.inviaSMS(prenotazione.getTelefono(), String.format(msgTemplate, ragione, prenotazione.getDataOra(), id));
    }

}
