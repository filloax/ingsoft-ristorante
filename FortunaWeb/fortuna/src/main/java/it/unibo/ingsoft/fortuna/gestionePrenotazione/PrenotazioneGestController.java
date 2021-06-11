package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;

@RestController
@RequestMapping("/gest-prenotazioni")
public class PrenotazioneGestController {

    @Autowired
    private PrenotazioneGestService service;

    @GetMapping
    public List<Prenotazione> list() {
        return service.listAll();
    }

    @GetMapping(value = "attesa")
    public List<Prenotazione> listInAttesa() {
        return service.listInAttesa();
    }
    @GetMapping(value = "accettati")
    public List<Prenotazione> listAccettati() {
        return service.listAccettati();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Prenotazione> get(@PathVariable Integer id) {
        try {
            Prenotazione prenotazione = service.get(id);
            return new ResponseEntity<Prenotazione>(prenotazione, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Prenotazione>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping
    public void add(@RequestBody Prenotazione prenotazione) {
        service.save(prenotazione);
    }

    // RequestBody per serializzazione
    // PathVariable per prendere dal URI quello tra parentesi e castarlo nella
    // variabile dichiata dopo
    @PutMapping(value = "{id}")
    public ResponseEntity<?> update(@RequestBody Prenotazione prenotazione, @PathVariable Integer id) {
        // controllo che la prenotazione esiste in database
        /*
         * è implicito il fatto che se passo dal URI ~/10, l'oggetto json che passo ha
         * id 10, non si sa perchè non c'è un controllo su questo (potenzialmente uno
         * può fare una richiesta ad ~/10 e aggiornare l'oggetto con id 9)
         * 
         */
        try {

            Prenotazione validPrenotazione = service.get(id);
            service.save(prenotazione);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {

            Prenotazione validPrenotazione = service.get(id);
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
