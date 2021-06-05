package it.unibo.ingsoft.fortuna;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "config")
@Component
/**
 * Valori impostati in application.properties, automaticamente assegnati a istanze di questa
 * classe iniettate da Spring
 * da usare come componente (campo @Autowired)
 */
public class ConfigProps {
    private DatabaseProps db;

    public DatabaseProps getDb() {
        return this.db;
    }

    public void setDb(DatabaseProps db) {
        this.db = db;
    }

    public static class DatabaseProps {
        private String host;
        private int port;
        private String username;
        private String password;    
        private String driver;
        private String dbms;
        private String name;
        
        public String getHost() {
            return this.host;
        }
    
        public void setHost(String host) {
            this.host = host;
        }
    
        public int getPort() {
            return this.port;
        }
    
        public void setPort(int port) {
            this.port = port;
        }
    
        public String getUsername() {
            return this.username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public String getPassword() {
            return this.password;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriver() {
            return this.driver;
        }
    
        public void setDriver(String driver) {
            this.driver = driver;
        }
            
        public String getDbms() {
            return this.dbms;
        }
    
        public void setDbms(String dbms) {
            this.dbms = dbms;
        }
    
        public String getName() {
            return this.name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    }
}
