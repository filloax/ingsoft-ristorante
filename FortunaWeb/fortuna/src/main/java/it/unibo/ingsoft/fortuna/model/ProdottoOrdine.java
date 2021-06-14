package it.unibo.ingsoft.fortuna.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;

@Entity
@Table(name = "prodotti_ordinati")
public class ProdottoOrdine {

    @EmbeddedId
    private ProdottoOrdineChiave id;

    @ManyToOne
    @MapsId("ordineId")
    @JoinColumn(name = "id_ordine")
    private Ordine ordine;

    @ManyToOne
    @MapsId("numeroProdotto")
    @JoinColumn(name = "numero_prod")
    private Prodotto prodotto;

    private Integer quantita;

    public ProdottoOrdine() {

    }

    public ProdottoOrdineChiave getId() {
        return id;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setId(ProdottoOrdineChiave id) {
        this.id = id;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

}
