package es.uv.etse.twcam.backend.apirest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TecnicoEndpoint", urlPatterns = { "/api/tecnicos" })
public class TecnicoEndpoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String[] tecnicos = { "tecnico1", "tecnico2", "tecnico3", "tecnico4" };

        // Construir el JSON de respuesta
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String tecnico : tecnicos) {
            jsonArrayBuilder.add(tecnico);
        }

        // Enviar la respuesta JSON
        PrintWriter pw = response.getWriter();
        pw.print(jsonArrayBuilder.build().toString());
        pw.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        addCORSHeaders(response); 
        response.setStatus(HttpServletResponse.SC_OK); 
    }

    private void addCORSHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Permitir el origen
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Allow-Credentials", "true");
    }

}
