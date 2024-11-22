package es.uv.etse.twcam.backend.apirest;

/**
 * Indicador de error al procesar una operaci&oacute;n sobre el API REST
 */
public class APIRESTException extends Exception {

	/**
	 * Identificador
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Constructor a partir de mensaje de error
     * @param msg Mensaje del error
     */
	public APIRESTException(String msg) {
		super(msg);
	}

}