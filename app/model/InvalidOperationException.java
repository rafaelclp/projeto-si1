package model;

public class InvalidOperationException extends Exception {
	private static final long serialVersionUID = 4904122534675169254L;

	/**
	 * Construtores de excecoes (apenas repassando parametros para Exception).
	 */
	public InvalidOperationException() {
		super();
	}

	public InvalidOperationException(String message) {
		super(message);
	}

	public InvalidOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidOperationException(Throwable cause) {
		super(cause);
	}
}
