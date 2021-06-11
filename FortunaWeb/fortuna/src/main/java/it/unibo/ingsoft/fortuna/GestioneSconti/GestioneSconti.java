package it.unibo.ingsoft.fortuna.gestioneSconti;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.interfacciaSconti.IGestioneSconti;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;

public class GestioneSconti extends Controller implements IGestioneSconti {

    private Set<Sconto> sconti;

    public GestioneSconti() {
        sconti = new HashSet<Sconto>();
        //scarica "sconti" da database
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent) {
        Sconto toAdd = new Sconto();
        if(isPercent)
            toAdd = Sconto.of(start, end, quantita, 0, 0);
        else
            toAdd = Sconto.of(start, end, 0, quantita, 0);
        
        sconti.add(toAdd);
        //aggiunta di toAdd al DB
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, Prodotto per,
        double prezzoMinimo) {
        Sconto toAdd = new Sconto();
        if(isPercent)
            toAdd = Sconto.ofProdotti(start, end, quantita, 0, prezzoMinimo, per);
        else
        toAdd = Sconto.ofProdotti(start, end, 0, quantita, prezzoMinimo, per);
        
        sconti.add(toAdd);
       //aggiunta di toAdd al DB 
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            Prodotto per) {
        Sconto toAdd = new Sconto();
        if(isPercent)
            toAdd = Sconto.ofProdotti(start, end, quantita, 0, 0, per);
        else
        toAdd = Sconto.ofProdotti(start, end, 0, quantita, 0, per);
        
        sconti.add(toAdd);
        //aggiunta di toAdd al DB
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            double prezzoMinimo) {
        Sconto toAdd = new Sconto();
        if(isPercent)
            toAdd = Sconto.of(start, end, quantita, 0, prezzoMinimo);
        else
            toAdd = Sconto.of(start, end, 0, quantita, prezzoMinimo);
        
        sconti.add(toAdd);
        //aggiunta di toAdd al DB
    }

    @Override
    public void rimuoviSconto(Sconto toRemove) {
        sconti.remove(toRemove);
        //rimuovi toRemove dal DB
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end) {
        Set<Sconto> out = new HashSet<Sconto>();
        for (Sconto sconto : sconti) {
            if(sconto.getInizio().equals(start) && sconto.getFine().equals(end))
            out.add(sconto);
        }
        return out;
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, Prodotto per) {
        Set<Sconto> out = new HashSet<Sconto>();
        for (Sconto sconto : sconti) {
            if(sconto.getInizio().equals(start)
                && sconto.getFine().equals(end)
                && sconto.getPerProdotti().contains(per))
            out.add(sconto);
        }
        return out;
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, double prezzoMin) {
        Set<Sconto> out = new HashSet<Sconto>();
        for (Sconto sconto : sconti) {
            if(sconto.getInizio().equals(start) 
                && sconto.getFine().equals(end)
                && sconto.getCostoMinimo() == prezzoMin)
            out.add(sconto);
        }
        return out;
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, Prodotto per, double prezzoMin) {
        Set<Sconto> out = new HashSet<Sconto>();
        for (Sconto sconto : sconti) {
            if(sconto.getInizio().equals(start)
                && sconto.getFine().equals(end)
                && sconto.getPerProdotti().contains(per)
                && sconto.getCostoMinimo() == prezzoMin)
            out.add(sconto);
        }
        return out;
    }

    @Override
    public Set<Sconto> listaScontiTotali() {
        return this.sconti;
    }
    
}
