package it.unibo.ingsoft.fortuna.prenotazione;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DatiPrenotazione {
    private String nome;
    private String telefono;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime ora;
    private int numeroPersone;

    // public void setData(String data) {
    //     System.out.println("\n\n\n\n");
    //     System.out.println("data: " + data);
    //     System.out.println("\n\n\n\n");
    //     this.data = data;
    // }

    // public void setOra(String ora) {
    //     System.out.println("\n\n\n\n");
    //     System.out.println("ora: " + ora);
    //     System.out.println("\n\n\n\n");
    //     this.ora = ora;
    // }

}
