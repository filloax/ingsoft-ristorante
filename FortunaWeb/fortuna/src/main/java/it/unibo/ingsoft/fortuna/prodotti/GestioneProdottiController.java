package it.unibo.ingsoft.fortuna.prodotti;

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
import it.unibo.ingsoft.fortuna.model.Prodotto;

@RestController
@RequestMapping("/gest-prodotti")
public class GestioneProdottiController extends AbstractController {
    @Autowired
    private IGestioneProdotti service;

    @GetMapping
    public List<Prodotto> list() {
        return service.listaProdotti();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Prodotto> get(@PathVariable Integer id) {
        Optional<Prodotto> prodottoTrovato = service.listaProdotti().stream()
            .filter(pr -> pr.getNumero() == id)
            .findFirst();

        if (prodottoTrovato.isPresent()) {
            return new ResponseEntity<Prodotto>(prodottoTrovato.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Prodotto>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping
    public ResponseEntity<?> aggiungiProdotto(HttpServletRequest request, @RequestBody Prodotto prodotto) {
        scriviOperazione(request.getRemoteAddr(), String.format("aggiungiProdotto(numero: %d)", prodotto.getNumero()));

        try {
            // Di fatto viene ricreato il prodotto, per come era fatta l'interfaccia; non è un grande sacrificio di memoria
            service.aggiungiProdotto(prodotto.getNome(), prodotto.getNumero(), prodotto.getPrezzo(), prodotto.getDesc(), prodotto.getNome());
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable int id) {
        scriviOperazione(request.getRemoteAddr(), String.format("deleteProdotto(numero: %d)", id));

        try {
            service.rimuoviProdotto(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
