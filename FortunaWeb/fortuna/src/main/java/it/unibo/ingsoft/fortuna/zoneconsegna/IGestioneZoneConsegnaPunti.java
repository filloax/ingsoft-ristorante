package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.util.List;

import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.Vector;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaPunti;

public interface IGestioneZoneConsegnaPunti {
    public ZonaConsegnaPunti aggiungiZonaConsegna(List<Vector> punti, double prezzoMinimo) throws DatabaseException;
    public boolean rimuoviZonaConsegna(ZonaConsegnaPunti zonaConsegna) throws DatabaseException;
    public boolean rimuoviZonaConsegna(int id) throws DatabaseException;
    public List<ZonaConsegnaPunti> list();
}
