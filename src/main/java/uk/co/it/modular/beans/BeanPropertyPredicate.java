/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * @author Stewart.Bissett
 */
public interface BeanPropertyPredicate {

	public boolean matches(final BeanPropertyInstance property);

}
