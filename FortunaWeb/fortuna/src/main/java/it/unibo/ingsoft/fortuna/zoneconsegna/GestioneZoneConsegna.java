package it.unibo.ingsoft.fortuna.zoneconsegna;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.IZonaConsegna;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.Vector;
import it.unibo.ingsoft.fortuna.model.zonaconsegna.ZonaConsegnaPunti;

@Component
@Primary
public class GestioneZoneConsegna extends Controller implements IGestioneZoneConsegna, IListaZoneConsegna {

    private Set<ZonaConsegnaPunti> zone;

    public GestioneZoneConsegna() {
        zone = new HashSet<ZonaConsegnaPunti>();
    }

    @PostConstruct
    private void init() throws DatabaseException {
        load();
    }

    @Override
    public ZonaConsegnaPunti aggiungiZonaConsegna(List<Vector> punti, double prezzoMinimo) throws DatabaseException {
        ZonaConsegnaPunti toAdd = new ZonaConsegnaPunti(prezzoMinimo, punti);
        aggiungiDb(toAdd);
        zone.add(toAdd);

        return toAdd;
    }
    
    @Override
    public boolean rimuoviZonaConsegna(ZonaConsegnaPunti toRemove) throws DatabaseException {
        rimuoviDb(toRemove);
        zone.remove(toRemove);

        return true;
    }

    @Override
    public List<IZonaConsegna> listaZoneConsegna() {
        return List.copyOf(zone);
    }

    
    private void load() throws DatabaseException {
        zone.clear();

        String query = "SELECT * FROM zona_consegna";

        try (Connection connection = getConnection();
                PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double prezzoMinimo = resultSet.getBigDecimal("prezzo_minimo").doubleValue();

                ZonaConsegnaPunti zona = new ZonaConsegnaPunti().id(id).prezzoMinimo(prezzoMinimo);

                String pointsQuery = "SELECT latitudine, longitudine, id_lista FROM zona_consegna_punti WHERE id = " + id;

                try (PreparedStatement subStatement = connection.prepareStatement(pointsQuery)) {
                    ResultSet subResultSet = subStatement.executeQuery();
                    
                    Map<Integer, Vector> puntiOrderMap = new HashMap<>();
                    List<Vector> punti = new ArrayList<>();

                    while (subResultSet.next()) {
                        double lat = subResultSet.getBigDecimal("latitudine").doubleValue();
                        double lon = subResultSet.getBigDecimal("longitudine").doubleValue();
                        int listOrder = subResultSet.getInt("id_lista");

                        if (subResultSet.wasNull()) { // ordine non prefissato
                            if (!puntiOrderMap.isEmpty())
                                throw new DatabaseException("Zona consegna " + id + ": mix di punti con ordine prefissato e non!");
                            punti.add(new Vector(lat, lon));
                        } else { // ordine prefissato
                            if (!punti.isEmpty())
                                throw new DatabaseException("Zona consegna " + id + ": mix di punti con ordine prefissato e non!");
                            puntiOrderMap.put(listOrder, new Vector(lat, lon));
                        }
                    }

                    if (!puntiOrderMap.isEmpty()) {
                        // Trasforma mappa <int, valore> in lista ordinata secondo int
                        punti = puntiOrderMap.entrySet().stream()
                            .sorted(Comparator.comparing(Entry::getKey))
                            .map(Entry::getValue)
                            .collect(Collectors.toList());
                    }

                    if (punti.isEmpty()) {
                        scriviMessaggio(String.format("WARN: Caricata zona di consegna con id %s e prezzo minimo %f senza punti", id, prezzoMinimo));
                    } else {
                        zona.setPunti(punti);
                        zone.add(zona);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }

        scriviMessaggio("Caricate zone da DB con successo");
        System.out.println("Caricati: " + zone.toString());
    }

    private void aggiungiDb(ZonaConsegnaPunti zona) throws DatabaseException {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO zona_consegna (prezzo_minimo) VALUES (?, ?)";
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.setBigDecimal(1, BigDecimal.valueOf(zona.getPrezzoMinimo()));

                preparedStmt.executeUpdate();
            }

            int id = -1;
            String idQuery = "SELECT LAST_INSERT_ID() AS last_id from zona_consegna";
            try (PreparedStatement preparedStmt = connection.prepareStatement(idQuery)) {
                ResultSet rs = preparedStmt.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("last_id");
                }
            }
    
            if (id < 0)
                throw new DatabaseException("Errore sconosciuti nell'ottenere l'id zona");
            
            StringJoiner sj = new StringJoiner(", ");
            for (int i = 0; i < zona.getPunti().size(); i++) sj.add("(?, ?, ?, ?)");

            String subQuery = "INSERT INTO zona_consegna_punti (id, latitudine, longitudine, id_lista) VALUES " + sj.toString();
            try (PreparedStatement preparedStmt = connection.prepareStatement(subQuery)) {
                for (int i = 0, j = 0; i < zona.getPunti().size(); i++, j+=4) {
                    preparedStmt.setInt       (j + 1, id);
                    preparedStmt.setBigDecimal(j + 2, BigDecimal.valueOf(zona.getPunti().get(i).getX()));
                    preparedStmt.setBigDecimal(j + 3, BigDecimal.valueOf(zona.getPunti().get(i).getY()));
                    preparedStmt.setInt       (j + 4, i);
                }

                preparedStmt.executeUpdate();
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private void rimuoviDb(ZonaConsegnaPunti zona) throws DatabaseException {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM zona_consegna_punti WHERE id = " + zona.getId();
            try (PreparedStatement preparedStmt = connection.prepareStatement(query)) {
                preparedStmt.executeUpdate();
            }

            String query2 = "DELETE FROM zona_consegna WHERE id = " + zona.getId();
            try (PreparedStatement preparedStmt = connection.prepareStatement(query2)) {
                preparedStmt.executeUpdate();
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
