package es.uv.etse.twcam.backend.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IncidentTest {

    @Test
    public void testConstructorAndGetters() {
        Incident incident = new Incident("1", "Descripción del incidente", "Valencia", "en tránsito", "asignada",
                "Técnico 1", "Bicicleta 1");

        assertEquals("1", incident.getId());
        assertEquals("Descripción del incidente", incident.getDescripcion());
        assertEquals("Valencia", incident.getUbicacion());
        assertEquals("en tránsito", incident.getSituacion());
        assertEquals("asignada", incident.getEstado());
        assertEquals("Técnico 1", incident.getTecnico());
        assertEquals("Bicicleta 1", incident.getBicicleta());
    }

    @Test
    public void testSetters() {
        Incident incident = new Incident();

        incident.setId("2");
        incident.setDescripcion("Nueva descripción");
        incident.setUbicacion("Madrid");
        incident.setSituacion("bloqueada en una estación");
        incident.setEstado("en proceso");
        incident.setTecnico("Técnico 2");
        incident.setBicicleta("Bicicleta 2");

        assertEquals("2", incident.getId());
        assertEquals("Nueva descripción", incident.getDescripcion());
        assertEquals("Madrid", incident.getUbicacion());
        assertEquals("bloqueada en una estación", incident.getSituacion());
        assertEquals("en proceso", incident.getEstado());
        assertEquals("Técnico 2", incident.getTecnico());
        assertEquals("Bicicleta 2", incident.getBicicleta());
    }

    @Test
    public void testRepairDetailsSettersAndGetters() {
        Incident incident = new Incident();

        incident.setAccionesRealizadas("Cambio de rueda");
        incident.setPiezasReemplazadas("Rueda delantera");
        incident.setDetallesAdicionales("Reparación completada sin problemas");
        incident.setDetallesBicicleta("Modelo: MTB-2023");

        assertEquals("Cambio de rueda", incident.getAccionesRealizadas());
        assertEquals("Rueda delantera", incident.getPiezasReemplazadas());
        assertEquals("Reparación completada sin problemas", incident.getDetallesAdicionales());
        assertEquals("Modelo: MTB-2023", incident.getDetallesBicicleta());
    }

    @Test
    public void testDefaultConstructor() {
        Incident incident = new Incident();

        assertNull(incident.getId());
        assertNull(incident.getDescripcion());
        assertNull(incident.getUbicacion());
        assertNull(incident.getSituacion());
        assertNull(incident.getEstado());
        assertNull(incident.getTecnico());
        assertNull(incident.getBicicleta());
        assertNull(incident.getAccionesRealizadas());
        assertNull(incident.getPiezasReemplazadas());
        assertNull(incident.getDetallesAdicionales());
        assertNull(incident.getDetallesBicicleta());
    }
}
