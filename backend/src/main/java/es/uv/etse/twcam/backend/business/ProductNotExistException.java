package es.uv.etse.twcam.backend.business;

public class ProductNotExistException extends ProductException {

	private static final long serialVersionUID = 1L;

	public ProductNotExistException(Integer id) {
		super("El producto "+id+" no existe");
	}

}
