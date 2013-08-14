
package uk.co.it.modular.beans;

import java.util.List;
import java.util.Map;

/**
 * Utility methods for inspecting Objects which expose properties which follow the Java Bean get/set standard
 * 
 * @author Stewart Bissett
 */
public abstract class BeanUtils {

	public static Bean bean(final Object instance) {
		return Bean.bean(instance);
	}

	/**
	 * Return a list of the publicly exposes get/set properties on the Bean. For example:
	 * <p/>
	 * <code>
	 * List&lt;BeanProperty&gt; properties = BeanUtils.propertyList(myObject);
	 * </code>
	 * @param instance an object to get the properties list from
	 */
	public static List<BeanPropertyInstance> propertyList(final Object instance) {
		return bean(instance).propertyList();
	}

	/**
	 * Return a list of the publicly exposes get/set properties on the class. For example:
	 * <p/>
	 * <code>
	 * List&lt;BeanProperty&gt; properties = BeanUtils.propertyList(MyObject.class);
	 * </code>
	 * @param type a class to get the properties list from
	 */
	public static List<BeanProperty> propertyList(final Class<?> type) {
		return type(type).propertyList();
	}

	/**
	 * Return a map of the publicly exposes get/set properties on the Bean with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <code>
	 * Map&lt;String, BeanProperty&gt; propertyMap = BeanUtils.propertyMap(myObject);
	 * </code>
	 * @param instance an object to get the properties for
	 */
	public static Map<String, BeanPropertyInstance> propertyMap(final Object instance) {
		return bean(instance).propertyMap();
	}

	/**
	 * Return a map of the publicly exposes get/set properties on the type with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <code>
	 * Map&lt;String, BeanProperty&gt; propertyMap = BeanUtils.propertyMap(MyObject.class);
	 * </code>
	 * @param instance an object to get the properties for
	 */
	public static Map<String, BeanProperty> propertyMap(final Class<?> type) {
		return type(type).propertyMap();
	}

	/**
	 * Test if the supplied instance has a Bean property with the given name
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.hasProperty(user, "surname")) == true;
	 * </code>
	 * @param instance an object to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Object instance, final String name) {
		return bean(instance).hasProperty(name);
	}

	/**
	 * Test if the supplied instance has a Bean property with the given name
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.hasProperty(user, "surname")) == true;
	 * </code>
	 * @param instance an object to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).hasProperty(predicate);
	}

	/**
	 * Test if the supplied type has a property with the given name
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.hasProperty(Person.class, "surname")) == true;
	 * </code>
	 * @param type an type to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Class<?> type, final String name) {
		return type(type).hasProperty(name);
	}

	public static Type type(final Class<?> type) {
		return Type.type(type);
	}

	/**
	 * Get the requested property from the instance or return <code>null</code> if the property is not present
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanProperty surname = BeanUtils.property(myUser, &quot;surname&quot;);
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance propertyNamed(final Object instance, final String name) {
		return bean(instance).propertyNamed(name);
	}

	/**
	 * Get the requested property from the type or return <code>null</code> if the property is not present
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanProperty surname = BeanUtils.property(Person.class, &quot;surname&quot;);
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanProperty propertyNamed(final Class<?> type, final String name) {
		return type(type).propertyNamed(name);
	}

	/**
	 * Get the requested property from the instance or return <code>null</code> if the property is not present
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanProperty surname = BeanUtils.property(myUser, &quot;surname&quot;);
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance get(final Object instance, final String name) {
		return bean(instance).get(name);
	}

	/**
	 * Get the requested property from the type or return <code>null</code> if the property is not present
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanProperty surname = BeanUtils.property(Person.class, &quot;surname&quot;);
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanProperty get(final Class<?> type, final String name) {
		return type(type).get(name);
	}

	/**
	 * Get the requested property from the instance or return <code>null</code> if the property is not present
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanProperty surname = BeanUtils.property(myUser, &quot;surname&quot;);
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance get(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).get(predicate);
	}

	/**
	 * Set the property value on the instance to the supplied value.
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.setProperty(myUser, &quot;surname&quot;, &quot;Smith&quot;);
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param value the value to set the property to
	 */
	public static boolean setProperty(final Object instance, final String name, final Object value) {
		return bean(instance).setProperty(name, value);
	}

