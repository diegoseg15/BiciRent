package es.uv.etse.twcam.backend.apirest;

import com.google.gson.Gson;

import es.uv.etse.twcam.backend.business.Bike;
import es.uv.etse.twcam.backend.business.Rental;
import es.uv.etse.twcam.backend.business.Station;
import es.uv.etse.twcam.backend.business.Services.RentalService;
import es.uv.etse.twcam.backend.business.Services.StationService;
import es.uv.etse.twcam.backend.business.Services.BikeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.apache.logging.log4j.*;

@WebServlet(name = "BikeStationsEndpoint", urlPatterns = { "/api/rent", "/api/rents", "/api/bikes", "/api/stations",
        "/api/devolucion" })
public class BikeStationsEndpoint extends HttpServlet {
    private final RentalService rentalService = new RentalService();
    private final BikeService bikeService = new BikeService();
    private final StationService stationService = new StationService();
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Habilitar CORS para solicitudes PUT
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String path = request.getServletPath();

        if (path.equals("/api/devolucion")) {
            handleDevolucion(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Habilitar CORS para solicitudes GET
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String path = request.getServletPath();

        if (path.equals("/api/bikes")) {
            handleGetBikes(request, response);
        } else if (path.equals("/api/stations")) {
            handleGetStations(request, response);
        } else if (path.equals("/api/rents")) {
            handleGetRents(request, response);
        }
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
                    || newRent.getBicicletaId() == null || newRent.getEstado() == null) {
                logger.warn("Parámetros inválidos para la renta de bicicleta: {}", newRent);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Parámetros inválidos\"}");
                return;
            }

            logger.info("Solicitud de renta recibida para el usuario: {} ",
                    newRent.getUsuarioId(), " bici: {} ", newRent.getBicicletaId(), " estado: {} ",
                    newRent.getEstado());

            // Intentar rentar la bicicleta
            if (rentalService.renta(newRent.getUsuarioId(), newRent.getBicicletaId(),
                    newRent.getFechaHoraRecogida(), newRent.getDistancia(), newRent.getEstado())) {
                response.getWriter().write("{\"message\": \"Renta guardada exitosamente\"}");
            } else {
                // En caso de que la renta ya esté registrada
                logger.warn("Renta ya registrada: {} => {} => {}",
                        newRent.getUsuarioId(), newRent.getBicicletaId(), newRent.getFechaHoraRecogida(),
                        newRent.getEstado());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"error\": \"La renta ya está registrada\"}");
            }

        } catch (Exception e) {
            logger.error("Error procesando la renta", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

    private void handleDevolucion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Leer JSON del cuerpo de la solicitud
            BufferedReader reader = request.getReader();
            Rental newRent = gson.fromJson(reader, Rental.class);

            // Buscar la renta
            Rental rent = rentalService.getRental(newRent.getUsuarioId(), newRent.getBicicletaId(),
                    newRent.getFechaHoraRecogida());

            // Validar los parámetros
            if (rent == null) {
                logger.warn("Renta no encontrada: {} => {} => {}",
                        newRent.getUsuarioId(), newRent.getBicicletaId(), newRent.getFechaHoraRecogida());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Renta no encontrada\"}");
                return;
            }

            logger.info("Solicitud de devolución recibida para el usuario: {} ",
                    newRent.getUsuarioId(), " bici: {} ", newRent.getBicicletaId(), " estado: {} ",
                    newRent.getEstado());

            // Intentar devolver la bicicleta
            if (rentalService.devolucion(newRent.getUsuarioId(), newRent.getBicicletaId(),
                    newRent.getFechaHoraRecogida(), newRent.getDistancia(), newRent.getEstado())) {
                response.getWriter().write("{\"message\": \"Devolución guardada exitosamente\"}");
            } else {
                // En caso de que la renta no exista
                logger.warn("Renta no encontrada: {} => {} => {}",
                        newRent.getUsuarioId(), newRent.getBicicletaId(), newRent.getFechaHoraRecogida(),
                        newRent.getEstado());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Renta no encontrada\"}");
            }

        } catch (Exception e) {
            logger.error("Error procesando la devolución", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

    private void handleGetBikes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Obtener el parámetro 'estacionId' de la URL
            String estacionId = request.getParameter("estacionId");

            List<Bike> bikesList;

            if (estacionId != null && !estacionId.isEmpty()) {
                // Si se proporcionó 'estacionId', filtrar las bicicletas por esta estación
                bikesList = bikeService.getBikesByEstacionId(estacionId);
            } else {
                // Si no se proporcionó 'estacionId', obtener todas las bicicletas
                bikesList = bikeService.getAllBikes();
            }

            // Convertir la lista de bicicletas a JSON
            String bikesJson = gson.toJson(bikesList);

            // Escribir la respuesta JSON
            response.getWriter().write(bikesJson);
        } catch (Exception e) {
            logger.error("Error obteniendo la lista de bicicletas", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

    private void handleGetRents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Obtener el parámetro 'usuarioId' de la URL
            String usuarioId = request.getParameter("usuarioId");

            List<Rental> rentaList;

            if (usuarioId != null && !usuarioId.isEmpty()) {
                // Si se proporcionó 'usuarioId', filtrar las rentas por este usuario
                rentaList = rentalService.getRentalAll(usuarioId);
            } else {
                // Si no se proporcionó 'usuarioId', devolver un mensaje informando el error
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"El parámetro 'usuarioId' es requerido\"}");
                return; // Detener la ejecución aquí
            }

            // Convertir la lista de rentas a JSON
            String rentaJson = gson.toJson(rentaList);

            // Escribir la respuesta JSON
            response.getWriter().write(rentaJson);
        } catch (Exception e) {
            logger.error("Error obteniendo la lista de rentas", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

    private void handleGetStations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Obtener todas las estaciones sin filtrar por 'stationId'
            List<Station> stationsList = stationService.getAllStations();

            // Convertir la lista de estaciones a JSON
            String stationsJson = gson.toJson(stationsList);

            // Escribir la respuesta JSON
            response.getWriter().write(stationsJson);
        } catch (Exception e) {
            logger.error("Error obteniendo la lista de estaciones", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno del servidor\"}");
        }
    }

}
