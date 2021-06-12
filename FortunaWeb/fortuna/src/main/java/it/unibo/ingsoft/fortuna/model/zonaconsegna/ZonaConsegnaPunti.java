package it.unibo.ingsoft.fortuna.model.zonaconsegna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

import it.unibo.ingsoft.fortuna.SpringContext;
import it.unibo.ingsoft.fortuna.log.ILogManager;
import lombok.Data;

@Data
public class ZonaConsegnaPunti implements IZonaConsegna {
    // Usati da API di google per impostare la preferenza di ricerca
    // non restrittivi, rappresentano solo una preferenza
    public static final LatLng BOUNDS_BOLOGNA_SW = new LatLng( 44.461555, 11.262151 );
    public static final LatLng BOUNDS_BOLOGNA_NE = new LatLng( 44.562733, 11.430363 );
    public static final String PREFER_CITY = "Bologna";

    private GeoApiContext geoApiContext;

    private ILogManager log;

    private double prezzoMinimo;
    private List<Vector> punti;
    private int id;

    public ZonaConsegnaPunti() {};

    public ZonaConsegnaPunti(double prezzoMinimo, List<Vector> punti) {
        this.prezzoMinimo = prezzoMinimo;
        this.punti = punti;
    }

    public ZonaConsegnaPunti(double prezzoMinimo) {
        this(prezzoMinimo, new ArrayList<>());
    }

    @Override
    public boolean include(Vector coordinata, double prezzo) throws ZonaConsegnaException {
        if (prezzo < prezzoMinimo)
            return false;

        boolean result = false;
        for (int i = 0, j = punti.size() - 1; i < punti.size(); j = i++) {
            if (coordinata.equals(punti.get(i)))
                return true;

            if ((punti.get(i).getY() >= coordinata.getY()) != (punti.get(j).getY() > coordinata.getY())) {
                double pendenza = (coordinata.getX() - punti.get(i).getX()) * (punti.get(j).getY() - punti.get(i).getY()) 
                    - (punti.get(j).getX() - punti.get(i).getX()) * (coordinata.getY() - punti.get(i).getY());
                
                if (pendenza == 0) //lato
                    return true;
                if ((pendenza < 0) != (punti.get(j).getY() < punti.get(i).getY()))
                    result = !result;
            }
        }
        return result;    
    }

    @Override
    public boolean include(String indirizzo, double prezzo) throws ZonaConsegnaException {
        if (prezzo < prezzoMinimo)
            return false;
        
        return include(coordDaIndirizzo(indirizzo), prezzo);
    }
    
    private Vector coordDaIndirizzo(String indirizzo) throws ZonaConsegnaException {
        if (getGeoApiContext() == null)
            throw new InvalidConfigurationPropertyValueException("GEOCODING_KEY", "null", "API Key per Google Maps Geocoding non impostata!");

        // String urlAddress = new URLEncoder().encode(indirizzo, Charset.defaultCharset());
        String region = "it";

        // String requestURL = String.format("https://maps.googleapis.com/maps/api/geocode/json?key=%s&region=%s&address=%s", mapsApiKey, region, urlAddress);
        
        GeocodingResult result = null;
        try {
            getLog().scriviMessaggio(String.format("Richiesta a API Google per Geocoding di '%s'...", indirizzo));

            GeocodingResult[] results =  GeocodingApi.geocode(getGeoApiContext(), indirizzo)
                .region(region) //preferenza, non restrittivo
                .bounds(BOUNDS_BOLOGNA_SW, BOUNDS_BOLOGNA_NE) //preferenza, non restrittivo
                .await();
                    
            for (GeocodingResult r : results) {
                if (getLocality(r).equals(PREFER_CITY)) {
                    result = r;
                    break; //sì è brutto ma è brutto in generale tutto ciò visto che hardcoda città ecc
                }
            }

            if (result == null && results.length > 0)
                result = results[0];
        } catch (ApiException | InterruptedException | IOException e) {
            getLog().scriviMessaggio(String.format("Richiesta geocoding '%s' fallita: %s", indirizzo, e.getMessage()));
            throw new ZonaConsegnaException("Errore in connessione a API Google Maps!", e);
        }

        if (result == null) {
            getLog().scriviMessaggio(String.format("Richiesta geocoding '%s' fallita per indirizzo sconosciuto", indirizzo));
            throw new IndirizzoSconosciutoException("Indirizzo sconosciuto: " + indirizzo);
        }

        String locality = getLocality(result);

        if (!locality.equals(PREFER_CITY)) {
            getLog().scriviMessaggio(String.format("Richiesta geocoding per '%s' ricevuta da fuori città, possibile spam?", indirizzo));
        }

        LatLng coords = result.geometry.location;

        getLog().scriviMessaggio(String.format("Richiesta geocoding riuscita: risultato (%f, %f)", coords.lat, coords.lng));

        return new Vector(coords.lat, coords.lng);
    }

    private String getLocality(GeocodingResult geocodingResult) {
        String city = "";
        for (AddressComponent addComp : geocodingResult.addressComponents) {
            if (Arrays.asList(addComp.types).contains(AddressComponentType.LOCALITY)) {
                city = addComp.longName;
            }
        }
        return city;
    }

    public ZonaConsegnaPunti prezzoMinimo(double prezzoMinimo) {
        setPrezzoMinimo(prezzoMinimo);
        return this;
    }

    public ZonaConsegnaPunti punti(List<Vector> punti) {
        setPunti(punti);
        return this;
    }

    public ZonaConsegnaPunti id(int id) {
        setId(id);
        return this;
    }

    private ILogManager getLog() { 
        if (log == null)
            log = SpringContext.getBean(ILogManager.class);

        return log;
    }

    private GeoApiContext getGeoApiContext() {
        if (geoApiContext == null)
            geoApiContext = SpringContext.getBean(GeoApiContext.class);

        return geoApiContext;
    }
}
