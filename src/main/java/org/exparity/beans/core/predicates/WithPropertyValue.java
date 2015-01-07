package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class WithPropertyValue implements BeanPropertyPredicate {

	private final Object value;
	private final String name;

	public WithPropertyValue(Object value, String name) {
		this.value = value;
		this.name = name;
	}

	public boolean matches(final BeanProperty property) {
		return property.hasName(name) && value.equals(property.getValue());
	}
}