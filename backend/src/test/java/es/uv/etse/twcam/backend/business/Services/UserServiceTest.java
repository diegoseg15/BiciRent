package es.uv.etse.twcam.backend.business.Services;

import es.uv.etse.twcam.backend.business.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Esditar loadUsersFromFile
        userService = new UserService() {
            @Override
            public void loadUsersFromFile() {
                users = new ArrayList<>();
                users.add(
                        new User("12345678A", "Juan", "Pérez", "123456789", "juan@example.com", "password123", "user"));
                users.add(
                        new User("87654321B", "Ana", "García", "987654321", "ana@example.com", "securepass", "admin"));

            }

            @Override
            public void saveUsersToFile() {
                // No guardar en archivo durante los tests
            }
        };
    }

    @Test
    void testLogin() {
        // Caso válido
        User user = userService.login("juan@example.com", "password123");
        assertNotNull(user);
        assertEquals("Juan", user.getNombre());

        // Validar contraseña incorrecta
        user = userService.login("juan@example.com", "wrongpassword");
        assertNull(user);

        // Validar correo no registrado
        user = userService.login("noexist@example.com", "password123");
        assertNull(user);
    }

    @Test
    void testRegister() {
        // Registro válido
        boolean result = userService.register("55555555C", "Pedro", "López", "1122334455", "pedro@example.com",
                "newpassword", "user");
        assertTrue(result);

        // Registrar correo ya registrado
        result = userService.register("66666666D", "Maria", "Hernández", "5566778899", "juan@example.com",
                "password123", "user");
        assertFalse(result);
    }
}
