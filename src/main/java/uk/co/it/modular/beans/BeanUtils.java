
package uk.co.it.modular.beans;

import java.util.List;
import java.util.Map;
import static uk.co.it.modular.beans.Bean.bean;
import static uk.co.it.modular.beans.Type.type;

/**
 * Utility methods for inspecting Objects which expose properties which follow the Java Bean get/set standard
 * 
 * @author Stewart Bissett
 */
public abstract class BeanUtils {

	/**
	 * Return a list of the publicly exposes get/set properties on the Bean. For example:
	 * <p/>
	 * 
	 * <pre>
	 * List&lt;BeanPropertyInstance&gt; properties = BeanUtils.propertyList(myObject)
	 * </pre>
	 * 
	 * @param instance an object to get the properties list from
	 */
	public static List<BeanPropertyInstance> propertyList(final Object instance) {
		return bean(instance).propertyList();
	}

	/**
	 * Return a list of the publicly exposes get/set properties on a class. For example:
	 * <p/>
	 * 
	 * <pre>
	 * List&lt;BeanProperty&gt; properties = BeanUtils.propertyList(MyObject.class)
	 * </pre>
	 * 
	 * @param type a class to get the properties list from
	 */
	public static List<BeanProperty> propertyList(final Class<?> type) {
		return type(type).propertyList();
	}

	/**
	 * Return a map of the publicly exposes get/set properties on the Bean with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <pre>
	 * Map&lt;String, BeanPropertyInstance&gt; propertyMap = BeanUtils.propertyMap(myObject)
	 * </pre>
	 * 
	 * @param instance an object to get the properties for
	 */
	public static Map<String, BeanPropertyInstance> propertyMap(final Object instance) {
		return bean(instance).propertyMap();
	}

	/**
	 * Return a map of the publicly exposes get/set properties on the type with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <pre>
	 * Map&lt;String, BeanProperty&gt; propertyMap = BeanUtils.propertyMap(MyObject.class)
	 * </pre>
	 * 
	 * @param type a type to get the properties for
	 */
	public static Map<String, BeanProperty> propertyMap(final Class<?> type) {
		return type(type).propertyMap();
	}

	/**
	 * Test if the supplied instance has a Bean property with the given name. For example</p>
	 * 
	 * <pre>
	 * if ( BeanUtils.hasProperty(aUser, "surname"))) {
	 * 	// Do Something;
	 * }
	 * </pre>
	 * 
	 * @param instance an object to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Object instance, final String name) {
		return bean(instance).hasProperty(name);
	}

	/**
	 * Test if the supplied instance has a Bean property which matches the given predicate. For example:
	 * <p/>
	 * 
	 * <pre>
	 * if ( BeanUtils.hasProperty(aUser, BeanPredicates.named("surname")))) {
	 * 	// Do Something;
	 * }
	 * </pre>
	 * 
	 * @param instance an object to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).hasProperty(predicate);
	}

	/**
	 * Test if the supplied type has a property with the given name. For example:</p>
	 * 
	 * <pre>
	 * if ( BeanUtils.hasProperty(MyObject.class, "surname"))) {
	 * 	// Do Something;
	 * }
	 * </pre>
	 * 
	 * @param type an type to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Class<?> type, final String name) {
		return type(type).hasProperty(name);
	}

	/**
	 * Get the requested property from the instance or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = BeanUtils.propertyNamed(aUser, &quot;surname&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance propertyNamed(final Object instance, final String name) {
		return bean(instance).propertyNamed(name);
	}

	/**
	 * Get the requested property from the type or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * BeanProperty surname = BeanUtils.propertyNamed(MyObject.class, &quot;surname&quot;)
	 * </pre>
	 * 
	 * @param type a type to get the property from
	 * @param name the property name
	 */
	public static BeanProperty propertyNamed(final Class<?> type, final String name) {
		return type(type).propertyNamed(name);
	}

