
package com.modularit.beans;

import static com.modularit.beans.BeanUtils.propertyList;
import static java.lang.System.identityHashCode;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class which inspects the bean and exposes the properties of the bean to support the visitor pattern
 * 
 * @author Stewart Bissett
 */
class BeanInspector {

	private static final Logger LOG = LoggerFactory.getLogger(BeanInspector.class);

	private final ThreadLocal<Map<Object, Integer>> inspected = new ThreadLocal<Map<Object, Integer>>() {

		@Override
		protected Map<Object, Integer> initialValue() {
			return new HashMap<Object, Integer>();
		}
	};

	private final boolean recurse;
	private final boolean stopOverflow;
	private final Integer overflowLimit = 0;

	BeanInspector(final boolean recurse, final boolean stopOverflow) {
		this.recurse = recurse;
		this.stopOverflow = stopOverflow;
	}

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object
	 * 
	 * @param instance
	 *            an object instance to inspect for Java Bean properties
	 * @param visitor
	 *            the visitor to raise events when Java Bean properties are found
	 */
	void inspect(final Object instance, final BeanVisitor visitor) {
		inspect(instance, "", visitor);
	}

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object.
	 * <p>
	 * The root object will be referred to be the supplied rootPath parameter.
	 * </p>
	 * 
	 * @param instance
	 *            an object instance to inspect for Java Bean properties
	 * @param rootPath
	 *            a name to be used as the root object name for the path included when the visitor is notified
	 * @param visitor
	 *            the visitor to raise events when Java Bean properties are found
	 */
	void inspect(final Object instance, final String rootPath, final BeanVisitor visitor) {
		try {
			inspectObject(new ArrayList<Object>(), rootPath, instance, visitor);
		} finally {
			inspected.get().clear();
		}
	}

	@SuppressWarnings("rawtypes")
	private void inspectObject(final List<Object> currentStack, final String path, final Object instance, final BeanVisitor visitor) {

		final List<Object> stack = new ArrayList<Object>(currentStack);
		logInspection(path, "Object", instance);

		if (instance == null) {
			return;
		}

		if (stopOverflow) {
			int instanceKey = identityHashCode(instance);
			Integer hits = inspected.get().get(instanceKey);
			if (hits != null) {
				if (hits > overflowLimit) {
					return;
				} else {
					inspected.get().put(instanceKey, ++hits);
				}
			} else {
				inspected.get().put(instanceKey, 1);
			}
		}

		if (!recurse) {
			for (BeanProperty property : propertyList(instance)) {
				visitor.visit(property, instance, nextPath(path, property), stack.toArray());
			}
		} else {
			Class<? extends Object> type = instance.getClass();
			if (type.isArray()) {
				inspectArray(new ArrayList<Object>(), path, instance, visitor);
			} else if (Iterable.class.isAssignableFrom(type)) {
				inspectIterable(new ArrayList<Object>(), path, (Iterable) instance, visitor);
			} else if (Map.class.isAssignableFrom(type)) {
				inspectMap(new ArrayList<Object>(), path, (Map) instance, visitor);
			} else {
				for (BeanProperty property : propertyList(instance)) {
					stack.add(instance);
					String nextPath = nextPath(path, property);
					visitor.visit(property, instance, nextPath, stack.toArray());
					if (property.isArray()) {
						Object value = property.getValue();
						if (value != null) {
							inspectArray(stack, nextPath, value, visitor);
						}
					} else if (property.isIterable()) {
						Iterable value = property.getValue(Iterable.class);
						if (value != null) {
							inspectIterable(stack, nextPath, value, visitor);
						}
					} else if (property.isMap()) {
						Map value = property.getValue(Map.class);
						if (value != null) {
							inspectMap(stack, nextPath, value, visitor);
						}
					} else {
						Object propertyValue = property.getValue();
						if (propertyValue != null) {
							inspectObject(stack, nextPath, propertyValue, visitor);
						}
					}
				}
			}
		}
	}

	private String nextPath(final String path, final BeanProperty property) {
		return path.isEmpty() ? property.getName() : path + "." + property.getName();
	}

	private void inspectMap(final List<Object> stack, final String path, final Map<?, ?> instance, final BeanVisitor visitor) {
		logInspection(path, "Map", instance);
		for (Map.Entry<?, ?> entry : instance.entrySet()) {
			String nextPath = path.isEmpty() ? "map" : path;
			inspectObject(stack, nextPath + "[" + entry.getKey() + "]", entry.getValue(), visitor);
		}
	}

	private void inspectArray(final List<Object> stack, final String path, final Object instance, final BeanVisitor visitor) {
		logInspection(path, "Array", instance);
		for (int i = 0; i < Array.getLength(instance); ++i) {
			String nextPath = path.isEmpty() ? "array" : path;
			inspectObject(stack, nextPath + "[" + i + "]", Array.get(instance, i), visitor);
		}
	}

	private void inspectIterable(final List<Object> stack, final String path, final Iterable<?> instance, final BeanVisitor visitor) {
		logInspection(path, "Iterable", instance);
		int seq = 0;
		for (Object object : instance) {
			String nextPath = path.isEmpty() ? "collection" : path;
			inspectObject(stack, nextPath + "[" + (seq++) + "]", object, visitor);
		}
	}

	private void logInspection(final String path, final String loggedType, final Object instance) {
		LOG.trace("Inspect Path [{}]. {} [{}:{}]", new Object[] {
				path, loggedType, instance.getClass().getSimpleName(), identityHashCode(instance)
		});
	}
}
