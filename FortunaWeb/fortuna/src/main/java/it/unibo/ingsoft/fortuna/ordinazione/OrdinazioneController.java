package it.unibo.ingsoft.fortuna.ordinazione;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.PeriodiController;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineAlTavolo;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineDomicilio;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineTakeAway;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.prodotti.IGestioneProdotti;
import it.unibo.ingsoft.fortuna.sconti.IGestioneSconti;
import it.unibo.ingsoft.fortuna.zoneconsegna.IGestioneZoneConsegna;

@Service
public class OrdinazioneController extends Controller implements IOrdinazioneController {
    private PeriodiController periodiDisattivazione;

    // Campi Autowired inseriti automaticamente da Spring Boot a partire da classi
    // marcate con @Component del tipo corrispondente
    @Autowired
    private IGestioneProdotti gestioneProdotti;

    @Autowired
    private IGestioneSconti gestioneSconti;

    @Autowired
    private IGestioneZoneConsegna gestioneZoneConsegna;

    @Autowired
    private IPagamentoOnline pagamentoOnline;

    public OrdinazioneController() {
        periodiDisattivazione = PeriodiController.getInstance();
    }

    @Override
    public Set<TipoDisattivazione> getTipoOrdiniDisabilitati() {
        Set<TipoDisattivazione> tipiDisattivati = periodiDisattivazione.getPeriodi().getTipiDisattivati(LocalDateTime.now());
        tipiDisattivati.remove(TipoDisattivazione.PRENOTAZIONE);
        tipiDisattivati.remove(TipoDisattivazione.PRODOTTO);
        return tipiDisattivati;
    }

    @Override
    public Set<Prodotto> getProdottiDisabilitati() {
        return periodiDisattivazione.getPeriodi().getProdottiDisattivati(LocalDateTime.now());
    }

    @Override
    public List<Prodotto> getMenu() {
        return gestioneProdotti.listaProdotti();
    }

    @Override
    public List<Sconto> getSconti() {
        return gestioneSconti.listaScontiTotali();
    }

