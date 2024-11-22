package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de productos basado en un diccionario sin
 * persistencia.
 * 
 * La implementación sigue el patrón de singleton.
 * 
 * @author <a href="mailto:raul.penya@uv.es">Ra&uacute;l Pe&ntilde;a-Ortiz</a>
 */
public class ProductsServiceDictionaryImpl implements ProductsService{
	
	/**
	 * Instancia única.
	 */
	private static ProductsServiceDictionaryImpl the;
	
	/**
	 * Diccionario para el almacenamiento de productos.
	 */
	protected Map<Integer,Producto> dictionary;

	
	/**
	 * Crea un servicio sin datos.
	 */
	private ProductsServiceDictionaryImpl() {
		dictionary = new Hashtable<>();
	}
	
	/**
	 * Obtiene la instancia única del servicio.
	 * @return Instancia única.
	 */
	public static ProductsServiceDictionaryImpl getInstance() {

		if (the == null) {
			the = new ProductsServiceDictionaryImpl();
		}

		return the;
	}
	
	/**
	 * Limpia la instancia única del servicio.
	 * 
	 */
	public static void clearInstance() {

		if (the != null) {
			the.dictionary.clear();
			the = null;
		}

	}
	
	/**
	 * Añade un producto al servicio.
	 * @param pro Información del producto.
	 * @return Producto añadido por el servicio.
	 * @throws ProductException Indicador de error en la adición.
	 */
	public Producto create(Producto pro) throws ProductException {
		
		if (pro != null && pro.getId() != null) {
			dictionary.put(pro.getId(), pro);
		} else {
			throw new IncorrectProductException("Producto o su identificador son nulos");
		}
		
		return pro;
		
	}
	
	@Override
	public List<Producto> listAll() {
		List<Producto> products = new ArrayList<>();

		products.addAll(dictionary.values());

		return products;
	}
	
	@Override
	public List<Producto> listSales() {
		return dictionary.values().stream().filter(Producto::isOferta).collect(Collectors.toList());
	}

	@Override
	public Producto update(Producto pro) throws ProductException {
		
		if (pro!=null && dictionary.containsKey(pro.getId())) {
			dictionary.put(pro.getId(), pro);
		} else {
			if (pro==null)
				throw new IncorrectProductException("Producto o su identificador son nulos");
			else
				throw new ProductNotExistException(pro.getId());
		}
		
		return pro;
	}

	@Override
	public Producto find(Integer id) throws ProductNotExistException {
		
		if (dictionary.containsKey(id)) {
			return dictionary.get(id);
		} else {
			throw new ProductNotExistException(id);
		}
	}

}
