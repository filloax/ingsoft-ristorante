package it.unibo.ingsoft.fortuna.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Entity
@Table(name = "prodotti")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "prodottoOrdines" })

public class Prodotto {
    @Id
    @Column(name = "numero")
    @Getter
    private Integer numero;
    @Column(name = "nome")
    private String nome;
    @Column(name = "prezzo")
    private double prezzo;
    @Column(name = "descrizione")
    private String desc;
    @Column(name = "immagine")
    private String img;

    @OneToMany(mappedBy = "prodotto")
    @ToString.Exclude @JsonIgnore
    private List<ProdottoOrdine> prodottoOrdines;

    @Transient
    @ToString.Exclude @JsonIgnore
    private List<Ordine> ordini;

    public Prodotto() {

    }

    public Prodotto(String nome, int numero, double prezzo, String desc, String img) {
        this.numero = numero;
        this.nome = nome;
        this.prezzo = prezzo;
        this.desc = desc;
        this.img = img;
    }

    public Prodotto(String nome, int numero, double prezzo) {
        this.numero = numero;
        this.nome = nome;
        this.prezzo = prezzo;
        this.desc = "";
        this.img = "";
    }

    public Prodotto numero(int numero) {
        setNumero(numero);
        return this;
    }

    public Prodotto nome(String nome) {
        setNome(nome);
        return this;
    }

    public Prodotto prezzo(double prezzo) {
        setPrezzo(prezzo);
        return this;
    }

    public Prodotto desc(String desc) {
        setDesc(desc);
        return this;
    }

    public Prodotto img(String img) {
        setImg(img);
        return this;
    }
}