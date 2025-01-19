package es.uv.etse.twcam.backend.apirest;

import es.uv.etse.twcam.backend.business.Incident;
import es.uv.etse.twcam.backend.business.Services.IncidentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class IncidentEndpointTest {

    private IncidentEndpoint servlet;

    @Mock
    private IncidentService incidentServiceMock;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Crear instancia del servlet
        servlet = new IncidentEndpoint();

        // Usar Reflection para inyectar el mock en el campo privado
        Field field = IncidentEndpoint.class.getDeclaredField("incidentService");
        field.setAccessible(true);
        field.set(servlet, incidentServiceMock);

        // Preparar captura de la salida
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }



    @Test
    void testDoPutValidStateChange() throws Exception {
        // Configurar parámetros de la solicitud
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("estado")).thenReturn("completada");

        // Simular datos del servicio
        Incident incident = new Incident("1", "Descripción", "Ubicación", "Situación", "en proceso", "Técnico", "Bicicleta");
        when(incidentServiceMock.getIncidentById("1")).thenReturn(incident);
        when(incidentServiceMock.updateIncidentState("1", "completada")).thenReturn(true);

        // Ejecutar el método PUT
        servlet.doPut(request, response);

        // Verificar respuesta
        verify(response).setStatus(HttpServletResponse.SC_OK);
        String responseContent = stringWriter.toString();
        assertTrue(responseContent.contains("\"message\": \"Estado cambiado a 'completada' exitosamente\""));
    }

    @Test
    void testDoPutInvalidStateChange() throws Exception {
        // Configurar parámetros de la solicitud
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("estado")).thenReturn("completada");

        // Simular datos del servicio
        Incident incident = new Incident("1", "Descripción", "Ubicación", "Situación", "asignada", "Técnico", "Bicicleta");
        when(incidentServiceMock.getIncidentById("1")).thenReturn(incident);

        // Ejecutar el método PUT
        servlet.doPut(request, response);

        // Verificar respuesta
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String responseContent = stringWriter.toString();
        assertTrue(responseContent.contains("\"error\": \"Estado no válido para cambiar a 'completada'\""));
    }

    @Test
    void testDoPutMissingParameters() throws Exception {
        // Configurar parámetros de la solicitud incompletos
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("estado")).thenReturn("completada");

        // Ejecutar el método PUT
        servlet.doPut(request, response);

        // Verificar respuesta
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String responseContent = stringWriter.toString();
        assertTrue(responseContent.contains("\"error\": \"Faltan parámetros obligatorios\""));
    }
}