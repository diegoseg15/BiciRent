package es.uv.etse.twcam.backend.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import org.apache.logging.log4j.*;

/**
 * Test unitario para la clase ProductsServiceDictionaryImpl
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductsServiceDictionaryImplTest {

    /**
     * Logger
     */
    private static Logger logger = null;

    /**
     * Servicio a probar.
     */
    protected ProductsServiceDictionaryImpl service;

    
    /**
     * Crea el test unitario
     */
    public ProductsServiceDictionaryImplTest() {
        this.service = ProductsServiceDictionaryImpl.getInstance();
    }

    @BeforeAll
    public static void setLogger() {
        logger = LogManager.getLogger(ProductsServiceDictionaryImplTest.class.getName());
    }

    @Test
    @Order(1)
    void testGetInstance() {
        assertNotNull(service);
    }

    @Test
    @Order(2)
    void testCreate() throws ProductException {

        List<Comment> comentarios = new ArrayList<Comment>();

        Comment comentario = new Comment(3, "Comentario medio", "Autor medio", "Cadena de texto de fecha");

        comentarios.add(comentario);

        comentario = new Comment(0, "Comentario malo", "Autor estricto", "Cadena de texto de fecha");

        comentarios.add(comentario);

        Producto producto = new Producto(1, "Crece pelo", 10.5, "producto.jpg", true, comentarios);

        producto = service.create(producto);

        assertNotNull(producto);

        producto = new Producto(2, "Peluquín", 20.5, "producto2.jpg", false, null);

        producto = service.create(producto);

        assertNotNull(producto);

    }

    @Test
    @Order(3)
    void testListAll() {
        List<Producto> products = service.listAll();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    @Order(4)
    void testListSales() {
        List<Producto> products = service.listSales();
        assertNotNull(products);
        assertEquals(1, products.size());
    }

    @Test
    @Order(5)
    void testUpdate() throws ProductException {

        Producto producto = service.find(1);
        producto.setOferta(false);
        producto = service.update(producto);
        assertNotNull(producto);
        assertEquals(false, producto.isOferta());
    }
    
    @Test
    @Order(6)
    void testFind() throws ProductException {

        Producto producto = service.find(1);
        assertNotNull(producto);
        assertEquals(1, producto.getId());
        assertEquals(false, producto.isOferta());
        assertEquals(2, producto.getComentarios().size());
    }

    @Test
    @Order(7)
    void testFailedUpdate() {

        try {
            service.update(null);
            fail("El valor nulo en producto no estaba controlado");
        } catch (ProductException e) {
            logger.info("El valor nulo ha sido controlado correctamente.");
        }

    }

    @Test
    @Order(8)
    void testFailedCreationFromNull() {

        try {
            service.create(null);
            fail("El valor nulo en producto no estaba controlado");
        } catch (ProductException e) {
            logger.info("El valor nulo ha sido controlado correctamente.");
        }

    }

    @Test
    @Order(9)
    void testFailedCreationFromIdNull() {

        try {
            Producto p = new Producto(null, null, null, null, false, null);
            service.create(p);
            fail("El valor nulo en identificador de producto no estaba controlado");
        } catch (ProductException e) {
            logger.info("El valor nulo en identificador de ha sido controlado correctamente.");
        }

    }

    @Test
    @Order(10)
    void testClearInstance() {

        this.service.clearInstance();
        assertNotNull(this.service.getInstance());
        this.service.clearInstance();

    }
}
