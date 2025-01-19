package es.uv.etse.twcam.backend.apirest;

import es.uv.etse.twcam.backend.business.Incidencia;
import es.uv.etse.twcam.backend.business.Services.IncidenciaService;
import es.uv.etse.twcam.backend.business.Services.IncidentService;

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
import jakarta.json.JsonReader;

import java.util.List;

@WebServlet(name = "ReportarEndpoint", urlPatterns = { "/api/incidencias" })
public class ReportarEndpoint extends HttpServlet {
    private final IncidenciaService incidenciaService = new IncidenciaService();
    private final IncidentService incidentService = new IncidentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        addCORSHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (JsonReader jsonReader = Json.createReader(request.getReader())) {
            JsonObject body = Json.createReader(request.getReader()).readObject();

            String fecha = body.getString("fecha");
            String hora = body.getString("hora");
            String ubicacion = body.getString("ubicacion");
            String descripcion = body.getString("descripcion");
            String dni = body.getString("dni", null);
            String bicicleta = body.getString("bicicleta", null);
    

            if (fecha == null || hora == null || ubicacion == null || descripcion == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Todos los campos son obligatorios\"}");
                return;
            }

            Incidencia incidencia = incidenciaService.registrarIncidencia(fecha, hora, ubicacion, descripcion,
                    "reportada", "bloqueada", dni, bicicleta);

            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("id", incidencia.getIncidenciaId())
                    .add("fecha", incidencia.getFecha())
                    .add("hora", incidencia.getHora())
                    .add("ubicacion", incidencia.getUbicacion())
                    .add("descripcion", incidencia.getDescripcion())
                    .add("estado", incidencia.getEstado())
                    .add("situacion", incidencia.getSituacion())
                    .add("dni", incidencia.getDni() != null ? incidencia.getDni() : "")
                    .add("bicicleta", incidencia.getBicicleta() != null ? incidencia.getBicicleta() : "")
                    .build();

            PrintWriter pw = response.getWriter();
            pw.print(jsonResponse.toString());
            pw.flush();

            incidentService.loadIncidentsFromFile();

        } catch (Exception e) {
            //e.printStackTrace();
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
                        .add("tecnico", inc.getTecnico() != null ? inc.getTecnico() : "")
                        .add("dni", inc.getDni() != null ? inc.getDni() : "")
                        .add("bicicleta", inc.getBicicleta() != null ? inc.getBicicleta() :"")
                );
            }

            PrintWriter pw = response.getWriter();
            pw.print(jsonArrayBuilder.build().toString());
            pw.flush();
        } catch (Exception e) {
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al obtener las incidencias\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (JsonReader jsonReader = Json.createReader(request.getReader())) {
            JsonObject body = Json.createReader(request.getReader()).readObject();
            String id = body.getString("id");
            String tecnico = body.getString("tecnico");

            System.out.println("PUT recibido - ID: " + id + ", Técnico: " + tecnico); // Agrega este log

            if (id == null || tecnico == null || tecnico.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"El ID de incidencia y el técnico son obligatorios\"}");
                return;
            }

            Incidencia incidencia = incidenciaService.obtenerPorId(id);
            if (incidencia != null && "reportada".equalsIgnoreCase(incidencia.getEstado())) {
                incidenciaService.asignarIncidencia(id, tecnico);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Incidencia asignada con éxito\"}");
                incidentService.loadIncidentsFromFile();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Incidencia no encontrada o ya asignada\"}");
            }
            
        } catch (Exception e) {
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al asignar incidencia\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"El ID de la incidencia es obligatorio\"}");
            return;
        }

        try {
            Incidencia incidencia = incidenciaService.obtenerPorId(id);
            if (incidencia != null) {
                incidenciaService.eliminarIncidencia(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Incidencia eliminada con éxito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Incidencia no encontrada\"}");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al eliminar la incidencia\"}");
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
