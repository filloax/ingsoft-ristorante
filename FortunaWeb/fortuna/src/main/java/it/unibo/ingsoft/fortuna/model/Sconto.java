package it.unibo.ingsoft.fortuna.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "sconti")
@Data
public class Sconto {
    @Column(name = "inizio")
    private LocalDateTime inizio;
    @Column(name = "fine")
    private LocalDateTime fine;
    @Column(name = "quantita")
    private double quantita;
    @Column(name = "quantitaPct")
    private double quantitaPct;
    @Column(name = "costoMinimo")
    private double costoMinimo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id; // generato dal db, impostato qua quando caricati dal db

    @ManyToMany
    private Set<Prodotto> perProdotti;

    public Sconto() {
    }

    public static Sconto of(LocalDateTime inizio, LocalDateTime fine, double quantita, double quantitaPct,
            double costoMinimo) {
        Sconto sconto = new Sconto();
        return sconto.inizio(inizio).fine(fine).quantita(quantita).quantitaPct(quantitaPct).costoMinimo(costoMinimo);
    }

    public static Sconto ofProdotti(LocalDateTime inizio, LocalDateTime fine, double quantita, double quantitaPct,
            double costoMinimo, Set<Prodotto> perProdotti) {
        return of(inizio, fine, quantita, quantitaPct, costoMinimo).perProdotti(perProdotti);
    }

    public static Sconto ofProdotti(LocalDateTime inizio, LocalDateTime fine, double quantita, double quantitaPct,
            double costoMinimo, Prodotto perProdotto) {
        Set<Prodotto> prodotti = new HashSet<Prodotto>();
        prodotti.add(perProdotto);
        return of(inizio, fine, quantita, quantitaPct, costoMinimo).perProdotti(prodotti);
    }

    public boolean isInTempo(LocalDateTime tempo) {
        return (tempo.isAfter(inizio) || tempo.isEqual(inizio)) && tempo.isBefore(fine);
    }

    public boolean overlapsTempo(LocalDateTime iniziop, LocalDateTime finep) {
        return (fine.isAfter(iniziop) && fine.isBefore(finep)) || (inizio.isAfter(iniziop) && inizio.isBefore(finep));
    }

    public boolean isAttivo(LocalDateTime tempo, Prodotto prodotto, double costoTotale) {
        return isDateCostGood(tempo, costoTotale) && (perProdotti != null && perProdotti.contains(prodotto));
    }

    public boolean isAttivo(LocalDateTime tempo, double costoTotale) {
        return isDateCostGood(tempo, costoTotale) && (perProdotti == null || perProdotti.isEmpty());
    }

    private boolean isDateCostGood(LocalDateTime tempo, double costoTotale) {
        return isInTempo(tempo) && costoTotale >= costoMinimo;
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
}
