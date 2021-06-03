package it.unibo.ingsoft.fortuna.model;

import java.util.List;
import java.util.Map.Entry;

public class ZonaConsegnaPunti implements IZonaConsegna{
    private double prezzoMinimo;
    private List<Entry<Double, Double>> punti;
    
    @Override
    public boolean include(Entry<Double, Double> coordinata, double prezzo) {
        
        //IF check se coordinata è all'interno di punti && prezzo >= prezzoMinimo
        return true;
        //ELSE
        //return false;
    }
    @Override
    public boolean include(String indirizzo, double prezzo) {
        //IF check se l'indirizzo è all'interno di punti e prezzo >= prezzoMinimo
        return true;
        //ELSE
        //return false;
    }


}