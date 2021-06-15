package it.unibo.ingsoft.fortunagest.model;

import java.util.List;

import lombok.Data;

@Data
public class DatiZonaConsegnaPunti {
    private double prezzoMinimo = 0;
    private List<Vector> punti;
    private Integer id;
}
