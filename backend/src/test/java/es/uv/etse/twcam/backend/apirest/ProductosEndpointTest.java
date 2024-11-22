package es.uv.etse.twcam.backend.apirest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.uv.etse.twcam.backend.business.Comment;
import es.uv.etse.twcam.backend.business.ProductException;
import es.uv.etse.twcam.backend.business.Producto;
import es.uv.etse.twcam.backend.business.ProductsServiceDictionaryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import org.apache.logging.log4j.*;

/**
 * Tests unitarios sobre el Endpoint the productos
 */
public class ProductosEndpointTest {

    /**
     * Logger
     */
    private static Logger logger = null;

    /**
     * Parser JSON de Google
     */
    private static Gson gson = null;

    /**
     * Crea un logger.
     */
    @BeforeAll
    public static void setLogger() {
        logger = LogManager.getLogger(ProductosEndpointTest.class.getName());
    }

    /**
     * Crea un parser JSON
     */
    @BeforeAll
    public static void setGson() {
        gson = new GsonBuilder().create();
    }

    /**
     * Configura el servicio con datos de prueba.
     * @throws ProductException Error en la configuración.
     */
    @BeforeAll
    public static void ProductsService() throws ProductException {

        ProductsServiceDictionaryImpl.clearInstance();
        ProductsServiceDictionaryImpl service = ProductsServiceDictionaryImpl.getInstance();
        
        List<Comment> comentarios = new ArrayList<Comment>();
        
        Comment comentario = new Comment(3, "Comentario medio", "Autor medio", "2016-12-03T07:52:24.236094Z");

        comentarios.add(comentario);

        comentario = new Comment(0, "Comentario malo", "Autor estricto", "2017-10-16T12:32:23.126094Z");

        comentarios.add(comentario);

        Producto producto = new Producto(1, "Crece pelo", 10.5, "producto.jpg", true, comentarios);

        producto = service.create(producto);

        comentarios = new ArrayList<Comment>();

        comentario = new Comment(5, "Comentario muy bueno", "Autor pelota", "2017-10-16T12:32:23.126094Z");

        comentarios.add(comentario);

        producto = new Producto(2, "Peluquín", 20.5, "producto2.jpg", false, comentarios);

        producto = service.create(producto);
    }

    /**
     * Ejecuta un test sobre el método getProductoId
     * 
     * @param url URL del API REST.
     * @return Resultado al invocar getProductoId
     * @throws APIRESTException Excepción en la llamada
     */
    private Integer executeGetProductIdInTest(String url) throws APIRESTException {

        HttpServletRequest request = mock(HttpServletRequest.class);

        StringBuffer buffer = new StringBuffer();
        buffer.append(url);
        when(request.getRequestURL()).thenReturn(buffer);

        Integer result = null;

        result = ProductosEndpoint.getProductoId(request);

        return result;
    }

    /**
     * Validar el caso correcto de llamada al endpoint de productos sin identificador
     */
    @Test
    void testGetProductoId1() {

        Integer result = null;

        try {
            result = executeGetProductIdInTest("http://localhost:8080/api/productos");

            assertNull(result);

            logger.info("URL correcta");
        } catch (Exception e) {
            assertNull(e);
        }
    }


    /**
     * Validar el caso incorrecto de llamada al endpoint de productos sin
     * identificador
     */
    @Test
    void testGetProductoId2() {

        try {
            executeGetProductIdInTest("http://localhost:8080/api/productos/");
        } catch (Exception e) {
            logger.info("URL es errónea porque acaba el /");

            assertNotNull(e);
        }
    }

    /**
     * Validar el caso correcto de llamada al endpoint de productos con
     * identificador
     */
    @Test
    void testGetProductoId3() {

        Integer result = null;

        try {
            result = executeGetProductIdInTest("http://localhost:8080/api/productos/1");

            assertEquals(Integer.valueOf(1), result);

            logger.info("URL correcta");

        } catch (Exception e) {
            assertNull(e);
        }
    }

