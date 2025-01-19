package es.uv.etse.twcam.backend.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RentalTest {

    @Test
    public void testConstructorAndGetters() {
        Rental rental = new Rental("user123", "bike456", "2025-01-01 10:00", 12.5, "activo");

        assertEquals("user123", rental.getUsuarioId());
        assertEquals("bike456", rental.getBicicletaId());
        assertEquals("2025-01-01 10:00", rental.getFechaHoraRecogida());
        assertEquals(12.5, rental.getDistancia());
        assertEquals("activo", rental.getEstado());
    }

    @Test
    public void testSetters() {
        Rental rental = new Rental();

        rental.setUsuarioId("user789");
        rental.setBicicletaId("bike123");
        rental.setFechaHoraRecogida("2025-02-15 15:30");
        rental.setDistancia(20.0);
        rental.setEstado("completado");

        assertEquals("user789", rental.getUsuarioId());
        assertEquals("bike123", rental.getBicicletaId());
        assertEquals("2025-02-15 15:30", rental.getFechaHoraRecogida());
        assertEquals(20.0, rental.getDistancia());
        assertEquals("completado", rental.getEstado());
    }

    @Test
    public void testDefaultConstructor() {
        Rental rental = new Rental();

        assertNull(rental.getUsuarioId());
        assertNull(rental.getBicicletaId());
        assertNull(rental.getFechaHoraRecogida());
        assertNull(rental.getDistancia());
        assertNull(rental.getEstado());
    }

    @Test
    public void testSetFechaHoraRecogida_Valid() {
        Rental rental = new Rental();

        rental.setFechaHoraRecogida("2025-03-01 12:00");

        assertEquals("2025-03-01 12:00", rental.getFechaHoraRecogida());
    }

    @Test
    public void testSetFechaHoraRecogida_Invalid() {
        Rental rental = new Rental();

        rental.setFechaHoraRecogida(null);

        assertEquals("2000-01-01 00:00", rental.getFechaHoraRecogida());
    }

    @Test
    public void testObtenerFechaFormateada_Valid() {
        Rental rental = new Rental("user123", "bike456", "2025-04-01 14:30", 10.0, "activo");

        assertEquals("2025-04-01 14:30", rental.obtenerFechaFormateada());
    }

    @Test
    public void testObtenerFechaFormateada_Invalid() {
        Rental rental = new Rental("user123", "bike456", "invalid-date", 10.0, "activo");

        assertEquals("Fecha inválida", rental.obtenerFechaFormateada());
    }
}
