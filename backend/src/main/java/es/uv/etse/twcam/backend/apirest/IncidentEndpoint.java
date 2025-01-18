package es.uv.etse.twcam.backend.apirest;

import es.uv.etse.twcam.backend.business.Incident;
import es.uv.etse.twcam.backend.business.Services.IncidentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

import java.io.IOException;

@WebServlet(name = "IncidentEndpoint", urlPatterns = { "/api/incidents" })
public class IncidentEndpoint extends HttpServlet {

    private final IncidentService incidentService = new IncidentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response); // Agregar encabezados CORS

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Incident incident : incidentService.getAllIncidents()) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("id", incident.getId())
                    .add("descripcion", incident.getDescripcion())
                    .add("ubicacion", incident.getUbicacion())
                    .add("situacion", incident.getSituacion())
                    .add("estado", incident.getEstado())
                    .add("tecnico", incident.getTecnico()));
        }

        response.getWriter().write(arrayBuilder.build().toString());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response);

        String id = request.getParameter("id");
        String newState = request.getParameter("estado");

        if (id == null || newState == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Faltan parámetros obligatorios\"}");
            return;
        }

        // Manejar cambio de estado a "completada"
        if ("completada".equals(newState)) {
            Incident incident = incidentService.getIncidentById(id);
            if (incident != null && ("en proceso".equals(incident.getEstado())
                    || "Esperando repuestos".equals(incident.getEstado()))) {
                boolean updated = incidentService.updateIncidentState(id, newState);
                if (updated) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Estado cambiado a 'completada' exitosamente\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\": \"No se pudo actualizar el estado a 'completada'\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Estado no válido para cambiar a 'completada'\"}");
            }
        } else {
            // Manejo general para otros estados
            boolean updated = incidentService.updateIncidentState(id, newState);
            if (updated) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Estado actualizado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Incidencia no encontrada o estado no válido\"}");
            }
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        addCORSHeaders(response); // Responder también a las solicitudes preflight
    }

    private void addCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
