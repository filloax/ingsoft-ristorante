package it.unibo.ingsoft.fortuna.sconti;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;

@Component
@Primary
public class GestioneSconti extends Controller implements IGestioneSconti {

    private Set<Sconto> sconti;

    public GestioneSconti() {
        sconti = new HashSet<Sconto>();
        // scarica "sconti" da database
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita,
            boolean isPercent) {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.of(start, end, quantita, 0, 0);
        else
            toAdd = Sconto.of(start, end, 0, quantita, 0);

        sconti.add(toAdd);
        // aggiunta di toAdd al DB

        return toAdd;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita,
            boolean isPercent, double prezzoMinimo, Prodotto perProdotto) {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.ofProdotti(start, end, quantita, 0, prezzoMinimo, perProdotto);
        else
            toAdd = Sconto.ofProdotti(start, end, 0, quantita, prezzoMinimo, perProdotto);

        // aggiunta di toAdd al DB
        sconti.add(toAdd);

        return toAdd;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita,
            boolean isPercent, Prodotto perProdotto) {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.ofProdotti(start, end, quantita, 0, 0, perProdotto);
        else
            toAdd = Sconto.ofProdotti(start, end, 0, quantita, 0, perProdotto);

        // aggiunta di toAdd al DB
        sconti.add(toAdd);

        return toAdd;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita,
            boolean isPercent, double prezzoMinimo) {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.of(start, end, quantita, 0, prezzoMinimo);
        else
            toAdd = Sconto.of(start, end, 0, quantita, prezzoMinimo);

        sconti.add(toAdd);
        // aggiunta di toAdd al DB

        return toAdd;
    }

    @Override
    public boolean rimuoviSconto(Sconto toRemove) {
        sconti.remove(toRemove);
        // rimuovi toRemove dal DB

        return true;
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime inizio, LocalDateTime fine) {
        return sconti.stream().filter(sconto -> sconto.overlapsTempo(inizio, fine))
                .collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo) {
        return sconti.stream().filter(sconto -> sconto.isInTempo(tempo))
                .collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, Prodotto perProdotto) {
        return sconti.stream().filter(sconto -> sconto.isAttivo(tempo, perProdotto, 0))
                .collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, double prezzoMin) {
        return sconti.stream().filter(sconto -> sconto.isAttivo(tempo, 0))
                .collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, double prezzoMin, Prodotto perProdotto) {
        return sconti.stream().filter(sconto -> sconto.isAttivo(tempo, perProdotto, prezzoMin))
                .collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaScontiTotali() {
        return List.copyOf(sconti);
    }

}
