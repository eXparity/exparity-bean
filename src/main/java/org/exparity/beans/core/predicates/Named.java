package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

/**
 * @author Stewart Bissett
 */
public class Named implements BeanPropertyPredicate {

	private final String name;

	public Named(String name) {
		this.name = name;
	}

	public boolean matches(final BeanProperty property) {
		return equalsIgnoreCase(name, property.getName());
	}
}