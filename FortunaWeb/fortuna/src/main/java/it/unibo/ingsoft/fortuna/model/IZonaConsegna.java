package it.unibo.ingsoft.fortuna.model;

import java.util.Map.Entry;

public interface IZonaConsegna {
    public boolean include(Entry<Double,Double> coordinata, double prezzo);
    public boolean include(String indirizzo, double prezzo);
}