    @Override
    public boolean verificaZonaConsegna(String indirizzo, double costo) {
        List<IZonaConsegna> zoneConsegna = gestioneZoneConsegna.listaZoneConsegna();

        for (IZonaConsegna zonaConsegna : zoneConsegna) {
            if (zonaConsegna.include(indirizzo, costo)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Sconto> getScontiApplicabili(List<Prodotto> prodotti, Optional<LocalDateTime> dataOra) {
        List<Sconto> scontiApplicabili = new ArrayList<>();
        
        if (dataOra.isPresent()) {
            scontiApplicabili.addAll(gestioneSconti.listaSconti(dataOra.get(), calcolaTotale(prodotti)));
            for (Prodotto prodotto : prodotti) {
                scontiApplicabili.addAll(gestioneSconti.listaSconti(dataOra.get(), calcolaTotale(prodotti), prodotto));
            }
        } else {
            scontiApplicabili.addAll(gestioneSconti.listaScontiTotali().stream()
                .filter(sconto -> sconto.getPerProdotti() == null || sconto.getPerProdotti().isEmpty())
                .collect(Collectors.toList()));
            for (Prodotto prodotto : prodotti) {
                scontiApplicabili.addAll(gestioneSconti.listaScontiTotali().stream()
                    .filter(sconto -> sconto.getPerProdotti() != null || sconto.getPerProdotti().contains(prodotto))
                    .collect(Collectors.toList()));
            }
    
        }

        return scontiApplicabili;
    }

    @Override
    public double calcolaTotaleScontato(List<Prodotto> prodotti, LocalDateTime dataOra) {
        // Agisci creando ordine e facendo calcolare a lui, visto che ha già la logica
        // Al tavolo è arbitrario, visto che Ordine è astratta come classe
        Ordine tempOrdine = new OrdineAlTavolo()
            .dataOra(dataOra)
            .prodotti(prodotti)
            .sconti(getScontiApplicabili(prodotti, Optional.of(dataOra)));

        return tempOrdine.calcolaCostoScontato();
    }

    @Override
    public double calcolaTotale(List<Prodotto> prodotti) {
        if (prodotti.isEmpty())
            return 0.;
        return prodotti.stream().map(p -> p.getPrezzo()).reduce((a, b) -> a + b).get();
    }

    private void impostaOrdine(Ordine ordine, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note) {
        ordine.nominativo(nome)
            .prodotti(prodotti)
            .dataOra(dataOra)
            .note(note);
        
        // try {
        //     ordine.setIdRichiesta(generaId());
        // } catch (SQLException e) {
        //     e.printStackTrace();
        //     return false;
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return false;
        // }

        ordine.setSconti(getScontiApplicabili(prodotti, Optional.of(dataOra)));
    }

    private boolean verificaTipo(TipoDisattivazione tipo) {
        return !getTipoOrdiniDisabilitati().contains(tipo);
    }

    // Controlla che nessuno degli ordini richiesti sia disabilitato o inesistente
    /**
     * @param prodotti
     * @return prodotti invalidi
     */
    private boolean verificaProdotti(List<Prodotto> prodotti) {
        return getProdottiInvalidi(prodotti).isEmpty();
    }

    private List<Prodotto> getProdottiInvalidi(List<Prodotto> prodotti) {
        Stream<Prodotto> disabilitati = prodotti.stream()
            .filter(prodotto -> getProdottiDisabilitati().contains(prodotto));

        Stream<Prodotto> inesistenti = prodotti.stream()
            .filter(prodotto -> !getMenu().contains(prodotto));

        return Stream.concat(disabilitati, inesistenti).collect(Collectors.toList());
    }

    private boolean verificaTavolo(String tavolo) {
        // TODO: controllare se tavolo esiste, è valido, ecc, funzionalità aggiuntiva probabilmente
        return true;
    }

    @Override
    public OrdineAlTavolo creaOrdineTavolo(HttpServletRequest request, String nome, List<Prodotto> prodotti, String note,
            String tavolo) throws IOException {
        scriviOperazione(request.getRemoteAddr(), String.format("creaOrdineTavolo(tavolo: %s)", tavolo));

        if (!verificaTipo(TipoDisattivazione.ORDINAZ_TAVOLO))
            throw new IllegalStateException(String.format("Tipo ordinazione non attivo: %s!", TipoDisattivazione.ORDINAZ_TAVOLO));
        if (!verificaProdotti(prodotti))
            throw new IllegalArgumentException(String.format("Non tutti i prodotti sono validi: %s!", getProdottiInvalidi(prodotti)));
        if (!verificaTavolo(tavolo))
            throw new IllegalArgumentException(String.format("Tavolo non valido: %s!", tavolo));

        OrdineAlTavolo ordine = new OrdineAlTavolo();
        impostaOrdine(ordine, nome, prodotti, LocalDateTime.now(), note);

        ordine.setTavolo(tavolo);

        try (Connection connection = getConnection()) {
            String query = "INSERT INTO ordini (nome, note, data_ora, tavolo) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.setString(1, ordine.getNominativo());
                preparedStmt.setString(2, ordine.getNote());
                preparedStmt.setTimestamp(3, Timestamp.valueOf(ordine.getDataOra()));
                preparedStmt.setString(4, ordine.getTavolo());
                
                preparedStmt.executeUpdate();
            }

            if (!inserisciProdottiScontiInDb(connection, ordine)) {
                throw new IOException("Accesso al database fallito");
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new IOException("Accesso al database fallito");
        }

        return ordine;
    }

    @Override
    public OrdineDomicilio creaOrdineDomicilio(HttpServletRequest request, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo) throws IOException {
        return creaOrdineDomicilio(request, nome, prodotti, dataOra, note, telefono, indirizzo, "");
    }

    @Override
    public OrdineDomicilio creaOrdineDomicilio(HttpServletRequest request, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo, String tokenPagamento) throws IOException {
        scriviOperazione(request.getRemoteAddr(), String.format("creaOrdineDomicilio(dataOra: %s, pagamento: %s)", dataOra.toString(), tokenPagamento));

        if (!verificaTipo(TipoDisattivazione.ORDINAZ_DOMICILIO))
            throw new IllegalStateException(String.format("Tipo ordinazione non attivo: %s", TipoDisattivazione.ORDINAZ_DOMICILIO));
        if (!verificaProdotti(prodotti))
            throw new IllegalArgumentException(String.format("Non tutti i prodotti sono validi: %s", getProdottiInvalidi(prodotti)));

        OrdineDomicilio ordine = new OrdineDomicilio();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);

        if (!verificaZonaConsegna(indirizzo, ordine.calcolaCostoTotale()))
            throw new IllegalArgumentException(String.format("Indirizzo non valido (a questa fascia di prezzo?): %s", indirizzo));
    
        ordine.setTelefono(telefono);
        ordine.setIndirizzo(indirizzo);
        ordine.setTokenPagamento(tokenPagamento);
    
        boolean pagamentoValido = false;
        try {
            pagamentoValido = tokenPagamento.isEmpty() || pagamentoOnline.verificaAutorizzazione(ordine);
        } catch (PaymentException e) {
            throw new IOException("Errore di comunicazione con servizio di pagamento", e);
        }
        if (!pagamentoValido)
            throw new IllegalArgumentException("Token pagamento non valido!");

        try (Connection connection = getConnection()) {
            String query = tokenPagamento.isEmpty() 
                ? "INSERT INTO ordini (nome, note, data_ora, telefono, indirizzo) VALUES (?, ?, ?, ?, ?)"
                : "INSERT INTO ordini (nome, note, data_ora, telefono, indirizzo, pagamento) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.setString(1, ordine.getNominativo());
                preparedStmt.setString(2, ordine.getNote());
                preparedStmt.setTimestamp(3, Timestamp.valueOf(ordine.getDataOra()));
                preparedStmt.setString(4, ordine.getTelefono());
                preparedStmt.setString(5, ordine.getIndirizzo());
                if (!tokenPagamento.isEmpty())
                    preparedStmt.setString(6, tokenPagamento);

                preparedStmt.executeUpdate();
            }
            
            if (!inserisciProdottiScontiInDb(connection, ordine)) {
                throw new IOException("Accesso al database fallito");
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new IOException("Accesso al database fallito");
        }

        return ordine;
    }

    @Override
    public OrdineTakeAway creaOrdineAsporto(HttpServletRequest request, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono) throws IOException {
        scriviOperazione(request.getRemoteAddr(), String.format("creaOrdineAsporto(dataOra: %s)", dataOra.toString()));

        if (!verificaTipo(TipoDisattivazione.ORDINAZ_ASPORTO))
            throw new IllegalStateException(String.format("Tipo ordinazione non attivo: %s!", TipoDisattivazione.ORDINAZ_ASPORTO));
        if (!verificaProdotti(prodotti))
            throw new IllegalArgumentException(String.format("Non tutti i prodotti sono validi: %s!", getProdottiInvalidi(prodotti)));

        OrdineTakeAway ordine = new OrdineTakeAway();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);

        ordine.setTelefono(telefono);

        try (Connection connection = getConnection()) {
            String query = "INSERT INTO ordini (nome, note, data_ora, telefono) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.setString(1, ordine.getNominativo());
                preparedStmt.setString(2, ordine.getNote());
                preparedStmt.setTimestamp(3, Timestamp.valueOf(ordine.getDataOra()));
                preparedStmt.setString(4, ordine.getTelefono());

                preparedStmt.executeUpdate();
            }

            if (!inserisciProdottiScontiInDb(connection, ordine)) {
                throw new IOException("Accesso al database fallito");
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new IOException("Accesso al database fallito");
        }

        return ordine;
    }

    // TODO: rimuovi ordine se non va tutto a buon fine, oppure usa transazioni
    private boolean inserisciProdottiScontiInDb(Connection connection, Ordine ordine) throws SQLException {
        int id = -1;
        String query = "SELECT LAST_INSERT_ID() AS last_id from ordini";
        try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("last_id");
            }
        }

