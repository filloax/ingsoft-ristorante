package it.unibo.ingsoft.fortunagest.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DatiOrdine {
    private String nominativo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataOra;
    private String note;
    private String telefono;
    private String indirizzo;
    private String tavolo;
    private Integer idRichiesta;
    private String tokenPagamento;
    // @JsonDeserialize(using = DeserializeListProdotti.class)
    private List<Integer> prodotti;
    private List<DatiSconto> sconti;

    private String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
