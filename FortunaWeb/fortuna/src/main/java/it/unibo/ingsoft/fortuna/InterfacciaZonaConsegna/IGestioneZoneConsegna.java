package it.unibo.ingsoft.fortuna.InterfacciaZonaConsegna;
import java.util.Set;
//import java.util.List;

import it.unibo.ingsoft.fortuna.model.ZonaConsegna;

public interface IGestioneZoneConsegna {
    //public void aggiungZonaConsegna(List<Pair<Double,Double>>, double prezzoMinimo);
    public void rimuoviZonaConsegna(ZonaConsegna toRemove);
    public Set<ZonaConsegna> listaZoneConsegna();


}