
package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * Implemenation of a BeanPropertyPredicate which matches if the property path with no ordinals matches the given path
 * 
 * @author Stewart Bissett
 */
public class HasPathIgnoreOrdinal implements BeanPropertyPredicate {

	private final String path;

	public HasPathIgnoreOrdinal(final String path) {
		this.path = path;
	}

	public boolean matches(final BeanProperty property) {
		return path.matches(property.getPath().fullPathWithNoIndexes());
	}

}
