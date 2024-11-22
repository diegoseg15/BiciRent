package es.uv.etse.twcam.backend.business;

public class IncorrectProductException extends ProductException {

	public IncorrectProductException(String bug) {
		super(bug+" con formato erróneo");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
