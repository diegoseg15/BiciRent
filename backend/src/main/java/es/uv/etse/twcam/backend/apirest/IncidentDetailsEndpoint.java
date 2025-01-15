package es.uv.etse.twcam.backend.apirest;

import es.uv.etse.twcam.backend.business.Services.IncidentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "IncidentDetailsEndpoint", urlPatterns = { "/api/incidents/details" })
public class IncidentDetailsEndpoint extends HttpServlet {

    private final IncidentService incidentService = new IncidentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response);

        String id = request.getParameter("id");
        String acciones = request.getParameter("acciones");
        String piezas = request.getParameter("piezas");
        String detalles = request.getParameter("detalles");

        if (id == null || acciones == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Faltan parámetros obligatorios\"}");
            return;
        }


        boolean registered = incidentService.registerIncidentDetails(id, acciones, piezas, detalles);
        if (registered) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Detalles registrados exitosamente\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Incidencia no encontrada\"}");
        }
    }

    private void addCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
