
package uk.co.it.modular.beans;

/**
 * @author Stewart Bissett
 */
public class BeanBuilderException extends RuntimeException {

	private static final long serialVersionUID = 3815822809921345204L;

	public BeanBuilderException() {
	}

	public BeanBuilderException(final String message) {
		super(message);
	}

	public BeanBuilderException(final Throwable cause) {
		super(cause);
	}

	public BeanBuilderException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
