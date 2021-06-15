package it.unibo.ingsoft.fortunagest.model;

import lombok.Data;

@Data
public class DatiProdotto {
    private int numero;
    private String nome;
    private double prezzo;
    private String desc;
    private String img;
}