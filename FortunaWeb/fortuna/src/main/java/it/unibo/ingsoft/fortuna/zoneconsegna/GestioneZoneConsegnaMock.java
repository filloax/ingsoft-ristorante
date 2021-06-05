package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.model.zonaconsegna.DoublePair;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaMock;

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

        listaZone.add(new ZonaConsegnaMock());

        return listaZone;
    }
    
}
