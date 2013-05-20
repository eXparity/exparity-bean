
package uk.co.it.modular.beans;

import java.util.List;
import java.util.Map;

/**
 * Utility methods for inspecting Objects which expose properties which follow the Java Bean get/set standard
 * 
 * @author Stewart Bissett
 */
public abstract class GraphUtils {

	public static Graph graph(final Object instance) {
		return Graph.graph(instance);
	}

	/**
	 * Return a list of the publicly exposes get/set properties on the Bean. For example:
	 * <p/>
	 * <code>
	 * List&lt;BeanProperty&gt; properties = BeanUtils.propertyList(myObject);
	 * </code>
	 * @param instance
	 *            an object to get the properties list from
	 */
	public static List<BeanPropertyInstance> propertyList(final Object instance) {
		return graph(instance).propertyList();
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
	public static Map<String, BeanPropertyInstance> propertyMap(final Object instance) {
		return graph(instance).propertyMap();
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
		return graph(instance).hasProperty(name);
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
	public static boolean hasProperty(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).hasProperty(predicate);
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
	public static BeanPropertyInstance propertyNamed(final Object instance, final String name) {
		return graph(instance).propertyNamed(name);
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
		return graph(instance).setProperty(name, value);
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
	public static boolean setProperty(final Object instance, final BeanPropertyPredicate predicate, final Object value) {
		return graph(instance).setProperty(predicate, value);
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
	public static Object propertyValue(final Object instance, final String propertyName) {
		return graph(instance).propertyValue(propertyName);
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
	public static <T> T propertyValue(final Object instance, final String propertyName, final Class<T> type) {
		return graph(instance).propertyValue(propertyName, type);
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
	public static Class<?> propertyType(final Object instance, final String propertyName) {
		return graph(instance).propertyType(propertyName);
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
	public static boolean isPropertyType(final Object instance, final String propertyName, final Class<?> type) {
		return graph(instance).isPropertyType(propertyName, type);
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
	public static boolean isPropertyType(final Object instance, final BeanPropertyPredicate predicate, final Class<?> type) {
		return graph(instance).isPropertyType(predicate, type);
	}

	/**
	 * Find all property instances which match the predicate in the supplied instance and it's assosciates. For example</p>
	 * 
	 * <code>
	 * List<BeanPropery> relativesCalledBob = BeanUtils.find(myFamilyTree, BeanPredicates.stringProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static List<BeanPropertyInstance> find(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).find(predicate);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties which match the predicate in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <code>
	 * BeanUtils.applyToGraph(myFamilyTree, deletePerson(), isDeceased()));
	 * </code></p>
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		graph(instance).apply(function, predicate);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <code>
	 * BeanUtils.applyToGraph(myFamilyTree, pruneDeceased()));
	 * </code></p>
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function) {
		graph(instance).apply(function);
	}

	/**
	 * Find the first instance of the property which matches the given predicate in the given instance and it's assosciates. For example</p>
	 * 
	 * <code>
	 * BeanPropery property = BeanUtils.findPropertyInGraph(myFamilyTree, BeanPredicates.withProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static BeanPropertyInstance findFirst(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).findFirst(predicate);
	}

	/**
	 * Visit the supplied bean instance and notify the visitor for each bean property found. This method <stong>will</strong> recurse into the object graph by looping over
	 * collections and will visit all properties on assosciated objects and their assosciated objects, and so on.
	 * <p>
	 * This method will at most visit each object once even when the object refers to iteself
	 * </p>
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
		graph(instance).visit(visitor);
	}
}
