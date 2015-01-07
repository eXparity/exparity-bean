package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class MatchesOneOf implements BeanPropertyPredicate {

	private final BeanPropertyPredicate[] predicates;

	public MatchesOneOf(BeanPropertyPredicate[] predicates) {
		this.predicates = predicates;
	}

	public boolean matches(final BeanProperty property) {
		for (BeanPropertyPredicate predicate : predicates) {
			if (predicate.matches(property)) {
				return true;
			}
		}
		return false;
	}
}