package it.unibo.ingsoft.fortuna.model.richiesta;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "ordine_take_away")
@DiscriminatorValue("ordine_take_away")
public class OrdineTakeAway extends Ordine {
    @Column(name="telefono")
    private String telefono;

    public OrdineTakeAway() {
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public OrdineTakeAway telefono(String telefono) {
        setTelefono(telefono);
        return this;
    }

}
