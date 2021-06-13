package it.unibo.ingsoft.fortuna.ordinazione;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.unibo.ingsoft.fortuna.ConfigProps;
import it.unibo.ingsoft.fortuna.model.*;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IndirizzoSconosciutoException;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaException;

@Controller
public class OrdinazioneServlet {
    @Autowired
    IOrdinazioneController ordinazione;

    @Autowired
    GeoApiContext geoApiContext;

    @Autowired
    ConfigProps cfg;

    @RequestMapping({"/ordine"})
    public String scegliTipo(HttpServletRequest request) {

        return "ordinazione/tipo";
    }

    @RequestMapping({"/ordine-prodotti"})
    public String scegliProdotti(Model model, HttpServletRequest request, HttpSession session,
            @RequestParam(value = "tipoOrdine", required = false) String tipoOrdine, 
            @RequestParam(value = "add", required = false) String toAddNumero, 
            @RequestParam(value = "remove", required = false) String toRemoveIndex) {

        if (tipoOrdine != null)
            session.setAttribute("tipoOrdine", tipoOrdine);
        else if (session.getAttribute("tipoOrdine") == null)
            return "redirect:/ordine";

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

    private String getFullAddress(String baseAddress) throws ApiException, InterruptedException, IOException, IllegalArgumentException {
        GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, baseAddress)
            .region(cfg.getGeo().getRegion()) //preferenza, non restrittivo
            .bounds(cfg.getGeo().getGapiBounds().southwest, cfg.getGeo().getGapiBounds().northeast) //preferenza, non restrittivo
            .await();
            
        GeocodingResult bestResult = null;

        for (GeocodingResult result : results) {
            for (AddressComponent addComp : result.addressComponents) {
                if (Arrays.asList(addComp.types).contains(AddressComponentType.LOCALITY)) {
                    String city = addComp.longName;
                    if (city.equals(cfg.getGeo().getPreferCity())) {
                        bestResult = result;
                        break;
                    }
                }
            }
        }

        if (bestResult == null && results.length > 0)
            bestResult = results[0];

        if (bestResult == null) {
            throw new IllegalArgumentException("Indirizzo non trovato!");
        }

        return bestResult.formattedAddress;
    }

