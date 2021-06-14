package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;

@Service
public class OrdineGestService {
    @Autowired
    private OrdineGestRepository repo;

    public List<Ordine> listAll() {

        // List<Ordine> result = repo.fetchTutti();
        // Integer quantita;
        // for (Ordine ordine : result) {
        // List<Prodotto> prodottiUnici =
        // repo.fetchProdottiUniciDiOrdine(ordine.getIdRichiesta());
        // for (Prodotto prodotto : prodottiUnici) {
        // quantita = repo.fetchQuantitaPerProdottoOrdine(ordine.getIdRichiesta(),
        // prodotto.getNumero())
        // .getQuantita();
        // for (int i = 1; i < quantita; i++) {
        // ordine.getProdotti().add(prodotto);
        // }
        // }

        // }

        // return result;
        return repo.findAll();
    }

    public void save(Ordine ordine) {
        repo.save(ordine);
    }

    public Ordine get(Integer id) {

        // return repo.fetchUno(id).get(0);
        return repo.findById(id).get();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

}
