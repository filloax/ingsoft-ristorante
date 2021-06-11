package it.unibo.ingsoft.fortuna.model.zonaconsegna;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ZonaConsegnaTest {
    private ZonaConsegnaPunti zonaConsegna1;
    private ZonaConsegnaPunti zonaConsegna2;
    private List<Vector> puntiRettangolo;
    private List<Vector> puntiTrapezio;

    @BeforeEach
    public void beforeEach() {
        puntiRettangolo = List.of(new Vector(0, 0), new Vector(5, 0), new Vector(5, 2), new Vector(0, 2));
        puntiTrapezio= List.of(new Vector(0, 0), new Vector(5, 0), new Vector(2, 3), new Vector(0, 2));

        
        zonaConsegna1 = new ZonaConsegnaPunti(0.0, puntiRettangolo);
        zonaConsegna2 = new ZonaConsegnaPunti(5.0, puntiTrapezio);
    }

    @Test
    public void testShapeBase() throws ZonaConsegnaException {
        assertTrue(zonaConsegna1.include(new Vector(3, 1), 99));
        assertTrue(zonaConsegna2.include(new Vector(3, 1), 99));
        assertFalse(zonaConsegna1.include(new Vector(-1, 1), 99));
        assertFalse(zonaConsegna2.include(new Vector(-1, 1), 99));
        assertTrue(zonaConsegna1.include(new Vector(4, 1), 99));
        assertFalse(zonaConsegna2.include(new Vector(4.5, 1), 99));
    }
    
    @Test
    public void testShapeLati() throws ZonaConsegnaException {
        assertTrue(zonaConsegna1.include(new Vector(0, 1), 99));
        assertTrue(zonaConsegna1.include(new Vector(5, 1), 99));
        assertTrue(zonaConsegna1.include(new Vector(3, 2), 99));
        assertTrue(zonaConsegna1.include(new Vector(3, 0), 99));

        assertTrue(zonaConsegna2.include(new Vector(0, 1), 99));
        assertTrue(zonaConsegna2.include(new Vector(4, 1), 99));
        assertTrue(zonaConsegna2.include(new Vector(2, 2), 99));
        assertTrue(zonaConsegna2.include(new Vector(3, 0), 99));
    }

    @Test
    public void testShapeVertici() throws ZonaConsegnaException {
        for (Vector p : puntiRettangolo)
            assertTrue(zonaConsegna1.include(p, 99));
        for (Vector p : puntiTrapezio)
            assertTrue(zonaConsegna2.include(p, 99));
    }
}