    @PostMapping({"/ordine-indirizzo"})
    public String scegliIndirizzoPost(Model model, HttpServletRequest request, HttpSession session, RedirectAttributes rAttributes,
            @RequestParam(value = "indirizzo", required = false) String indirizzo) {
        
        if (indirizzo != null) {
            if (indirizzo.isEmpty()) {
                model.addAttribute("error", "Indirizzo vuoto.");
                return "ordinazione/indirizzo";
            }

            @SuppressWarnings("unchecked")
            List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
            // Zona consegna scelta in base a totale, non totale scontato
            double costo = prodotti.stream()
                .map(prodotto -> prodotto.getPrezzo())
                .reduce(0., (a, b) -> a + b);

            try {
                if (ordinazione.verificaZonaConsegna(indirizzo, costo)) {
                    // Indirizzo completato da google maps API
                    String indirizzoCompleto = null;
                    try {
                        indirizzoCompleto = getFullAddress(indirizzo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        model.addAttribute("error", "Indirizzo non valido, ricontrolla");
                        return "ordinazione/indirizzo";    
                    }

                    session.setAttribute("indirizzo", indirizzoCompleto);
                    return "redirect:/ordine-dati";
                } else {
                    model.addAttribute("error", "Non possiamo fare consegne a domicilio a queste distanze, in questa fascia di prezzo.");
                    return "ordinazione/indirizzo";
                }
            } catch (ZonaConsegnaException | InvalidConfigurationPropertyValueException | IllegalStateException e) {
                if (e instanceof ZonaConsegnaException)
                    e.printStackTrace();
                else
                    System.err.println("scegliIndirizzoPost(): " + e.getMessage());
                    
                if (e instanceof IndirizzoSconosciutoException) {
                    model.addAttribute("error", "Indirizzo sconosciuto.");
                    return "ordinazione/indirizzo";
                } else {
                    // Se non funzionano i servizi di localizzazione, allora 
                    // fai andare avanti l'ordinazione, e lascia lo smistamento
                    // al titolare
                    session.setAttribute("indirizzo", indirizzo);
                    rAttributes.addFlashAttribute("errorZonaconsegna", true);
                    return "redirect:/ordine-dati";
                }
            }


        } else {
            return scegliIndirizzoGet(model, request, session);
        }
    }

    @RequestMapping({"/ordine-dati"})
    public String inviaDati(Model model, HttpServletRequest request, HttpSession session,
            @ModelAttribute("datiOrdine") DatiOrdine datiOrdine,
            @RequestParam(value = "conferma", defaultValue = "false") String conferma,
            @RequestParam(value = "pagamento", defaultValue = "false") String pagamento) {
        
        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
        if (prodotti == null)
                return "redirect:/ordine-prodotti";

        model.addAttribute("totale", ordinazione.calcolaTotale(prodotti));
    
        model.addAttribute("chiediTelefono", "ORDINAZ_DOMICILIO".equals(session.getAttribute("tipoOrdine")) || "ORDINAZ_ASPORTO".equals(session.getAttribute("tipoOrdine")));
        model.addAttribute("chiediTavolo", "ORDINAZ_TAVOLO".equals(session.getAttribute("tipoOrdine")));
        model.addAttribute("chiediData", !"ORDINAZ_TAVOLO".equals(session.getAttribute("tipoOrdine")));

        boolean chiediPagamentoOnline = "ORDINAZ_DOMICILIO".equals(session.getAttribute("tipoOrdine"));
        model.addAttribute("chiediPagamentoOnline", chiediPagamentoOnline);

        if (request.getMethod().equals("POST")) {
            String verificaResult = verificaDati(datiOrdine, session, prodotti);
            if (!verificaResult.equals("ok")) {
                model.addAttribute("error", verificaResult);
                return "ordinazione/dati";
            }

            if (pagamento.equals("true")) {
                if (chiediPagamentoOnline) {
                    session.setAttribute("datiOrdine", datiOrdine);
                    return "redirect:/checkout";
                } else {
                    model.addAttribute("error", "Tentativo di pagamento per tipo non concesso di ordinazione");
                    return "ordinazione/dati";
                }
            }

            if (conferma.equals("true")) {
                if ("ORDINAZ_TAVOLO".equals(session.getAttribute("tipoOrdine"))) {
                    datiOrdine.setData(LocalDate.now());
                    datiOrdine.setOra(LocalTime.now());
                }
                session.setAttribute("datiOrdine", datiOrdine);

                return "redirect:/ordine-finale";
            }
        } 

        return "ordinazione/dati";
    }

    @GetMapping("/ordine-finale")
    public String confermaFinaleGet(Model model, HttpServletRequest request, HttpSession session) {
        DatiOrdine datiOrdine = (DatiOrdine) session.getAttribute("datiOrdine");

        if (datiOrdine == null)
            return "redirect:/ordine-dati";

        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
        double totale = ordinazione.calcolaTotale(prodotti);
        double totaleScontato = ordinazione.calcolaTotaleScontato(prodotti, LocalDateTime.of(datiOrdine.getData(), datiOrdine.getOra()));

        model.addAttribute("datiOrdine", datiOrdine);
        model.addAttribute("totale", totale);
        if (totaleScontato != totale) {
            model.addAttribute("totaleScontato", totaleScontato);
        }

        return "ordinazione/review";
    }

    @PostMapping("/ordine-finale")
    public String confermaFinalePost(Model model, HttpServletRequest request, HttpSession session, RedirectAttributes rdAttrs,
      @RequestParam(value = "conferma", defaultValue = "false") String conferma) {

        if (conferma.equals("true")) {
            @SuppressWarnings("unchecked")
            List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
            DatiOrdine datiOrdine = (DatiOrdine) session.getAttribute("datiOrdine");
            String tokenPagamento = (String) session.getAttribute("tokenPagamento");

            LocalDateTime dataOra = LocalDateTime.of(datiOrdine.getData(), datiOrdine.getOra());

            Ordine ordine = null;

            try {
                switch((String) session.getAttribute("tipoOrdine")) {
                    case "ORDINAZ_DOMICILIO":
                        if (tokenPagamento != null && !tokenPagamento.isEmpty()) {
                            ordine = ordinazione.creaOrdineDomicilio(request, datiOrdine.getNome(), prodotti, dataOra, datiOrdine.getNote(), 
                                datiOrdine.getTelefono(), (String)session.getAttribute("indirizzo"), tokenPagamento); 
                        } else {
                            ordine = ordinazione.creaOrdineDomicilio(request, datiOrdine.getNome(), prodotti, dataOra, datiOrdine.getNote(), 
                                datiOrdine.getTelefono(), (String)session.getAttribute("indirizzo")); 
                        }
                        break;
                    case "ORDINAZ_TAVOLO":
                        ordine = ordinazione.creaOrdineTavolo(request, datiOrdine.getNome(), prodotti, datiOrdine.getNote(), 
                            datiOrdine.getTavolo()); 
                        break;
                    case "ORDINAZ_ASPORTO": 
                        ordine = ordinazione.creaOrdineAsporto(request, datiOrdine.getNome(), prodotti, dataOra, datiOrdine.getNote(), 
                            datiOrdine.getTelefono()); 
                        break;
                    default:
                        throw new IllegalStateException("Tipo ordine non valido!");
                }
            } catch (Exception e) {
                rdAttrs.addFlashAttribute("error", e.getMessage());
                return "redirect:/ordine-dati";
            }
    
            model.addAttribute("ordineEseguito", ordine);
            model.addAttribute("tipoOrdineEseguito", session.getAttribute("tipoOrdine"));
            session.invalidate();
            
            return "ordinazione/finale";
        }

        return confermaFinaleGet(model, request, session);
    }

    private String verificaDati(DatiOrdine datiOrdine, HttpSession session, List<Prodotto> prodotti) {
        String tipoOrdine = (String) session.getAttribute("tipoOrdine");
        TipoDisattivazione tipo = TipoDisattivazione.valueOf(tipoOrdine);

        if (getTipoOrdiniDisabilitati().contains(tipo))
            return "bad-type";

        if (!"ORDINAZ_TAVOLO".equals(session.getAttribute("tipoOrdine"))) {
            LocalDateTime dataOra = LocalDateTime.of(datiOrdine.getData(), datiOrdine.getOra());
            if (dataOra.isBefore(LocalDateTime.now())) {
                return "past-date";
            }
        }

        for (Prodotto prodotto : prodotti)    {
            if (!getMenu().contains(prodotto))
                return "bad-product";
            else if (getProdottiDisabilitati().contains(prodotto))
                return "disabled-product";
        }

        if (datiOrdine.getNome().isEmpty()) {
            return "no-name";
        }

        if (tipo != TipoDisattivazione.ORDINAZ_TAVOLO) {
            if (datiOrdine.getTelefono().isEmpty() || !Pattern.matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", datiOrdine.getTelefono())) {
                return "bad-phone";
            }
        }

        return "ok";
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
        DatiOrdine datiOrdine = new DatiOrdine();
        datiOrdine.setData(LocalDate.now().plusDays(1));
        datiOrdine.setOra(LocalTime.now());
        return datiOrdine;
    }

    @ModelAttribute("ordinazioneService")
    public IOrdinazioneController getOrdinazione() {
        return this.ordinazione;
    }

    public void setOrdinazione(IOrdinazioneController ordinazione) {
        this.ordinazione = ordinazione;
    }
}
