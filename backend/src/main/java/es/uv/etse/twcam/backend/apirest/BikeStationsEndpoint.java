package es.uv.etse.twcam.backend.apirest;

import com.google.gson.Gson;
import es.uv.etse.twcam.backend.business.Rental;
import es.uv.etse.twcam.backend.business.Services.RentalService;
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

@WebServlet(name = "BikeStationsEndpoint", urlPatterns = { "/api/rent", "/api/bikes", "/api/stations" })
public class BikeStationsEndpoint extends HttpServlet {
    private final RentalService rentalService = new RentalService();
    private static final Logger logger = LogManager.getLogger(BikeStationsEndpoint.class.getName());
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

        if (path.equals("/api/rent")) {
            handleRent(request, response);
        }
        // else if (path.equals("/api/register")) {
        // handleRegister(request, response);
        // }
    }

    private void handleRent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Leer JSON del cuerpo de la solicitud
            BufferedReader reader = request.getReader();
            Rental newRent = gson.fromJson(reader, Rental.class);

            // Validar los parámetros
            if (newRent.getUsuarioId() == null || newRent.getFechaHoraRecogida() == null
                    || newRent.getBicicletaId() == null) {
                logger.warn("Parámetros inválidos para la renta de bicicleta: {}", newRent);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Parámetros inválidos\"}");
                return;
            }

            logger.info("Solicitud de renta recibida para el usuario: {} y la bici es: {}",
                    newRent.getUsuarioId(), newRent.getBicicletaId());

            // Intentar rentar la bicicleta
            if (rentalService.renta(newRent.getBicicletaId(), newRent.getUsuarioId(),
                    newRent.getFechaHoraRecogida())) {
                response.getWriter().write("{\"message\": \"Renta guardada exitosamente\"}");
            } else {
                // En caso de que la renta ya esté registrada
                logger.warn("Renta ya registrada: {} => {} => {}",
                        newRent.getUsuarioId(), newRent.getBicicletaId(), newRent.getFechaHoraRecogida());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"error\": \"La renta ya está registrada\"}");
            }

        } catch (Exception e) {
            logger.error("Error procesando la renta", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }
}
