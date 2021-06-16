package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.ProdottoOrdine;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import org.springframework.data.jpa.repository.Query;

public interface OrdineGestRepository extends JpaRepository<Ordine, Integer> {

    @Query(value = "SELECT * FROM ordini", nativeQuery = true)
    List<Ordine> fetchTutti();

    @Query(value = "SELECT * FROM ordini WHERE id=?1", nativeQuery = true)
    List<Ordine> fetchUno(Integer ordineId);

    // TODO workaround , dovrei gestire le dipendenze nelle molti a molti di sconti
    // e prodotti
    @Modifying
    @Query(value = "DELETE FROM ordini WHERE id=?1", nativeQuery = true)
    void deleteOrdine(Integer ordineId);

    @Query(value = "SELECT * FROM ordini o WHERE o.accettato IS NULL", nativeQuery = true)
    List<Ordine> findInAttesa();

    @Query(value = "SELECT * FROM ordini o WHERE o.accettato = 'Y'", nativeQuery = true)
    List<Ordine> findAccettati();

    @Modifying
    @Query(value = "UPDATE ordini o SET o.accettato='Y' WHERE o.id =?1", nativeQuery = true)
    int accetta(Integer id);

}
