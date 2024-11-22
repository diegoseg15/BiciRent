package es.uv.etse.twcam.backend.apirest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.uv.etse.twcam.backend.business.Producto;
import es.uv.etse.twcam.backend.business.ProductsService;
import es.uv.etse.twcam.backend.business.ProductsServiceDictionaryImpl;

import org.apache.logging.log4j.*;

/**
 * Implementaci&oacute;n b&aacute;sica del Endpoint <b>Productos</b>.
 * 
 * @author <a href="mailto:raul.penya@uv.es">Ra&uacute;l Pe&ntilde;a-Ortiz</a>
 */
@WebServlet("/api/productos/*") // <1>
public class ProductosEndpoint extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(ProductosEndpoint.class.getName());  // <7>

	/**
	 * Nombre del endpoint
	 */
	private static final String END_POINT_NAME = "productos";

	/**
	 * Gson parser
	 */
	private final Gson g = new GsonBuilder().create();

	/**
	 * Servicio sobre productos.
	 */
	private static ProductsService service = ProductsServiceDictionaryImpl.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductosEndpoint() {
		super();
		logger.info("Product EndPoint creado"); // <7>
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String result = null;
		Integer id = null;

		try {
			id = getProductoId(request);
		} catch (Exception e) {
			logger.info("No se ha podido obtener el identificador del request"); // <7>
		}
		
		logger.info("GET at {} with ID: {}", request.getContextPath(),id); // <7>
		
		if (id == null) {
			List<Producto> productos = null;
			String oferta = request.getParameter("oferta");

			if (oferta!= null && oferta.equals("true")) {
				logger.info("GET sales"); // <7>
				productos = service.listSales();
			} else
				productos = service.listAll();

			result = g.toJson(productos);
		} else {
			try {
				Producto pro = service.find(id);
				result = g.toJson(pro);
			} catch (Exception e) {
				logger.error("Producto no encontrado"); // <7>
			}	
		}

		addCORSHeaders(response); // <2>

		if (result != null) {
			response.setStatus(HttpServletResponse.SC_ACCEPTED);// <3>
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);// <3>
			result="{}";
		}

		try {
			PrintWriter pw = response.getWriter();
			pw.println(result);
			pw.flush();
			pw.close();
		} catch (IOException ex) {
			logger.error("Imposible enviar respuesta",ex); // <7>
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		
		Producto pro = null;
		
		try {

			pro = getProductoFromRequest(request);
			
			if (pro == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND); // <3>
				addCORSHeaders(response); // <2>
				logger.error("Producto no actualizado por no se puede extraer desde JSON"); // <7>
			} else {
				pro = service.update(pro);

				logger.info("PUT at: {} with {} ", request.getContextPath(), pro); // <7>

				response.setStatus(HttpServletResponse.SC_ACCEPTED); // <3>
				addCORSHeaders(response); // <2>

				PrintWriter pw = response.getWriter();
				pw.println(g.toJson(pro));
				pw.flush();
				pw.close();
			}
			
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); // <3>
			logger.error("Producto no actualizado", e); // <7>
		}
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) {

		addCORSHeaders(response); // <2>

		try {
			super.doOptions(request, response);
		} catch (ServletException se) {
			logger.error("Error genérico en la clase padre"); // <7>
		} catch (IOException ioe) {
			logger.error("Error genérico de salida la clase padre"); // <7>
		}
	}

	/**
	 * Obtiene el Product de un stream JSON
	 * @param stream Stream JSON
	 * @return Product
	 */
	private Producto getProductFromInputStream(InputStream stream) { 

		Producto pro = null;

		try {

			pro = g.fromJson(new InputStreamReader(stream), Producto.class); // <4>

		} catch (Exception e) {
			pro = null;
			logger.error("Error al obtener producto desde JSON",e); // <7>
		}

		return pro;
	}
	
	/**
	 * Obtiene el identificador de un Producto como parte de la URL de la petición HTTP.
	 * @param request Petición HTTP
	 * @return Identificador del Producto
	 */
	protected static Integer getProductoId(HttpServletRequest request) throws APIRESTException{  // <5>
		
		String url = request.getRequestURL().toString();
		
		int posIni = url.lastIndexOf("/");

		int posEnd = url.lastIndexOf("?");

		if (posEnd < 0) {
			posEnd = url.length();
		}

		String id = url.substring(posIni+1,posEnd);
		
		logger.debug("ID: {}", id);// <7>
		
		if (id.trim().isEmpty()) {
			id = null;
		}

		if (id == null) {
			throw new APIRESTException("Faltan parámetros en el EndPoint");
		} else {
			if (id.equals(END_POINT_NAME)) {
				id = null;
			}
		}
		
		Integer valor = null;

		if (id != null) {
			valor = Integer.valueOf(id);
		}
		
		return valor;
	}
	
	/**
	 * Obtiene el Product desde la petición HTTP y el identificador como parte de la URL.
	 * @param request Petición HTTP
	 * @return Product
	 */
	private Producto getProductoFromRequest(HttpServletRequest request) {  

		Producto pro = null;
		
		try {

			Integer id = getProductoId(request);

			if (id != null) {
				pro = getProductFromInputStream(request.getInputStream());
				if (pro!=null && !pro.getId().equals(id)) // <6>
					pro = null;
			} 

		} catch (Exception e) {
			pro = null;
		}

		return pro;
	}

	/**
	 * Añade cabeceras Cross-origin resource sharing (CORS) para poder invocar el API
	 * REST desde Angular
	 * 
	 * @param response Repuesta HTTP a la que añadir cabeceras
	 */
	private void addCORSHeaders(HttpServletResponse response) { // <2>
		response.addHeader("Content-Type", "application/json");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
		response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
		response.addHeader("Access-Control-Allow-Origin", "*");
	}
}