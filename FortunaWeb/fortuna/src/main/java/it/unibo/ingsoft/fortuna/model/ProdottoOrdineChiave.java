package it.unibo.ingsoft.fortuna.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProdottoOrdineChiave implements Serializable {

    @Column(name = "id_ordine")
    private Integer ordineId;
    @Column(name = "numero_prod")
    private Integer numeroProdotto;

    public ProdottoOrdineChiave() {

    }

    public Integer getNumeroProdotto() {
        return numeroProdotto;
    }

    public Integer getOrdineId() {
        return ordineId;
    }

    public void setNumeroProdotto(Integer numeroProdotto) {
        this.numeroProdotto = numeroProdotto;
    }

    public void setOrdineId(Integer ordineId) {
        this.ordineId = ordineId;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ProdottoOrdineChiave) {
            ProdottoOrdineChiave other = (ProdottoOrdineChiave) obj;
            return other.getNumeroProdotto().equals(this.getNumeroProdotto())
                    && other.getOrdineId().equals(this.getOrdineId());
        }
        return false;
    }
}
