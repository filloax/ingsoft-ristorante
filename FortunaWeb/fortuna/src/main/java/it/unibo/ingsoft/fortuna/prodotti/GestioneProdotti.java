package it.unibo.ingsoft.fortuna.prodotti;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.model.Prodotto;

@Component
@Primary
public class GestioneProdotti extends Controller implements IGestioneProdotti {

    private List<Prodotto> menu;

    public GestioneProdotti() {
        menu = new ArrayList<>();
        //scaricare menu da Database
    }

    @Override
    public Prodotto aggiungiProdotto(String nome, int numero, double prezzo, String descrizione, String immagine) {
        Prodotto toAdd;
        toAdd = new Prodotto(nome, numero, prezzo, descrizione, immagine);
        menu.add(toAdd);
        //Query per aggiungere toAdd al DB

        return toAdd;
    }

    @Override
    public boolean rimuoviProdotto(Prodotto prod) {
        menu.remove(prod);
        //Query per rimuover prod dal DB   
        
        return true;
    }

    @Override
    public List<Prodotto> listaProdotti() {
        return menu;
    }
    
}
