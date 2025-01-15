package es.uv.etse.twcam.backend.apirest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TecnicoEndpoint", urlPatterns = {"/api/tecnicos"})
public class TecnicoEndpoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String[] tecnicos = {"tecnico1","tecnico2","tecnico3","tecnico4"};

        //Construir el JSON de respuesta
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String tecnico : tecnicos) {
            jsonArrayBuilder.add(tecnico);
        }

        //Enviar la respuesta JSON
        PrintWriter pw = response.getWriter();
        pw.print(jsonArrayBuilder.build().toString());
        pw.flush();
    }
}
