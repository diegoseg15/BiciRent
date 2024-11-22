package es.uv.etse.twcam.backend.business;

/**
 * Comentario asociado a un producto.
 * 
 * Javabean para almacenar el comentario de un producto.
 * 
 * @author <a href="mailto:raul.penya@uv.es">Ra&uacute;l Pe&ntilde;a-Ortiz</a
 */
public class Comment {

    /**
     * Número de estrellas de 0 a 5
     */
    private Integer estrellas;

    /**
     * Texto del comentario
     */
    private String comentario;


    /**
     * Autor del comentario
     */
    private String autor;

    /**
     * Fecha el comentario
     */
    private String fecha;

    

    /**
     * Crea un comentario a partir de sus campos
     * @param estrellas Estrellas
     * @param comentario Texto del comentario
     * @param autor Autor del comentario
     * @param fecha Fecha del comentario
     */
    public Comment(Integer estrellas, String comentario, String autor, String fecha) {
        this.estrellas = estrellas;
        this.comentario = comentario;
        this.autor = autor;
        this.fecha = fecha;
    }

    /**
     * @return the estrellas
     */
    public Integer getEstrellas() {
        return estrellas;
    }

    /**
     * @param estrellas the estrellas to set
     */
    public void setEstrellas(Integer estrellas) {
        this.estrellas = estrellas;
    }

    /**
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        return "Comentario [autor=" + autor + ", comentario=" + comentario + ", estrellas=" + estrellas + ", fecha="
                + fecha + "]";
    } 
   
}