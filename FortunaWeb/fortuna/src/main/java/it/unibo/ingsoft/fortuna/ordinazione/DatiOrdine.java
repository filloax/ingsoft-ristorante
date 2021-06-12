package it.unibo.ingsoft.fortuna.ordinazione;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class DatiOrdine {
    private String nome;
    private String note;
    private String telefono;
    private String tavolo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime ora;
    private String indirizzo;
}
