package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.ProdottoOrdine;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;

@Service
public class OrdineGestService {
    @Autowired
    private OrdineGestRepository repo;

    @Autowired ProdottoRepository prodottoRepo;

    public List<Ordine> listAll() {

        // List<Ordine> result = repo.findAll();
        // for (Ordine ordine : result) {
        //     List<ProdottoOrdine> prodottoOrdini = repo.fetchQuantitaPerOrdine(ordine.getIdRichiesta());
        //     List<Prodotto> prodottiUnici = repo.fetchProdottiUniciDiOrdine(ordine.getIdRichiesta());

        //     for (Prodotto prodottoUnique : prodottiUnici) {
        //         for (ProdottoOrdine p : prodottoOrdini) {
        //             if (p.getId().getNumeroProdotto().equals(prodottoUnique.getNumero())) {
        //                 for (int i = 1; i < p.getQuantita(); i++) {
        //                     ordine.getProdotti().add(prodottoUnique);
        //                 }

        //             }

        //         }

        //     }

        // }

        // return result;
         return repo.findAll();
    }

    public void save(Ordine ordine) {
        repo.save(ordine);
    }

    public Ordine get(Integer id) {

        // return repo.fetchUno(id).get(0);
        Ordine ordine = repo.findById(id).get();

      //  List<ProdottoOrdine> prodottoOrdini = repo.fetchQuantitaPerOrdine(ordine.getIdRichiesta());
        List<Prodotto> prodottiUnici = prodottoRepo.fetchProdottiUniciDiOrdine(ordine.getIdRichiesta());
        ordine.setProdotti(prodottiUnici);

        // for (Prodotto prodottoUnique : prodottiUnici) {
            // for (ProdottoOrdine p : prodottoOrdini) {
            // if (p.getId().getNumeroProdotto().equals(prodottoUnique.getNumero())) {
            // for (int i = 1; i < p.getQuantita(); i++) {
            // ordine.getProdotti().add(prodottoUnique);
            // }

            // }

            // }
           // ordine.getProdotti().add(prodottoUnique);
        // }

        return ordine;
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

}
