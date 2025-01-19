package es.uv.etse.twcam.backend.apirest;

import es.uv.etse.twcam.backend.business.Services.IncidentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "IncidentDetailsEndpoint", urlPatterns = { "/api/incidents/details" })
public class IncidentDetailsEndpoint extends HttpServlet {

    // Instancia del servicio de negocio que gestiona las incidencias
    private final IncidentService incidentService = new IncidentService();

    /**
     * Método HTTP POST: Registra los detalles de una incidencia específica.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP que se devolverá.
     * @throws IOException En caso de error al escribir en la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response); // Agrega los encabezados CORS para permitir solicitudes externas.

        // Obtiene los parámetros enviados en la solicitud.
        String id = request.getParameter("id"); // ID de la incidencia
        String acciones = request.getParameter("acciones"); // Acciones realizadas durante la reparación
        String piezas = request.getParameter("piezas"); // Piezas reemplazadas, si las hay
        String detalles = request.getParameter("detalles"); // Detalles adicionales sobre la reparación
        String bicicleta = request.getParameter("bicicleta"); // Identificador de la bicicleta, si aplica

        // Verifica que los parámetros obligatorios estén presentes.
        if (id == null || acciones == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Código de estado: 400 (Bad Request)
            response.getWriter().write("{\"error\": \"Faltan parámetros obligatorios\"}");
            return; // Finaliza el manejo de la solicitud si faltan parámetros.
        }

        // Llama al servicio para registrar los detalles de la incidencia.
        boolean registered = incidentService.registerIncidentDetails(id, acciones, piezas, detalles, bicicleta);
        if (registered) {
            response.setStatus(HttpServletResponse.SC_OK); // Código de estado: 200 (OK)
            response.getWriter().write("{\"message\": \"Detalles registrados exitosamente\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Código de estado: 404 (No encontrado)
            response.getWriter().write("{\"error\": \"Incidencia no encontrada\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response);

        // Obtén el ID de la bicicleta de la solicitud
        String bicicletaId = request.getParameter("bicicleta");

        if (bicicletaId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Falta el parámetro obligatorio 'bicicleta'\"}");
            return;
        }

        // Llama al servicio para eliminar los detalles de reparación
        boolean deleted = incidentService.deleteIncidentDetailByBici(bicicletaId);

        if (deleted) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Detalles de reparación eliminados exitosamente\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"No se encontraron detalles para la bicicleta especificada\"}");
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
        addCORSHeaders(response); // Agrega los encabezados CORS necesarios.
        response.setStatus(HttpServletResponse.SC_OK); // Responde con código 200 (OK).
    }

    /**
     * Agrega los encabezados necesarios para solicitudes CORS.
     *
     * @param response La respuesta HTTP a la que se agregarán los encabezados.
     */
    private void addCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Permite solicitudes desde esta URL.
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS"); // Métodos HTTP permitidos.
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Encabezados permitidos.
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Permite el uso de credenciales (cookies).
    }
}
