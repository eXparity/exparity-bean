/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * @author Stewart Bissett
 */
public class BeanPropertyNotFound extends RuntimeException {

	private static final long serialVersionUID = 4861964109957223071L;

	public BeanPropertyNotFound(final String message) {
		super(message);
	}

}
