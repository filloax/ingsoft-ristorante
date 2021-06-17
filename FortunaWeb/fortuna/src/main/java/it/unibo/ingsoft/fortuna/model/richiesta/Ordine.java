package it.unibo.ingsoft.fortuna.model.richiesta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.AttributeOverride;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.unibo.ingsoft.fortuna.model.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ordini_tipo")
@Table(name = "ordini")
@AttributeOverrides({ @AttributeOverride(name = "iDRichiesta", column = @Column(name = "id")),
        @AttributeOverride(name = "dataOra", column = @Column(name = "data_ora")) })
public abstract class Ordine extends Richiesta {

    @Column(name = "note")
    private String note;
    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL)
    private List<ProdottoOrdine> prodottoOrdini;

    @Transient
    private List<Prodotto> prodotti;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "sconti_applicati", joinColumns = @JoinColumn(name = "id_ordine"), inverseJoinColumns = @JoinColumn(name = "id_sconto"))
    private List<Sconto> sconti;

    protected Ordine() {
    }

    protected Ordine(String nominativo, LocalDateTime dataOra, String note, List<Prodotto> prodotti,
            List<Sconto> sconti) {
        super(nominativo, dataOra);
        this.note = note;
        this.prodotti = prodotti;
        this.sconti = sconti;
    }

    public double calcolaCostoTotale() {
        return prodotti.stream().map(prodotto -> prodotto.getPrezzo()).reduce(0., (a, b) -> a + b);
    }

    public double calcolaCostoScontato() {
        double totale = calcolaCostoTotale();
        double costo = totale;

        for (Sconto sconto : sconti) {
            if (sconto.isAttivo(this.getDataOra(), totale)) {
                costo -= sconto.getQuantita();
                costo -= sconto.getQuantitaPct() * totale;
            }

            for (Prodotto prodotto : prodotti) {
                if (sconto.isAttivo(this.getDataOra(), prodotto, totale)) {
                    costo -= sconto.getQuantita();
                    costo -= sconto.getQuantitaPct() * prodotto.getPrezzo();
                }
            }
        }

        return costo;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Prodotto> getProdotti() {
        return this.prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = new ArrayList<>(prodotti);
    }

    public List<Sconto> getSconti() {
        return this.sconti;
    }

    public void setSconti(List<Sconto> sconti) {
        this.sconti = sconti;
    }

    public Ordine dataOra(LocalDateTime dataOra) {
        this.setDataOra(dataOra);
        return this;
    }

    public Ordine nominativo(String nominativo) {
        this.setNominativo(nominativo);
        return this;
    }

    public Ordine note(String note) {
        setNote(note);
        return this;
    }

    public Ordine prodotti(List<Prodotto> prodotti) {
        setProdotti(prodotti);
        return this;
    }

    public Ordine sconti(List<Sconto> sconti) {
        setSconti(sconti);
        return this;
    }
}
