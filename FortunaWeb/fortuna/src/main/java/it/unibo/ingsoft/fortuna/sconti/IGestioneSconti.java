package it.unibo.ingsoft.fortuna.sconti;

import java.time.LocalDateTime;
import java.util.List;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;

public interface IGestioneSconti {
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent);
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, double prezzoMin);
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, Prodotto perProdotto);
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, double prezzoMin, Prodotto perProdotto);
    
    public boolean rimuoviSconto(Sconto sconto);

    public List<Sconto> listaSconti(LocalDateTime tempo);
    public List<Sconto> listaSconti(LocalDateTime inizioPeriodo, LocalDateTime finePeriodo);
    public List<Sconto> listaSconti(LocalDateTime tempo, double prezzo);
    public List<Sconto> listaSconti(LocalDateTime tempo, Prodotto perProdotto);
    public List<Sconto> listaSconti(LocalDateTime tempo, double prezzo, Prodotto perProdotto);
    public List<Sconto> listaScontiTotali();
}
