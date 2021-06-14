package it.unibo.ingsoft.fortuna.gestionePrenotazione;

import java.time.LocalDateTime;
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

import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;

@RestController
@RequestMapping("/disa-prenotazioni")
public class DisabilitaPrenotazioneController {
    @Autowired
    private DisabilitaPrenotazioneService service;

    @GetMapping
    public List<PeriodoDisattivazione> listDisabilitazionePrenotazioni() {

        return service.getDisabilitazionePrenotazioni();
    }

    @PostMapping
    public void add(@RequestBody PeriodoDisattivazione periodo) {
        periodo.setTipo(TipoDisattivazione.PRENOTAZIONE);
        service.save(periodo);
    }

    @PostMapping(value = "test")
    public void addDefault() {
        PeriodoDisattivazione periodo = new PeriodoDisattivazione();
        periodo.setInizio(LocalDateTime.now());
        periodo.setFine(LocalDateTime.of(2021, 7, 5, 13, 33));
        periodo.setTipo(TipoDisattivazione.PRENOTAZIONE);
        service.save(periodo);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        // TODO:
        // Log
        try {

            PeriodoDisattivazione validPeriodo = service.get(id);
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
