package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.Vector;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaPunti;

@Component
@Primary
public class GestioneZoneConsegna extends Controller implements IGestioneZoneConsegna, IListaZoneConsegna {

    private Set<ZonaConsegnaPunti> zone;

    public GestioneZoneConsegna() {
        zone = new HashSet<ZonaConsegnaPunti>();
        //scarica zona di consegna da database
    }

    @Override
    public ZonaConsegnaPunti aggiungiZonaConsegna(List<Vector> punti, double prezzoMinimo) {
        ZonaConsegnaPunti toAdd = new ZonaConsegnaPunti(prezzoMinimo, punti);
        zone.add(toAdd);
        //aggiungere ZonaDiConsegna nuova anche al DB

        return toAdd;
    }
    
    @Override
    public boolean rimuoviZonaConsegna(ZonaConsegnaPunti toRemove) {
        zone.remove(toRemove);

        //rimuovere ZonaDiConsegna nuova anche al DB
        return true;
    }

    @Override
    public List<IZonaConsegna> listaZoneConsegna() {
        return List.copyOf(zone);
    }
}
