package it.unibo.ingsoft.fortuna.sconti;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.AbstractService;
import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;
import it.unibo.ingsoft.fortuna.prodotti.IGestioneProdotti;

@Service
@Primary
public class GestioneSconti extends AbstractService implements IGestioneSconti {
    @Autowired
    private IGestioneProdotti gestioneProdotti;

    private Set<Sconto> sconti;

    public GestioneSconti() {
        sconti = new HashSet<Sconto>();
    }

    @PostConstruct
    private void init() throws DatabaseException {
        loadSconti();
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent)
            throws DatabaseException {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.of(start, end, quantita, 0, 0);
        else
            toAdd = Sconto.of(start, end, 0, quantita, 0);

        if (sconti.add(toAdd)) //se non era già presente
            try {
                aggiungiScontoDb(toAdd);
            } catch (DatabaseException e) {
                sconti.remove(toAdd);
                throw e;
            }

        return toAdd;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            double prezzoMinimo, Prodotto perProdotto) throws DatabaseException {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.ofProdotti(start, end, quantita, 0, prezzoMinimo, perProdotto);
        else
            toAdd = Sconto.ofProdotti(start, end, 0, quantita, prezzoMinimo, perProdotto);

        if (sconti.add(toAdd)) //se non era già presente
            try {
                aggiungiScontoDb(toAdd);
            } catch (DatabaseException e) {
                sconti.remove(toAdd);
                throw e;
            }

        return toAdd;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            Prodotto perProdotto) throws DatabaseException {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.ofProdotti(start, end, quantita, 0, 0, perProdotto);
        else
            toAdd = Sconto.ofProdotti(start, end, 0, quantita, 0, perProdotto);

        if (sconti.add(toAdd)) //se non era già presente
            try {
                aggiungiScontoDb(toAdd);
            } catch (DatabaseException e) {
                sconti.remove(toAdd);
                throw e;
            }

        return toAdd;
    }

    @Override
    public Sconto aggiungiSconto(LocalDateTime start, LocalDateTime end, double quantita, boolean isPercent,
            double prezzoMinimo) throws DatabaseException {
        Sconto toAdd = new Sconto();
        if (isPercent)
            toAdd = Sconto.of(start, end, quantita, 0, prezzoMinimo);
        else
            toAdd = Sconto.of(start, end, 0, quantita, prezzoMinimo);

        if (sconti.add(toAdd)) //se non era già presente
            try {
                aggiungiScontoDb(toAdd);
            } catch (DatabaseException e) {
                sconti.remove(toAdd);
                throw e;
            }

        return toAdd;
    }

    @Override
    public boolean rimuoviSconto(Sconto toRemove) throws DatabaseException {
        rimuoviScontoDB(toRemove);

        return sconti.remove(toRemove);
    }

    @Override
    public boolean rimuoviSconto(int id) throws DatabaseException {
        rimuoviScontoDB(id);

        return sconti.removeIf(sconto -> sconto.getId() == id);
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime inizio, LocalDateTime fine) {
        return sconti.stream().filter(sconto -> sconto.overlapsTempo(inizio, fine)).collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo) {
        return sconti.stream().filter(sconto -> sconto.isInTempo(tempo)).collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, Prodotto perProdotto) {
        return sconti.stream().filter(sconto -> sconto.isAttivo(tempo, perProdotto, 0)).collect(Collectors.toList());
    }

