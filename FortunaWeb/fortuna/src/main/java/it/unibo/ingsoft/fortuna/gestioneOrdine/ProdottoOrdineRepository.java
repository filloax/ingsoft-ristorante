package it.unibo.ingsoft.fortuna.gestioneordine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.unibo.ingsoft.fortuna.model.ProdottoOrdine;
import it.unibo.ingsoft.fortuna.model.ProdottoOrdineChiave;

public interface ProdottoOrdineRepository extends JpaRepository<ProdottoOrdine, ProdottoOrdineChiave> {

    @Query(value = "SELECT * FROM prodotti_ordinati WHERE id_ordine = ?1", nativeQuery = true)
    List<ProdottoOrdine> fetchQuantitaPerOrdine(Integer ordineId);
}
