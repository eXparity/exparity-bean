
package com.modularit.beans;

import org.apache.commons.lang.StringUtils;

/**
 * Static repository of useful {@link BeanPropertyPredicate} instances
 * 
 * @author Stewart Bissett
 */
public abstract class BeanPredicates {

	/**
	 * Return a {@link BeanPropertyPredicate} which matches a {@link BeanProperty} which matches the name of the {@link BeanPropertyDescriptor}
	 */
	public static <P> BeanPropertyPredicate isProperty(final BeanPropertyDescriptor<P> descriptor) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				return property.hasName(descriptor.getName()) && property.isType(descriptor.getType());
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which matches a {@link BeanProperty} which matches the name of the {@link BeanPropertyDescriptor}
	 */
	public static BeanPropertyPredicate contains(final String needle) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				return property.isType(String.class) && StringUtils.contains(property.getValue(String.class), needle);
			}
		};
	}

	/**
	 * Return a {@link BeanPredicates} which returns <code>true</code> when all of the supplied predicates match
	 */
	public static BeanPropertyPredicate matchesAll(final BeanPropertyPredicate... predicates) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				for (BeanPropertyPredicate predicate : predicates) {
					if (!predicate.matches(property)) {
						return false;
					}
				}
				return true;
			}
		};
	}

	/**
	 * Return a {@link BeanPredicates} which returns <code>true</code> when any of the supplied predicates match
	 */
	public static BeanPropertyPredicate matchesAny(final BeanPropertyPredicate... predicates) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				for (BeanPropertyPredicate predicate : predicates) {
					if (predicate.matches(property)) {
						return true;
					}
				}
				return false;
			}
		};
	}

	/**
	 * Return a {@link BeanPredicates} which returns <code>true</code> if the property value matches the supplied argument
	 */
	public static BeanPropertyPredicate hasValue(final Object value) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				return value.equals(property.getValue());
			}
		};
	}
}
