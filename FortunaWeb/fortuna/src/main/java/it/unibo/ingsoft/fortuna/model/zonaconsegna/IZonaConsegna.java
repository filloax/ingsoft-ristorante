package it.unibo.ingsoft.fortuna.model.zonaconsegna;

public interface IZonaConsegna {
    public boolean include(DoublePair coordinata, double prezzo);
    public boolean include(String indirizzo, double prezzo);
}
