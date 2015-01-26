
package org.exparity.beans;

import org.exparity.beans.core.BeanNamingStrategy;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.Instance;
import org.exparity.beans.core.naming.CamelCaseNamingStrategy;
import static org.exparity.beans.core.InstanceInspector.graphInspector;

/**
 * Utility class for inspecting objects which expose properties which follow the Java Bean get/set standard. For example;</p>
 * 
 * <pre>
 * List&lt;BeanPropertyInstance&gt; = Graph.graph(myObject).propertyList()
 * </pre>
 */
public class Graph extends Instance {

	public static Graph graph(final Object instance) {
		return graph(instance, new CamelCaseNamingStrategy());
	}

	public static Graph graph(final Object instance, final BeanNamingStrategy naming) {
		return new Graph(instance, naming);
	}

	public Graph(final Object instance, final BeanNamingStrategy naming) {
		super(graphInspector(), instance, naming);
	}

	public Graph(final Object instance) {
		this(instance, new CamelCaseNamingStrategy());
	}

	/**
	 * Get the requested property by its path from the instance or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = bean(myObject).propertyAtPath("person.surname")
	 * </pre>
	 * 
	 * @param path the property path
	 */
	public BeanProperty propertyAtPath(final String path) {
		return findAny(BeanPredicates.hasPath(path));
	}

	/**
	 * Get the requested property by its path whilst ignoriing any ordinals if its in a collection, map, or array from the instance or return <code>null</code> if the property is
	 * not present. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance nameOfFirstSibling = bean(myObject).propertyAtPathIgnoreOrdinal("person.siblings.firstname")
	 * </pre>
	 * 
	 * @param path the property path
	 */
	public BeanProperty propertyAtPathIgnoreOrdinal(final String path) {
		return findAny(BeanPredicates.hasPathIgnoreOrdinal(path));
	}
}
