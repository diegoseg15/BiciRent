package es.uv.etse.twcam.backend.apirest;

import es.uv.etse.twcam.backend.business.Incidencia;
import es.uv.etse.twcam.backend.business.Services.IncidenciaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import java.util.List;

@WebServlet(name = "ReportarEndpoint", urlPatterns = { "/api/incidencias" })
public class ReportarEndpoint extends HttpServlet {
    private final IncidenciaService incidenciaService = new IncidenciaService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        addCORSHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            JsonObject body = Json.createReader(request.getReader()).readObject();

            String fecha = body.getString("fecha");
            String hora = body.getString("hora");
            String ubicacion = body.getString("ubicacion");
            String descripcion = body.getString("descripcion");

            if (fecha == null || hora == null || ubicacion == null || descripcion == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Todos los campos son obligatorios\"}");
                return;
            }

            Incidencia incidencia = incidenciaService.registrarIncidencia(fecha, hora, ubicacion, descripcion,
                    "reportada", "bloqueada");

            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("id", incidencia.getIncidenciaId())
                    .add("fecha", incidencia.getFecha())
                    .add("hora", incidencia.getHora())
                    .add("ubicacion", incidencia.getUbicacion())
                    .add("descripcion", incidencia.getDescripcion())
                    .add("estado", incidencia.getEstado())
                    .add("situacion", incidencia.getSituacion())
                    .build();

            PrintWriter pw = response.getWriter();
            pw.print(jsonResponse.toString());
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String estado = request.getParameter("estado");

        try {
            List<Incidencia> incidencias;
            if (estado != null && !estado.isEmpty()) {
                incidencias = incidenciaService.obtenerPorEstado(estado);
            } else {
                incidencias = incidenciaService.obtenerTodas();
            }

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Incidencia inc : incidencias) {
                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", inc.getIncidenciaId())
                        .add("fecha", inc.getFecha())
                        .add("hora", inc.getHora())
                        .add("ubicacion", inc.getUbicacion())
                        .add("descripcion", inc.getDescripcion())
                        .add("estado", inc.getEstado())
                        .add("situacion", inc.getSituacion())
                        .add("tecnico", inc.getTecnico() != null ? inc.getTecnico() : ""));
            }

            PrintWriter pw = response.getWriter();
            pw.print(jsonArrayBuilder.build().toString());
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al obtener las incidencias\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            JsonObject body = Json.createReader(request.getReader()).readObject();
            String incidenciaId = body.getString("id");
            String tecnico = body.getString("tecnico");

            System.out.println("PUT recibido - ID: " + incidenciaId + ", Técnico: " + tecnico); // Agrega este log

            if (incidenciaId == null || tecnico == null || tecnico.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"El ID de incidencia y el técnico son obligatorios\"}");
                return;
            }

            Incidencia incidencia = incidenciaService.obtenerPorId(incidenciaId);
            if (incidencia != null && "reportada".equalsIgnoreCase(incidencia.getEstado())) {
                incidenciaService.asignarIncidencia(incidenciaId, tecnico);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Incidencia asignada con éxito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Incidencia no encontrada o ya asignada\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al asignar incidencia\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void addCORSHeaders(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Allow-Credentials", "true");
    }
}
