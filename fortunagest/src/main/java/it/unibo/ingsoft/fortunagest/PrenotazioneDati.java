package it.unibo.ingsoft.fortunagest;

import java.time.LocalDateTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class PrenotazioneDati {
    private String nominativo;
    private LocalDateTime dataOra;
    private Integer numeroPersone;
    private String telefono;
    private Integer idRichiesta;


    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public Integer getIdRichiesta() {
        return idRichiesta;
    }

    public String getNominativo() {
        return nominativo;
    }

    public Integer getNumeroPersone() {
        return numeroPersone;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public void setIdRichiesta(Integer idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public void setNumeroPersone(Integer numeroPersone) {
        this.numeroPersone = numeroPersone;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
         //configure objectMapper for pretty input
         mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
         mapper.registerModule(new JavaTimeModule());

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
