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
    public String scegliProdotti(Model model, HttpServletRequest request, 
            @RequestParam(value = "tipoOrdine", required = false) String tipoOrdine, 
            @RequestParam(value = "add", required = false) String toAddNumero, 
            @RequestParam(value = "remove", required = false) String toRemoveIndex) {
        HttpSession session = request.getSession();
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

            System.out.println("Debug: aggiunto prodotto " + prodotto.getNome() + ", menu: " + String.join(", ", prodotti.stream().map(p -> p.getNome()).collect(Collectors.toList())));
        }

        if (toRemoveIndex != null) {
            int i = Integer.parseInt(toRemoveIndex);
            Prodotto prodotto = prodotti.remove(i);

            System.out.println("Debug: rimosso prodotto " + prodotto.getNome());
        }

        model.addAttribute("totale", prodotti.stream()
            .map(prodotto -> prodotto.getPrezzo())
            .reduce(0., (a, b) -> a + b));

        return "ordinazione/menu";
    }

    @GetMapping({"/ordine-indirizzo"})
    public String scegliIndirizzo(Model model, HttpServletRequest request) {
        return "ordinazione/indirizzo";
    }

    @RequestMapping({"/ordine-dati"})
    public String inviaDatiFinaliGet(Model model, HttpServletRequest request,
            @ModelAttribute("datiOrdine") DatiOrdine datiOrdine,
            @RequestParam(value = "conferma", defaultValue = "false") String conferma) {

        HttpSession session = request.getSession();
        
        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
        model.addAttribute("totale", prodotti.stream()
            .map(prodotto -> prodotto.getPrezzo())
            .reduce(0., (a, b) -> a + b));
    
        model.addAttribute("chiediTelefono", "domicilio".equals(session.getAttribute("tipoOrdine")) || "takeAway".equals(session.getAttribute("tipoOrdine")));
        model.addAttribute("chiediTavolo", "tavolo".equals(session.getAttribute("tipoOrdine")));
        model.addAttribute("chiediData", !"tavolo".equals(session.getAttribute("tipoOrdine")));

        if (request.getMethod().equals("POST") && conferma.equals("true")) {
            LocalDateTime dataOra;
            dataOra = LocalDateTime.now(); //TODO

            session.setAttribute("indirizzo", "sos"); //TODO

            switch((String) session.getAttribute("tipoOrdine")) {
                case "domicilio": ordinazione.creaOrdineDomicilio(datiOrdine.getNome(), prodotti, dataOra, datiOrdine.getNote(), 
                    datiOrdine.getTelefono(), (String)session.getAttribute("indirizzo")); break;
                case "tavolo": ordinazione.creaOrdineTavolo(datiOrdine.getNome(), prodotti, datiOrdine.getNote(), 
                    datiOrdine.getTavolo()); break;
                case "takeAway": ordinazione.creaOrdineAsporto(datiOrdine.getNome(), prodotti, dataOra, datiOrdine.getNote(), 
                    datiOrdine.getTelefono()); break;
                default: //TODO gestire tipo ordine invalido
            }

            return "ordinazione/finale";
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
