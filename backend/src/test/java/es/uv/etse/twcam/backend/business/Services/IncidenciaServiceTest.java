package es.uv.etse.twcam.backend.business.Services;

import static org.junit.jupiter.api.Assertions.*;

import es.uv.etse.twcam.backend.business.Incidencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class IncidenciaServiceTest {

    private IncidenciaService incidenciaServiceMock;
    private List<Incidencia> mockIncidencias; // Variable compartida

    @BeforeEach
    void setup() {
        // Lista simulando el almacenamiento de incidencias
        mockIncidencias = new ArrayList<>();
        mockIncidencias.add(new Incidencia("2025-01-01", "14:13", "girardot,13,12", "test1", "reportada", "bloqueada",
                "1", null, "12345555L", "Bici111"));
        mockIncidencias.add(new Incidencia("2025-01-02", "17:16", "Valencia, El carmen, 54", "test2", "asignada",
                "pendiente", "2", "tecnico2", "22222222L", "Bici222"));
        mockIncidencias.add(new Incidencia("2025-01-03", "19:18", "Madrid, Puerta del Sol", "test3", "asignada",
                "pendiente", "3", "tecnico3", "33333333L", "Bici333"));

        incidenciaServiceMock = new IncidenciaService() {
            @Override
            protected List<Incidencia> leerIncidenciasDesdeArchivo() {
                return mockIncidencias;
            }

            @Override
            protected void escribirIncidenciasEnArchivo(List<Incidencia> incidencias) {
                // mockIncidencias.clear();
                mockIncidencias.addAll(incidencias); // actualiza la lista
                // System.out.println("Contenido de mockIncidencias FINAL: " + mockIncidencias);
            }
        };
    }

    @Test
    void testRegistrarIncidencia() {
        Incidencia nuevaIncidencia = incidenciaServiceMock.registrarIncidencia(
                "2025-01-04",
                "20:00",
                "Barcelona, Sagrada Familia",
                "Descripción nueva",
                "reportada",
                "bloqueada",
                "12348888L",
                "Bici444");

        assertNotNull(nuevaIncidencia, "La incidencia registrada no debería ser nula");
        assertEquals("2025-01-04", nuevaIncidencia.getFecha(), "La fecha no coincide");
        assertEquals("reportada", nuevaIncidencia.getEstado(), "El estado debería ser 'reportada'");
        assertEquals("bloqueada", nuevaIncidencia.getSituacion(), "La situación debería ser 'bloqueada'");
    }

    @Test
    void testObtenerPorEstado() {
        List<Incidencia> asignadas = incidenciaServiceMock.obtenerPorEstado("asignada");

        assertNotNull(asignadas, "La lista de incidencias no debería ser nula");
        assertEquals(2, asignadas.size(), "Debería haber dos incidencias con estado 'asignada'");
        for (Incidencia incidencia : asignadas) {
            assertEquals("asignada", incidencia.getEstado(), "El estado debería ser 'asignada'");
        }
    }

    @Test
    void testAsignarIncidencia() {
        // Ejecuta el método para asignar un técnico
        String id = "1"; // ID existente
        String tecnico = "tecnico1"; // Técnico asignado
        incidenciaServiceMock.asignarIncidencia(id, tecnico);

        // System.out.println("Contenido de mockIncidencias: " + mockIncidencias);
        assertNotNull(mockIncidencias.get(0), "La incidencia debería existir");
        assertEquals("asignada", mockIncidencias.get(0).getEstado(),
                "El estado de la incidencia debería ser 'asignada'");
        assertEquals(tecnico, mockIncidencias.get(0).getTecnico(), "El técnico asignado debería ser 'tecnico1'");
    }

    @Test
    void testObtenerTodas() {
        List<Incidencia> todas = incidenciaServiceMock.obtenerTodas();

        assertNotNull(todas, "La lista de incidencias no debería ser nula");
        assertEquals(3, todas.size(), "Debería haber tres incidencias en total");
    }

    @Test
    void testObtenerPorId() {
        Incidencia incidencia = incidenciaServiceMock.obtenerPorId("2");

        assertNotNull(incidencia, "La incidencia no debería ser nula");
        assertEquals("2", incidencia.getIncidenciaId(), "El ID debería ser '2'");
        assertEquals("Valencia, El carmen, 54", incidencia.getUbicacion(), "La ubicación no coincide");
    }

    @Test
    void testEliminarIncidencia() {
        Incidencia incidenciaAntes = incidenciaServiceMock.obtenerPorId("1");
        assertNotNull(incidenciaAntes, "La incidencia con ID '1' debería existir antes de la eliminación");

        boolean resultado = incidenciaServiceMock.eliminarIncidencia("1");

        assertTrue(resultado, "El método debería retornar true indicando que la incidencia fue eliminada");

        Incidencia incidenciaDespues = incidenciaServiceMock.obtenerPorId("1");
        assertNull(incidenciaDespues, "La incidencia con ID '1' debería haber sido eliminada");
    }

}
