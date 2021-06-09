package it.unibo.ingsoft.fortuna.gestioneProdotti;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.interfacciaGestioneProdotti.IGestioneProdotti;
import it.unibo.ingsoft.fortuna.model.Prodotto;

public class GestioneProdotti extends Controller implements IGestioneProdotti {

    private List<Prodotto> menu;

    public GestioneProdotti() {
        menu = new ArrayList<>();
        //scaricare menu da Database
    }

    @Override
    public void aggiungProdotto(String nome, int numero, double prezzo, String descrizione, String immagine) {
        Prodotto toAdd;
        toAdd = new Prodotto(nome, numero, prezzo, descrizione, immagine);
        menu.add(toAdd);
        //Query per aggiungere toAdd al DB
    }

    @Override
    public void rimuoviProdotto(Prodotto prod) {
        menu.remove(prod);
        //Query per rimuover prod dal DB      
    }

    @Override
    public List<Prodotto> listaProdotti() {
        return menu;
    }
    
}
