package es.uv.etse.twcam.backend.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IncidenciaTest {

    @Test
    public void testConstructorAndGetters() {
        Incidencia incidencia = new Incidencia("2025-01-01", "10:00", "Valencia", "Descripción de prueba",
                "reportada", "bloqueada en una estacion", "123", "Técnico 1", "12345678A", "Bicicleta1");

        assertEquals("2025-01-01", incidencia.getFecha());
        assertEquals("10:00", incidencia.getHora());
        assertEquals("Valencia", incidencia.getUbicacion());
        assertEquals("Descripción de prueba", incidencia.getDescripcion());
        assertEquals("reportada", incidencia.getEstado());
        assertEquals("bloqueada en una estacion", incidencia.getSituacion());
        assertEquals("123", incidencia.getIncidenciaId());
        assertEquals("Técnico 1", incidencia.getTecnico());
        assertEquals("12345678A", incidencia.getDni());
        assertEquals("Bicicleta1", incidencia.getBicicleta());
    }

    @Test
    public void testSetters() {
        Incidencia incidencia = new Incidencia();

        incidencia.setEstado("en proceso");
        incidencia.setSituacion("en tránsito");
        incidencia.setIncidenciaId("456");
        incidencia.setTecnico("Técnico 2");
        incidencia.setDni("87654321B");
        incidencia.setBicicleta("Bicicleta2");

        assertEquals("en proceso", incidencia.getEstado());
        assertEquals("en tránsito", incidencia.getSituacion());
        assertEquals("456", incidencia.getIncidenciaId());
        assertEquals("Técnico 2", incidencia.getTecnico());
        assertEquals("87654321B", incidencia.getDni());
        assertEquals("Bicicleta2", incidencia.getBicicleta());
    }

    @Test
    public void testToString() {
        Incidencia incidencia = new Incidencia("2025-01-01", "10:00", "Valencia", "Descripción de prueba",
                "reportada", "bloqueada en una estacion", "123", "Técnico 1", "12345678A", "Bicicleta1");

        // Verificar que el método toString incluye los valores correctos
        String expected = "Incidencia{" +
                "fecha='2025-01-01', hora='10:00', ubicacion='Valencia', descripcion='Descripción de prueba', " +
                "estado='reportada', situacion='bloqueada en una estacion', id='123', tecnico='Técnico 1', " +
                "dni='12345678A', bicicleta='Bicicleta1'}";

        assertEquals(expected, incidencia.toString());
    }
}
