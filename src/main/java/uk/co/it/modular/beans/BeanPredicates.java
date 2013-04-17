
package uk.co.it.modular.beans;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import org.apache.commons.lang.StringUtils;

/**
 * Static repository of useful {@link BeanPropertyPredicate} instances
 * 
 * @author Stewart Bissett
 */
public abstract class BeanPredicates {

	/**
	 * Return a {@link BeanPropertyPredicate} which matches a {@link BeanProperty} which contains the needle
	 */
	public static BeanPropertyPredicate whichContains(final String needle) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				return property.isType(String.class) && StringUtils.contains(property.getValue(String.class), needle);
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which matches a {@link BeanProperty} which matches the supplied regular expression.
	 */
	public static BeanPropertyPredicate whichMatches(final String pattern) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				if (property.isType(String.class)) {
					String value = property.getValue(String.class);
					return property.isType(String.class) && value.matches(pattern);
				} else {
					return false;
				}
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
	public static BeanPropertyPredicate withValue(final Object value) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				return value.equals(property.getValue());
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which always returns <code>true</code>
	 */
	public static BeanPropertyPredicate anyProperty() {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				return true;
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied name
	 */
	public static BeanPropertyPredicate withName(final String name) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				return equalsIgnoreCase(name, property.getName());
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has any of the supplied types
	 */
	public static BeanPropertyPredicate withType(final Class<?>... types) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanProperty property) {
				for (Class<?> type : types) {
					if (property.getType().equals(type)) {
						return true;
					}
				}
				return false;
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} for a property named with the supplied name and is of one of the supplied types
	 */
	public static BeanPropertyPredicate withProperty(final String propertyName, final Class<?>... types) {
		return matchesAll(withName(propertyName), withType(types));
	}

	/**
	 * Return a {@link BeanPropertyPredicate} for a property named with the supplied name and is of one of the supplied types
	 */
	public static BeanPropertyPredicate withPropertyValue(final String propertyName, final Object value) {
		return matchesAll(withName(propertyName), withValue(value));
	}
}
