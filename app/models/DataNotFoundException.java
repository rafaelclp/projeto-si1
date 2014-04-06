package models;

/**
 * Exceção lançada caso algum dado requisitado não esteja acessível
 * ou não possa ser obtido da fonte onde foi requisitado.
 *
 */
public class DataNotFoundException extends Exception {
	private static final long serialVersionUID = 84213894201381382L;

	/**
	 * Construtores de excecoes (apenas repassando parametros para Exception).
	 */
	public DataNotFoundException() {
		super();
	}

	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataNotFoundException(Throwable cause) {
		super(cause);
	}
}
