package it.unibo.ingsoft.fortuna.model.richiesta;

import java.time.LocalDateTime;

public abstract class Richiesta {
    private String nominativo;
    private String idRichiesta;
    private LocalDateTime dataOra;

    protected Richiesta() {}

    protected Richiesta(String nominativo, LocalDateTime dataOra)
    {
        this.nominativo = nominativo;
        this.dataOra = dataOra;
    }


    public String getNominativo() {
        return this.nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public String getIdRichiesta() {
        return this.idRichiesta;
    }

    public void setIdRichiesta(String idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

    public LocalDateTime getDataOra() {
        return this.dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
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
