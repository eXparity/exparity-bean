package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class HasType implements BeanPropertyPredicate {

	private final Class<?>[] types;

	public HasType(Class<?>[] types) {
		this.types = types;
	}

	public boolean matches(final BeanProperty property) {
		for (Class<?> type : types) {
			if (property.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}
}