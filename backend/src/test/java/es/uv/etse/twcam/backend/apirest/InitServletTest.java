package es.uv.etse.twcam.backend.apirest;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class InitServletTest {

    @Test
    public void testInitServletInitialization() {
        InitServlet initServlet = new InitServlet();

        // Simular un ServletConfig para el método init
        ServletConfig config = Mockito.mock(ServletConfig.class);

        // Verificar que no se lanza ninguna excepción al inicializar
        assertDoesNotThrow(() -> {
            initServlet.init(config);
        });
    }

    @Test
    public void testInitLogsMessage() {
        InitServlet initServlet = new InitServlet();

        // Simular un ServletConfig para el método init
        ServletConfig config = Mockito.mock(ServletConfig.class);

        // Redirigir la salida estándar para capturar el mensaje de inicialización
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        try {
            initServlet.init(config);
        } catch (ServletException e) {
            fail("ServletException should not have been thrown.");
        }

        // Verificar que el mensaje de inicialización se imprimió correctamente
        String output = outputStream.toString().trim();
        assertEquals("BiciRent API inicializada correctamente", output);

        // Restaurar la salida estándar
        System.setOut(System.out);
    }
}
