package it.unibo.ingsoft.fortuna.model.richiesta;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "prenotazioni")
@AttributeOverrides({ @AttributeOverride(name = "iDRichiesta", column = @Column(name = "id")),
        @AttributeOverride(name = "dataOra", column = @Column(name = "data_ora")) })
public class Prenotazione extends Richiesta {
    @Column(name = "numero_persone")
    private int numeroPersone;
    @Column(name = "telefono")
    private String telefono;

    public Prenotazione(String nominativo, LocalDateTime dataOra, String telefono, int numeroPersone) {
        super(nominativo, dataOra);
        this.numeroPersone = numeroPersone;
        this.telefono = telefono;

    }

    public Prenotazione() {

    }

    public int getNumeroPersone() {
        return numeroPersone;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNumeroPersone(int numeroPersone) {
        this.numeroPersone = numeroPersone;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
