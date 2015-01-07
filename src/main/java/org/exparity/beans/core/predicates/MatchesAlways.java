package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class MatchesAlways implements BeanPropertyPredicate {

	public boolean matches(final BeanProperty property) {
		return true;
	}
}