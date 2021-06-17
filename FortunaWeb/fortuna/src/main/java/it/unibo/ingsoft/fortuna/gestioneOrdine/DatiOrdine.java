package it.unibo.ingsoft.fortuna.gestioneordine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.richiesta.*;
import it.unibo.ingsoft.fortuna.sconti.DatiSconti;

import lombok.Data;

@Data
public class DatiOrdine {
    private String nominativo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataOra;
    private String note;
    private String telefono;
    private String indirizzo;
    private String tavolo;
    private Integer idRichiesta;
    private String tokenPagamento;
    private List<Integer> prodotti;
    private List<DatiSconti> sconti;

    public static DatiOrdine fromOrdine(Ordine ordine) {
        DatiOrdine dati = new DatiOrdine();

        dati.setNominativo(ordine.getNominativo());
        dati.setDataOra(ordine.getDataOra());
        dati.setIdRichiesta(ordine.getIdRichiesta());
        dati.setNote(ordine.getNote());
        dati.setProdotti(ordine.getProdotti().stream().map(Prodotto::getNumero).collect(Collectors.toList()));
        dati.setSconti(ordine.getSconti().stream().map(s -> DatiSconti.fromSconto(s)).collect(Collectors.toList()));

        if (ordine instanceof OrdineAlTavolo) {
            dati.setTavolo(((OrdineAlTavolo) ordine).getTavolo());
        } else if (ordine instanceof OrdineDomicilio) {
            dati.setTelefono(((OrdineDomicilio) ordine).getTelefono());
            dati.setIndirizzo(((OrdineDomicilio) ordine).getIndirizzo());
            dati.setTokenPagamento(((OrdineDomicilio) ordine).getTokenPagamento());
        } else if (ordine instanceof OrdineTakeAway) {
            dati.setTelefono(((OrdineTakeAway) ordine).getTelefono());
        }

        return dati;
    }
}
