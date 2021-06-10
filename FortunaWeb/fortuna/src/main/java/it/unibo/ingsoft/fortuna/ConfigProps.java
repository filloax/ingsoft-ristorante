package it.unibo.ingsoft.fortuna;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    //Data crea automaticamente getter e setter
    @Data
    @NoArgsConstructor
    public static class DatabaseProps {
        private String host;
        private int port;
        private String username;
        private String password;    
        private String driver;
        private String dbms;
        private String name;
    }

    @Data
    @NoArgsConstructor
    public static class LogProps { 
        private String host;
        private int port;
    }
}
