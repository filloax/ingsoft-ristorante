package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.ProdottoOrdine;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import org.springframework.data.jpa.repository.Query;

public interface OrdineGestRepository extends JpaRepository<Ordine, Integer> {

    @Query(value = "SELECT * FROM ordini", nativeQuery = true)
    List<Ordine> fetchTutti();


    @Query(value="SELECT * FROM ordini WHERE id=?1", nativeQuery = true)
    List<Ordine> fetchUno(Integer ordineId);

   

    @Query(value = "SELECT * FROM prodotti_ordinati WHERE id_ordine = ?1", nativeQuery = true)
    List<ProdottoOrdine> fetchQuantitaPerOrdine(Integer ordineId);
}
