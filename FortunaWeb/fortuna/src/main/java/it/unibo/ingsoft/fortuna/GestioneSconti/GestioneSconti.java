package it.unibo.ingsoft.fortuna.GestioneSconti;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.InterfacciaSconti.IGestioneSconti;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;

public class GestioneSconti extends Controller implements IGestioneSconti {

    private Set<Sconto> sconti;

    public GestioneSconti() {
        sconti = new HashSet<Sconto>();
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, Prodotto per,
            double prezzoMinimo) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            Prodotto per) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            double prezzoMinimo) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rimuoviSconto(Sconto toRemove) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, Prodotto per) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, double prezzoMin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, Prodotto per, double prezzoMin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Sconto> listaScontiTotali() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
