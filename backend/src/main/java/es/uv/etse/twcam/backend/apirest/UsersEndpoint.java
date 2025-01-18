package es.uv.etse.twcam.backend.apirest;

import com.google.gson.Gson;
import es.uv.etse.twcam.backend.business.User;
import es.uv.etse.twcam.backend.business.Services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.apache.logging.log4j.*;

@WebServlet(name = "UsersEndpoint", urlPatterns = { "/api/login", "/api/register" })
public class UsersEndpoint extends HttpServlet {
    private final UserService userService = new UserService();
    private static final Logger logger = LogManager.getLogger(UsersEndpoint.class.getName());
    private static final Gson gson = new Gson();

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Habilitar CORS para solicitudes preflight
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK); // Responder con 200 OK
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Habilitar CORS para solicitudes POST
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String path = request.getServletPath();

        if (path.equals("/api/login")) {
            handleLogin(request, response);
        } else if (path.equals("/api/register")) {
            handleRegister(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Leer JSON del cuerpo de la solicitud
            BufferedReader reader = request.getReader();
            User loginRequest = gson.fromJson(reader, User.class);

            if (loginRequest.getCorreo() == null || loginRequest.getPassword() == null) {
                logger.warn("Parámetros inválidos: correo o contraseña no proporcionados");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Parámetros inválidos\"}");
                return;
            }

            logger.info("Solicitud de login recibida para correo: {}", loginRequest.getCorreo());

            // Verificar si el usuario existe con el correo y contraseña proporcionados
            User user = userService.login(loginRequest.getCorreo(), loginRequest.getPassword());

            if (user != null) {
                // Responder con los datos del usuario en caso de login exitoso
                JsonObject jsonResponse = Json.createObjectBuilder()
                        .add("dni", user.getDni())
                        .add("nombre", user.getNombre())
                        .add("apellido", user.getApellido())
                        .add("telefono", user.getTelefono())
                        .add("correo", user.getCorreo())
                        .add("rol", user.getRol())
                        .build();

                response.getWriter().write(jsonResponse.toString());
            } else {
                // En caso de login fallido
                logger.warn("Intento de login fallido para correo: {}", loginRequest.getCorreo());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Usuario no autorizado\"}");
            }

        } catch (Exception e) {
            logger.error("Error procesando el login", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Leer JSON del cuerpo de la solicitud
            BufferedReader reader = request.getReader();
            User newUser = gson.fromJson(reader, User.class);

            if (newUser.getCorreo() == null || newUser.getPassword() == null || newUser.getDni() == null) {
                logger.warn("Parámetros inválidos para registro");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Parámetros inválidos\"}");
                return;
            }

            logger.info("Solicitud de registro recibida para correo: {}", newUser.getCorreo());

            // Intentar registrar el nuevo usuario
            if (userService.register(newUser.getDni(), newUser.getNombre(), newUser.getApellido(),
                    newUser.getTelefono(), newUser.getCorreo(), newUser.getPassword(), newUser.getRol())) {
                response.getWriter().write("{\"message\": \"Usuario registrado exitosamente\"}");
            } else {
                // En caso de que el correo ya esté registrado
                logger.warn("Correo ya registrado: {}", newUser.getCorreo());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"error\": \"El correo ya está registrado\"}");
            }

        } catch (Exception e) {
            logger.error("Error procesando el registro", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }
}
