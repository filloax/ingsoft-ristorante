package it.unibo.ingsoft.fortuna.interfacciaGestioneProdotti;

import java.util.List;

import it.unibo.ingsoft.fortuna.model.Prodotto;

public interface IGestioneProdotti {
    public void aggiungProdotto(String nome, int numero, double prezzo, String descrizione, String immagine);
    public void rimuoviProdotto(Prodotto prod);
    public List<Prodotto> listaProdotti();
}
