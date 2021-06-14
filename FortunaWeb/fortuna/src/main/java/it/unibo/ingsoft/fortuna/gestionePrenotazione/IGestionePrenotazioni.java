package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.io.IOException;
import java.util.List;

import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;
import it.unibo.ingsoft.fortuna.sms.SMSException;

public interface IGestionePrenotazioni {
    public List<Prenotazione> listAll();
    public List<Prenotazione> listInAttesa();
    public List<Prenotazione> listAccettati();
    public Prenotazione get(Integer id);
    public int accetta(Integer id) throws DatabaseException, SMSException, IOException;
    public void cancella(Integer id, String ragione) throws IOException, SMSException;
}
