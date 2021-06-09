package it.unibo.ingsoft.fortuna.zonaConsegna;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.interfacciaZonaConsegna.IGestioneZoneConsegna;
import it.unibo.ingsoft.fortuna.model.ZonaConsegnaPunti;

public class GestioneZoneConsegna extends Controller implements IGestioneZoneConsegna {

    private Set<ZonaConsegnaPunti> zone;

    public GestioneZoneConsegna() {
        zone = new HashSet<ZonaConsegnaPunti>();
        //scarica zona di consegna da database
    }

    @Override
    public void aggiungiZonaConsegna(List<Entry<Double, Double>> punti, double prezzoMinimo) {
       ZonaConsegnaPunti toAdd = new ZonaConsegnaPunti(punti, prezzoMinimo);
       zone.add(toAdd);
        //aggiungere ZonaDiConsegna nuova anche al DB
    }

    @Override
    public void rimuoviZonaConsegna(ZonaConsegnaPunti toRemove) {
        zone.remove(toRemove);
         //rimuovere ZonaDiConsegna nuova anche al DB
    }

    @Override
    public Set<ZonaConsegnaPunti> listaZoneConsegna() {
        return zone;
    }
    
}
