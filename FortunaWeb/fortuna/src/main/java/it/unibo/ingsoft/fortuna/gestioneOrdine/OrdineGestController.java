package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.GenericGenerators;
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

import it.unibo.ingsoft.fortuna.AbstractController;
import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import it.unibo.ingsoft.fortuna.sms.SMSException;

@RestController
@RequestMapping("/gest-ordini")
public class OrdineGestController extends AbstractController {

    @Autowired
    private OrdineGestService service;

    @GetMapping
    public List<Ordine> list() {
        return service.listAll();
    }

    @GetMapping(value = "attesa")
    public List<Ordine> listInAttesa() {
        return service.listInAttesa();
    }
    @GetMapping(value = "accettati")
    public List<Ordine> listAccettati() {
        return service.listAccettati();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Ordine> get(@PathVariable Integer id) {
        try {
            Ordine ordine = service.get(id);
            return new ResponseEntity<Ordine>(ordine, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Ordine>(HttpStatus.NOT_FOUND);
        }

    }

    // @PostMapping
    // public void add(@RequestBody Ordine ordine) {
    // service.save(ordine);
    // // TODO:
    // // Log
    // // NotificaSMS
    // }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> accetta(HttpServletRequest request, @PathVariable Integer id) {
        // controllo che la prenotazione esiste in database
        /*
         * è implicito il fatto che se passo dal URI ~/10, l'oggetto json che passo ha
         * id 10, non si sa perchè non c'è un controllo su questo (potenzialmente uno
         * può fare una richiesta ad ~/10 e aggiornare l'oggetto con id 9)
         * 
         */

        scriviOperazione(request.getRemoteAddr(), String.format("accettaOrdine(id: %d)", id));

        try {
            service.accetta(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DatabaseException | SMSException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Integer id) {
        scriviOperazione(request.getRemoteAddr(), String.format("deleteOrdine(id: %d)", id));

        String ragione = "";

        try {
            service.cancella(id, ragione);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SMSException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
