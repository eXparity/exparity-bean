
package uk.co.it.modular.beans;

import static uk.co.it.modular.beans.Graph.graph;
import java.util.List;
import java.util.Map;

/**
 * Utility methods for inspecting objects which expose properties which follow the Java Bean get/set standard.</p>
 * <p>
 * {@link GraphUtils} will perform a deep inspection of the object and perform the requested action across the object and across all it's children and their children and so on.
 * </p>
 * <p>
 * The sister utility {@link BeanUtils} should be use if only a shallow inspection is required.
 * </p>
 * 
 * @author Stewart Bissett
 */
public abstract class GraphUtils {

	/**
	 * Visit the graph and notify the visitor for each bean property found. This method <stong>will</strong> descend into the object graph by looping over collections and will
	 * visit all properties on assosciated objects and their assosciated objects and so on. <strong>This method will at most visit each object once even when the object refers to
	 * itself in order to prevent stack overflows.</strong>
	 * 
	 * <pre>
	 * GraphUtils.visit(myUser, new BeanVisitor() {
	 * 
	 * 	public void visit(final BeanPropertyInstance property, final Object current, final BeanPropertyPath path, final Object[] stack) {
	 * 		System.out.println(path.fullPath());
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @param instance an object to visit the properties on
	 * @param visitor the visitor which will be notified of every bean property encountered
	 */
	public static void visit(final Object instance, final BeanVisitor visitor) {
		graph(instance).visit(visitor);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties which match the predicate in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <pre>
	 * GraphUtils.apply(myFamilyTree, deletePerson(), isDeceased()));
	 * </pre>
	 * 
	 * @param instance an object to apply the function to
	 * @param function the function to be applied to each matching property
	 * @param predicate the predicate to select which properties to apply the function to
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		graph(instance).apply(function, predicate);
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties in the supplied instance and in any of the objects assosciates. For example</p>
	 * 
	 * <pre>
	 * GraphUtils.apply(myFamilyTree, pruneDeceased()));
	 * </pre>
	 * 
	 * @param instance an object to apply the function to
	 * @param function the function to be applied to each matching property
	 */
	public static void apply(final Object instance, final BeanPropertyFunction function) {
		graph(instance).apply(function);
	}

	/**
	 * Find all property instances which match the predicate in the supplied graph. For example</p>
	 * 
	 * <pre>
	 * List<BeanPropertyInstance> relativesCalledBob = GraphUtils.find(myFamilyTree, BeanPredicates.likeValue(&quot;name&quot;,"Bob.*"))
	 * </pre>
	 * 
	 * @param instance an object to find the properties on
	 * @param predicate the predicate to select which properties to return
	 */
	public static List<BeanPropertyInstance> find(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).find(predicate);
	}

	/**
	 * Find the first instance of the property which matches the given predicate in the given instance and it's assosciates. For example</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance property = GraphUtils.findAny(myFamilyTree, BeanPredicates.withProperty(&quot;name&quot; "Bob"));
	 * </pre>
	 * 
	 * @param instance an object to find the properties on
	 * @param predicate the predicate to select which properties to return
	 */
	public static BeanPropertyInstance findAny(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).findAny(predicate);
	}

	/**
	 * Return a list of the publicly exposes get/set properties on the graph. For example:
	 * <p/>
	 * 
	 * <pre>
	 * List&lt;BeanPropertyInstance&gt; properties = GraphUtils.propertyList(myObject)
	 * </pre>
	 * 
	 * @param instance an object to get the properties list from
	 */
	public static List<BeanPropertyInstance> propertyList(final Object instance) {
		return graph(instance).propertyList();
	}

	/**
	 * Return a map of the publicly exposes get/set properties on the graph with the property name as the key and the initial character lowercased. Child properties will use
	 * dot-notation for the property name. For example:
	 * <p/>
	 * 
	 * <pre>
	 * Map&lt;String, BeanPropertyInstance&gt; propertyMap = GraphUtils.propertyMap(anyPerson)
	 * BeanPropertyInstance property = propertyMap.get("person.address")
	 * </pre>
	 * 
	 * @param instance an object to get the properties for
	 */
	public static Map<String, BeanPropertyInstance> propertyMap(final Object instance) {
		return graph(instance).propertyMap();
	}

	/**
	 * Test if the supplied instance or it's children have a property with the given name. For example</p>
	 * 
	 * <pre>
	 * if (GraphUtils.hasProperty(user, &quot;person.children&quot;)) {
	 * 	// do something
	 * }
	 * </pre>
	 * 
	 * @param instance an object to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Object instance, final String name) {
		return graph(instance).hasProperty(name);
	}

	/**
	 * Test if the supplied instance or it's children have a property with the given name. For example</p>
	 * 
	 * <pre>
	 * if (GraphUtils.hasProperty(user, &quot;person.employment.employed&quot;, true)) {
	 * 	// do something
	 * }
	 * </pre>
	 * 
	 * @param instance an object to test against
	 * @param name the property name
	 * @param value the value to test for
	 */
	public static boolean hasProperty(final Object instance, final String name, final Object value) {
		return graph(instance).hasProperty(name, value);
	}

	/**
	 * Test if the supplied instance or it's children have a property which matches the given predicate. For example</p>
	 * 
	 * <pre>
	 * if (GraphUtils.hasProperty(user, BeanPredicates.withValue(&quot;surname&quot;, &quot;Smith&quot;))) {
	 * 	System.Out.println("Found a Smith")
	 * }
	 * </pre>
	 * 
	 * @param instance an object to test against
	 * @param name the property name
	 */
	public static boolean hasProperty(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).hasProperty(predicate);
	}

	/**
	 * Get the requested property from the graph or return <code>null</code> if the property is not present. For example</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = GraphUtils.propertyNamed(myUser, &quot;surname&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance propertyNamed(final Object instance, final String name) {
		return graph(instance).propertyNamed(name);
	}

	/**
	 * Get the requested property from the graph or return <code>null</code> if the property is not present. For example</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = GraphUtils.get(myUser, &quot;surname&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance get(final Object instance, final String name) {
		return graph(instance).get(name);
	}

	/**
	 * Get the firt property from the graph which matches the predicate or return <code>null</code> if the property is not present. For example</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = GraphUtils.get(myUser, BeanPredicates.named(&quot;surname&quot;))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static BeanPropertyInstance get(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).get(predicate);
	}

	/**
	 * Set the property value on the instance or it's children to the supplied value. For example:
	 * 
	 * <pre>
	 * GraphUtils.setProperty(myUser, &quot;surname&quot;, &quot;Smith&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param value the value to set the property to
	 */
	public static boolean setProperty(final Object instance, final String name, final Object value) {
		return graph(instance).setProperty(name, value);
	}

	/**
	 * Set the property value on the instance or it's children which matches the predicate to the supplied value. For example: </p>
	 * 
	 * <pre>
	 * GraphUtils.setProperty(myUser, BeanPredicates.named(&quot;surname&quot;), &quot;Smith&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param value the value to set the property to
	 */
	public static boolean setProperty(final Object instance, final BeanPropertyPredicate predicate, final Object value) {
		return graph(instance).setProperty(predicate, value);
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the graph. For example:
	 * <p/>
	 * 
	 * <pre>
	 * Integer age = (Integer)GraphUtils.propertyValue(myUser, &quot;age&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Object propertyValue(final Object instance, final String propertyName) {
		return graph(instance).propertyValue(propertyName);
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the graph. For example:
	 * <p/>
	 * 
	 * <pre>
	 * Integer age = GraphUtils.propertyValue(myUser, &quot;age&quot;, Integer.class)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param name the type to return the property as
	 */
	public static <T> T propertyValue(final Object instance, final String propertyName, final Class<T> type) {
		return graph(instance).propertyValue(propertyName, type);
	}

	/**
	 * Return the first property value on the instance for the property which matches the supplied predicate or return <code>null</code> if the property is not present on the
	 * graph. For example:
	 * <p/>
	 * 
	 * <pre>
	 * Object age = GraphUtils.propertyValue(myUser, BeanPredicates.named(&quot;age&quot;))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param predicate a predicate to match the property
	 */
	public static Object propertyValue(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).propertyValue(predicate);
	}

	/**
	 * Return the first property value on the instance for the property which matches the supplied predicate or return <code>null</code> if the property is not present on the
	 * graph. For example:
	 * <p/>
	 * 
	 * <pre>
	 * Intenger age = GraphUtils.propertyValue(myUser, BeanPredicates.named(&quot;age&quot;), Integer.class)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param predicate a predicate to match the property
	 * @param type the type to return the property as
	 */
	public static <T> T propertyValue(final Object instance, final BeanPropertyPredicate predicate, final Class<T> type) {
		return graph(instance).propertyValue(predicate, type);
	}

	/**
	 * Return the property type on the instance for the first property which matches the predicate or <code>null</code> if no matching property exists. For example:</p>
	 * 
	 * <pre>
	 * Class&lt;?&gt; managerType = GraphUtils.propertyType(myUser, BeanPr&quot;person.manager&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param predicate a predicate to match the property
	 */
	public static Class<?> propertyType(final Object instance, final BeanPropertyPredicate predicate) {
		return graph(instance).propertyType(predicate);
	}

	/**
	 * Return the property type on the instance for the supplied property name or <code>null</code> if the property doesn't exist. For example:</p>
	 * 
	 * <pre>
	 * Class&lt;?&gt; managerType = GraphUtils.propertyType(myUser, &quot;person.manager&quot;)
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public static Class<?> propertyType(final Object instance, final String propertyName) {
		return graph(instance).propertyType(propertyName);
	}

	/**
	 * Test if the property on the supplied instance is of the supplied type. For example:
	 * 
	 * <pre>
	 * if (GraphUtils.isPropertyType(myUser, &quot;person.manager&quot;, Director.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public static boolean isPropertyType(final Object instance, final String propertyName, final Class<?> type) {
		return graph(instance).isPropertyType(propertyName, type);
	}

	/**
	 * Test if the property on the supplied instance is of the supplied type. For example
	 * <p/>
	 * 
	 * <pre>
	 * if (GraphUtils.isPropertyType(myUser, BeanPredicates.named(&quot;person.manager&quot;), Director.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public static boolean isPropertyType(final Object instance, final BeanPropertyPredicate predicate, final Class<?> type) {
		return graph(instance).isPropertyType(predicate, type);
	}
}
