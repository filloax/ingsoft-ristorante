package it.unibo.ingsoft.fortuna.prenotazione;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.PeriodiController;
import it.unibo.ingsoft.fortuna.model.attivazione.PeriodoDisattivazione;
import it.unibo.ingsoft.fortuna.model.attivazione.TipoDisattivazione;
import it.unibo.ingsoft.fortuna.model.richiesta.Prenotazione;

@Component
public class PrenotazioneController extends Controller implements IPrenotazioneController {
    private PeriodiController periodiDisattivazione;

    public PrenotazioneController() {
        periodiDisattivazione = PeriodiController.getInstance();
    }

    @Override
    public List<PeriodoDisattivazione> getPeriodiDisattivati() {
        return periodiDisattivazione.getPeriodi().getPeriodi().stream()
            .filter(periodo -> periodo.getTipo() == TipoDisattivazione.PRENOTAZIONE)
            .collect(Collectors.toList());
    }

    @Override
    public String creaPrenotazione(HttpServletRequest request, String nome, LocalDateTime dataOra, String telefono, int numeroPersone) {
        // Log prima di controllo, serve a evitare DOS
        scriviOperazione(request.getRemoteAddr(), String.format("creaPrenotazione(dataOra: %s)", dataOra.toString()));

        boolean dataValida = getPeriodiDisattivati().stream()
            .noneMatch(periodo -> (periodo.getInizio().isBefore(dataOra) || periodo.getInizio().equals(dataOra)) && periodo.getFine().isAfter(dataOra));
        if (!dataValida)
            return "err-data";

        Prenotazione prenotazione = new Prenotazione(nome, dataOra, telefono, numeroPersone); //crea comunque in caso di eventuali controlli del costruttore
        // try {
        //     prenotazione.setIdRichiesta(generaId());
        // } catch(Exception e) {
        //     e.printStackTrace();
        //     return "err-database-id";
        // }

        PreparedStatement preparedStmt = null; 

        try (Connection connection = getConnection()) {
            String query = "INSERT INTO prenotazioni (nome, telefono, numero_persone, data_ora) VALUES (?, ?, ?, ?)";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, prenotazione.getNominativo());
            preparedStmt.setString(2, prenotazione.getTelefono());
            preparedStmt.setInt(3, prenotazione.getNumeroPersone());
            preparedStmt.setTimestamp(4, Timestamp.valueOf(prenotazione.getDataOra()));
            preparedStmt.executeUpdate();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "err-database";
        } finally {
            try {
                if (preparedStmt != null) preparedStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "success";
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
            String query = "SELECT(NEXTVAL FOR prenotazione_id_seq) INTO newId";
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
            System.out.println("Generato ID: " + "P" + String.format("%5d", id));
            return "P" + String.format("%5d", id);
        } else {
            throw new Exception("ID invalido"); //opzionalmente sostituire con eccezione più appropriata
        }
    }
}
