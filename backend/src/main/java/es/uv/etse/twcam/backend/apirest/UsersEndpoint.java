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
import org.apache.logging.log4j.*;

@WebServlet(name = "UsersEndpoint", urlPatterns = { "/api/login" })
public class UsersEndpoint extends HttpServlet {
    private final UserService userService = new UserService();
    private static final Logger logger = LogManager.getLogger(UsersEndpoint.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Validación de parámetros
            String email = request.getParameter("correo");
            String password = request.getParameter("password");

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                logger.warn("Parámetros inválidos: correo o contraseña no proporcionados");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                addCORSHeaders(request, response);
                response.getWriter().write("{\"error\": \"Parámetros inválidos\"}");
                return;
            }

            logger.info("Solicitud de login recibida para correo: {}", email);

            // Intentar login
            User user = userService.login(email, password);

            if (user != null) {
                JsonObject jsonResponse = Json.createObjectBuilder()
                        .add("dni", user.getDni())
                        .add("nombre", user.getNombre())
                        .add("apellido", user.getApellido())
                        .add("telefono", user.getTelefono())
                        .add("correo", user.getCorreo())
                        .add("rol", user.getRol())
                        .build();

                addCORSHeaders(request, response);
                PrintWriter pw = response.getWriter();
                pw.print(jsonResponse.toString());
                pw.flush();

            } else {
                logger.warn("Intento de login fallido para correo: {}", email);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                addCORSHeaders(request, response);
                response.getWriter().write("{\"error\": \"Usuario no autorizado\"}");
            }

        } catch (Exception e) {
            logger.error("Error procesando el login", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            addCORSHeaders(request, response);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        addCORSHeaders(request, response);
    }

    private void addCORSHeaders(HttpServletRequest request, HttpServletResponse response) {
        // Configuración dinámica del header CORS
        String origin = request.getHeader("Origin");
        if (origin != null && (origin.equals("https://dominio-seguro.com") || origin.equals("https://otro-dominio-seguro.com"))) {
            response.addHeader("Access-Control-Allow-Origin", origin);
        } else {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        response.addHeader("Content-Type", "application/json");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
    }
}