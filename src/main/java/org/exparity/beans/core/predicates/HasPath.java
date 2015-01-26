
package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPath;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * Implementation of a BeanPropertyPredicate which matches a BeanProperty which has the given path
 * @author Stewart Bissett
 */
public class HasPath implements BeanPropertyPredicate {

	private final BeanPropertyPath path;

	public HasPath(final String path) {
		this.path = new BeanPropertyPath(path);
	}

	public boolean matches(final BeanProperty property) {
		return path.equals(property.getPath());
	}
}
