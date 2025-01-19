package es.uv.etse.twcam.backend.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testConstructorAndGetters() {
        User user = new User("12345678A", "Juan", "Pérez", "600123456", "juan.perez@example.com", "password123", "admin");

        assertEquals("12345678A", user.getDni());
        assertEquals("Juan", user.getNombre());
        assertEquals("Pérez", user.getApellido());
        assertEquals("600123456", user.getTelefono());
        assertEquals("juan.perez@example.com", user.getCorreo());
        assertEquals("password123", user.getPassword());
        assertEquals("admin", user.getRol());
    }

    @Test
    public void testSetters() {
        User user = new User();

        user.setDni("87654321B");
        user.setNombre("María");
        user.setApellido("López");
        user.setTelefono("600987654");
        user.setCorreo("maria.lopez@example.com");
        user.setPassword("securepass");
        user.setRol("user");

        assertEquals("87654321B", user.getDni());
        assertEquals("María", user.getNombre());
        assertEquals("López", user.getApellido());
        assertEquals("600987654", user.getTelefono());
        assertEquals("maria.lopez@example.com", user.getCorreo());
        assertEquals("securepass", user.getPassword());
        assertEquals("user", user.getRol());
    }

    @Test
    public void testDefaultConstructor() {
        User user = new User();

        assertNull(user.getDni());
        assertNull(user.getNombre());
        assertNull(user.getApellido());
        assertNull(user.getTelefono());
        assertNull(user.getCorreo());
        assertNull(user.getPassword());
        assertNull(user.getRol());
    }
}
