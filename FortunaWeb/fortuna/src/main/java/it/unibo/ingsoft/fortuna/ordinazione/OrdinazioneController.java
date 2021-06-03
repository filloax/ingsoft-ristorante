package it.unibo.ingsoft.fortuna.ordinazione;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.PeriodiController;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineAlTavolo;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineDomicilio;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineTakeAway;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.prodotti.IGestioneProdotti;
import it.unibo.ingsoft.fortuna.sconti.IGestioneSconti;
import it.unibo.ingsoft.fortuna.zoneconsegna.IGestioneZoneConsegna;

@Component
public class OrdinazioneController extends Controller implements IOrdinazioneController {
    private PeriodiController periodiDisattivazione;

    // Campi Autowired inseriti automaticamente da Spring Boot a partire da classi
    // marcate con @Component del tipo corrispondente
    @Autowired
    private IGestioneProdotti gestioneProdotti;

    @Autowired
    private IGestioneSconti gestioneSconti;

    @Autowired
    private IGestioneZoneConsegna gestioneZoneConsegna;

    public OrdinazioneController() {
        periodiDisattivazione = PeriodiController.getInstance();
    }

    @Override
    public Set<TipoDisattivazione> getTipoOrdiniDisabilitati() {
        Set<TipoDisattivazione> tipiDisattivati = periodiDisattivazione.getPeriodi().getTipiDisattivati(LocalDateTime.now());
        tipiDisattivati.remove(TipoDisattivazione.PRENOTAZIONE);
        tipiDisattivati.remove(TipoDisattivazione.PRODOTTO);
        return tipiDisattivati;
    }

    @Override
    public Set<Prodotto> getProdottiDisabilitati() {
        return periodiDisattivazione.getPeriodi().getProdottiDisattivati(LocalDateTime.now());
    }

    @Override
    public List<Prodotto> getMenu() {
        return gestioneProdotti.listaProdotti();
    }

    @Override
    public List<Sconto> getSconti() {
        return gestioneSconti.listaScontiTotali();
    }

    @Override
    public boolean verificaZonaConsegna(String indirizzo, double costo) {
        List<IZonaConsegna> zoneConsegna = gestioneZoneConsegna.listaZoneConsegna();

        for (IZonaConsegna zonaConsegna : zoneConsegna) {
            if (zonaConsegna.include(indirizzo, costo)) {
                return true;
            }
        }

        return false;
    }

    private void impostaOrdine(Ordine ordine, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note) {
        ordine.nominativo(nome)
            .prodotti(prodotti)
            .dataOra(dataOra)
            .note(note);

        List<Sconto> scontiApplicabili = new ArrayList<>();
        scontiApplicabili.addAll(gestioneSconti.listaSconti(dataOra, ordine.calcolaCostoTotale()));
        for (Prodotto prodotto : prodotti) {
            scontiApplicabili.addAll(gestioneSconti.listaSconti(dataOra, ordine.calcolaCostoTotale(), prodotto));
        }
        ordine.setSconti(scontiApplicabili);
    }

    private boolean verificaTipo(TipoDisattivazione tipo) {
        return !getTipoOrdiniDisabilitati().contains(tipo);
    }

    // Controlla che nessuno degli ordini richiesti sia disabilitato o inesistente
    private boolean verificaProdotti(List<Prodotto> prodotti) {
        boolean contieneDisabilitato = prodotti.stream()
            .anyMatch(prodotto -> getProdottiDisabilitati().contains(prodotto));

        boolean contieneInesistente = !getMenu().containsAll(prodotti);

        return !contieneDisabilitato && !contieneInesistente;
    }

    private boolean verificaTavolo(String tavolo) {
        // TODO: controllare se tavolo esiste, è valido, ecc, funzionalità aggiuntiva probabilmente
        return true;
    }

    @Override
    public String creaOrdineTavolo(String nome, List<Prodotto> prodotti, String note,
            String tavolo) {
        if (!verificaTipo(TipoDisattivazione.ORDINAZ_TAVOLO))
            return "err-tipo";
        if (!verificaProdotti(prodotti))
            return "err-prodotti";
        if (!verificaTavolo(tavolo))
            return "err-tavolo";

        OrdineAlTavolo ordine = new OrdineAlTavolo();
        impostaOrdine(ordine, nome, prodotti, LocalDateTime.now(), note);
        ordine.setTavolo(tavolo);

        // TODO Inserisci in DB

        System.out.println(String.format("Creato ordine tavolo: %s | %d prodotti | %s | tavolo %s",
            nome, prodotti.size(), note, tavolo));

        return "success";
    }

    @Override
    public String creaOrdineDomicilio(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo) {
        return creaOrdineDomicilio(nome, prodotti, dataOra, note, telefono, indirizzo, "");
    }

    @Override
    public String creaOrdineDomicilio(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo, String tokenPagamento) {
        if (!verificaTipo(TipoDisattivazione.ORDINAZ_DOMICILIO))
            return "err-tipo";
        if (!verificaProdotti(prodotti))
            return "err-prodotti";
    
        OrdineDomicilio ordine = new OrdineDomicilio();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);
    
        if (!verificaZonaConsegna(indirizzo, ordine.calcolaCostoTotale()))
            return "err-zona";
    
        ordine.setTelefono(telefono);
        ordine.setTelefono(indirizzo);
        ordine.setTokenPagamento(tokenPagamento);

        // TODO Inserisci in DB

        System.out.println(String.format("Creato ordine domicilio: %s | %d prodotti | %s | %s | telefono %s | indirizzo %s | token %s",
            nome, prodotti.size(), dataOra.toString(), note, telefono, indirizzo, tokenPagamento));

        return "success";
    }

    @Override
    public String creaOrdineAsporto(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono) {
        if (!verificaTipo(TipoDisattivazione.ORDINAZ_ASPORTO))
            return "err-tipo";
        if (!verificaProdotti(prodotti))
            return "err-prodotti";

        OrdineTakeAway ordine = new OrdineTakeAway();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);
        ordine.setTelefono(telefono);
        
        // TODO Inserisci in DB

        System.out.println(String.format("Creato ordine asporto: %s | %d prodotti | %s | %s | telefono %s",
            nome, prodotti.size(), dataOra.toString(), note, telefono));

        return "success";
    }


    public PeriodiController getPeriodiDisattivazione() {
        return this.periodiDisattivazione;
    }

    public void setPeriodiDisattivazione(PeriodiController periodiDisattivazione) {
        this.periodiDisattivazione = periodiDisattivazione;
    }

    public IGestioneProdotti getGestioneProdotti() {
        return this.gestioneProdotti;
    }

    public void setGestioneProdotti(IGestioneProdotti gestioneProdotti) {
        this.gestioneProdotti = gestioneProdotti;
    }

    public IGestioneSconti getGestioneSconti() {
        return this.gestioneSconti;
    }

    public void setGestioneSconti(IGestioneSconti gestioneSconti) {
        this.gestioneSconti = gestioneSconti;
    }

    public IGestioneZoneConsegna getGestioneZoneConsegna() {
        return this.gestioneZoneConsegna;
    }

    public void setGestioneZoneConsegna(IGestioneZoneConsegna gestioneZoneConsegna) {
        this.gestioneZoneConsegna = gestioneZoneConsegna;
    }
}
