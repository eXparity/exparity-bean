/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * @author Stewart Bissett
 */
class BeanPropertyInstancePredicateAdaptor implements BeanPropertyInstancePredicate {

	static BeanPropertyInstancePredicate adapted(final BeanPropertyPredicate raw) {
		return new BeanPropertyInstancePredicateAdaptor(raw);
	}

	private final BeanPropertyPredicate predicate;

	BeanPropertyInstancePredicateAdaptor(final BeanPropertyPredicate predicate) {
		this.predicate = predicate;
	}

	public boolean matches(final BeanPropertyInstance property) {
		return predicate.matches(property.getProperty());
	}
}