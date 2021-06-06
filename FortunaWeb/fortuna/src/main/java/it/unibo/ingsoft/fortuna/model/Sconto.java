package it.unibo.ingsoft.fortuna.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Sconto {
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private double quantita;
    private double quantitaPct;
    private double costoMinimo;
    private int id; //generato dal db, impostato qua quando caricati dal db

    private Set<Prodotto> perProdotti;

    public Sconto() {}

    public static Sconto of(LocalDateTime inizio, LocalDateTime fine, double quantita, double quantitaPct, double costoMinimo) {
        Sconto sconto = new Sconto();
        return sconto.inizio(inizio)
            .fine(fine)
            .quantita(quantita)
            .quantitaPct(quantitaPct)
            .costoMinimo(costoMinimo);
    }

    public static Sconto ofProdotti(LocalDateTime inizio, LocalDateTime fine, double quantita, double quantitaPct, double costoMinimo, Set<Prodotto> perProdotti) {
        return of(inizio, fine, quantita, quantitaPct, costoMinimo)
            .perProdotti(perProdotti);
    }

    public static Sconto ofProdotti(LocalDateTime inizio, LocalDateTime fine, double quantita, double quantitaPct, double costoMinimo, Prodotto perProdotto) {
        Set<Prodotto> prodotti = new HashSet<Prodotto>();
        prodotti.add(perProdotto);
        return of(inizio, fine, quantita, quantitaPct, costoMinimo)
            .perProdotti(prodotti);
    }

    public boolean isAttivo(LocalDateTime tempo, Prodotto prodotto, double costoTotale)
    {
        return isDateCostGood(tempo, costoTotale) && (perProdotti != null && perProdotti.contains(prodotto));
    }

    public boolean isAttivo(LocalDateTime tempo, double costoTotale)
    {
        return isDateCostGood(tempo, costoTotale) && (perProdotti == null || perProdotti.isEmpty());
    }

    private boolean isDateCostGood(LocalDateTime tempo, double costoTotale)
    {
        return (tempo.isAfter(inizio) || tempo.isEqual(inizio)) && tempo.isBefore(fine) && costoTotale >= costoMinimo;
    }



    public LocalDateTime getInizio() {
        return this.inizio;
    }

    public void setInizio(LocalDateTime inizio) {
        this.inizio = inizio;
    }

    public LocalDateTime getFine() {
        return this.fine;
    }

    public void setFine(LocalDateTime fine) {
        this.fine = fine;
    }

    public double getQuantita() {
        return this.quantita;
    }

    public void setQuantita(double quantita) {
        this.quantita = quantita;
    }

    public double getQuantitaPct() {
        return this.quantitaPct;
    }

    public void setQuantitaPct(double quantitaPct) {
        this.quantitaPct = quantitaPct;
    }

    public double getCostoMinimo() {
        return this.costoMinimo;
    }

    public void setCostoMinimo(double costoMinimo) {
        this.costoMinimo = costoMinimo;
    }

    public Set<Prodotto> getPerProdotti() {
        return this.perProdotti;
    }

    public void setPerProdotti(Set<Prodotto> perProdotti) {
        this.perProdotti = perProdotti;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Sconto inizio(LocalDateTime inizio) {
        setInizio(inizio);
        return this;
    }

    public Sconto fine(LocalDateTime fine) {
        setFine(fine);
        return this;
    }

    public Sconto quantita(double quantita) {
        setQuantita(quantita);
        return this;
    }

    public Sconto quantitaPct(double quantitaPct) {
        setQuantitaPct(quantitaPct);
        return this;
    }

    public Sconto costoMinimo(double costoMinimo) {
        setCostoMinimo(costoMinimo);
        return this;
    }

    public Sconto perProdotti(Set<Prodotto> perProdotti) {
        setPerProdotti(perProdotti);
        return this;
    }

    public Sconto id(int id) {
        setId(id);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Sconto)) {
            return false;
        }
        Sconto sconto = (Sconto) o;
        return Objects.equals(inizio, sconto.inizio) && Objects.equals(fine, sconto.fine) && quantita == sconto.quantita && quantitaPct == sconto.quantitaPct && costoMinimo == sconto.costoMinimo && Objects.equals(perProdotti, sconto.perProdotti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inizio, fine, quantita, quantitaPct, costoMinimo, perProdotti);
    }
}
