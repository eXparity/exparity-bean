/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * Exception raised when an explicitly requested bean property is not found on a {@link Bean}, {@link Graph}, or {@link Type}
 * @author Stewart Bissett
 */
public class BeanPropertyNotFoundException extends BeanPropertyException {

	private static final long serialVersionUID = 8254059732935930954L;

	public BeanPropertyNotFoundException(final Class<?> type, final String propertyName) {
		super("Bean property '" + propertyName + "' not found on '" + type.getCanonicalName() + "'");
	}
}
