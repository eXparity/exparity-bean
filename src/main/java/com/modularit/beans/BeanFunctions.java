
package com.modularit.beans;

import static com.modularit.beans.BeanPredicates.isProperty;
import static com.modularit.beans.BeanPredicates.matchesAll;
import static com.modularit.beans.BeanUtils.visitAll;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for inspecting Objects which expose properties which follow the Java Bean get/set standard
 * 
 * @author Stewart Bissett
 */
public abstract class BeanFunctions {

	/**
	 * Find all instances of the supplied property on the object and it's assosciates. For example</p>
	 * 
	 * <code>
	 * List<BeanProperty> nameProperties = BeanUtils.find(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;));
	 * </code></p>
	 */
	public static <P> List<BeanProperty> find(final Object instance, final BeanPropertyDescriptor<P> property) {
		return find(instance, property, isProperty(property));
	}

	/**
	 * Find all instances of the supplied property on the object and it's assosciates which match the predicate. For example</p>
	 * 
	 * <code>
	 * List<BeanPropery> relativesCalledBob = BeanUtils.find(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;), BeanPredicates.contains("Bob"));
	 * </code></p>
	 */
	public static List<BeanProperty> find(final Object instance, final BeanPropertyPredicate predicate) {
		final List<BeanProperty> collection = new ArrayList<BeanProperty>();
		visitAll(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property)) {
					collection.add(property);
				}
			}
		});
		return collection;
	}

	/**
	 * Find the first instance of the property which matches the given descriptor. For example</p>
	 * 
	 * <code>
	 * BeanProperty firstRelativesName = BeanUtils.first(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;));
	 * </code></p>
	 */
	public static BeanProperty first(final Object instance, final BeanPropertyDescriptor<?> descriptor) {
		return first(instance, isProperty(descriptor));
	}

	/**
	 * Find the first instance of the property which matches the given predicate and descriptor. For example</p>
	 * 
	 * <code>
	 * BeanProperty firstRelativeCalledBob = BeanUtils.first(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;), BeanPredicates.contains("Bob"));
	 * </code></p>
	 */
	public static <P> BeanProperty first(final Object instance, final BeanPropertyDescriptor<P> descriptor, final BeanPropertyPredicate predicate) {
		return first(instance, matchesAll(isProperty(descriptor), predicate));
	}

	/**
	 * Find the first instance of the property which matches the given predicate. For example</p>
	 * 
	 * <code>
	 * BeanPropery firstRelativeCalledBob = BeanUtils.first(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;), BeanPredicates.contains("Bob"));
	 * </code></p>
	 */
	public static <P> BeanProperty first(final Object instance, final BeanPropertyPredicate predicate) {

		@SuppressWarnings("serial")
		class HaltVisitException extends RuntimeException {
		}

		final List<BeanProperty> collection = new ArrayList<BeanProperty>();
		try {
			visitAll(instance, new BeanVisitor() {

				public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
					if (predicate.matches(property)) {
						collection.add(property);
						throw new HaltVisitException();
					}
				}
			});
		} catch (HaltVisitException e) {
			return collection.get(0);
		}
		return null;
	}

	/**
	 * Find the last instance of the property which matches the given descriptor. For example</p>
	 * 
	 * <code>
	 * BeanProperty lastRelativesName = BeanUtils.last(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;));
	 * </code></p>
	 */
	public static BeanProperty last(final Object instance, final BeanPropertyDescriptor<?> descriptor) {
		return last(instance, isProperty(descriptor));
	}

	/**
	 * Find the last instance of the property which matches the given predicate and descriptor. For example</p>
	 * 
	 * <code>
	 * BeanProperty lastRelativeCalledBob = BeanUtils.last(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;), BeanPredicates.contains("Bob"));
	 * </code></p>
	 */
	public static <P> BeanProperty last(final Object instance, final BeanPropertyDescriptor<P> descriptor, final BeanPropertyPredicate predicate) {
		return last(instance, matchesAll(isProperty(descriptor), predicate));
	}

	/**
	 * Find the first instance of the property which matches the given predicate. For example</p>
	 * 
	 * <code>
	 * BeanPropery lastRelativeCalledBob = BeanUtils.last(myFamilyTree, BeanPredicates.contains("Bob"));
	 * </code></p>
	 */
	public static <P> BeanProperty last(final Object instance, final BeanPropertyPredicate predicate) {
		List<BeanProperty> properties = find(instance, predicate);
		if (!properties.isEmpty()) {
			return properties.get(properties.size() - 1);
		}
		return null;
	}

	/**
	 * Find all instances of the supplied property on the object and it's assosciates. For example</p>
	 * 
	 * <code>
	 * long relativesCalledBob = BeanUtils.count(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;), BeanPredicates.contains("Bob"));
	 * </code></p>
	 */
	public static <P> List<BeanProperty> find(final Object instance, final BeanPropertyDescriptor<P> descriptor, final BeanPropertyPredicate predicate) {
		return find(instance, matchesAll(isProperty(descriptor), predicate));
	}

	/**
	 * Count all instances of the supplied property on the object and it's assosciates. For example</p>
	 * 
	 * <code>
	 * int relativesCalledBob = BeanUtils.count(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;), BeanUtils.equalTo("Bob"));
	 * </code></p>
	 */
	public static <P> int count(final Object instance, final BeanPropertyDescriptor<P> property) {
		return count(instance, property, isProperty(property));
	}

	/**
	 * Count all instances of the supplied property on the object and it's assosciates which match the supplied predicate. For example</p>
	 * 
	 * <code>
	 * int relativesCalledBob = BeanUtils.count(myFamilyTree, BeanUtils.stringProperty(&quot;name&quot;), BeanUtils.equalTo("Bob"));
	 * </code></p>
	 */
	public static <P> int count(final Object instance, final BeanPropertyDescriptor<P> property, final BeanPropertyPredicate predicate) {
		return find(instance, property, predicate).size();
	}

	/**
	 * Sum all instances of the supplied property on the object and it's assosciates. For example</p>
	 * 
	 * <code>
	 * int totalAge = BeanUtils.sum(myFamilyTree, BeanUtils.intProperty(&quot;age&quot;));
	 * </code></p>
	 * 
	 * <em>Warning: This method will use the double value from the Number opening the possibility of rounding errors</em></p>
	 */
	public static <T extends Number> Number sum(final Object instance, final BeanPropertyDescriptor<T> property) {
		return sum(instance, property, isProperty(property));
	}

	/**
	 * Sum all instances of the supplied property on the object and it's assosciates which match the predicate. For example</p>
	 * 
	 * <code>
	 * int totalAge = BeanUtils.sum(myFamilyTree, BeanUtils.intProperty(&quot;age&quot;), BeanUtils.lessThan(;
	 * </code></p>
	 * 
	 * <em>Warning: This method will use the double value from the Number opening the possibility of rounding errors</em></p>
	 */
	public static <T extends Number> Number sum(final Object instance, final BeanPropertyDescriptor<T> property, final BeanPropertyPredicate predicate) {
		return Numbers.sum(collectValues(instance, property, predicate).toArray(new Number[0]));
	}

	/**
	 * Average all instances of the supplied property on the object and it's assosciates. For example, </p>
	 * 
	 * <code>
	 * int averageAge = BeanUtils.avg(myFamilyTree, BeanUtils.intProperty(&quot;age&quot;));
	 * </code></p>
	 * 
	 * <em>Warning: This method will use the double value from the Number opening the possibility of rounding errors</em></p>
	 */
	public static <T extends Number> Number average(final Object instance, final BeanPropertyDescriptor<T> property) {
		return average(instance, property, isProperty(property));
	}

	/**
	 * Average all instances of the supplied property on the object and it's assosciates which match the predicate. For example, </p>
	 * 
	 * <code>
	 * Double averageAge = BeanUtils.avg(myFamilyTree, BeanProperties.intProperty(&quot;age&quot;), BeanPredicates.greaterThan(80) );
	 * </code></p>
	 * 
	 * <em>Warning: This method will use the double value from the Number opening the possibility of rounding errors</em></p>
	 */
	public static <T extends Number> Number average(final Object instance, final BeanPropertyDescriptor<T> property, final BeanPropertyPredicate predicate) {
		return Numbers.average(collectValues(instance, property, predicate).toArray(new Number[0]));
	}

	/**
	 * Find the maximum value for the supplied property on the object and it's assosciates. For example</p>
	 * 
	 * <code>
	 * int maxAge = BeanUtils.max(myFamilyTree, BeanUtils.intProperty(&quot;age&quot;));
	 * </code></p> This method works for property types which extend {@link Comparable}.</p>
	 */
	public static <P extends Comparable<? super P>> P max(final Object instance, final BeanPropertyDescriptor<P> property) {
		return max(instance, property, isProperty(property));
	}

	/**
	 * Find the maximum value for the supplied property on the object and it's assosciates which matches the predicate. For example</p>
	 * 
	 * <code>
	 * int maxAge = BeanUtils.max(myFamilyTree, BeanUtils.intProperty(&quot;age&quot;), BeanPredicates.lessThan(20));
	 * </code></p>
	 */
	public static <P extends Comparable<? super P>> P max(final Object instance, final BeanPropertyDescriptor<P> property, final BeanPropertyPredicate predicate) {
		return Iterables.max(collectValues(instance, property, predicate));
	}

	/**
	 * Find the minimum value for the supplied property on the object and it's assosciates. For example</p>
	 * 
	 * <code>
	 * int minAge = BeanUtils.min(myFamilyTree, BeanUtils.intProperty(&quot;age&quot;));
	 * </code></p>
	 */
	public static <P extends Comparable<? super P>> P min(final Object instance, final BeanPropertyDescriptor<P> property) {
		return min(instance, property, isProperty(property));
	}

	/**
	 * Find the minimum value for the supplied property on the object and it's assosciates which matches the predicate. For example</p>
	 * 
	 * <code>
	 * int minAge = BeanUtils.min(myFamilyTree, BeanUtils.intProperty(&quot;age&quot;), BeanPredicates.greaterThan(65));
	 * </code></p>
	 */
	public static <P extends Comparable<? super P>> P min(final Object instance, final BeanPropertyDescriptor<P> property, final BeanPropertyPredicate predicate) {
		return Iterables.min(collectValues(instance, property, predicate));
	}

	/**
	 * General purpose function for collecting all values for the supplied property on the object and it's assosciates. For example </p>
	 * 
	 * <code>
	 * List<Integer> values = BeanFunctions.collect(myFamilyTree, BeanProperties.intProperty(&quot;age&quot;));
	 * </code> </p>
	 */
	public static <P> List<P> collect(final Object instance, final BeanPropertyDescriptor<P> property) {
		return collect(instance, property, isProperty(property));
	}

	/**
	 * General purpose function for collecting all values for the supplied property on the object and it's assosciates which match the predicate. For example </p>
	 * 
	 * <code>
	 * List<Integer> values = BeanFunctions.collect(myFamilyTree, BeanProperties.intProperty(&quot;age&quot;), BeanPredicates.greaterThan(65));
	 * </code> </p>
	 */
	public static <P> List<P> collect(final Object instance, final BeanPropertyDescriptor<P> descriptor, final BeanPropertyPredicate predicate) {
		final List<P> collection = new ArrayList<P>();
		visitAll(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (isProperty(descriptor).matches(property) && predicate.matches(property)) {
					collection.add(descriptor.getValue(current));
				}
			}
		});
		return collection;
	}

	private static <P> List<P> collectValues(final Object instance, final BeanPropertyDescriptor<P> property, final BeanPropertyPredicate predicate) {
		final List<P> collection = new ArrayList<P>();
		visitAll(instance, new BeanVisitor() {

			@SuppressWarnings("unchecked")
			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property)) {
					collection.add((P) property.getValue());
				}
			}
		});
		return collection;
	}

}
