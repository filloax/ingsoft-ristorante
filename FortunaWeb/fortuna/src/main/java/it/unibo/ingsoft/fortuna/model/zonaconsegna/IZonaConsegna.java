package it.unibo.ingsoft.fortuna.model.zonaconsegna;

public interface IZonaConsegna {
    public boolean include(Vector coordinata, double prezzo) throws ZonaConsegnaException;
    public boolean include(String indirizzo, double prezzo) throws ZonaConsegnaException;
}
