package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.model.zonaconsegna.DoublePair;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegna;

@Component
public class GestioneZoneConsegnaMock implements IGestioneZoneConsegna {

    @Override
    public IZonaConsegna aggiungiZonaConsegna(List<DoublePair> punti, double prezzoMinimo) {
        return null;
    }

    @Override
    public boolean rimuoviZonaConsegna(IZonaConsegna zonaConsegna) {
        return false;
    }

    @Override
    public List<IZonaConsegna> listaZoneConsegna() {
        List<IZonaConsegna> listaZone = new ArrayList<>();

        DoublePair[] punti = {new DoublePair(0, 0), new DoublePair(0, 999), new DoublePair(999, 999),  new DoublePair(999, 0)};

        listaZone.add(new ZonaConsegna(0, Arrays.asList(punti)));

        return listaZone;
    }
    
}
