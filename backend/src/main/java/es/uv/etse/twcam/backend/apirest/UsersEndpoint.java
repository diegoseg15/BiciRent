package es.uv.etse.twcam.backend.apirest;

import es.uv.etse.twcam.backend.business.User;
import es.uv.etse.twcam.backend.business.Services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@WebServlet(name = "UsersEndpoint", urlPatterns = {"/api/login"})
public class UsersEndpoint extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Simular parámetros
        String email = request.getParameter("correo");
        String password = request.getParameter("password");

        User user = userService.login(email, password);

        if (user != null) {
            JsonObject jsonResponse = Json.createObjectBuilder()
                .add("dni", user.getDni())
                .add("nombre", user.getNombre())
                .add("apellido", user.getApellido())
                .add("telefono", user.getTelefono())
                .add("correo", user.getCorreo())
                .add("password", user.getPassword())
                .add("rol", user.getRol())
                .build();

            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Usuario no autorizado\"}");
        }
    }
}
