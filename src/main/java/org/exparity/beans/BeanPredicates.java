
package org.exparity.beans;

import org.exparity.beans.core.BeanPropertyPredicate;
import org.exparity.beans.core.predicates.HasPath;
import org.exparity.beans.core.predicates.HasPathIgnoreOrdinal;
import org.exparity.beans.core.predicates.HasType;
import org.exparity.beans.core.predicates.MatchersPattern;
import org.exparity.beans.core.predicates.MatchesAll;
import org.exparity.beans.core.predicates.MatchesAlways;
import org.exparity.beans.core.predicates.MatchesOneOf;
import org.exparity.beans.core.predicates.HasName;
import org.exparity.beans.core.predicates.OfDeclaringType;
import org.exparity.beans.core.predicates.WithPropertyValue;
import org.exparity.beans.core.predicates.WithValue;

/**
 * Static repository of useful {@link BeanPropertyPredicate} instances
 * 
 * @author Stewart Bissett
 */
public abstract class BeanPredicates {

	/**
	 * Return a {@link BeanPredicates} which returns <code>true</code> when all of the supplied predicates match
	 */
	public static BeanPropertyPredicate matchesAll(final BeanPropertyPredicate... predicates) {
		return new MatchesAll(predicates);
	}

	/**
	 * Return a {@link BeanPredicates} which returns <code>true</code> when any of the supplied predicates match
	 */
	public static BeanPropertyPredicate matchesOneOf(final BeanPropertyPredicate... predicates) {
		return new MatchesOneOf(predicates);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which always returns <code>true</code>
	 */
	public static BeanPropertyPredicate anyProperty() {
		return new MatchesAlways();
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied name
	 */
	public static BeanPropertyPredicate named(final String name) {
		return new HasName(name);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied value
	 */
	public static BeanPropertyPredicate hasValue(final Object value) {
		return new WithValue(value);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied name and value
	 */
	public static BeanPropertyPredicate hasPropertyValue(final String name, final Object value) {
		return new WithPropertyValue(value, name);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has any of the supplied types
	 */
	public static BeanPropertyPredicate ofType(final Class<?>... types) {
		return new HasType(types);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied name and matches the supplied regular expression
	 */
	public static BeanPropertyPredicate matchesPattern(final String name, final String pattern) {
		return new MatchersPattern(name, pattern);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has any of the types as it's declaring type
	 */
	public static BeanPropertyPredicate ofDeclaringType(final Class<?>... types) {
		return new OfDeclaringType(types);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} for a property named with the supplied name and is of one of the supplied types
	 */
	public static BeanPropertyPredicate named(final String propertyName, final Class<?>... types) {
		return matchesAll(named(propertyName), ofType(types));
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied path
	 */
	public static BeanPropertyPredicate hasPath(final String path) {
		return new HasPath(path);
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied path regardless of any ordinals
	 */
	public static BeanPropertyPredicate hasPathIgnoreOrdinal(final String path) {
		return new HasPathIgnoreOrdinal(path);
	}
}
