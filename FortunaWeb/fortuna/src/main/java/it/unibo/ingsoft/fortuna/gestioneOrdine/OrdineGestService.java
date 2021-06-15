package it.unibo.ingsoft.fortuna.gestioneOrdine;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.DatabaseException;
import it.unibo.ingsoft.fortuna.ResourceUtilsLib;
import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.ProdottoOrdine;
import it.unibo.ingsoft.fortuna.model.richiesta.Ordine;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineAlTavolo;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineDomicilio;
import it.unibo.ingsoft.fortuna.model.richiesta.OrdineTakeAway;
import it.unibo.ingsoft.fortuna.sms.IInvioSms;
import it.unibo.ingsoft.fortuna.sms.SMSException;

@Service
public class OrdineGestService {
    @Autowired
    private OrdineGestRepository repo;

    @Autowired
    private IInvioSms sms;

    @Autowired
    private ProdottoRepository prodottoRepo;

    @Autowired
    private ProdottoOrdineRepository prodottoOrdineRepo;

    public List<Ordine> listAll() {

        List<Ordine> result = repo.findAll();
        for (Ordine ordine : result) {
            ordine.setProdotti(this.getListaProdotti(ordine.getIdRichiesta()));
        }
        return result;
    }

    public List<Ordine> listInAttesa() {
        return repo.findInAttesa();
    }

    public List<Ordine> listAccettati() {

        return repo.findAccettati();
    }

    public void save(Ordine ordine) {
        repo.save(ordine);
    }

    public Ordine get(Integer id) {

        Ordine ordine = repo.findById(id).get();
        ordine.setProdotti(this.getListaProdotti(ordine.getIdRichiesta()));
        return ordine;
    }

    private List<Prodotto> getListaProdotti(Integer ordineId) {
        List<ProdottoOrdine> prodottoOrdini = prodottoOrdineRepo.fetchQuantitaPerOrdine(ordineId);
        List<Prodotto> prodottiUnici = prodottoRepo.fetchProdottiUniciDiOrdine(ordineId);
        List<Prodotto> result = new ArrayList<>(prodottiUnici);

        for (Prodotto prodottoUnique : prodottiUnici) {
            for (ProdottoOrdine p : prodottoOrdini) {
                if (p.getId().getNumeroProdotto().equals(prodottoUnique.getNumero())) {
                    for (int i = 1; i < p.getQuantita(); i++) {
                        result.add(prodottoUnique);
                    }

                }

            }
        }
        return result;

    }

    // Definire il servizio Transactional per "validare" l'operazione a
    // hybernate/jpa, senza di
    // questo il metodo non ha permesso a eseguire aggiornamenti alle tabelle,
    // provocando eccezioni
    @Transactional
    public int accetta(Integer id) throws DatabaseException, SMSException, IOException {
        int rowsUpdated = repo.accetta(id);

        if (rowsUpdated == 1) {
            Ordine ordine = get(id);
            if (ordine instanceof OrdineDomicilio) {
                String msgTemplate = ResourceUtilsLib.loadResourceToString("/sms/prenot-accettata.txt");
                sms.inviaSMS(((OrdineDomicilio) ordine).getTelefono(),
                        String.format(msgTemplate,
                                ordine.getDataOra().format(
                                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)),
                                id));
            } else if (ordine instanceof OrdineTakeAway) {
                String msgTemplate = ResourceUtilsLib.loadResourceToString("/sms/prenot-accettata.txt");
                sms.inviaSMS(((OrdineTakeAway) ordine).getTelefono(),
                        String.format(msgTemplate,
                                ordine.getDataOra().format(
                                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)),
                                id));

            }

        } else {
            throw new DatabaseException("Aggiornato un numero inaspettato di righe con l'accettazione: " + id);
        }

        return rowsUpdated;
    }

    // @Transactional
    public void cancella(Integer id, String ragione) throws IOException, SMSException {
        Ordine ordine = get(id);
        // repo.deleteOrdine(id); TODO da sostituire
        repo.deleteById(id);

        if (!(ordine instanceof OrdineAlTavolo)) {
            String msgTemplate = ResourceUtilsLib.loadResourceToString("sms/prenot-accettata.txt");
            sms.inviaSMS(((OrdineDomicilio) ordine).getTelefono(), String.format(msgTemplate, ordine.getDataOra()
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)), id));
        }
    }

}
