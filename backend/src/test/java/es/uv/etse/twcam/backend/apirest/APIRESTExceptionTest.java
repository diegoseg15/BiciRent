package es.uv.etse.twcam.backend.apirest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class APIRESTExceptionTest {

    @Test
    public void testExceptionMessage() {
        String message = "This is a custom exception message";
        APIRESTException exception = new APIRESTException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        APIRESTException exception = new APIRESTException("Test message");

        assertTrue(exception instanceof RuntimeException);
    }
}