        if (id < 0)
            return false;

        StringJoiner sj = new StringJoiner(", ");
        for (int i = 0; i < ordine.getProdotti().size(); i++) sj.add("(?, ?, 1)");

        query = "INSERT INTO prodotti_ordinati (id_ordine, numero_prod, quantita) VALUES "
        + sj.toString()
        + " ON DUPLICATE KEY UPDATE quantita=quantita+1";

        try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            int i = 1;
            for (Prodotto prodotto : ordine.getProdotti()) {
                preparedStmt.setInt(i, id);
                i++;
                preparedStmt.setInt(i, prodotto.getNumero());    
                i++;        
            }

            preparedStmt.executeUpdate();
        }

        sj = new StringJoiner(", ");
        for (int i = 0; i < ordine.getSconti().size(); i++) sj.add("(?, ?)");

        query = "INSERT INTO sconti_applicati (id_ordine, id_sconto) VALUES"
        + sj.toString();

        try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            int i = 1;
            for (Sconto sconto : ordine.getSconti()) {
                preparedStmt.setInt(i, id);
                i++;
                preparedStmt.setInt(i, sconto.getId());
                i++;
            }
            
            preparedStmt.executeUpdate();
        }


        return true;
    }

    
    public PeriodiController getPeriodiDisattivazione() {
        return this.periodiDisattivazione;
    }

    public void setPeriodiDisattivazione(PeriodiController periodiDisattivazione) {
        this.periodiDisattivazione = periodiDisattivazione;
    }

    public IGestioneProdotti getGestioneProdotti() {
        return this.gestioneProdotti;
    }

    public void setGestioneProdotti(IGestioneProdotti gestioneProdotti) {
        this.gestioneProdotti = gestioneProdotti;
    }

    public IGestioneSconti getGestioneSconti() {
        return this.gestioneSconti;
    }

    public void setGestioneSconti(IGestioneSconti gestioneSconti) {
        this.gestioneSconti = gestioneSconti;
    }

    public IGestioneZoneConsegna getGestioneZoneConsegna() {
        return this.gestioneZoneConsegna;
    }

    public void setGestioneZoneConsegna(IGestioneZoneConsegna gestioneZoneConsegna) {
        this.gestioneZoneConsegna = gestioneZoneConsegna;
    }
}
