package it.unibo.ingsoft.fortuna.InterfacciaSconti;

import java.time.LocalDateTime;
import java.util.Set;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;

public interface IGestioneSconti {
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent);
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, Prodotto per, double prezzoMinimo);
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, Prodotto per);
    public void aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent, double prezzoMinimo);
    public void rimuoviSconto(Sconto toRemove);
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end);
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, Prodotto per);
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, double prezzoMin);
    public Set<Sconto> listaSconti(LocalDateTime start, LocalDateTime end, Prodotto per, double prezzoMin);
    public Set<Sconto> listaScontiTotali();
}