package it.unibo.ingsoft.fortuna.InterfacciaZonaConsegna;
import java.util.Set;
import java.util.Map.Entry;
import java.util.List;

import it.unibo.ingsoft.fortuna.model.ZonaConsegnaPunti;

public interface IGestioneZoneConsegna {
    public void aggiungZonaConsegna(List<Entry<Double,Double>> punti, double prezzoMinimo);
    public void rimuoviZonaConsegna(ZonaConsegnaPunti toRemove);
    public Set<ZonaConsegnaPunti> listaZoneConsegna();
}