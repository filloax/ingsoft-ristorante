package it.unibo.ingsoft.fortunagest.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DeserializeListProdotti extends StdDeserializer<List<Integer>> {
    public DeserializeListProdotti() { 
        this(null); 
    } 

    public DeserializeListProdotti(Class<?> vc) { 
        super(vc); 
    }
    
    @Override
    public List<Integer> deserialize(JsonParser parser, DeserializationContext ctx)
            throws IOException, JsonProcessingException {
        String listText = parser.getText();
        System.out.println(listText);
        
        return Arrays.stream(listText.replace("[", "").replace("]", "").split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
    
}
