package it.unibo.ingsoft.fortuna.gestionePrenotazione;

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
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;

@RestController
@RequestMapping("/disa-prenotazioni")
public class DisabilitaPrenotazioneController extends AbstractController {
    @Autowired
    private IDisabilitazionePrenotazioni service;

    @GetMapping
    public List<PeriodoDisattivazione> listDisabilitazionePrenotazioni() {
        return service.listaPeriodiDisattivazione();
    }

    @PostMapping
    public void add(HttpServletRequest request, @RequestBody PeriodoDisattivazione periodo) {
        scriviOperazione(request.getRemoteAddr(),
                String.format("addPeriodoDisattivazionePrenotazioni(da: %s, a: %s)", periodo.getInizio(), periodo.getFine()));
        service.disabilitaPrenotazioni(periodo.getInizio(), periodo.getFine());
    }

    @PostMapping(value = "test")
    public void addDefault() {
        service.disabilitaPrenotazioni(LocalDateTime.now(), LocalDateTime.of(2021, 7, 5, 13, 33));
    }

    // TODO possibile non funzioni il delete su disattiva prodotti perchè cancello
    // riga in relazione con altre tabelle
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Integer id) {
        scriviOperazione(request.getRemoteAddr(), String.format("deletePeriodoDisattivazionePrenotazioni(id: %d)", id));

        try {
            service.riabilitaPrenotazioni(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
