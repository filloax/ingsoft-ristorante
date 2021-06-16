package it.unibo.ingsoft.fortuna.sconti;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;
import it.unibo.ingsoft.fortuna.prodotti.IGestioneProdotti;

@RestController
@RequestMapping("/gest/sconti")
public class GestioneScontiController extends AbstractController {
    @Autowired
    private IGestioneSconti service;
    @Autowired
    private IGestioneProdotti prodotti;

    // Restituisce formato più leggibile
    @GetMapping
    public List<DatiSconti> list() {
        return service.listaScontiTotali().stream().map(s -> DatiSconti.fromSconto(s)).collect(Collectors.toList());
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<DatiSconti> get(@PathVariable Integer id) {
        Optional<Sconto> scontoTrovato = service.listaScontiTotali().stream()
            .filter(pr -> pr.getId() == id)
            .findFirst();

        if (scontoTrovato.isPresent()) {
            Sconto sconto = scontoTrovato.get();
            DatiSconti datiSconto = DatiSconti.fromSconto(sconto);

            return new ResponseEntity<DatiSconti>(datiSconto, HttpStatus.OK);
        } else {
            return new ResponseEntity<DatiSconti>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping
    public ResponseEntity<?> aggiungiSconto(HttpServletRequest request, @RequestBody DatiSconti dati) {
        scriviOperazione(request.getRemoteAddr(), String.format("aggiungiSconto(inizio: %s, fine: %s, quant: %s, numero: %d, prezzoMinimo: %f)", 
            dati.getStart(), 
            dati.getEnd(),
            dati.isPercent() ? (dati.getQuantita() * 100 + "%") : dati.getQuantita(),
            dati.getNumeroProdotto(),
            dati.getPrezzoMin()));

        try {
            if (dati.getNumeroProdotto() == null) {
                service.aggiungiSconto(dati.getStart(), dati.getEnd(), dati.getQuantita(), dati.isPercent(), dati.getPrezzoMin());
            } else {
                Prodotto prodotto = prodotti.listaProdotti().stream()
                    .filter(pr -> pr.getNumero() == dati.getNumeroProdotto())
                    .findFirst()
                    .get();
                service.aggiungiSconto(dati.getStart(), dati.getEnd(), dati.getQuantita(), dati.isPercent(), dati.getPrezzoMin(), prodotto);
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable int id) {
        scriviOperazione(request.getRemoteAddr(), String.format("deleteSconto(id: %d)", id));

        try {
            service.rimuoviSconto(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
