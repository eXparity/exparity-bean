
package org.exparity.beans.core;

/**
 * @author Stewart Bissett
 */
public class BeanPropertyException extends RuntimeException {

	private static final long serialVersionUID = 787620207455033419L;

	public BeanPropertyException(final String message) {
		super(message);
	}

	public BeanPropertyException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
