package es.uv.etse.twcam.backend.apirest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "InitServlet", urlPatterns = {"/init"}, loadOnStartup = 1)
public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("BiciRent API inicializada correctamente");
    }
}