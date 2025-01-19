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

    // Instancia del servicio de negocio que gestiona las incidencias
    private final IncidentService incidentService = new IncidentService();

    /**
     * Método HTTP GET: Devuelve todas las incidencias en formato JSON.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP que se devolverá.
     * @throws IOException En caso de error al escribir en la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response); // Agrega los encabezados CORS para permitir solicitudes desde un frontend externo.

        response.setContentType("application/json"); // Especifica el tipo de contenido como JSON.
        response.setCharacterEncoding("UTF-8"); // Configura la codificación de caracteres.

        // Construcción de un arreglo JSON con las incidencias.
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Incident incident : incidentService.getAllIncidents()) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("id", incident.getId())
                    .add("descripcion", incident.getDescripcion())
                    .add("ubicacion", incident.getUbicacion())
                    .add("situacion", incident.getSituacion())
                    .add("estado", incident.getEstado())
                    .add("tecnico", incident.getTecnico())
                    .add("bicicleta", incident.getBicicleta()));
        }

        // Escribe el arreglo JSON en la respuesta HTTP.
        response.getWriter().write(arrayBuilder.build().toString());
    }

    /**
     * Método HTTP PUT: Permite actualizar el estado de una incidencia específica.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP que se devolverá.
     * @throws IOException En caso de error al escribir en la respuesta.
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response); // Agrega los encabezados CORS.

        // Obtiene los parámetros necesarios de la solicitud.
        String id = request.getParameter("id");
        String newState = request.getParameter("estado");

        // Verifica que los parámetros necesarios estén presentes.
        if (id == null || newState == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Código de estado: 400 (Bad Request).
            response.getWriter().write("{\"error\": \"Faltan parámetros obligatorios\"}");
            return;
        }

        // Lógica específica para cambiar el estado a "completada".
        if ("completada".equals(newState)) {
            Incident incident = incidentService.getIncidentById(id);
            // Verifica que el estado actual sea válido para la transición a "completada".
            if (incident != null && ("en proceso".equals(incident.getEstado())
                    || "Esperando repuestos".equals(incident.getEstado()))) {
                boolean updated = incidentService.updateIncidentState(id, newState);
                if (updated) {
                    response.setStatus(HttpServletResponse.SC_OK); // Código de estado: 200 (OK).
                    response.getWriter().write("{\"message\": \"Estado cambiado a 'completada' exitosamente\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Código de estado: 500 (Error interno).
                    response.getWriter().write("{\"error\": \"No se pudo actualizar el estado a 'completada'\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Código de estado: 400 (Bad Request).
                response.getWriter().write("{\"error\": \"Estado no válido para cambiar a 'completada'\"}");
            }
        } else {
            // Manejo general para otros estados.
            boolean updated = incidentService.updateIncidentState(id, newState);
            if (updated) {
                response.setStatus(HttpServletResponse.SC_OK); // Código de estado: 200 (OK).
                response.getWriter().write("{\"message\": \"Estado actualizado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Código de estado: 404 (No encontrado).
                response.getWriter().write("{\"error\": \"Incidencia no encontrada o estado no válido\"}");
            }
        }
    }

    /**
     * Método HTTP OPTIONS: Responde a las solicitudes preflight CORS.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP que se devolverá.
     */
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        addCORSHeaders(response); // Responde con los encabezados necesarios para CORS.
    }

    /**
     * Agrega los encabezados necesarios para CORS a la respuesta HTTP.
     *
     * @param response La respuesta HTTP que se devolverá.
     */
    private void addCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Permite solicitudes desde esta URL.
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS"); // Métodos HTTP permitidos.
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Encabezados permitidos.
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Permite el uso de credenciales (cookies).
    }
}
