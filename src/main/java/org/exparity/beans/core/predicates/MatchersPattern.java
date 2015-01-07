package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class MatchersPattern implements BeanPropertyPredicate {

	private final String name;
	private final String pattern;

	public MatchersPattern(String name, String pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	public boolean matches(final BeanProperty property) {
		return property.hasName(name) && property.isString() && !property.isNull() && property.getValue(String.class).matches(pattern);
	}
}