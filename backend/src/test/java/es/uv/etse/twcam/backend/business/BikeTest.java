package es.uv.etse.twcam.backend.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BikeTest {

    @Test
    public void testConstructorAndGetters() {
        Bike bike = new Bike("1", "Bicicleta A", 15.5, "Estacion 1");

        assertEquals("1", bike.getId());
        assertEquals("Bicicleta A", bike.getNombre());
        assertEquals(15.5, bike.getCosto());
        assertEquals("Estacion 1", bike.getEstacionId());
    }

    @Test
    public void testSetters() {
        Bike bike = new Bike();

        bike.setId("2");
        bike.setNombre("Bicicleta B");
        bike.setCosto(20.0);
        bike.setEstacionId("Estacion 2");

        assertEquals("2", bike.getId());
        assertEquals("Bicicleta B", bike.getNombre());
        assertEquals(20.0, bike.getCosto());
        assertEquals("Estacion 2", bike.getEstacionId());
    }

    @Test
    public void testDefaultConstructor() {
        Bike bike = new Bike();

        assertNull(bike.getId());
        assertNull(bike.getNombre());
        assertNull(bike.getCosto());
        assertNull(bike.getEstacionId());
    }
}
