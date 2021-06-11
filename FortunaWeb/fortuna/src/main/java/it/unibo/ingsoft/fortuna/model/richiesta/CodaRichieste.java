package it.unibo.ingsoft.fortuna.model.richiesta;

import java.util.ArrayList;
import java.util.List;

public class CodaRichieste {
    private ArrayList<Richiesta> accettati;
    private ArrayList<Richiesta> inAttesa;

    public CodaRichieste() {
        accettati = new ArrayList<Richiesta>();
        inAttesa = new ArrayList<Richiesta>();
    }


    public List<Richiesta> getAccettati() {
        return this.accettati;
    }
    
    public List<Richiesta> getInAttesa() {
        return this.inAttesa;
    }

    public void aggiungi(Richiesta richiesta) {
        inAttesa.add(richiesta);
    }

    public boolean accetta(Richiesta richiesta) {
        if (inAttesa.remove(richiesta)) {
            return accettati.add(richiesta);
        } else {
            return false;
        }
    }

    public boolean accetta(Integer idRichiesta) {
        int index = -1;

        for (int i = 0; i < inAttesa.size() && index < 0; i++) {
            if (inAttesa.get(i).getIdRichiesta().equals(idRichiesta)) {
                index = i;
            }
        }

        if (index < 0) {
            return false;
        }
        Richiesta richiesta = inAttesa.remove(index);
        return accettati.add(richiesta);
    }

    public boolean cancella(Richiesta richiesta) {
        return inAttesa.remove(richiesta) || accettati.remove(richiesta);
    }

    public boolean cancella(Integer idRichiesta) {
        Richiesta richiesta = null;

        for (int i = 0; i < inAttesa.size() && richiesta == null; i++) {
            if (inAttesa.get(i).getIdRichiesta().equals(idRichiesta)) {
                richiesta = inAttesa.get(i);
            }
        }
        for (int i = 0; i < inAttesa.size() && richiesta == null; i++) {
            if (accettati.get(i).getIdRichiesta().equals(idRichiesta)) {
                richiesta = accettati.get(i);
            }
        }

        if (richiesta != null) {
            return inAttesa.remove(richiesta) || accettati.remove(richiesta);
        } else {
            return false;
        }
    }
}
