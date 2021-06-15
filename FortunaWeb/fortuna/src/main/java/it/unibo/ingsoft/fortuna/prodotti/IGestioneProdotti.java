package it.unibo.ingsoft.fortuna.prodotti;

import java.util.List;

import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.Prodotto;

public interface IGestioneProdotti {
    public Prodotto aggiungiProdotto(String nome, int numero, double prezzo, String descrizione, String immagine) throws DatabaseException;
    public boolean rimuoviProdotto(Prodotto prodotto) throws DatabaseException;
    public boolean rimuoviProdotto(int numero) throws DatabaseException;
    public List<Prodotto> listaProdotti();
}
