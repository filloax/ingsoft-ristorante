package it.unibo.ingsoft.fortuna.prodotti;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.model.Prodotto;

@Component
public class GestioneProdottiMock implements IGestioneProdotti {

    @Override
    public Prodotto aggiungiProdotto(String nome, int numero, double prezzo, String descrizione, String immagine) {
        return null;
    }

    @Override
    public boolean rimuoviProdotto(Prodotto prodotto) {
        return false;
    }

    @Override
    public List<Prodotto> listaProdotti() {
        ArrayList<Prodotto> listaProdotti = new ArrayList<>();

        listaProdotti.add(new Prodotto("Involtini", 102, 2.50));
        listaProdotti.add(new Prodotto("Ravioli", 104, 3.50));
        listaProdotti.add(new Prodotto("Riso", 150, 4));

        return listaProdotti;
    }
    
}
