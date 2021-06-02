package it.unibo.ingsoft.fortuna.model;

import java.util.Objects;

public class Prodotto {
    private int numero;
    private String nome;
    private double prezzo;
    private String desc;
    private String img;

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


    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return this.prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Prodotto)) {
            return false;
        }
        Prodotto prodotto = (Prodotto) o;
        return numero == prodotto.numero && Objects.equals(nome, prodotto.nome) && prezzo == prodotto.prezzo && Objects.equals(desc, prodotto.desc) && Objects.equals(img, prodotto.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, nome, prezzo, desc, img);
    }
}