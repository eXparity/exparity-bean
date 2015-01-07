package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class WithValue implements BeanPropertyPredicate {

	private final Object value;

	public WithValue(Object value) {
		this.value = value;
	}

	public boolean matches(final BeanProperty property) {
		return value.equals(property.getValue());
	}
}