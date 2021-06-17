package it.unibo.ingsoft.fortuna.gestioneordine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.unibo.ingsoft.fortuna.model.Prodotto;

public interface ProdottoRepository extends JpaRepository<Prodotto, Integer> {

    @Query(value = "SELECT * FROM prodotti p WHERE p.numero =ANY ( SELECT numero_prod FROM prodotti_ordinati WHERE id_ordine = ?1);", nativeQuery = true)
    List<Prodotto> fetchProdottiUniciDiOrdine(Integer ordineId);
}
