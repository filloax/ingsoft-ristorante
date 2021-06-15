package it.unibo.ingsoft.fortunagest.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DatiSconti {
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime end;
    private double quantita;
    private boolean isPercent = false;
    private double prezzoMin = 0;
    private Integer numeroProdotto;
    private Integer id; //usato solo per response
}
