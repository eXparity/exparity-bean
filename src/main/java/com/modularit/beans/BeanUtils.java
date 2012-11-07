
package com.modularit.beans;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Utility methods for inspecting Objects which expose properties which follow the Java Bean get/set standard
 * 
 * @author Stewart Bissett
 */
public abstract class BeanUtils {

	private static final String MUTATOR_PROPERTY_NAME = "set";
	private static final String[] ACCESSOR_PROPERTY_NAMES = new String[] {
			"is", "get"
	};
	private static final BeanInspector shallowInspector = new BeanInspector(false, false);
	private static final BeanInspector unsafeInspector = new BeanInspector(true, false);
	private static final BeanInspector safeInspector = new BeanInspector(true, true);

	/**
	 * Return a list of the publicly exposes get/set properties on the Bean. For example:
	 * <p/>
	 * <code>
	 * List&lt;BeanProperty&gt; properties = BeanUtils.getProperties(myObject);
	 * </code>
	 * @param instance
	 *            an object to get the properties list from
	 */
	public static List<BeanProperty> getProperties(final Object instance) {
		return convertToList(getPropertyMap(instance).values());
	}

	/**
	 * Return a map of the publicly exposes get/set properties on the Bean with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <code>
	 * Map&lt;String, BeanProperty&gt; propertyMap = BeanUtils.mapProperties(myObject);
	 * </code>
	 * @param instance
	 *            an object to get the properties for
	 */
	public static Map<String, BeanProperty> getPropertyMap(final Object instance) {

		Map<String, Method> accessorMap = getAccessorMap(instance);
		Map<String, List<Method>> mutatorMap = getMutatorMap(instance);

		Map<String, BeanProperty> propertyMap = new HashMap<String, BeanProperty>();
		for (Entry<String, Method> accessorEntry : accessorMap.entrySet()) {
			Method mutator = getMutatorForAccessor(instance, mutatorMap, accessorEntry);
			if (mutator != null) {
				propertyMap.put(accessorEntry.getKey(), createBeanProperty(instance, accessorEntry.getKey(), accessorEntry.getValue(), mutator));
			}
		}
		return propertyMap;
	}

	private static Method getMutatorForAccessor(final Object instance, final Map<String, List<Method>> mutatorMap, final Entry<String, Method> accessorEntry) {
		List<Method> mutatorList = mutatorMap.get(accessorEntry.getKey());
		if (mutatorList != null && !mutatorList.isEmpty()) {
			for (Method mutator : mutatorList) {
				Method accessor = accessorEntry.getValue();
				if (mutator.getParameterTypes()[0].isAssignableFrom(accessor.getReturnType())) {
					return mutator;
				}
			}
		}
		return null;
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
		return getPropertyMap(instance).containsKey(name);
	}

	/**
	 * Get the requested property from the instance or return <code>null</code> if the property is not present
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * if ( property("surname").valueOn(myUser)
	 * BeanProperty surname = BeanUtils.getProperty(myUser, &quot;surname&quot;);
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 */
	public static BeanProperty getProperty(final Object instance, final String name) {
		return getPropertyMap(instance).get(name);
	}

	/**
	 * Set the property value on the instance to the supplied value and return <code>true</code> if the value was successfullly set. Returns <code>false</code> if the property was
	 * not present on the instance
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
	public static boolean setPropertyValue(final Object instance, final String name, final Object value) {
		BeanProperty property = getProperty(instance, name);
		if (property != null) {
			property.setValue(value);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance
	 * <p/>
	 * For example, a class with a property getSurname() and setSurname(...):
	 * 
	 * <code>
	 * &quot;Smith&quot;.equals(BeanUtils.getPropertyValue(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 */
	public static Object getPropertyValue(final Object o, final String propertyName) {
		BeanProperty property = getProperty(o, propertyName);
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
	 * String.class.equals(BeanUtils.getPropertyType(myUser, &quot;surname&quot;));
	 * </code>
	 * @param instance
	 *            an object to get the property from
	 * @param name
	 *            the property name
	 */
	public static Class<?> getPropertyType(final Object o, final String propertyName) {
		BeanProperty property = getProperty(o, propertyName);
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
	public static <T> T getPropertyValue(final Object o, final String propertyName, final Class<T> type) {
		return (T) getPropertyValue(o, propertyName);
	}

	/**
	 * Return the accessor method for the given property
	 */
	public static Method getAccessor(final String propertyName, final Class<?> declaringType) {
		return getAccessorMap(declaringType).get(propertyName);
	}

	/**
	 * Return the mutator method for the given property
	 */
	public static Method getMutator(final String propertyName, final Class<?> declaringType, final Class<?> propertyType) {
		for (Method method : getMutatorMap(declaringType).get(propertyName)) {
			if (method.getParameterTypes()[0].equals(propertyType)) {
				return method;
			}
		}
		return null;
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
		BeanProperty property = getProperty(o, propertyName);
		if (property != null) {
			return property.isType(type);
		}
		return false;
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
	public static void visitAllAllowOverflow(final Object instance, final BeanVisitor visitor) {
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
	public static void visitAll(final Object instance, final BeanVisitor visitor) {
		safeInspector.inspect(instance, visitor);
	}

	private static BeanProperty createBeanProperty(final Object instance, final String propertyName, final Method accessor, final Method mutator) {
		return new BeanProperty(instance, propertyName, accessor, mutator);
	}

	private static Map<String, Method> getAccessorMap(final Object o) {
		return getAccessorMap(o.getClass());
	}

	private static Map<String, Method> getAccessorMap(final Class<?> type) {
		SortedMap<String, Method> propertyMap = new TreeMap<String, Method>();
		for (Method method : type.getMethods()) {
			String methodName = method.getName();
			for (String prefix : ACCESSOR_PROPERTY_NAMES) {
				if (methodName.startsWith(prefix) && method.getParameterTypes().length == 0) {
					propertyMap.put(convertToPropertyName(methodName, prefix.length()), method);
				}
			}
		}
		return propertyMap;
	}

	private static Map<String, List<Method>> getMutatorMap(final Object o) {
		return getMutatorMap(o.getClass());
	}

	private static Map<String, List<Method>> getMutatorMap(final Class<?> type) {
		SortedMap<String, List<Method>> propertyMap = new TreeMap<String, List<Method>>();
		for (Method method : type.getMethods()) {
			String methodName = method.getName();
			if (methodName.startsWith(MUTATOR_PROPERTY_NAME) && method.getParameterTypes().length == 1) {
				String propertyName = convertToPropertyName(methodName, MUTATOR_PROPERTY_NAME.length());
				List<Method> list = propertyMap.get(propertyName);
				if (list == null) {
					list = new ArrayList<Method>();
					list.add(method);
					propertyMap.put(propertyName, list);
				} else {
					list.add(method);
				}
			}
		}
		return propertyMap;
	}

	private static String convertToPropertyName(final String methodName, final int startPos) {
		return Character.toLowerCase(methodName.charAt(startPos)) + methodName.substring(startPos + 1);
	}

	private static List<BeanProperty> convertToList(final Collection<BeanProperty> collection) {
		return new ArrayList<BeanProperty>(collection);
	}

}
