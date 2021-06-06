package it.unibo.ingsoft.fortuna.ordinazione;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unibo.ingsoft.fortuna.model.*;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;

@Controller
public class OrdinazioneServlet {
    @Autowired
    IOrdinazioneController ordinazione;

    @RequestMapping({"/ordine"})
    public String scegliTipo(HttpServletRequest request) {
        return "ordinazione/tipo";
    }

    @PostMapping({"/ordine-prodotti"})
    public String scegliProdotti(Model model, HttpServletRequest request, HttpSession session,
            @RequestParam(value = "tipoOrdine", required = false) String tipoOrdine, 
            @RequestParam(value = "add", required = false) String toAddNumero, 
            @RequestParam(value = "remove", required = false) String toRemoveIndex) {

        if (tipoOrdine != null)
            session.setAttribute("tipoOrdine", tipoOrdine);
        
        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");

        if (prodotti == null) {
            prodotti = new ArrayList<>();
            session.setAttribute("prodotti", prodotti);
        }

        if (toAddNumero != null) {
            int numero = Integer.parseInt(toAddNumero);
            Prodotto prodotto = getMenu().stream()
                .filter(prod -> prod.getNumero() == numero)
                .findFirst().get();
            prodotti.add(prodotto);
        }

        if (toRemoveIndex != null) {
            int i = Integer.parseInt(toRemoveIndex);
            prodotti.remove(i);
        }

        model.addAttribute("totale", prodotti.stream()
            .map(prodotto -> prodotto.getPrezzo())
            .reduce(0., (a, b) -> a + b));

        return "ordinazione/menu";
    }

    @GetMapping({"/ordine-indirizzo"})
    public String scegliIndirizzoGet(Model model, HttpServletRequest request, HttpSession session) {
        model.addAttribute("indirizzoSbagliato", false);
        return "ordinazione/indirizzo";
    }

    @PostMapping({"/ordine-indirizzo"})
    public String scegliIndirizzoPost(Model model, HttpServletRequest request, HttpSession session,
            @RequestParam(value = "indirizzo", required = false) String indirizzo) {
        
        if (indirizzo != null) {
            @SuppressWarnings("unchecked")
            List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
            // Zona consegna scelta in base a totale, non totale scontato
            double costo = prodotti.stream()
                .map(prodotto -> prodotto.getPrezzo())
                .reduce(0., (a, b) -> a + b);

            if (ordinazione.verificaZonaConsegna(indirizzo, costo)) {
                session.setAttribute("indirizzo", indirizzo);
                return "redirect:/ordine-dati";
            } else {
                model.addAttribute("indirizzoSbagliato", true);
                return "ordinazione/indirizzo";
            }


        } else {
            return scegliIndirizzoGet(model, request, session);
        }
    }

    @RequestMapping({"/ordine-dati"})
    public String inviaDatiFinaliGet(Model model, HttpServletRequest request, HttpSession session,
            @ModelAttribute("datiOrdine") DatiOrdine datiOrdine,
            @RequestParam(value = "conferma", defaultValue = "false") String conferma) {
        
        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
        model.addAttribute("totale", prodotti.stream()
            .map(prodotto -> prodotto.getPrezzo())
            .reduce(0., (a, b) -> a + b));
    
        model.addAttribute("chiediTelefono", "domicilio".equals(session.getAttribute("tipoOrdine")) || "takeAway".equals(session.getAttribute("tipoOrdine")));
        model.addAttribute("chiediTavolo", "tavolo".equals(session.getAttribute("tipoOrdine")));
        model.addAttribute("chiediData", !"tavolo".equals(session.getAttribute("tipoOrdine")));

        // TODO: pagamento

        if (request.getMethod().equals("POST") && conferma.equals("true")) {
            LocalDateTime dataOra = null;
            if (!"tavolo".equals(session.getAttribute("tipoOrdine"))) {
                dataOra = LocalDateTime.of(datiOrdine.getData(), datiOrdine.getOra());
            }

            String status = "err-unknown";

            switch((String) session.getAttribute("tipoOrdine")) {
                case "domicilio": status = ordinazione.creaOrdineDomicilio(request, datiOrdine.getNome(), prodotti, dataOra, datiOrdine.getNote(), 
                    datiOrdine.getTelefono(), (String)session.getAttribute("indirizzo")); break;
                case "tavolo": status = ordinazione.creaOrdineTavolo(request, datiOrdine.getNome(), prodotti, datiOrdine.getNote(), 
                    datiOrdine.getTavolo()); break;
                case "takeAway": status = ordinazione.creaOrdineAsporto(request, datiOrdine.getNome(), prodotti, dataOra, datiOrdine.getNote(), 
                    datiOrdine.getTelefono()); break;
                default: //TODO gestire tipo ordine invalido
            }

            if (status.equals("success")) {
                return "ordinazione/finale";
            } else {
                model.addAttribute("error", status);
                return "ordinazione/dati";
            }
        } 

        return "ordinazione/dati";
    }

    @ModelAttribute("tipiDisabilitati")
    public Set<TipoDisattivazione> getTipoOrdiniDisabilitati() {
        return ordinazione.getTipoOrdiniDisabilitati();
    }

    @ModelAttribute("prodottiDisabilitati")
    public Set<Prodotto> getProdottiDisabilitati() {
        return ordinazione.getProdottiDisabilitati();
    }

    @ModelAttribute("menu")
    public List<Prodotto> getMenu() {
        return ordinazione.getMenu();
    }

    @ModelAttribute("tuttiSconti")
    public List<Sconto> getSconti() {
        return ordinazione.getSconti();
    }

    @ModelAttribute("scontiProdotti")
    public List<Sconto> getScontiProdotti() {
        return ordinazione.getSconti().stream()
            .filter(sconto -> sconto.getPerProdotti() != null && sconto.getPerProdotti().size() > 0)
            .collect(Collectors.toList());
    }

    @ModelAttribute("scontiGen")
    public List<Sconto> getScontiGen() {
        return ordinazione.getSconti().stream()
            .filter(sconto -> sconto.getPerProdotti() == null || sconto.getPerProdotti().size() == 0)
            .collect(Collectors.toList());
    }

    @ModelAttribute("datiOrdine")
    public DatiOrdine setupDatiOrdine() {
        return new DatiOrdine();
    }

    public IOrdinazioneController getOrdinazione() {
        return this.ordinazione;
    }

    public void setOrdinazione(IOrdinazioneController ordinazione) {
        this.ordinazione = ordinazione;
    }
}
