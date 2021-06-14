package it.unibo.ingsoft.fortuna.ordinazione;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.unibo.ingsoft.fortuna.model.*;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;
import it.unibo.ingsoft.fortuna.model.richiesta.*;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaException;

public interface IOrdinazioneService {
    public Set<TipoDisattivazione> getTipoOrdiniDisabilitati();
    public Set<Prodotto> getProdottiDisabilitati();

    public boolean verificaZonaConsegna(String indirizzo, double costo) throws ZonaConsegnaException;

    public List<Prodotto> getMenu();
    public List<Sconto> getSconti();

    /**
     * @param prodotti
     * @param dataOra <optional>
     * @return sconti applicabili a prodotti e data-ora se è impostata, altrimenti tutte le date
     */
    public List<Sconto> getScontiApplicabili(List<Prodotto> prodotti, Optional<LocalDateTime> dataOra);
    public double calcolaTotale(List<Prodotto> prodotti);
    public double calcolaTotaleScontato(List<Prodotto> prodotti, LocalDateTime dataOra);

    public OrdineAlTavolo creaOrdineTavolo(String nome, List<Prodotto> prodotti, 
        String note, String tavolo) throws IOException;
    public OrdineDomicilio creaOrdineDomicilio(String nome, List<Prodotto> prodotti, 
        LocalDateTime dataOra, String note, String telefono, String indirizzo) throws IOException;
    public OrdineDomicilio creaOrdineDomicilio(String nome, List<Prodotto> prodotti, 
        LocalDateTime dataOra, String note, String telefono, String indirizzo, String tokenPagamento) throws IOException;
    public OrdineTakeAway creaOrdineAsporto(String nome, List<Prodotto> prodotti, 
        LocalDateTime dataOra, String note, String telefono) throws IOException;
}
