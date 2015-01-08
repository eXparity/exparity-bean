
package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class Named implements BeanPropertyPredicate {

	private final String name;

	public Named(final String name) {
		this.name = name;
	}

	public boolean matches(final BeanProperty property) {
		return property.hasName(name);
	}
}