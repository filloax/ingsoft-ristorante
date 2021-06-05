package it.unibo.ingsoft.fortuna.prenotazione;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

public class DatiPrenotazione {
    private String nome;
    private String telefono;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;
    private LocalTime ora;
    private int numeroPersone;


    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public int getNumeroPersone() {
        return this.numeroPersone;
    }

    public void setNumeroPersone(int numeroPersone) {
        this.numeroPersone = numeroPersone;
    }
    
}
