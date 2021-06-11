package it.unibo.ingsoft.fortuna.sconti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class GestioneScontiMock implements IGestioneSconti {
    @Override
    public List<Sconto> listaScontiTotali() {
        ArrayList<Sconto> listaSconti = new ArrayList<Sconto>();
        listaSconti.add(Sconto.of(LocalDateTime.of(2021, 06, 05, 0, 0), LocalDateTime.of(2021, 06, 30, 23, 59), 0, 0.1, 0).id(1));
        listaSconti.add(Sconto.of(LocalDateTime.of(2021, 05, 05, 0, 0), LocalDateTime.of(2021, 06, 10, 23, 59), 1, 0, 4.5).id(2));
        listaSconti.add(Sconto.ofProdotti(LocalDateTime.of(2021, 06, 11, 0, 0), LocalDateTime.of(2021, 06, 21, 23, 59), 0, 0.6, 6, new Prodotto("Involtini", 102, 2.50)).id(3));

        return listaSconti;
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo) {
        return listaScontiTotali().subList(0, 1);
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, double prezzo) {
        return listaScontiTotali().subList(0, 1);
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, Prodotto perProdotto) {
        return listaScontiTotali().subList(2, 2);
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, double prezzo, Prodotto perProdotto) {
        return listaScontiTotali().subList(2, 2);
    }
    
    
    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent) {
        return null;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            double prezzoMin) {
        return null;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            Prodotto perProdotto) {
        return null;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            double prezzoMin, Prodotto perProdotto) {
        return null;
    }

    @Override
    public boolean rimuoviSconto(Sconto sconto) {
        return false;
    }


    @Override
    public List<Sconto> listaSconti(LocalDateTime inizioPeriodo, LocalDateTime finePeriodo) {
        // TODO Auto-generated method stub
        return null;
    }
}
