package it.unibo.ingsoft.fortuna.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.unibo.ingsoft.fortuna.SpringContext;
import it.unibo.ingsoft.fortuna.prodotti.IGestioneProdotti;

import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "sconti")
@Data
public class Sconto {
    @Column(name = "inizio")
    private LocalDateTime inizio;
    @Column(name = "fine")
    private LocalDateTime fine;
    @Column(name = "quantita")
    private Double quantita;
    @Column(name = "quantita_pct")
    private Double quantitaPct;
    @Column(name = "costo_minimo")
    private Double costoMinimo;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id; // generato dal db, impostato qua quando caricati dal db

    @ManyToMany
    @JoinTable(name = "prodotti_sconti", joinColumns = @JoinColumn(name = "id_sconto"), inverseJoinColumns = @JoinColumn(name = "numero_prod"))
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
        return isInTempo(tempo) && costoTotale >= getCostoMinimo();
    }


    public double getQuantita() {
        return quantita == null ? 0 : quantita.doubleValue();
    }

    public double getQuantitaPct() {
        return quantitaPct == null ? 0 : quantitaPct.doubleValue();
    }

    public double getCostoMinimo() {
        return costoMinimo == null ? 0 : costoMinimo.doubleValue();
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
