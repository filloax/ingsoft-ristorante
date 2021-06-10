package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.util.List;

import it.unibo.ingsoft.fortuna.model.zonaconsegna.Vector;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;

public interface IGestioneZoneConsegna {
    public IZonaConsegna aggiungiZonaConsegna(List<Vector> punti, double prezzoMinimo);
    public boolean rimuoviZonaConsegna(IZonaConsegna zonaConsegna);
    public List<IZonaConsegna> listaZoneConsegna();
}
