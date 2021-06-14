package it.unibo.ingsoft.fortuna.model.richiesta;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "ordine_al_tavolo")
@DiscriminatorValue("ordine_al_tavolo")
public class OrdineAlTavolo extends Ordine {
    @Column(name = "tavolo")
    private String tavolo;


    public OrdineAlTavolo() {
    }


    public String getTavolo() {
        return this.tavolo;
    }

    public void setTavolo(String tavolo) {
        this.tavolo = tavolo;
    }


    public OrdineAlTavolo tavolo(String tavolo) {
        setTavolo(tavolo);
        return this;
    }
}
