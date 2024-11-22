package es.uv.etse.twcam.backend.business;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class IncorrectProductExceptionTest {

    @Test
    void testIncorrectProductException() {

        IncorrectProductException obj = new IncorrectProductException("Mensaje de error");

        assertNotNull(obj);
    }
}
