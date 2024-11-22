package es.uv.etse.twcam.backend.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void testCreation() {

        List<Comment> comentarios = new ArrayList<>();

        Comment c = new Comment(5, "Comentario", "Autor", "Fecha");

        comentarios.add(c);

        Producto p = new Producto(1, "Producto", 10.0, "imagen.png", false, comentarios);

        assertEquals(1, p.getId());
        assertEquals("Producto", p.getNombre());
        assertEquals(10, p.getPrecio());
        assertEquals("imagen.png", p.getImagen());
        assertEquals(false, p.isOferta());
        assertNotNull(p.getComentarios());
        
    }

    @Test
    void testUpdate() {

        List<Comment> comentarios = new ArrayList<>();

        Comment c = new Comment(5, "Comentario", "Autor", "Fecha");

        comentarios.add(c);

        Producto p = new Producto(1, "Producto", 10.0, "imagen.png", false, comentarios);

        p.setId(2);
        p.setNombre("Product");
        p.setPrecio(9.99);
        p.setImagen("image.png");
        p.setOferta(true);
        p.setComentarios(null);

        assertEquals(2, p.getId());
        assertEquals("Product", p.getNombre());
        assertEquals(9.99, p.getPrecio());
        assertEquals("image.png", p.getImagen());
        assertEquals(true, p.isOferta());
        assertNull(p.getComentarios());

    }
}
