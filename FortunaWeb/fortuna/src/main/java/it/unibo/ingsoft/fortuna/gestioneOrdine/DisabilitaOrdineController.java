package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

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

import it.unibo.ingsoft.fortuna.AbstractController;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;

@RestController
@RequestMapping("/disa-ordini")
public class DisabilitaOrdineController extends AbstractController {
    @Autowired
    private DisabilitaOrdineService service;

    @GetMapping
    public List<PeriodoDisattivazione> listDisabilitazionePrenotazioni() {
        return service.listaPeriodiDisattivazione();
    }

    @PostMapping
    public void add(HttpServletRequest request, @RequestBody LocalDateTime inizio, @RequestBody LocalDateTime fine,
            @RequestBody TipoDisattivazione tipo) {
        scriviOperazione(request.getRemoteAddr(),
                String.format("addPeriodoDisattivazioneOrdine(da: %s, a: %s, tipo : %s)", inizio, fine, tipo));
        service.disabilitaOrdini(inizio, fine, tipo);
    }

    @PostMapping(value = "test")
    public void addDefault() {
        service.disabilitaOrdini(LocalDateTime.now(), LocalDateTime.of(2021, 7, 5, 13, 33),
                TipoDisattivazione.ORDINAZ_TAVOLO);
    }

    @PostMapping(value = "prodotto")
    public void addDisattivaProdotto(HttpServletRequest request, @RequestBody LocalDateTime inizio,
            @RequestBody LocalDateTime fine, @RequestBody Prodotto prodotto) {
        scriviOperazione(request.getRemoteAddr(), String.format(
                "addPeriodoDisattivazioneProdotto(da: %s, a: %s, prodotto : %s)", inizio, fine, prodotto.getNome()));
        service.disabilitaOrdini(inizio, fine, prodotto);

    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Integer id) {
        scriviOperazione(request.getRemoteAddr(), String.format("deletePeriodoDisattivazioneOrdini(id: %d)", id));

        try {
            service.riabilitaOrdini(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
