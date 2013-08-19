
package uk.co.it.modular.beans;

import static uk.co.it.modular.beans.InstanceInspector.graphInspector;

/**
 * @author Stewart Bissett
 */
public class Graph extends Instance {

	public static Graph graph(final Object instance) {
		return new Graph(instance);
	}

	public Graph(final Object instance) {
		super(graphInspector(), instance);
	}
}
