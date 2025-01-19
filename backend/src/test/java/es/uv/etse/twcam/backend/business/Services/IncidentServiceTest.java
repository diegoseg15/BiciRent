package es.uv.etse.twcam.backend.business.Services;

import static org.junit.jupiter.api.Assertions.*;

import es.uv.etse.twcam.backend.business.Incident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class IncidentServiceTest {

    private IncidentService incidentServiceMock; // Mock del servicio IncidentService
    private List<Incident> mockIncidents; // Lista simulada para pruebas

    @BeforeEach
    void setup() {
        // Inicializa la lista simulada con datos de prueba
        mockIncidents = new ArrayList<>();
        mockIncidents.add(
                new Incident("1", "Descripción 1", "Ubicación 1", "bloqueada", "asignada", "tecnico1", "bicicleta1"));
        mockIncidents.add(
                new Incident("2", "Descripción 2", "Ubicación 2", "en transito", "completada", "tecnico2", "bicicleta2"));

        // Sobrescribe métodos del servicio para trabajar con la lista simulada
        incidentServiceMock = new IncidentService() {
            @Override
            public List<Incident> getAllIncidents() {
                return mockIncidents; // Devuelve la lista simulada
            }

            @Override
            public Incident getIncidentById(String id) {
                // Busca una incidencia por su ID en la lista simulada
                return mockIncidents.stream()
                        .filter(incident -> incident.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public boolean updateIncidentState(String id, String newState) {
                // Actualiza el estado de una incidencia si es válida
                Incident incident = getIncidentById(id);
                if (incident == null || !isValidTransition(incident.getEstado(), newState)) {
                    return false;
                }
                incident.setEstado(newState);
                return true;
            }

            @Override
            public boolean saveRepairDetailsToFile(String id, String acciones, String piezas, String detalles,
                    String bicicleta) {
                // Guarda los detalles de reparación en la incidencia simulada
                Incident incident = getIncidentById(id);
                if (incident == null)
                    return false;

                incident.setAccionesRealizadas(acciones);
                incident.setPiezasReemplazadas(piezas);
                incident.setDetallesAdicionales(detalles);
                incident.setDetallesBicicleta(bicicleta);

                return true; // Simula que los detalles se guardaron correctamente
            }

            @Override
            public boolean registerIncidentDetails(String id, String acciones, String piezas, String detalles,
                    String bicicleta) {
                // Registra los detalles si la incidencia está completada
                Incident incident = getIncidentById(id);
                if (incident != null && "completada".equals(incident.getEstado())) {
                    return saveRepairDetailsToFile(id, acciones, piezas, detalles, bicicleta);
                }
                return false;
            }
        };
    }

    @Test
    void testGetAllIncidents() {
        // Prueba para obtener todas las incidencias
        List<Incident> incidents = incidentServiceMock.getAllIncidents();

        assertNotNull(incidents, "La lista de incidencias no debería ser nula");
        assertEquals(2, incidents.size(), "Debería haber 2 incidencias en la lista");
    }

    @Test
    void testGetIncidentById() {
        // Prueba para obtener una incidencia por su ID
        Incident incident = incidentServiceMock.getIncidentById("1");

        assertNotNull(incident, "La incidencia no debería ser nula");
        assertEquals("1", incident.getId(), "El ID debería ser '1'");
        assertEquals("Descripción 1", incident.getDescripcion(), "La descripción no coincide");
    }

    @Test
    void testUpdateIncidentState() {
        // Caso exitoso: transición válida
        boolean result = incidentServiceMock.updateIncidentState("1", "en proceso"); // Cambia a "en proceso"
        assertTrue(result, "La actualización del estado debería ser exitosa");
        assertEquals("en proceso", mockIncidents.get(0).getEstado(), "El estado debería ser 'en proceso'");

        // Caso exitoso: transición adicional válida
        boolean nextTransitionResult = incidentServiceMock.updateIncidentState("1", "completada"); // Cambia a "completada"
        assertTrue(nextTransitionResult, "La actualización del estado debería ser exitosa");
        assertEquals("completada", mockIncidents.get(0).getEstado(), "El estado debería ser 'completada'");

        // Caso de incidencia no encontrada
        boolean notFoundResult = incidentServiceMock.updateIncidentState("99", "completada");
        assertFalse(notFoundResult, "La actualización no debería ser exitosa para una incidencia no encontrada");

        // Caso de transición no válida
        boolean invalidTransitionResult = incidentServiceMock.updateIncidentState("1", "asignada");
        assertFalse(invalidTransitionResult, "La transición no válida no debería actualizar el estado");
    }

    @Test
    void testIsValidTransition() {
        // Prueba de transición válida
        assertTrue(incidentServiceMock.updateIncidentState("1", "en proceso"), "La transición debería ser válida");

        // Prueba de transición no válida
        assertFalse(incidentServiceMock.updateIncidentState("1", "asignada"), "La transición no debería ser válida");
    }

    @Test
    void testSaveRepairDetailsToFile() {
        // Prueba para guardar los detalles de reparación
        boolean result = incidentServiceMock.saveRepairDetailsToFile("2", "Reparación completa", "Pieza A",
                "Detalles extra", "bicicleta2");

        assertTrue(result, "El guardado de los detalles de reparación debería ser exitoso");
        Incident incident = mockIncidents.get(1); // Verifica la segunda incidencia
        assertEquals("Reparación completa", incident.getAccionesRealizadas(), "Las acciones realizadas no coinciden");
        assertEquals("Pieza A", incident.getPiezasReemplazadas(), "Las piezas reemplazadas no coinciden");
    }

    @Test
    void testRegisterIncidentDetails() {
        // Prueba para registrar los detalles de reparación
        boolean result = incidentServiceMock.registerIncidentDetails("2", "Reparación completa", "Pieza A",
                "Detalles extra", "bicicleta2");

        assertTrue(result, "El registro de los detalles debería ser exitoso");
        Incident incident = mockIncidents.get(1); // Verifica la segunda incidencia
        assertEquals("Reparación completa", incident.getAccionesRealizadas(), "Las acciones realizadas no coinciden");
        assertEquals("Pieza A", incident.getPiezasReemplazadas(), "Las piezas reemplazadas no coinciden");
        assertEquals("Detalles extra", incident.getDetallesAdicionales(), "Los detalles adicionales no coinciden");
    }
}