    /**
     * Validar el caso correcto de llamada al endpoint de productos sin
     * identificador y atributo de ofertas
     */
    @Test
    void testGetProductoId4() {

        Integer result = null;

        try {
            result = executeGetProductIdInTest("http://localhost:8080/api/productos?oferta=true");

            assertNull(result);

            logger.info("URL correcta");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    /**
     * Validar el caso correcto de llamada al endpoint de productos GET /productos
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("GET /productos")
    void testDoGet1() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("oferta")).thenReturn(null);

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doGet(request, response);

        Producto[] result = gson.fromJson(sw.getBuffer().toString().trim(), Producto[].class);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    /**
     * Validar el caso correcto de llamada al endpoint de productos GET
     * /productos?oferta=true
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("GET /productos?oferta=true")
    void testDoGet2() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("oferta")).thenReturn("true");

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doGet(request, response);

        Producto[] result = gson.fromJson(sw.getBuffer().toString().trim(), Producto[].class);
        assertNotNull(result);
        assertEquals(1, result.length);

    }

    /**
     * Validar el caso correcto de llamada al endpoint de productos GET
     * /productos?oferta=false
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("GET /productos?oferta=false")
    void testDoGet3() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("oferta")).thenReturn("false");

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
        verify(response).addHeader("Content-Type", "application/json");

        Producto[] result = gson.fromJson(sw.getBuffer().toString().trim(), Producto[].class);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    /**
     * Validar el caso incorrecto de llamada al endpoint de productos GET
     * /productos/10000
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("GET productos/10000")
    void testDoGet4() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        StringBuffer buffer = new StringBuffer("http://localhost:8080/plc-pls-mps-tutorial/api/productos/10000");
        when(request.getRequestURL()).thenReturn(buffer);

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).addHeader("Content-Type", "application/json");
    }

    /**
     * Validar el caso correcto de llamada al endpoint de productos GET
     * /productos/1
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("GET productos/1")
    void testDoGet5() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        StringBuffer buffer = new StringBuffer("http://localhost:8080/plc-pls-mps-tutorial/api/productos/1");
        when(request.getRequestURL()).thenReturn(buffer);

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
        verify(response).addHeader("Content-Type", "application/json");

        Producto result = gson.fromJson(sw.getBuffer().toString().trim(), Producto.class);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(10.5, result.getPrecio());
        assertEquals(2,result.getComentarios().size());

    }
    
    /**
     * Validar el caso incorrecto de llamada al endpoint de productos PUT
     * /productos/100000
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("PUT productos/10000")
    void testDoPut1() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        StringBuffer urlBuffer = new StringBuffer("http://localhost:8080/plc-pls-mps-tutorial/api/productos/10000");

        when(request.getRequestURL()).thenReturn(urlBuffer);

        ServletInputStream mockServletInputStream = mock(ServletInputStream.class);

        when(mockServletInputStream.read(ArgumentMatchers.<byte[]>any(), anyInt(), anyInt())).thenAnswer(new Answer<Integer>() {

            String inputJSON = "{\"id\":10000,\"nombre\":\"Producto 1\",\"precio\":300,\"imagen\":\"imagenes/movil1.jpg\",\"oferta\":\"true\",\"comentarios\":[{\"estrellas\":5,\"comentario\":\"Producto funciona perfectamente, envío perfecto\",\"autor\":\"Juan García\",\"fecha\":\"2017-10-16T12:32:23.126094Z\"}]}";

            ByteArrayInputStream bytesInput = new ByteArrayInputStream(inputJSON.getBytes());

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                byte[] output = (byte[]) args[0];
                int offset = (int) args[1];
                int length = (int) args[2];
                return bytesInput.read(output, offset, length);
            }
        });

        when(request.getInputStream()).thenReturn(mockServletInputStream);

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    
    /**
     * Validar el caso incorrecto de llamada al endpoint de productos PUT
     * /productos/100000
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("PUT productos/10000 con ID 1")
    void testDoPut2() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        StringBuffer urlBuffer = new StringBuffer("http://localhost:8080/plc-pls-mps-tutorial/api/productos/10000");

        when(request.getRequestURL()).thenReturn(urlBuffer);

        ServletInputStream mockServletInputStream = mock(ServletInputStream.class);

        when(mockServletInputStream.read(ArgumentMatchers.<byte[]>any(), anyInt(), anyInt()))
                .thenAnswer(new Answer<Integer>() {

                    String inputJSON = "{\"id\":1,\"nombre\":\"Producto 1\",\"precio\":300,\"imagen\":\"imagenes/movil1.jpg\",\"oferta\":\"true\",\"comentarios\":[{\"estrellas\":5,\"comentario\":\"Producto funciona perfectamente, envío perfecto\",\"autor\":\"Juan García\",\"fecha\":\"2017-10-16T12:32:23.126094Z\"}]}";

                    ByteArrayInputStream bytesInput = new ByteArrayInputStream(inputJSON.getBytes());

                    @Override
                    public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                        Object[] args = invocationOnMock.getArguments();
                        byte[] output = (byte[]) args[0];
                        int offset = (int) args[1];
                        int length = (int) args[2];
                        return bytesInput.read(output, offset, length);
                    }
                });

        when(request.getInputStream()).thenReturn(mockServletInputStream);

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Validar el caso incorrecto de llamada al endpoint de productos PUT
     * /productos/1
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("PUT productos/1")
    void testDoPut3() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        StringBuffer urlBuffer = new StringBuffer("http://localhost:8080/plc-pls-mps-tutorial/api/productos/1");

        when(request.getRequestURL()).thenReturn(urlBuffer);

        ServletInputStream mockServletInputStream = mock(ServletInputStream.class);

        when(mockServletInputStream.read(ArgumentMatchers.<byte[]>any(), anyInt(), anyInt()))
                .thenAnswer(new Answer<Integer>() {

                    String inputJSON = "{\"id\":1,\"nombre\":\"Producto 1\",\"precio\":300,\"imagen\":\"imagenes/movil1.jpg\",\"oferta\":\"true\",\"comentarios\":[{\"estrellas\":5,\"comentario\":\"Producto funciona perfectamente, envío perfecto\",\"autor\":\"Juan García\",\"fecha\":\"2017-10-16T12:32:23.126094Z\"}]}";

                    ByteArrayInputStream bytesInput = new ByteArrayInputStream(inputJSON.getBytes());

                    @Override
                    public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                        Object[] args = invocationOnMock.getArguments();
                        byte[] output = (byte[]) args[0];
                        int offset = (int) args[1];
                        int length = (int) args[2];
                        return bytesInput.read(output, offset, length);
                    }
                });

        when(request.getInputStream()).thenReturn(mockServletInputStream);

        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
        verify(response).addHeader("Content-Type", "application/json");

        // Validación
        ProductsServiceDictionaryImpl service = ProductsServiceDictionaryImpl.getInstance();

        Producto producto = service.find(1);

        assertEquals("Producto 1", producto.getNombre());
    }
    
    /**
     * Validar inovación a OPTIONS
     * 
     * @throws Exception Propagación de errores
     */
    @Test
    @DisplayName("OPTIONS productos/")
    void testOptions() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ProductosEndpoint endpoint = new ProductosEndpoint();
        endpoint.doOptions(request, response);

        verify(response).addHeader("Content-Type", "application/json");
        verify(response).addHeader("Access-Control-Allow-Credentials", "true");
        verify(response).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
        verify(response).addHeader("Access-Control-Allow-Headers", "authorization,content-type");
        verify(response).addHeader("Access-Control-Allow-Origin", "*");
    }
}