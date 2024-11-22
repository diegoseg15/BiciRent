package es.uv.etse.twcam.backend.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void testCreation() {

        Comment c = new Comment(5, "Comentario", "Autor", "Fecha");

        assertEquals(5, c.getEstrellas());
        assertEquals("Comentario", c.getComentario());
        assertEquals("Autor", c.getAutor());
        assertEquals("Fecha", c.getFecha());
    }

    @Test
    void testUpdate() {

        Comment c = new Comment(5, "Comentario", "Autor", "Fecha");

        c.setEstrellas(4);
        c.setComentario("Comentari");
        c.setAutor("Autora");
        c.setFecha("Data");

        assertEquals(4, c.getEstrellas());
        assertEquals("Comentari", c.getComentario());
        assertEquals("Autora", c.getAutor());
        assertEquals("Data", c.getFecha());
    }
}
