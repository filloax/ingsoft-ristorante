package it.unibo.ingsoft.fortuna;

import java.util.List;

import it.unibo.ingsoft.fortuna.model.Prodotto;

public interface IGestioneProdotti {
    public Prodotto aggiungiProdotto(String nome, int numero, double prezzo, String descrizione, String immagine);
    public boolean rimuoviProdotto(Prodotto prodotto);
    public List<Prodotto> listaProdotti();
}
