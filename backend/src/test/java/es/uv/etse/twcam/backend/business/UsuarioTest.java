package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.Arrays;

import org.jmock.Expectations;
import static org.jmock.Expectations.returnValue;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


public class UsuarioTest {
    String user;
    String password;
    Usuario usuario;
    String nombre;
    String apellido; 
    String dni;
    
    @BeforeEach
    public void setUp() {
        user = "carolina";
        password = "123abc";
        usuario = new Usuario();
        nombre = "Friduchis";
        apellido = "khalo"; 
        dni = "123abc";
    }

    @Test
    public void loginTest(){
        boolean resultado = usuario.login(user,password);
        assertTrue(resultado);
    }

    @Test
    public void registrarseTest(){
        String resultado = usuario.registrarse(nombre,apellido,dni,user,password);
    }

}
