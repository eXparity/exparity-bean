
package uk.co.it.modular.beans;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

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
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanPropertyInstance property) {
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

			public boolean matches(final BeanPropertyInstance property) {
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
	 * Return a {@link BeanPropertyPredicate} which always returns <code>true</code>
	 */
	public static BeanPropertyPredicate anyProperty() {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanPropertyInstance property) {
				return true;
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied name
	 */
	public static BeanPropertyPredicate withName(final String name) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanPropertyInstance property) {
				return equalsIgnoreCase(name, property.getName());
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied value
	 */
	public static BeanPropertyPredicate withValue(final Object value) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanPropertyInstance property) {
				return value.equals(property.getValue());
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has the supplied name and value
	 */
	public static BeanPropertyPredicate withValue(final String name, final Object value) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanPropertyInstance property) {
				return property.hasName(name) && value.equals(property.getValue());
			}
		};
	}

	/**
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has any of the supplied types
	 */
	public static BeanPropertyPredicate withType(final Class<?>... types) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanPropertyInstance property) {
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
	 * Return a {@link BeanPropertyPredicate} which returns <code>true</code> if the property has any of the types as it's declaring type
	 */
	public static BeanPropertyPredicate withDeclaringType(final Class<?>... types) {
		return new BeanPropertyPredicate() {

			public boolean matches(final BeanPropertyInstance property) {
				for (Class<?> type : types) {
					if (property.getDeclaringType().equals(type)) {
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
}
