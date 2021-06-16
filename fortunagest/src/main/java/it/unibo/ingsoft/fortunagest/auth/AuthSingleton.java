package it.unibo.ingsoft.fortunagest.auth;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.Data;

@Data
public class AuthSingleton {
    private static final AuthSingleton instance = new AuthSingleton();

    private HttpHeaders authHeaders = null;
    private boolean loggedIn = false;

    public static AuthSingleton getInstance() {
        return instance;
    }

    public void logout() {
        authHeaders = null;
        loggedIn = false;
    }

    public boolean login(String username, String password) {
        authHeaders = createHeaders(username, password);
        
        // Bruttissimo ma vabbè, crea richiesta arbitraria e controlla se funziona
        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/gest/sconti/";
        
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<String>(authHeaders), String.class);
            loggedIn = true;
        } catch (HttpClientErrorException.Unauthorized e) {
            authHeaders = null;
            loggedIn = false;
            System.out.println("Tentativo di login fallito!");
        }

        return loggedIn;
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
              String auth = username + ":" + password;
              byte[] encodedAuth = Base64.encodeBase64( 
                 auth.getBytes(Charset.forName("US-ASCII")) );

              String authHeader = "Basic " + new String( encodedAuth );

              set( "Authorization", authHeader );
           }};
    }
}
