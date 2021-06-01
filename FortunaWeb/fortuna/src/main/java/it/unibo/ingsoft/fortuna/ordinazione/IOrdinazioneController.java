package it.unibo.ingsoft.fortuna.ordinazione;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import it.unibo.ingsoft.fortuna.model.*;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;
import it.unibo.ingsoft.fortuna.model.richiesta.*;

public interface IOrdinazioneController {
    public Set<TipoDisattivazione> getTipoOrdiniDisabilitati();
    public Set<Prodotto> getProdottiDisabilitati();

    public boolean verificaZonaConsegna(String indirizzo, double costo);

    public List<Prodotto> getMenu();
    public List<Sconto> getSconti();

    public OrdineAlTavolo creaOrdineTavolo(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note, String tavolo);
    public OrdineDomicilio creaOrdineDomicilio(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note, String telefono, String indirizzo);
    public OrdineDomicilio creaOrdineDomicilio(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note, String telefono, String indirizzo, String tokenPagamento);
    public OrdineTakeAway creaOrdineAsporto(String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note, String telefono);
}
