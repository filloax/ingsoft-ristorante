package it.unibo.ingsoft.fortuna.model;

import java.util.Objects;

import lombok.Data;

@Data
public class Prodotto {
    private int numero;
    private String nome;
    private double prezzo;
    private String desc;
    private String img;

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