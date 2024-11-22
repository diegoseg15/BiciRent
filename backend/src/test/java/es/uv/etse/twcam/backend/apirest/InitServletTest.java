package es.uv.etse.twcam.backend.apirest;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.logging.log4j.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.uv.etse.twcam.backend.business.Producto;
import es.uv.etse.twcam.backend.business.ProductsService;

public class InitServletTest {

    /**
     * Logger
     */
    private static Logger logger = null;
    
    @BeforeAll
    public static void setLogger() {
        logger = LogManager.getLogger(InitServletTest.class.getName());
    }

    @Test
    void testInit() throws Exception {

        InitServlet servlet = new InitServlet();

        assertNotNull(servlet);

        try {
            servlet.init();
            fail("La inicialización sin base de datos no ha fallado");
        } catch (ServletException se) {
            logger.info("La inicialización sin base de datos ha fallado de forma controlada");
        }

    }

    @Test
    void testInitProductsService() throws Exception {


        InputStream jsonStream = InitServlet.class.getClassLoader().getResourceAsStream("db.json");

        ProductsService service = InitServlet.initProductsService(jsonStream);
        
        assertNotNull(service);
        
        List<Producto> productos = service.listAll();

        assertNotNull(productos);
        assertEquals(4, productos.size());
    }
}
