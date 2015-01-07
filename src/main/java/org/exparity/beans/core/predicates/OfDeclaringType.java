package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class OfDeclaringType implements BeanPropertyPredicate {

	private final Class<?>[] types;

	public OfDeclaringType(Class<?>[] types) {
		this.types = types;
	}

	public boolean matches(final BeanProperty property) {
		for (Class<?> type : types) {
			if (property.getDeclaringType().equals(type)) {
				return true;
			}
		}
		return false;
	}
}