/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * @author Stewart Bissett
 */
public class BeanPropertyException extends RuntimeException {

	private static final long serialVersionUID = 787620207455033419L;

	public BeanPropertyException() {
	}

	public BeanPropertyException(final String message) {
		super(message);
	}

	public BeanPropertyException(final Throwable cause) {
		super(cause);
	}

	public BeanPropertyException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
