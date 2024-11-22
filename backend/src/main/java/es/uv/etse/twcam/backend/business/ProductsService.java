package es.uv.etse.twcam.backend.business;

import java.util.List;

/**
 * Servicio sobre productos.
 * 
 * Implementa toda la funcionalidad sobre los productos.
 * 
 * @author <a href="mailto:raul.penya@uv.es">Ra&uacute;l Pe&ntilde;a-Ortiz</a>
 */
public interface ProductsService {

    /**
     * Obtiene la lista completa del cat&aacute;logo de productos.
     * @return Lista de productos.
     */
    public List<Producto> listAll();

    /**
     * Obtiene la lista de productos del cat&aacute;logo que est&aacute;n en oferta.
     * 
     * @return Lista de productos en oferta.
     */
    public List<Producto> listSales();

    /**
     * Obtiene un producto a partir de su identificador.
     * En el caso de que el producto no exista lo indica con una excepci&oacute;n.
     * @param id Identificador del producto.
     * @return Producto asociado al indentificador.
     * @throws ProductNotExistException Indicador de identificador no existente.
     */
    public Producto find(Integer id) throws ProductNotExistException;

    /**
     * Actualiza la informaci&oacute;n del producto.
     * @param pro Informaci&oacute;n del producto a actualizar.
     * @return Producto actualizado.
     * @throws ProductException Indicador de error en la actualizaci&oacute;n.
     */
	public Producto update(Producto pro) throws ProductException;

}