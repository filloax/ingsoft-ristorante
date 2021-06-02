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
    // marcate con @Component("nome") es: @Component("gestioneProdotti")
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
            if (!zonaConsegna.include(indirizzo, costo)) {
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

    @Override
    public OrdineAlTavolo creaOrdineTavolo(String nome, List<Prodotto> prodotti, String note,
            String tavolo) {
        OrdineAlTavolo ordine = new OrdineAlTavolo();
        impostaOrdine(ordine, nome, prodotti, LocalDateTime.now(), note);
        ordine.setTavolo(tavolo);

        // TODO Inserisci in DB

        System.out.println(String.format("Creato ordine tavolo: %s | %d prodotti | %s | tavolo %s",
            nome, prodotti.size(), note, tavolo));

        return ordine;
    }

    @Override
    public OrdineDomicilio creaOrdineDomicilio(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo) {
        OrdineDomicilio ordine = new OrdineDomicilio();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);
        ordine.setTelefono(telefono);
        ordine.setTelefono(indirizzo);
        ordine.setTokenPagamento("");

        System.out.println(String.format("Creato ordine domicilio: %s | %d prodotti | %s | %s | telefono %s | indirizzo %s",
            nome, prodotti.size(), dataOra.toString(), note, telefono, indirizzo));

        // TODO Inserisci in DB

        return ordine;
    }

    @Override
    public OrdineDomicilio creaOrdineDomicilio(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo, String tokenPagamento) {
        OrdineDomicilio ordine = new OrdineDomicilio();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);
        ordine.setTelefono(telefono);
        ordine.setTelefono(indirizzo);
        ordine.setTokenPagamento(tokenPagamento);

        // TODO Inserisci in DB

        System.out.println(String.format("Creato ordine domicilio: %s | %d prodotti | %s | %s | telefono %s | indirizzo %s | token %s",
            nome, prodotti.size(), dataOra.toString(), note, telefono, indirizzo, tokenPagamento));

        return ordine;
    }

    @Override
    public OrdineTakeAway creaOrdineAsporto(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono) {
        OrdineTakeAway ordine = new OrdineTakeAway();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);
        ordine.setTelefono(telefono);
        
        // TODO Inserisci in DB

        System.out.println(String.format("Creato ordine asporto: %s | %d prodotti | %s | %s | telefono %s",
            nome, prodotti.size(), dataOra.toString(), note, telefono));

        return ordine;
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
