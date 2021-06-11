package it.unibo.ingsoft.fortuna.model.richiesta;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Richiesta {
    @Column(name="nome")
    private String nominativo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer iDRichiesta;

    @Column(name="data_ora")
    private LocalDateTime dataOra;
    
    protected Richiesta(){}
    protected Richiesta(String nominativo, LocalDateTime dataOra){
        this.nominativo= nominativo;
        this.dataOra=dataOra;

    }
    public LocalDateTime getDataOra() {
        return dataOra;
    }
    public String getNominativo() {
        return nominativo;
    }
    public Integer getIdRichiesta() {
        return iDRichiesta;
    }
    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }
    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }
    public void setIdRichiesta(Integer iDRichiesta) {
        this.iDRichiesta = iDRichiesta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Richiesta)) {
            return false;
        }
        Richiesta richiesta = (Richiesta) o;
        return this.getIdRichiesta().equals(richiesta.getIdRichiesta());
    }
    
}
