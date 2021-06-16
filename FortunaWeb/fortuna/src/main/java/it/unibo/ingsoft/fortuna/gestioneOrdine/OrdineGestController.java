package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;

@RestController
@RequestMapping("/gest/ordini")
public class OrdineGestController {

    @Autowired
    private OrdineGestService service;

    @GetMapping
    public List<Ordine> list() {
        return service.listAll();
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

    @PostMapping
    public void add(@RequestBody Ordine ordine) {
        service.save(ordine);
        // TODO:
        // Log
        // NotificaSMS
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        // TODO:
        // Log
        // NotificaSMS
        try {

            Ordine validOrdine = service.get(id);
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
