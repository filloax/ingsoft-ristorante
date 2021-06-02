package it.unibo.ingsoft.fortuna.model.zonaconsegna;

public class ZonaConsegnaMock implements IZonaConsegna {

    @Override
    public boolean include(DoublePair coordinata, double prezzo) {
        return true;
    }

    @Override
    public boolean include(String indirizzo, double prezzo) {
        if (indirizzo.equals("no"))
            return false;
        return true;
    }
    
}