    @Override
    public List<Sconto> listaSconti(LocalDateTime tempo, double prezzoMin) {
        return sconti.stream().filter(sconto -> sconto.isAttivo(tempo, 0)).collect(Collectors.toList());
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

    private void loadSconti() throws DatabaseException {
        sconti.clear();

        String query = "SELECT * FROM sconti";

        try (Connection connection = getConnection();
                PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDateTime inizio = resultSet.getTimestamp("inizio").toLocalDateTime();
                LocalDateTime fine = resultSet.getTimestamp("fine").toLocalDateTime();

                Sconto sconto = new Sconto().inizio(inizio).fine(fine).id(id);

                if (resultSet.getBigDecimal("quantita") != null) {
                    sconto.setQuantita(resultSet.getBigDecimal("quantita").doubleValue());
                }
                if (resultSet.getBigDecimal("quantita_pct") != null) {
                    sconto.setQuantitaPct(resultSet.getBigDecimal("quantita_pct").doubleValue());
                }
                if (resultSet.getBigDecimal("costo_minimo") != null) {
                    sconto.setCostoMinimo(resultSet.getBigDecimal("costo_minimo").doubleValue());
                }

                String productsQuery = "SELECT numero_prod FROM prodotti_sconti WHERE id_sconto = " + id;

                try (PreparedStatement productsStmt = connection.prepareStatement(productsQuery)) {
                    ResultSet prodResultSet = productsStmt.executeQuery();
                    Set<Prodotto> prodotti = null;

                    while (prodResultSet.next()) {
                        int numero_prod = prodResultSet.getInt("numero_prod");
                        if (prodotti == null) prodotti = new HashSet<>();

                        Optional<Prodotto> prodotto = gestioneProdotti.listaProdotti().stream()
                            .filter(pr -> pr.getNumero() == numero_prod)
                            .findFirst();

                        if (!prodotto.isPresent())
                            throw new DatabaseException("Errore nel ritrovare prodotto con id " + numero_prod);

                        prodotti.add(prodotto.get());
                    }

                    if (prodotti != null) {
                        sconto.setPerProdotti(prodotti);
                    }
                }

                sconti.add(sconto);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }

        scriviMessaggio("Caricati sconti da DB con successo");
        System.out.println("Caricati: " + sconti.toString());
    }

    private void aggiungiScontoDb(Sconto sconto) throws DatabaseException {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO sconti (inizio, fine, quantita, quantita_pct, costo_minimo) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.setTimestamp(1, Timestamp.valueOf(sconto.getInizio()));
                preparedStmt.setTimestamp(2, Timestamp.valueOf(sconto.getFine()));
                preparedStmt.setBigDecimal(3, BigDecimal.valueOf(sconto.getQuantita()));
                preparedStmt.setBigDecimal(4, BigDecimal.valueOf(sconto.getQuantitaPct()));
                preparedStmt.setBigDecimal(5, BigDecimal.valueOf(sconto.getCostoMinimo()));

                preparedStmt.executeUpdate();
            }

            if (sconto.getPerProdotti() != null && !sconto.getPerProdotti().isEmpty()) {
                int id = -1;
                String idQuery = "SELECT LAST_INSERT_ID() AS last_id from sconti";
                try (PreparedStatement preparedStmt = connection.prepareStatement(idQuery)) {
                    ResultSet rs = preparedStmt.executeQuery();
                    if (rs.next()) {
                        id = rs.getInt("last_id");
                    }
                }
        
                if (id < 0)
                    throw new DatabaseException("Errore sconosciuti nell'ottenere l'id sconto");
                
                StringJoiner sj = new StringJoiner(", ");
                for (int i = 0; i < sconto.getPerProdotti().size(); i++) sj.add("(?, ?)");

                String queryProdotti = "INSERT INTO prodotti_sconti (id_sconto, numero_prod) VALUES " + sj.toString();
                try (PreparedStatement preparedStmt = connection.prepareStatement(queryProdotti)) {
                    int j = 0;
                    for (Prodotto prodotto : sconto.getPerProdotti()) {
                        preparedStmt.setInt(j + 1, id);
                        preparedStmt.setInt(j + 2, prodotto.getNumero());
                        j += 2;    
                    }
    
                    preparedStmt.executeUpdate();
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private void rimuoviScontoDB(Sconto sconto) throws DatabaseException {
        rimuoviSconto(sconto.getId());
    }

    private void rimuoviScontoDB(int id) throws DatabaseException {
        try (Connection connection = getConnection()) {
            // Non richiesto: on delete cascade
            // String queryProdotti = "DELETE FROM prodotti_sconti WHERE id_sconto = " + id;
            // try (PreparedStatement preparedStmt = connection.prepareStatement(queryProdotti)) {
            //     preparedStmt.executeUpdate();
            // }

            // TODO: fare in modo che rimangano gli sconti nei prodotti (flag dentro tabella sconti?)
            // se presenti dentro a prodotto, e in quel caso rimuoverli appena tutti i prodotti a cui
            // fanno riferimento vengono rimossi
            String queryOrdini = "DELETE FROM sconti_applicati WHERE id_sconto = " + id;
            try (PreparedStatement preparedStmt = connection.prepareStatement(queryOrdini)) {
                preparedStmt.executeUpdate();
            }

            String query = "DELETE FROM sconti WHERE id = " + id;
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.executeUpdate();
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
