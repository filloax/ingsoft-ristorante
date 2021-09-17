package it.unibo.ingsoft.fortunagest;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PeriodoDisabilitazioneDati {
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private String tipo;
    private Integer id;

    public LocalDateTime getFine() {
        return fine;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setFine(LocalDateTime fine) {
        this.fine = fine;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInizio(LocalDateTime inizio) {
        this.inizio = inizio;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        // configure objectMapper for pretty input
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.registerModule(new JavaTimeModule());
        // Jackson così stampa in ISO
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // StdDateFormat is ISO8601 since jackson 2.9

        try {
            return mapper.writeValueAsString(this);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String toString() {
        return this.toJSON();
    }
}
