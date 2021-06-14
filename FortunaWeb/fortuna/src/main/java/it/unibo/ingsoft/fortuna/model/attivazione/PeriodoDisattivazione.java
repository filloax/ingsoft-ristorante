package it.unibo.ingsoft.fortuna.model.attivazione;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.unibo.ingsoft.fortuna.model.Prodotto;
import lombok.Data;

@Entity
@Table(name = "periodi_disattivazione")
@Data
public class PeriodoDisattivazione {
    @Column(name = "inizio")
    private LocalDateTime inizio;
    @Column(name = "fine")
    private LocalDateTime fine;
    @Column(name = "tipo")
    private TipoDisattivazione tipo;

    @ManyToOne
    @JoinColumn(name = "numero_prodotto")
    private Prodotto prodotto;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public PeriodoDisattivazione() {
    }

    public PeriodoDisattivazione(LocalDateTime inizio, LocalDateTime fine, TipoDisattivazione tipo, Prodotto prodotto) {
        this.inizio = inizio;
        this.fine = fine;
        this.tipo = tipo;
        this.prodotto = prodotto;
    }

    public PeriodoDisattivazione(LocalDateTime inizio, LocalDateTime fine, Prodotto prodotto) {
        this(inizio, fine, TipoDisattivazione.PRODOTTO, prodotto);
    }

    public PeriodoDisattivazione(LocalDateTime inizio, LocalDateTime fine, TipoDisattivazione tipo) {
        this(inizio, fine, tipo, null);
    }

    public boolean contieneTempo(LocalDateTime tempo) {
        return ( tempo.isAfter(inizio) || tempo.equals(inizio) ) && tempo.isBefore(tempo);
    }

    public PeriodoDisattivazione id(Integer id) {
        setId(id);
        return this;
    }

    public PeriodoDisattivazione inizio(LocalDateTime inizio) {
        setInizio(inizio);
        return this;
    }

    public PeriodoDisattivazione fine(LocalDateTime fine) {
        setFine(fine);
        return this;
    }

    public PeriodoDisattivazione tipo(TipoDisattivazione tipo) {
        setTipo(tipo);
        return this;
    }

    public PeriodoDisattivazione prodotto(Prodotto prodotto) {
        setProdotto(prodotto);
        return this;
    }    
}