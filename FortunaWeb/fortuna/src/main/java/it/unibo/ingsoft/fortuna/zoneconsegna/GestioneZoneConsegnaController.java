package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unibo.ingsoft.fortuna.AbstractController;
import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaPunti;

@RestController
@RequestMapping("/gest/zone")
public class GestioneZoneConsegnaController extends AbstractController {
    @Autowired
    private IGestioneZoneConsegnaPunti service;

    @GetMapping
    public List<ZonaConsegnaPunti> list() {
        return service.list();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<ZonaConsegnaPunti> get(@PathVariable Integer id) {
        Optional<ZonaConsegnaPunti> found = service.list().stream()
            .filter(z -> z.getId() == id)
            .findFirst();

        if (found.isPresent()) {
            return new ResponseEntity<ZonaConsegnaPunti>(found.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ZonaConsegnaPunti>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping
    public ResponseEntity<?> aggiungiZonaConsegna(HttpServletRequest request, @RequestBody ZonaConsegnaPunti zonaConsegna) {
        scriviOperazione(request.getRemoteAddr(), String.format("aggiungiZonaConsegna(zonaConsegna: %s)", zonaConsegna.toString()));

        try {
            service.aggiungiZonaConsegna(zonaConsegna.getPunti(), zonaConsegna.getPrezzoMinimo());
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable int id) {
        scriviOperazione(request.getRemoteAddr(), String.format("deleteZonaConsegna(id: %d)", id));

        try {
            service.rimuoviZonaConsegna(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
