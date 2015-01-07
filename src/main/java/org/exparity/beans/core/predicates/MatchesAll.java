package org.exparity.beans.core.predicates;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class MatchesAll implements BeanPropertyPredicate {

	private final BeanPropertyPredicate[] predicates;

	public MatchesAll(BeanPropertyPredicate[] predicates) {
		this.predicates = predicates;
	}

	public boolean matches(final BeanProperty property) {
		for (BeanPropertyPredicate predicate : predicates) {
			if (!predicate.matches(property)) {
				return false;
			}
		}
		return true;
	}
}