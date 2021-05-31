package it.unibo.ingsoft.fortuna;

import java.util.List;

import it.unibo.ingsoft.fortuna.model.zonaconsegna.DoublePair;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;

public interface IGestioneZoneConsegna {
    public IZonaConsegna aggiungiZonaConsegna(List<DoublePair> punti, double prezzoMinimo);
    public boolean rimuoviZonaConsegna(IZonaConsegna zonaConsegna);
    public List<IZonaConsegna> listaZoneConsegna();
}
