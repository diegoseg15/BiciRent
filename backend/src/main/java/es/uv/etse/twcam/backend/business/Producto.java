package es.uv.etse.twcam.backend.business;

import java.util.List;

/**
 * Producto.
 * 
 * Javabean para almacenar la información de un producto.
 * 
 * @author <a href="mailto:raul.penya@uv.es">Ra&uacute;l Pe&ntilde;a-Ortiz</a>
 *
 */
public class Producto {
	
	/**
	 * Identificador
	 */
	private Integer id;
	
	/**
	 * Nombre del producto
	 */
	private String nombre;

	/**
	 * Precio
	 */
	private Double precio;
	
	/**
	 * Imágen producto
	 */
	private String imagen;

	/**
	 * Indicador de producto en oferta
	 */
	private boolean oferta;
	
	/**
	 * Comentarios sobre el producto
	 */
	List<Comment> comentarios;

	
	/**
	 * Crea un producto a partir de la información del mismo.
	 * @param id Identificador del producto.
	 * @param nombre Nombre del producto.
	 * @param precio Precio del producto.
	 * @param imagen Fichero de imagen del producto.
	 * @param oferta Indicador de producto en oferta.
	 * @param comentarios Lista de comentarios del producto.
	 */
	public Producto(Integer id, String nombre, Double precio, String imagen, boolean oferta, List<Comment> comentarios) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
		this.oferta = oferta;
		this.comentarios = comentarios;
	}

	/**
	 * Obtiene el identificador.
	 * @return Idenficador.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Modifica el identificador.
	 * @param id Nuevo identificador.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	
	/**
	 * Obtiene el precio.
	 * 
	 * @return Precio.
	 */
	public Double getPrecio() {
		return precio;
	}

	/**
	 * Modifica el precio.
	 * @param precio Nuevo precio.
	 */
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	/**
	 * Obtiene el fichero de imagen del producto.
	 * 
	 * @return Fichero de imagen.
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * Modifica el fichero de imagen del producto.
	 * 
	 * @param imagen Nuevo fichero de imagen.
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * Obtiene si el producto está o no en oferta.
	 * 
	 * @return Indicador de oferta.
	 */
	public boolean isOferta() {
		return oferta;
	}

	/**
	 * Cambia el indicador de oferta del producto.
	 * @param oferta Indicador de oferta.
	 */
	public void setOferta(boolean oferta) {
		this.oferta = oferta;
	}

	/**
	 * Obtiene la lista de comentarios del producto.
	 * @return Lista de comentarios.
	 */
	public List<Comment> getComentarios() {
		return comentarios;
	}

	/**
	 * Modifica la lista de comentarios asociados al producto.
	 * @param comentarios Nueva lista de comentarios.
	 */
	public void setComentarios(List<Comment> comentarios) {
		this.comentarios = comentarios;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Producto [comentarios=" + comentarios + ", id=" + id + ", imagen=" + imagen + ", oferta=" + oferta
				+ ", precio=" + precio + "]";
	}

	/**
	 * Obtiene el nombre
	 * @return Nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Actualiza el nombre
	 * @param nombre Nuevo nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
