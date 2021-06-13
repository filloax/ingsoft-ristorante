package it.unibo.ingsoft.fortuna.prenotazione;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;

@Controller
public class PrenotazioneServlet {
    @Autowired
    IPrenotazioneController prenotazioneController;

    @RequestMapping({"/prenota"})
    public String prenotazione(Model model, HttpServletRequest request, HttpSession session,
            @ModelAttribute("datiPrenotazione") DatiPrenotazione datiPrenotazione,
            @RequestParam(value = "conferma", defaultValue = "false") String conferma) {
        if (request.getMethod().equals("POST") && "true".equals(conferma)) {
            LocalDateTime dataOra = LocalDateTime.of(datiPrenotazione.getData(), datiPrenotazione.getOra());

            String status = "err-unknown";

            status = prenotazioneController.creaPrenotazione(request, datiPrenotazione.getNome(), dataOra, datiPrenotazione.getTelefono(), datiPrenotazione.getNumeroPersone());

            if (status.equals("success")) {
                return "prenotazione/finale";
            } else {
                model.addAttribute("error", status);
                return "prenotazione/dati";
            }
        } 

        return "prenotazione/dati";
    }

    @ModelAttribute("periodiDisabilitati")
    public List<PeriodoDisattivazione> getPeriodiDisattivati() {
        return prenotazioneController.getPeriodiDisattivati();
    }

    @ModelAttribute("datiPrenotazione")
    public DatiPrenotazione setupDatiPrenotazione() {
        return new DatiPrenotazione();
    }

    public IPrenotazioneController getPrenotazioneController() {
        return this.prenotazioneController;
    }

    public void setPrenotazioneController(IPrenotazioneController prenotazioneController) {
        this.prenotazioneController = prenotazioneController;
    }
}
