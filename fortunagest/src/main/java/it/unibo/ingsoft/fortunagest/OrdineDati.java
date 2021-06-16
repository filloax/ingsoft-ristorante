package it.unibo.ingsoft.fortunagest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.format.annotation.DateTimeFormat;

import it.unibo.ingsoft.fortunagest.model.DatiProdotto;
import it.unibo.ingsoft.fortunagest.model.DatiSconto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrdineDati {
    private String nominativo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dataOra;
    private String note;
    private String telefono;
    private String indirizzo;
    private String tavolo;
    private Integer idRichiesta;
    private String tokenPagamento;
    private DatiProdotto[] prodotti;
    private DatiSconto[] sconti;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.toJSON();
    }
}