	/**
	 * Set the property value on the instance to the supplied value.
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.setProperty(myUser, &quot;surname&quot;, &quot;Smith&quot;);
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param value the value to set the property to
	 */
	public static boolean setProperty(final Object instance, final BeanPropertyPredicate predicate, final Object value) {
		return bean(instance).setProperty(predicate, value);
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * &quot;Smith&quot;.equals(BeanUtils.propertyValue(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Object propertyValue(final Object instance, final String propertyName) {
		return bean(instance).propertyValue(propertyName);
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * &quot;Smith&quot;.equals(BeanUtils.propertyValue(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Object propertyValue(final Object instance, final BeanPropertyPredicate predicated) {
		return bean(instance).propertyValue(predicated);
	}

	/**
	 * Return the property value on the instance to the supplied value or return <code>null</code> if the property was not present on the instance
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * &quot;Smith&quot;.equals(BeanUtils.getPropertyValue(myUser, &quot;surname&quot;, String.class));
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param name the type to return the property as
	 */
	public static <T> T propertyValue(final Object instance, final String propertyName, final Class<T> type) {
		return bean(instance).propertyValue(propertyName, type);
	}

	/**
	 * Return the property value on the instance to the supplied value or return <code>null</code> if the property was not present on the instance
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * &quot;Smith&quot;.equals(BeanUtils.getPropertyValue(myUser, &quot;surname&quot;, String.class));
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param name the type to return the property as
	 */
	public static <T> T propertyValue(final Object instance, final BeanPropertyPredicate predicate, final Class<T> type) {
		return bean(instance).propertyValue(predicate, type);
	}

	/**
	 * Return the property type on the instance for the supplied property name or <code>null</code> if the property doesn't exist
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * String.class.equals(BeanUtils.propertyType(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Class<?> propertyType(final Object instance, final String propertyName) {
		return bean(instance).propertyType(propertyName);
	}

	/**
	 * Return the property type on the type for the supplied property name or <code>null</code> if the property doesn't exist
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * String.class.equals(BeanUtils.propertyType(Person.class, &quot;surname&quot;));
	 * </code>
	 * @param type the type to get the property from
	 * @param name the property name
	 */
	public static Class<?> propertyType(final Class<?> type, final String propertyName) {
		return type(type).propertyType(propertyName);
	}

	/**
	 * Return the property type on the instance for the supplied property name or <code>null</code> if the property doesn't exist
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * String.class.equals(BeanUtils.propertyType(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Class<?> propertyType(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).propertyType(predicate);
	}

	/**
	 * Test if the property on the supplied instance is of the supplied type.
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.isPropertyType(myUser, &quot;surname&quot;, String.class) == true;
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public static boolean isPropertyType(final Object instance, final String propertyName, final Class<?> type) {
		return bean(instance).isPropertyType(propertyName, type);
	}

	/**
	 * Test if the property on the supplied instance is of the supplied type.
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.isPropertyType(myUser, &quot;surname&quot;, String.class) == true;
	 * </code>
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public static boolean isPropertyType(final Object instance, final BeanPropertyPredicate predicate, final Class<?> type) {
		return bean(instance).isPropertyType(predicate, type);
	}

	/**
	 * Test if the property on the type is of the supplied type.
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * BeanUtils.isPropertyType(Person.class, &quot;surname&quot;, String.class) == true;
	 * </code>
	 * @param type an object to get the property from
	 * @param name the property name
	 * @param expectedType the expected type of the property
	 */
	public static boolean isPropertyType(final Class<?> type, final String propertyName, final Class<?> expectedType) {
		return type(type).isPropertyType(propertyName, expectedType);
	}

	/**
	 * Find all property instances which match the predicate in the supplied instance and it's assosciates. For example</p>
	 * 
	 * <code>
	 * List<BeanPropery> relativesCalledBob = BeanUtils.find(myFamilyTree, BeanPredicates.stringProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static List<BeanPropertyInstance> find(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).find(predicate);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties which match the predicate in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <code>
	 * BeanUtils.applyToGraph(myFamilyTree, deletePerson(), isDeceased()));
	 * </code></p>
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		bean(instance).apply(function, predicate);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <code>
	 * BeanUtils.applyToGraph(myFamilyTree, pruneDeceased()));
	 * </code></p>
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function) {
		bean(instance).apply(function);
	}

	/**
	 * Find the first instance of the property which matches the given predicate in the given instance and it's assosciates. For example</p>
	 * 
	 * <code>
	 * BeanPropery property = BeanUtils.findPropertyInGraph(myFamilyTree, BeanPredicates.withProperty(&quot;name&quot; "Bob"));
	 * </code></p>
	 */
	public static BeanPropertyInstance findAny(final Object instance, final BeanPropertyPredicate predicate) {
		return bean(instance).findAny(predicate);
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
	 * @param instance an object to get the property from
	 * @param visitor the visitor which will be notified of every bean property encountered
	 */
	public static void visit(final Object instance, final BeanVisitor visitor) {
		bean(instance).visit(visitor);
	}
}
