package it.unibo.ingsoft.fortuna.sconti;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import it.unibo.ingsoft.fortuna.model.Sconto;
import lombok.Data;

@Data
public class DatiSconti {
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime end;
    private double quantita;
    private boolean isPercent = false;
    private double prezzoMin = 0;
    private Integer numeroProdotto;
    private Integer id; //usato solo per response

    public static DatiSconti fromSconto(Sconto sconto) {
        DatiSconti datiSconto = new DatiSconti();
        datiSconto.setStart(sconto.getInizio());
        datiSconto.setEnd(sconto.getFine());
        datiSconto.setPercent(sconto.getQuantitaPct() != null && sconto.getQuantitaPct() > 0);

        datiSconto.setQuantita(datiSconto.isPercent() ? sconto.getQuantitaPct() : sconto.getQuantita());
        if (sconto.getPerProdotti() != null)
            datiSconto.setNumeroProdotto(sconto.getPerProdotti().stream().map(Prodotto::getNumero).findFirst().get());
        datiSconto.setId(sconto.getId());

        return datiSconto;
    }
}
