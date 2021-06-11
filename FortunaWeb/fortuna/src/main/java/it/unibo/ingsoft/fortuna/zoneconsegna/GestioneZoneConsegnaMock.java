package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.model.zonaconsegna.Vector;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaPunti;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class GestioneZoneConsegnaMock implements IListaZoneConsegna {
    @Override
    public List<IZonaConsegna> listaZoneConsegna() {
        List<IZonaConsegna> listaZone = new ArrayList<>();

        Vector[] punti = {new Vector(0, 0), new Vector(0, 999), new Vector(999, 999),  new Vector(999, 0)};

        listaZone.add(new ZonaConsegnaPunti(0, Arrays.asList(punti)));

        return listaZone;
    }
    
}
