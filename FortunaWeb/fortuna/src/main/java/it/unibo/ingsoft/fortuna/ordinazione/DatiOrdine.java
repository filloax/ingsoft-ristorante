package it.unibo.ingsoft.fortuna.ordinazione;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

public class DatiOrdine {
    private String nome;
    private String note;
    private String telefono;
    private String tavolo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;
    private LocalTime ora;


    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTavolo() {
        return this.tavolo;
    }

    public void setTavolo(String tavolo) {
        this.tavolo = tavolo;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOra() {
        return this.ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }
}
