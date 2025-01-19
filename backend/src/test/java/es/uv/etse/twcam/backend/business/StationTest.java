package es.uv.etse.twcam.backend.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StationTest {

    @Test
    public void testConstructorAndGetters() {
        Station station = new Station("1", "Estación Central");

        assertEquals("1", station.getId());
        assertEquals("Estación Central", station.getNombre());
    }

    @Test
    public void testSetters() {
        Station station = new Station();

        station.setId("2");
        station.setNombre("Estación Norte");

        assertEquals("2", station.getId());
        assertEquals("Estación Norte", station.getNombre());
    }

    @Test
    public void testDefaultConstructor() {
        Station station = new Station();

        assertNull(station.getId());
        assertNull(station.getNombre());
    }
}
