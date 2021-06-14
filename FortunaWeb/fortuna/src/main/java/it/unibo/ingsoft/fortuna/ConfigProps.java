package it.unibo.ingsoft.fortuna;

import com.google.maps.model.Bounds;
import com.google.maps.model.LatLng;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ConfigurationProperties(prefix = "config")
@Component
@Data
@NoArgsConstructor
/**
 * Valori impostati in application.properties, automaticamente assegnati a istanze di questa
 * classe iniettate da Spring
 * da usare come componente (campo @Autowired)
 */
public class ConfigProps {
    private DatabaseProps db;
    private LogProps log;
    @ToString.Exclude private Keys keys;
    private Geoconfig geo;

    //Data crea automaticamente getter e setter
    @Data
    @NoArgsConstructor
    public static class DatabaseProps {
        private String host;
        private int port;
        private String username;
        @ToString.Exclude private String password;    
        private String driver;
        private String dbms;
        private String name;
    }

    @Data
    @NoArgsConstructor
    public static class LogProps { 
        private String host;
        private int port;
        private boolean localLog;
    }

    // Variabile di ambiente esterna per sicurezza (mai salvare key in chiaro su repository), 
	// chiave privata per Stripe da account
    @Data
    @NoArgsConstructor
    public static class Keys {
        private String stripePub = null;
        private String stripe = null;
        private String googleGeocoding = null;

        public String toString() {
            return super.toString();
        }
    }

    @Data
    @NoArgsConstructor
    public static class Geoconfig {
        private BoundsCfg bounds;
        private String region;
        private String preferCity;

        public Bounds getGapiBounds() {
            Bounds gapiBounds = new Bounds();
            gapiBounds.northeast = new LatLng(bounds.neLat, bounds.neLon);
            gapiBounds.southwest = new LatLng(bounds.swLat, bounds.swLon);
            return gapiBounds;
        }

        @Data
        @NoArgsConstructor
            public static class BoundsCfg {
            private double swLat;
            private double swLon;
            private double neLat;
            private double neLon;
        }
    }
}
