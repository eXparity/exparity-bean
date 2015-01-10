
package org.exparity.beans;

import org.exparity.beans.core.BeanNamingStrategy;
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
}
