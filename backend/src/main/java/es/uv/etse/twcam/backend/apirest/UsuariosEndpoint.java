package es.uv.etse.twcam.backend.apirest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.uv.etse.twcam.backend.business.Usuario; // Cambiar a la clase correcta de Usuario
import es.uv.etse.twcam.backend.business.UsersService; // Servicio para gestionar usuarios
import es.uv.etse.twcam.backend.business.UsersServiceDictionaryImpl; // Implementación del servicio

import org.apache.logging.log4j.*;

/**
 * Implementación básica del Endpoint <b>Usuarios</b>.
 */
@WebServlet("/api/usuarios/*")
public class UsuariosEndpoint extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger(UsuariosEndpoint.class.getName());

    /**
     * Nombre del endpoint
     */
    private static final String END_POINT_NAME = "usuarios";

    /**
     * Gson parser
     */
    private final Gson g = new GsonBuilder().create();

    /**
     * Servicio sobre usuarios.
     */
    private static UsersService service = UsersServiceDictionaryImpl.getInstance();

    public UsuariosEndpoint() {
        super();
        logger.info("Usuarios EndPoint creado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String result = null;
        Integer id = null;

        try {
            id = getUsuarioId(request);
        } catch (Exception e) {
            logger.info("No se ha podido obtener el identificador del request");
        }

        logger.info("GET at {} with ID: {}", request.getContextPath(), id);

        if (id == null) {
            List<Usuario> usuarios = service.listAll();
            result = g.toJson(usuarios);
        } else {
            try {
                Usuario usuario = service.find(id);
                result = g.toJson(usuario);
            } catch (Exception e) {
                logger.error("Usuario no encontrado");
            }
        }

        addCORSHeaders(response);

        if (result != null) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            result = "{}";
        }

        try (PrintWriter pw = response.getWriter()) {
            pw.println(result);
        } catch (IOException ex) {
            logger.error("Imposible enviar respuesta", ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = null;

        try {
            usuario = getUsuarioFromRequest(request);

            if (usuario == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                addCORSHeaders(response);
                logger.error("Usuario no actualizado porque no se puede extraer desde JSON");
            } else {
                usuario = service.update(usuario);
                logger.info("PUT at: {} with {}", request.getContextPath(), usuario);

                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                addCORSHeaders(response);

                try (PrintWriter pw = response.getWriter()) {
                    pw.println(g.toJson(usuario));
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.error("Usuario no actualizado", e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        addCORSHeaders(response);
        try {
            super.doOptions(request, response);
        } catch (ServletException | IOException e) {
            logger.error("Error genérico", e);
        }
    }

    private Usuario getUsuarioFromInputStream(InputStream stream) {
        Usuario usuario = null;
        try {
            usuario = g.fromJson(new InputStreamReader(stream), Usuario.class);
        } catch (Exception e) {
            logger.error("Error al obtener usuario desde JSON", e);
        }
        return usuario;
    }

    protected static Integer getUsuarioId(HttpServletRequest request) throws APIRESTException {
        String url = request.getRequestURL().toString();
        int posIni = url.lastIndexOf("/");
        int posEnd = url.lastIndexOf("?");

        if (posEnd < 0) {
            posEnd = url.length();
        }

        String id = url.substring(posIni + 1, posEnd);
        logger.debug("ID: {}", id);

        if (id.trim().isEmpty()) {
            throw new APIRESTException("Faltan parámetros en el EndPoint");
        }

        Integer valor = null;
        if (!id.equals(END_POINT_NAME)) {
            valor = Integer.valueOf(id);
        }

        return valor;
    }

    private Usuario getUsuarioFromRequest(HttpServletRequest request) {
        Usuario usuario = null;
        try {
            Integer id = getUsuarioId(request);
            if (id != null) {
                usuario = getUsuarioFromInputStream(request.getInputStream());
                if (usuario != null && !usuario.getId().equals(id)) {
                    usuario = null;
                }
            }
        } catch (Exception e) {
            usuario = null;
        }
        return usuario;
    }

    private void addCORSHeaders(HttpServletResponse response) {
        response.addHeader("Content-Type", "application/json");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}
