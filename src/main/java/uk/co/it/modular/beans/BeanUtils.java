
package uk.co.it.modular.beans;

import static uk.co.it.modular.beans.BeanPredicates.anyProperty;
import static uk.co.it.modular.beans.BeanPredicates.withName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility methods for inspecting Objects which expose properties which follow the Java Bean get/set standard
 * 
 * @author Stewart Bissett
 */
public abstract class BeanUtils {

	private static final BeanInspector shallowInspector = new BeanInspector(false, false);
	private static final BeanInspector unsafeInspector = new BeanInspector(true, false);
	private static final BeanInspector safeInspector = new BeanInspector(true, true);

	/**
	 * Return a list of the publicly exposes get/set properties on the Bean. For example:
	 * <p/>
	 * <code>
	 * List&lt;BeanProperty&gt; properties = BeanUtils.propertyList(myObject);
	 * </code>
	 * @param instance
	 *            an object to get the properties list from
	 */
	public static List<BeanProperty> propertyList(final Object instance) {
		return property(instance, anyProperty());
	}

	/**
	 * Return a map of the publicly exposes get/set properties on the Bean with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <code>
	 * Map&lt;String, BeanProperty&gt; propertyMap = BeanUtils.propertyMap(myObject);
	 * </code>
	 * @param instance
	 *            an object to get the properties for
	 */
	public static Map<String, BeanProperty> propertyMap(final Object instance) {
		final Map<String, BeanProperty> propertyMap = new HashMap<String, BeanProperty>();
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				propertyMap.put(property.getName(), property);
			}
		});
		return propertyMap;
	}

	/**
	 * Test if the supplied instance has a Bean property with the given name
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.hasProperty(user, "surname")) == true;
	 * </code>
	 * @param instance
	 *            an object to test against
	 * @param name
	 *            the property name
	 */
	public static boolean hasProperty(final Object instance, final String name) {
		return property(instance, withName(name)) != null;
	}

	/**
	 * Get the requested property from the instance or return <code>null</code> if the property is not present
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanProperty surname = BeanUtils.property(myUser, &quot;surname&quot;);
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 */
	public static BeanProperty property(final Object instance, final String name) {
		return findProperty(instance, BeanPredicates.withName(name));
	}

	/**
	 * Set the property value on the instance to the supplied value.
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.setProperty(myUser, &quot;surname&quot;, &quot;Smith&quot;);
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 * @param value
	 *            the value to set the property to
	 */
	public static boolean setProperty(final Object instance, final String name, final Object value) {
		final List<BeanProperty> valuesSet = new ArrayList<BeanProperty>();
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (withName(name).matches(property)) {
					property.setValue(value);
					valuesSet.add(property);
				}
			}
		});
		return !valuesSet.isEmpty();
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * &quot;Smith&quot;.equals(BeanUtils.propertyValue(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 */
	public static Object propertyValue(final Object o, final String propertyName) {
		BeanProperty property = property(o, propertyName);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	/**
	 * Return the property type on the instance for the supplied property name or <code>null</code> if the property doesn't exist
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * String.class.equals(BeanUtils.propertyType(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 */
	public static Class<?> propertyType(final Object o, final String propertyName) {
		BeanProperty property = property(o, propertyName);
		if (property != null) {
			return property.getType();
		}
		return null;
	}

	/**
	 * Return the property value on the instance to the supplied value or return <code>null</code> if the property was not present on the instance
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * &quot;Smith&quot;.equals(BeanUtils.getPropertyValue(myUser, &quot;surname&quot;, String.class));
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 * @param name
	 *            the type to return the property as
	 */
	@SuppressWarnings("unchecked")
	public static <T> T propertyValue(final Object o, final String propertyName, final Class<T> type) {
		return (T) propertyValue(o, propertyName);
	}

	/**
	 * Test if the property on the supplied instance is of the supplied type.
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.isPropertyType(myUser, &quot;surname&quot;, String.class) == true;
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 * @param type
	 *            the expected type of the property
	 */
	public static boolean isPropertyType(final Object o, final String propertyName, final Class<?> type) {
		BeanProperty property = property(o, propertyName);
		if (property != null) {
			return property.isType(type);
		}
		return false;
	}

	/**
	 * Find all property instances which match the predicate. For example</p>
	 * 
	 * <code>
	 * List<BeanPropery> relativesCalledBob = BeanUtils.find(myFamilyTree, BeanPredicates.stringProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static List<BeanProperty> property(final Object instance, final BeanPropertyPredicate predicate) {
		final List<BeanProperty> collection = new ArrayList<BeanProperty>();
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property)) {
					collection.add(property);
				}
			}
		});
		return collection;
	}

	/**
	 * Find all property instances which match the predicate in the supplied instance and it's assosciates. For example</p>
	 * 
	 * <code>
	 * List<BeanPropery> relativesCalledBob = BeanUtils.find(myFamilyTree, BeanPredicates.stringProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static List<BeanProperty> allGraphProperties(final Object instance, final BeanPropertyPredicate predicate) {
		final List<BeanProperty> collection = new ArrayList<BeanProperty>();
		visitGraph(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property)) {
					collection.add(property);
				}
			}
		});
		return collection;
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties which match the predicate For example</p>
	 * 
	 * <code>
	 * BeanUtils.apply(myFamilyTree, deletePerson(), isDeceased()));
	 * </code></p>
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property)) {
					function.apply(property);
				}
			}
		});
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties in the supplied instance. For example</p>
	 * 
	 * <code>
	 * BeanUtils.apply(myFamilyTree, pruneDeceased()));
	 * </code></p>
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function) {
		apply(instance, function, anyProperty());
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties which match the predicate in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <code>
	 * BeanUtils.applyToGraph(myFamilyTree, deletePerson(), isDeceased()));
	 * </code></p>
	 */
	public static void applyToGraph(final Object instance, final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		visitGraph(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property)) {
					function.apply(property);
				}
			}
		});
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <code>
	 * BeanUtils.applyToGraph(myFamilyTree, pruneDeceased()));
	 * </code></p>
	 */
	public static void applyToGraph(final Object instance, final BeanPropertyFunction function) {
		applyToGraph(instance, function, anyProperty());
	}

	/**
	 * Find the first instance of the property which matches the given predicate in the given instance. For example</p>
	 * 
	 * <code>
	 * BeanPropery property = BeanUtils.findProperty(myFamilyTree, BeanPredicates.withProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static BeanProperty findProperty(final Object instance, final BeanPropertyPredicate predicate) {

		@SuppressWarnings("serial")
		class HaltVisitException extends RuntimeException {
		}

		final List<BeanProperty> collection = new ArrayList<BeanProperty>();
		try {
			visit(instance, new BeanVisitor() {

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
	 * Find the first instance of the property which matches the given predicate in the given instance and it's assosciates. For example</p>
	 * 
	 * <code>
	 * BeanPropery property = BeanUtils.findPropertyInGraph(myFamilyTree, BeanPredicates.withProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static BeanProperty findPropertyInGraph(final Object instance, final BeanPropertyPredicate predicate) {

		@SuppressWarnings("serial")
		class HaltVisitException extends RuntimeException {
		}

		final List<BeanProperty> collection = new ArrayList<BeanProperty>();
		try {
			visit(instance, new BeanVisitor() {

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
	 * Visit the supplied bean instance and notify the visitor for each bean property found. This method not recurse into the object graph by looping over collections or by
	 * visiting assosciated objects. For example, a class with a property getSurname() and setSurname(...):</p>
	 * 
	 * <pre>
	 * BeanUtils.visit(myUser, new BeanPropertyVisitor() {
	 * 
	 * 	public void visit(final Object[] stack, final String path, final Object current, final BeanProperty property) {
	 * 		printer.println(&quot;Hello &quot; + property);
	 * 	}
	 * });
	 * </pre>
	 * @param instance
	 *            an object to get the property from
	 * @param visitor
	 *            the visitor which will be notified of every bean property encountered
	 */
	public static void visit(final Object instance, final BeanVisitor visitor) {
		shallowInspector.inspect(instance, visitor);
	}

	/**
	 * Visit the supplied bean instance and notify the visitor for each bean property found. This method <stong>will</strong> recurse into the object graph by looping over
	 * collections and will visit all properties on assosciated objects and their assosciated objects, and so on.
	 * <p>
	 * This method has no protection against stack overflows caused whee the object graph refers to other elements in itself
	 * </p>
	 * 
	 * <pre>
	 * BeanUtils.visitAllAllowOverflow(myUser, new BeanPropertyVisitor() {
	 * 
	 * 	public void visit(final Object[] stack, final String path, final Object current, final BeanProperty property) {
	 * 		printer.println(&quot;Hello &quot; + property);
	 * 	}
	 * });
	 * </pre>
	 * @param instance
	 *            an object to get the property from
	 * @param visitor
	 *            the visitor which will be notified of every bean property encountered
	 */
	public static void visitGraphAllowOverflow(final Object instance, final BeanVisitor visitor) {
		unsafeInspector.inspect(instance, visitor);
	}

	/**
	 * Visit the supplied bean instance and notify the visitor for each bean property found. This method <stong>will</strong> recurse into the object graph by looping over
	 * collections and will visit all properties on assosciated objects and their assosciated objects, and so on.
	 * <p>
	 * This method will at most visit each object once even when the object refers to iteself
	 * </p>
	 * 
	 * <pre>
	 * BeanUtils.visitAll(myUser, new BeanPropertyVisitor() {
	 * 
	 * 	public void visit(final Object[] stack, final String path, final Object current, final BeanProperty property) {
	 * 		printer.println(&quot;Hello &quot; + property);
	 * 	}
	 * });
	 * </pre>
	 * @param instance
	 *            an object to get the property from
	 * @param visitor
	 *            the visitor which will be notified of every bean property encountered
	 */
	public static void visitGraph(final Object instance, final BeanVisitor visitor) {
		safeInspector.inspect(instance, visitor);
	}
}
