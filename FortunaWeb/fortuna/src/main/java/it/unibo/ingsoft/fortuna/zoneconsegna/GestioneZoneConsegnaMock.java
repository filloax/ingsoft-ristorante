package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.model.zonaconsegna.Vector;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegna;

@Component
public class GestioneZoneConsegnaMock implements IGestioneZoneConsegna {

    @Override
    public IZonaConsegna aggiungiZonaConsegna(List<Vector> punti, double prezzoMinimo) {
        return null;
    }

    @Override
    public boolean rimuoviZonaConsegna(IZonaConsegna zonaConsegna) {
        return false;
    }

    @Override
    public List<IZonaConsegna> listaZoneConsegna() {
        List<IZonaConsegna> listaZone = new ArrayList<>();

        Vector[] punti = {new Vector(0, 0), new Vector(0, 999), new Vector(999, 999),  new Vector(999, 0)};

        listaZone.add(new ZonaConsegna(0, Arrays.asList(punti)));

        return listaZone;
    }
    
}
