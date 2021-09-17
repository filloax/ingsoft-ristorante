package it.unibo.ingsoft.fortuna.autenticazione;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.AbstractService;

// Per come è fatto l'algoritmo, forse potrebbe bastare anche
// solo una password senza username
@Service
public class AutenticazioneLocale extends AbstractService implements IAutenticazione {
    private static final String PASS_FILE = "titolare.txt";
    // se attivo, la password di default è vuota
    // per ora non ha alternative, possibili alternative: impostare a mano/generare password
    // di default prima di fare il deploy e metterla nel file, 
    // però bisogna trovare comunque il modo di resettare la pw
    private static final boolean DEBUG_DEFAULT_PASS_EMPTY = true;

    @Autowired
    private PasswordEncoder passEncoder;

    private String savedEncodedCredString = null;
    
    @PostConstruct
    public void init() {
        try {
            leggiFileCredenziali();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Impossibile leggere il file delle credenziali: " + e.getMessage());
        }
    }

    @Override
    public boolean autentica(String username, String password) throws AutenticazioneException {
        scriviMessaggio("Tentativo di accesso con username: " + username);
        if (savedEncodedCredString == null) {
            try {
                leggiFileCredenziali();
            } catch (IOException e) {
                throw new AutenticazioneException("Impossibile leggere il file delle credenziali: " + e.getMessage(), e);
            }
        }

        String credString = username + "," + password;

        if (passEncoder.matches(credString, savedEncodedCredString)) {
            scriviMessaggio("Tentativo di accesso RIUSCITO con username: " + username);
            return true;
        } else {
            scriviMessaggio("Tentativo di accesso FALLITO con username: " + username);
            return false;
        }
    }

    @Override
    public void setUsername(String username, String password, String newUsername) throws AutenticazioneException {
        if (autentica(username, password)) {
            try {
                salvaCredenzialiCifrate(newUsername, password);
                scriviMessaggio("Username modificato: " + newUsername);
            } catch (IOException e) {
                throw new AutenticazioneException("Impossibile salvare le credenziali modificate: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void setPassword(String username, String password, String newPassword) throws AutenticazioneException {
        if (autentica(username, password)) {
            try {
                salvaCredenzialiCifrate(username, newPassword);
                scriviMessaggio("Passwrd modificata!");
            } catch (IOException e) {
                throw new AutenticazioneException("Impossibile salvare le credenziali modificate: " + e.getMessage(), e);
            }
        }
    }

    private void leggiFileCredenziali() throws IOException {
        FileSystemResource passFile = new FileSystemResource("config/" + PASS_FILE);

        try (BufferedReader rd = new BufferedReader(new FileReader(passFile.getFile()))) {
            savedEncodedCredString = rd.readLine();
            scriviMessaggio("Password caricata con successo!");
        } catch (FileNotFoundException e) { // FILE CREDENZIALI NON TROVATO, DEFAULT
            savedEncodedCredString = passEncoder.encode("fortunAdmin,");
        }
    }
    
    private void salvaCredenzialiCifrate(String username, String password) throws IOException {
        String credString = username + "," + password;
        FileSystemResource passFile = new FileSystemResource("config/" + PASS_FILE);

        try(PrintWriter wr = new PrintWriter(new FileWriter(passFile.getFile()))) {
            String newEncodedCredString = passEncoder.encode(credString);
            wr.println(newEncodedCredString);
            savedEncodedCredString = newEncodedCredString;
        }
    }
}