	/**
	 * Get the requested property from the type or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = BeanUtils.get(aUser, &quot;surname&quot;)
	 * </pre>
	 * 
	 * @param instance an instance to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance get(final Object instance, final String name) {
		return bean(instance).get(name);
	}

	/**
	 * Get the property which matches the predicate from the instance or return <code>null</code> if not matching property is found. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = BeanUtils.get(aUser, BeanPredicates.named(&quot;surname&quot;))
	 * </pre>
	 * 
	 * @param instance an instance to get the property from
	 * @param predicate a predicate to match the property
	 */
	public static BeanPropertyInstance get(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).get(predicate);
	}

	/**
	 * Get the requested property from the type or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * BeanProperty surname = BeanUtils.get(MyObject.class, &quot;surname&quot;)
	 * </pre>
	 * 
	 * @param type a type to get the property from
	 * @param name the property name
	 */
	public static BeanProperty get(final Class<?> type, final String name) {
		return type(type).get(name);
	}

	/**
	 * Set the requested property on the given instance. For example:</p>
	 * 
	 * <pre>
	 * BeanUtils.setProperty(aUser, &quot;surname&quot;, &quot;Smith&quot;)
	 * </pre>
	 * 
	 * @param instance an instance to get the property from
	 * @param name the property name
	 * @param value the property value
	 */
	public static boolean setProperty(final Object instance, final String name, final Object value) {
		return bean(instance).setProperty(name, value);
	}

	/**
	 * Set the property which matches the predicate on the given instance. For example:</p>
	 * 
	 * <pre>
	 * BeanUtils.setProperty(aUser, BeanPredicates.named(&quot;surname&quot;), &quot;Smith&quot;)
	 * </pre>
	 * 
	 * @param instance an instance to get the property from
	 * @param predicate a predicate to match the properties
	 * @param value the property value
	 */
	public static boolean setProperty(final Object instance, final BeanPropertyPredicate predicate, final Object value) {
		return bean(instance).setProperty(predicate, value);
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance. For example:</p>
	 * 
	 * <pre>
	 * Object value = BeanUtils.propertyValue(aUser, &quot;surname&quot;))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Object propertyValue(final Object instance, final String propertyName) {
		return bean(instance).propertyValue(propertyName);
	}

	/**
	 * Return the first property value on the instance which matches the predicate or return <code>null</code> if the no matching properties are not present on the instance. For
	 * example:</p>
	 * 
	 * <pre>
	 * Object value = BeanUtils.propertyValue(aUser, BeanPredicates.named(&quot;surname&quot;)))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Object propertyValue(final Object instance, final BeanPropertyPredicate predicated) {
		return bean(instance).propertyValue(predicated);
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance. For example:</p>
	 * 
	 * <pre>
	 * String value = BeanUtils.propertyValue(aUser, &quot;surname&quot;, String.class))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param type the type to return the property value as
	 */
	public static <T> T propertyValue(final Object instance, final String propertyName, final Class<T> type) {
		return bean(instance).propertyValue(propertyName, type);
	}

	/**
	 * Return the first property value on the instance which matches the predicate or return <code>null</code> if the no matching properties are not present on the instance. For
	 * example:</p>
	 * 
	 * <pre>
	 * String value = BeanUtils.propertyValue(aUser, BeanPredicates.named(&quot;surname&quot;)), String.class)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param type the type to return the property value as
	 */
	public static <T> T propertyValue(final Object instance, final BeanPropertyPredicate predicate, final Class<T> type) {
		return bean(instance).propertyValue(predicate, type);
	}

	/**
	 * Return the property type on the instance for the supplied property name or <code>null</code> if the property doesn't exist. For example:</p>
	 * 
	 * <pre>
	 * if (String.class.equals(BeanUtils.propertyType(aUser, &quot;surname&quot;))) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Class<?> propertyType(final Object instance, final String propertyName) {
		return bean(instance).propertyType(propertyName);
	}

	/**
	 * Return the property type on the type for the supplied property name or <code>null</code> if the property doesn't exist. For example:</p>
	 * 
	 * <pre>
	 * if (String.class.equals(BeanUtils.propertyType(MyObject.class, &quot;surname&quot;))) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param type a type to get the property from
	 * @param name the property name
	 */
	public static Class<?> propertyType(final Class<?> type, final String propertyName) {
		return type(type).propertyType(propertyName);
	}

	/**
	 * Return the property type on the instance for the first property which matches the supplied predicate or <code>null</code> if no matching properties are found. For
	 * example:</p>
	 * 
	 * <pre>
	 * if (String.class.equals(BeanUtils.propertyType(aUser, BeanPredicates.named(&quot;surname&quot;)))) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Class<?> propertyType(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).propertyType(predicate);
	}

	/**
	 * Test if the property on the supplied instance is of the supplied type. For example:</p>
	 * 
	 * <pre>
	 * if (BeanUtils.isPropertyType(aUser, &quot;surname&quot;, String.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public static boolean isPropertyType(final Object instance, final String propertyName, final Class<?> type) {
		return bean(instance).isPropertyType(propertyName, type);
	}

	/**
	 * Test if the property which matches the predicate on the supplied instance is of the supplied type. For example:</p>
	 * 
	 * <pre>
	 * if (BeanUtils.isPropertyType(aUser, &quot;surname&quot;, String.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * @param instance an object to get the property from
	 * @param predicate a predicate to match the properties
	 * @param type the expected type of the property
	 */
	public static boolean isPropertyType(final Object instance, final BeanPropertyPredicate predicate, final Class<?> type) {
		return bean(instance).isPropertyType(predicate, type);
	}

	/**
	 * Test if the property on the type is of the supplied type. For example:</p>
	 * 
	 * <pre>
	 * if (BeanUtils.isPropertyType(MyObject.class, &quot;surname&quot;, String.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * @param type a type to get the property from
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public static boolean isPropertyType(final Class<?> type, final String propertyName, final Class<?> expectedType) {
		return type(type).isPropertyType(propertyName, expectedType);
	}

	/**
	 * Find all property properties which match the predicate. For example</p>
	 * 
	 * <pre>
	 * for ( BeanPropertyInstance property : BeanUtils.find(aUser, BeanPredicates.ofType(String.class)) {
	 *   property.setValue(null);
	 * }
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param predicate a predicate to match the properties
	 */
	public static List<BeanPropertyInstance> find(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).find(predicate);
	}

	/**
	 * Find the first instance of the property which matches the given predicate in the instance. For example</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance property = BeanUtils.findAny(aUser, BeanPredicates.withValue(&quot;name&quot; "Bob"))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param predicate a predicate to match the properties
	 */
	public static BeanPropertyInstance findAny(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).findAny(predicate);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties which match the predicate in the supplied instances. For example</p>
	 * 
	 * <pre>
	 * BeanUtils.apply(aUser, BeanPredicates.ofType(String.class), BeanFunctions.setValue(null))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param function the function to apply to the matching properties
	 * @param predicate a predicate to match the properties
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		bean(instance).apply(function, predicate);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties on the supplied instance. For example</p>
	 * 
	 * <pre>
	 * BeanUtils.apply(aUser, BeanFunctions.setValue(null))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param function the function to apply to the matching properties
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function) {
		bean(instance).apply(function);
	}

	/**
	 * Visit the supplied bean instance and notify the visitor for each bean property found. For example:</p>
	 * 
	 * <pre>
	 * BeanUtils.visit(aUser, new BeanPropertyVisitor() {
	 * 
	 * 	public void visit(final BeanPropertyInstance property, final Object current, final BeanPropertyPath path, final Object[] stack) {
	 * 		System.out.println(path.fullPath());
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param visitor the visitor which will be notified of every bean property encountered
	 */
	public static void visit(final Object instance, final BeanVisitor visitor) {
		bean(instance).visit(visitor);
	}

	/**
	 * Visit the supplied class and notify the visitor for each bean property found. For example:</p>
	 * 
	 * <pre>
	 * BeanUtils.visit(MyObject.class, new BeanPropertyVisitor() {
	 * 
	 * 	public void visit(final BeanProperty property) {
	 * 		System.out.println(property.getName());
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @param type the type to get the property from
	 * @param visitor the visitor which will be notified of every bean property encountered
	 */
	public static void visit(final Class<?> type, final TypeVisitor visitor) {
		type(type).visit(visitor);
	}
}
