package it.unibo.ingsoft.fortuna.prodotti;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.Controller;
import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.model.Prodotto;

@Component
@Primary
public class GestioneProdotti extends Controller implements IGestioneProdotti {

    private List<Prodotto> menu;

    public GestioneProdotti() {
        menu = new ArrayList<>();
    }

    @PostConstruct
    private void init() throws DatabaseException {
        loadMenu();
    }

    @Override
    public Prodotto aggiungiProdotto(String nome, int numero, double prezzo, String descrizione, String immagine) throws DatabaseException {
        Prodotto toAdd;
        toAdd = new Prodotto(nome, numero, prezzo, descrizione, immagine);
        aggiungiProdottoDb(toAdd);
        menu.add(toAdd);

        return toAdd;
    }

    @Override
    public boolean rimuoviProdotto(Prodotto prod) throws DatabaseException {
        rimuoviProdottoDB(prod);
        menu.remove(prod);
        
        return true;
    }

    @Override
    public List<Prodotto> listaProdotti() {
        return menu;
    }
    
    private void loadMenu() throws DatabaseException {
        menu.clear();

        String query = "SELECT * FROM prodotti";

        try (Connection connection = getConnection();
                PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int numero = resultSet.getInt("numero");
                String nome = resultSet.getString("nome");
                String descrizione = resultSet.getString("descrizione");
                double prezzo = resultSet.getBigDecimal("prezzo").doubleValue();
                String immagine = resultSet.getString("immagine");
                if (immagine == null) immagine = "";

                Prodotto prodotto = new Prodotto().numero(numero).nome(nome).desc(descrizione).prezzo(prezzo).img(immagine);

                menu.add(prodotto);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }

        scriviMessaggio("Caricato menu da DB con successo");
        System.out.println("Menu: " + menu.toString());
    }

    private void aggiungiProdottoDb(Prodotto prodotto) throws DatabaseException {
        String query = "INSERT INTO prodotti (numero, nome, descrizione, prezzo, immagine) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            preparedStmt.setInt(1, prodotto.getNumero());
            preparedStmt.setString(2, prodotto.getNome());
            preparedStmt.setString(3, prodotto.getDesc());
            preparedStmt.setBigDecimal(4, BigDecimal.valueOf(prodotto.getPrezzo()));
            preparedStmt.setString(5, prodotto.getImg());

            preparedStmt.executeUpdate();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private void rimuoviProdottoDB(Prodotto prodotto) throws DatabaseException {
        String query = "DELETE FROM prodotti WHERE id = " + prodotto.getNumero();
        try (Connection connection = getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(query)) {
            preparedStmt.executeUpdate();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
