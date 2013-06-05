
package uk.co.it.modular.beans;

import static java.lang.System.identityHashCode;
import static org.apache.commons.lang.StringUtils.uncapitalize;
import static uk.co.it.modular.beans.Type.type;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class which inspects the bean and exposes the properties of the bean to support the visitor pattern
 * 
 * @author Stewart Bissett
 */
class BeanInspector {

	static BeanInspector beanInspector() {
		return new BeanInspector(new BeanInspectorConfiguration());
	}

	static BeanInspector graphInspector() {
		return new BeanInspector(new BeanInspectorConfiguration() {

			{
				setInspectChildren(true);
			}
		});
	};

	static class BeanInspectorConfiguration {

		private boolean inspectChildren = false;
		private boolean stopOverflow = true;
		private Integer overflowLimit = 0;

		boolean isInspectChildren() {
			return inspectChildren;
		}

		void setInspectChildren(final boolean inspectChildren) {
			this.inspectChildren = inspectChildren;
		}

		boolean isStopOverflow() {
			return stopOverflow;
		}

		void setStopOverflow(final boolean stopOverflow) {
			this.stopOverflow = stopOverflow;
		}

		Integer getOverflowLimit() {
			return overflowLimit;
		}

		void setOverflowLimit(final Integer overflowLimit) {
			this.overflowLimit = overflowLimit;
		}

	}

	private static final Logger LOG = LoggerFactory.getLogger(BeanInspector.class);

	private final BeanInspectorConfiguration config;
	private final ThreadLocal<Map<Object, Integer>> inspected = new ThreadLocal<Map<Object, Integer>>() {

		@Override
		protected Map<Object, Integer> initialValue() {
			return new HashMap<Object, Integer>();
		}
	};

	BeanInspector(final BeanInspectorConfiguration config) {
		this.config = config;
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
		inspect(instance, null, visitor);
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

		if (instance == null) {
			return;
		}

		final List<Object> stack = new ArrayList<Object>(currentStack);
		logInspection(path, "Object", instance);

		if (config.isStopOverflow()) {
			int instanceKey = identityHashCode(instance);
			Integer hits = inspected.get().get(instanceKey);
			if (hits != null) {
				if (hits > config.getOverflowLimit()) {
					return;
				} else {
					inspected.get().put(instanceKey, ++hits);
				}
			} else {
				inspected.get().put(instanceKey, 1);
			}
		}

		if (!config.isInspectChildren() && currentStack.size() > 0) {
			return;
		}

		Class<? extends Object> type = instance.getClass();
		if (type.isArray()) {
			inspectArray(new ArrayList<Object>(), path, instance, visitor);
		} else if (Iterable.class.isAssignableFrom(type)) {
			inspectIterable(new ArrayList<Object>(), path, (Iterable) instance, visitor);
		} else if (Map.class.isAssignableFrom(type)) {
			inspectMap(new ArrayList<Object>(), path, (Map) instance, visitor);
		} else {
			String rootPath = path == null ? uncapitalize(type(instance.getClass()).simpleName()) : path;
			stack.add(instance);
			for (BeanProperty property : type(instance.getClass()).propertyList()) {
				String nextPath = nextPath(rootPath, property);
				visitor.visit(new BeanPropertyInstance(property, instance), instance, nextPath, stack.toArray());
				if (property.isArray()) {
					Object value = property.getValue(instance);
					if (value != null) {
						inspectArray(stack, nextPath, value, visitor);
					}
				} else if (property.isIterable()) {
					Iterable value = property.getValue(instance, Iterable.class);
					if (value != null) {
						inspectIterable(stack, nextPath, value, visitor);
					}
				} else if (property.isMap()) {
					Map value = property.getValue(instance, Map.class);
					if (value != null) {
						inspectMap(stack, nextPath, value, visitor);
					}
				} else {
					Object propertyValue = property.getValue(instance);
					if (propertyValue != null) {
						inspectObject(stack, nextPath, propertyValue, visitor);
					}
				}
			}
		}
	}

	private String nextPath(final String path, final BeanProperty property) {
		return StringUtils.isEmpty(path) ? property.getName() : path + "." + property.getName();
	}

	private void inspectMap(final List<Object> stack, final String path, final Map<?, ?> instance, final BeanVisitor visitor) {
		logInspection(path, "Map", instance);
		for (Map.Entry<?, ?> entry : instance.entrySet()) {
			String nextPath = path == null ? "map" : path;
			inspectObject(stack, nextPath + "[" + entry.getKey() + "]", entry.getValue(), visitor);
		}
	}

	private void inspectArray(final List<Object> stack, final String path, final Object instance, final BeanVisitor visitor) {
		logInspection(path, "Array", instance);
		for (int i = 0; i < Array.getLength(instance); ++i) {
			String nextPath = path == null ? "array" : path;
			inspectObject(stack, nextPath + "[" + i + "]", Array.get(instance, i), visitor);
		}
	}

	private void inspectIterable(final List<Object> stack, final String path, final Iterable<?> instance, final BeanVisitor visitor) {
		logInspection(path, "Iterable", instance);
		int seq = 0;
		for (Object object : instance) {
			String nextPath = path == null ? "collection" : path;
			inspectObject(stack, nextPath + "[" + (seq++) + "]", object, visitor);
		}
	}

	private void logInspection(final String path, final String loggedType, final Object instance) {
		LOG.trace("Inspect Path [{}]. {} [{}:{}]", new Object[] {
				path, loggedType, instance.getClass().getSimpleName(), identityHashCode(instance)
		});
	}
}
