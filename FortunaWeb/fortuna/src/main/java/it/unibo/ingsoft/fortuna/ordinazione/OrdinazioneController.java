package it.unibo.ingsoft.fortuna.ordinazione;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
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

        List<Sconto> scontiApplicabili = new ArrayList<>();
        scontiApplicabili.addAll(gestioneSconti.listaSconti(dataOra, ordine.calcolaCostoTotale()));
        for (Prodotto prodotto : prodotti) {
            scontiApplicabili.addAll(gestioneSconti.listaSconti(dataOra, ordine.calcolaCostoTotale(), prodotto));
        }
        ordine.setSconti(scontiApplicabili);
    }

    private boolean verificaTipo(TipoDisattivazione tipo) {
        return !getTipoOrdiniDisabilitati().contains(tipo);
    }

    // Controlla che nessuno degli ordini richiesti sia disabilitato o inesistente
    private boolean verificaProdotti(List<Prodotto> prodotti) {
        boolean contieneDisabilitato = prodotti.stream()
            .anyMatch(prodotto -> getProdottiDisabilitati().contains(prodotto));

        boolean contieneInesistente = !getMenu().containsAll(prodotti);

        return !contieneDisabilitato && !contieneInesistente;
    }

    private boolean verificaTavolo(String tavolo) {
        // TODO: controllare se tavolo esiste, è valido, ecc, funzionalità aggiuntiva probabilmente
        return true;
    }

    @Override
    public String creaOrdineTavolo(HttpServletRequest request, String nome, List<Prodotto> prodotti, String note,
            String tavolo) {
        scriviOperazione(request.getRemoteAddr(), String.format("creaOrdineTavolo(tavolo: %s)", tavolo));

        if (!verificaTipo(TipoDisattivazione.ORDINAZ_TAVOLO))
            return "err-tipo";
        if (!verificaProdotti(prodotti))
            return "err-prodotti";
        if (!verificaTavolo(tavolo))
            return "err-tavolo";

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
                return "err-database";
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "err-database";
        }

        return "success";
    }

    @Override
    public String creaOrdineDomicilio(HttpServletRequest request, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo) {
        return creaOrdineDomicilio(request, nome, prodotti, dataOra, note, telefono, indirizzo, "");
    }

    @Override
    public String creaOrdineDomicilio(HttpServletRequest request, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono, String indirizzo, String tokenPagamento) {
        scriviOperazione(request.getRemoteAddr(), String.format("creaOrdineDomicilio(dataOra: %s, pagamento: %s)", dataOra.toString(), tokenPagamento));

        if (!verificaTipo(TipoDisattivazione.ORDINAZ_DOMICILIO))
            return "err-tipo";
        if (!verificaProdotti(prodotti))
            return "err-prodotti";
    
        OrdineDomicilio ordine = new OrdineDomicilio();
        impostaOrdine(ordine, nome, prodotti, dataOra, note);

        if (!verificaZonaConsegna(indirizzo, ordine.calcolaCostoTotale()))
            return "err-zona";
    
        ordine.setTelefono(telefono);
        ordine.setIndirizzo(indirizzo);
        ordine.setTokenPagamento(tokenPagamento);

        try (Connection connection = getConnection()) {
            String query = "INSERT INTO ordini (nome, note, data_ora, telefono, indirizzo) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.setString(1, ordine.getNominativo());
                preparedStmt.setString(2, ordine.getNote());
                preparedStmt.setTimestamp(3, Timestamp.valueOf(ordine.getDataOra()));
                preparedStmt.setString(4, ordine.getTelefono());
                preparedStmt.setString(5, ordine.getIndirizzo());
    
                preparedStmt.executeUpdate();
            }
            
            if (!inserisciProdottiScontiInDb(connection, ordine)) {
                return "err-database";
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "err-database";
        }

        return "success";
    }

    @Override
    public String creaOrdineAsporto(HttpServletRequest request, String nome, List<Prodotto> prodotti, LocalDateTime dataOra, String note,
            String telefono) {
        scriviOperazione(request.getRemoteAddr(), String.format("creaOrdineAsporto(dataOra: %s)", dataOra.toString()));

        if (!verificaTipo(TipoDisattivazione.ORDINAZ_ASPORTO))
            return "err-tipo";
        if (!verificaProdotti(prodotti))
            return "err-prodotti";

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
                return "err-database";
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "err-database";
        }

        return "success";
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

    // Non utilizzato: mySql ha attributo auto_increment
    // che si occupa di generare ID unico quando viene
    // inserito un nuovo elemento alla tabella
    @SuppressWarnings("unused")
    private String generaId() throws SQLException, Exception {
        PreparedStatement statement = null;
        Connection connection = null;
        int id = -1;

        try {
            connection = getConnection();
            String query = "SELECT(NEXTVAL FOR ordine_id_seq) INTO newId";
            statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            if (result.next())
                id = result.getInt("newId");
            else
                throw new Exception("ID invalido"); //opzionalmente sostituire con eccezione più appropriata
        } catch (SQLException e) {
            throw e; //opzionalmente sostituire con eccezione più appropriata
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch(SQLException e) {
                throw e; //opzionalmente sostituire con eccezione più appropriata
            }
        }

        if (id >= 0) {
            return "O" + String.format("%5d", id);
        } else {
            throw new Exception("ID invalido"); //opzionalmente sostituire con eccezione più appropriata
        }
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
