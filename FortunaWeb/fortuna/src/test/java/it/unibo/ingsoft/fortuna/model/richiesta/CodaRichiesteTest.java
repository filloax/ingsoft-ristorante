package it.unibo.ingsoft.fortuna.model.richiesta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CodaRichiesteTest {
    private CodaRichieste codaRichieste;

    private ArrayList<Richiesta> listaRichieste1;

    @BeforeEach
    public void richiesteSetup() {
        codaRichieste = new CodaRichieste();

        listaRichieste1 = new ArrayList<Richiesta>();

        Richiesta richiesta1 = new OrdineTakeAway();
        richiesta1.setIdRichiesta(1);
        Richiesta richiesta2 = new OrdineAlTavolo();
        richiesta2.setIdRichiesta(2);
        Richiesta richiesta3 = new OrdineDomicilio();
        richiesta3.setIdRichiesta(3);
        Richiesta richiesta4 = new Prenotazione();
        richiesta4.setIdRichiesta(4);
        Richiesta richiesta5 = new OrdineAlTavolo();
        richiesta5.setIdRichiesta(5);

        listaRichieste1.add(richiesta1);
        listaRichieste1.add(richiesta2);
        listaRichieste1.add(richiesta3);
        listaRichieste1.add(richiesta4);
        listaRichieste1.add(richiesta5);
    }

    @Test
    public void testGetter() {
        assertTrue(codaRichieste.getAccettati().isEmpty());
        assertTrue(codaRichieste.getInAttesa().isEmpty());
    }

    @Test
    public void testAggiungi() {
        codaRichieste.aggiungi(listaRichieste1.get(0));

        assertEquals(listaRichieste1.get(0), codaRichieste.getInAttesa().get(0));
        assertTrue(codaRichieste.getAccettati().isEmpty());

        // Test ordine di aggiunta

        codaRichieste.aggiungi(listaRichieste1.get(1));

        assertEquals(listaRichieste1.get(0), codaRichieste.getInAttesa().get(0));
        assertEquals(listaRichieste1.get(1), codaRichieste.getInAttesa().get(1));
        assertTrue(codaRichieste.getAccettati().isEmpty());
    }

    @Test
    public void testAccetta() {
        codaRichieste.aggiungi(listaRichieste1.get(0));
        assertTrue(codaRichieste.accetta(listaRichieste1.get(0)));
        assertFalse(codaRichieste.accetta(listaRichieste1.get(4)));

        assertEquals(listaRichieste1.get(0), codaRichieste.getAccettati().get(0));
        assertTrue(codaRichieste.getInAttesa().isEmpty());

        // Test ordine di aggiunta, e accetta per id

        codaRichieste.aggiungi(listaRichieste1.get(3));
        codaRichieste.aggiungi(listaRichieste1.get(4));
        codaRichieste.accetta(listaRichieste1.get(3).getIdRichiesta());

        assertEquals(listaRichieste1.get(0), codaRichieste.getAccettati().get(0));
        assertEquals(listaRichieste1.get(3), codaRichieste.getAccettati().get(1));
        assertEquals(listaRichieste1.get(4), codaRichieste.getInAttesa().get(0));
    }

    @Test
    public void testCancella() {
        codaRichieste.aggiungi(listaRichieste1.get(0));
        codaRichieste.aggiungi(listaRichieste1.get(1));
        codaRichieste.accetta(listaRichieste1.get(1));
        codaRichieste.aggiungi(listaRichieste1.get(2));
        codaRichieste.accetta(listaRichieste1.get(2));
        codaRichieste.aggiungi(listaRichieste1.get(3));

        assertTrue(codaRichieste.cancella(listaRichieste1.get(0)));
        assertTrue(codaRichieste.cancella(listaRichieste1.get(1).getIdRichiesta()));
        assertFalse(codaRichieste.cancella(listaRichieste1.get(4)));

        assertEquals(listaRichieste1.get(3), codaRichieste.getInAttesa().get(0));
        assertEquals(listaRichieste1.get(2), codaRichieste.getAccettati().get(0));
    }
}
