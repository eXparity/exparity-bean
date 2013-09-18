
package uk.co.it.modular.beans;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.System.identityHashCode;
import static uk.co.it.modular.beans.Type.type;

/**
 * Helper class which inspects the bean and exposes the properties of the bean to support the visitor pattern
 * 
 * @author Stewart Bissett
 */
class InstanceInspector {

	static InstanceInspector beanInspector() {
		return new InstanceInspector(new BeanInspectorConfiguration() {

			{
				setInspectChildren(false);
				setOverflowLimit(0);
				setStopOverflow(true);
			}
		});
	}

	static InstanceInspector graphInspector() {
		return new InstanceInspector(new BeanInspectorConfiguration() {

			{
				setInspectChildren(true);
				setOverflowLimit(0);
				setStopOverflow(true);
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

	private static final Logger LOG = LoggerFactory.getLogger(InstanceInspector.class);

	private final BeanInspectorConfiguration config;
	private final ThreadLocal<Map<Object, Integer>> inspected = new ThreadLocal<Map<Object, Integer>>() {

		@Override
		protected Map<Object, Integer> initialValue() {
			return new HashMap<Object, Integer>();
		}
	};

	private BeanNamingStrategy naming = new CamelCaseNamingStrategy();

	InstanceInspector(final BeanInspectorConfiguration config) {
		this.config = config;
	}

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object
	 * 
	 * @param instance an object instance to inspect for Java Bean properties
	 * @param visitor the visitor to raise events when Java Bean properties are found
	 */
	void inspect(final Object instance, final BeanVisitor visitor) {
		inspect(instance, new BeanPropertyPath(null), visitor);
	}

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object.
	 * <p>
	 * The root object will be referred to be the supplied rootPath parameter.
	 * </p>
	 * 
	 * @param instance an object instance to inspect for Java Bean properties
	 * @param rootPath a name to be used as the root object name for the path included when the visitor is notified
	 * @param visitor the visitor to raise events when Java Bean properties are found
	 */
	void inspect(final Object instance, final BeanPropertyPath rootPath, final BeanVisitor visitor) {
		try {
			inspectObject(new ArrayList<Object>(), rootPath, instance, visitor);
		} finally {
			inspected.get().clear();
		}
	}

	@SuppressWarnings("rawtypes")
	private void inspectObject(final List<Object> currentStack, final BeanPropertyPath path, final Object instance, final BeanVisitor visitor) {

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

		Type type = type(instance.getClass());
		if (type.isArray()) {
			inspectArray(new ArrayList<Object>(), path, instance, visitor);
		} else if (type.is(Iterable.class)) {
			inspectIterable(new ArrayList<Object>(), path, (Iterable) instance, visitor);
		} else if (type.is(Map.class)) {
			inspectMap(new ArrayList<Object>(), path, (Map) instance, visitor);
		} else {
			BeanPropertyPath rootPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(instance.getClass())) : path;
			stack.add(instance);
			for (TypeProperty property : type.setNamingStrategy(naming).propertyList()) {
				BeanPropertyPath nextPath = rootPath.append(property.getName());
				visitor.visit(new BeanProperty(property.getName(), property.getAccessor(), property.getMutator(), instance), instance, nextPath, stack.toArray());
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
					try {
						Object propertyValue = property.getValue(instance);
						if (propertyValue != null) {
							inspectObject(stack, nextPath, propertyValue, visitor);
						}
					} catch (Exception e) {
						LOG.trace("Skip {}. Exception thrown on calling get", property);
					}
				}
			}
		}
	}

	private void inspectMap(final List<Object> stack, final BeanPropertyPath path, final Map<?, ?> instance, final BeanVisitor visitor) {
		logInspection(path, "Map", instance);
		for (Map.Entry<?, ?> entry : instance.entrySet()) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Map.class)) : path;
			inspectObject(stack, nextPath.appendIndex(entry.getKey().toString()), entry.getValue(), visitor);
		}
	}

	private void inspectArray(final List<Object> stack, final BeanPropertyPath path, final Object instance, final BeanVisitor visitor) {
		logInspection(path, "Array", instance);
		for (int i = 0; i < Array.getLength(instance); ++i) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Array.class)) : path;
			inspectObject(stack, nextPath.appendIndex(i), Array.get(instance, i), visitor);
		}
	}

	private void inspectIterable(final List<Object> stack, final BeanPropertyPath path, final Iterable<?> instance, final BeanVisitor visitor) {
		logInspection(path, "Iterable", instance);
		int seq = 0;
		for (Object object : instance) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Collection.class)) : path;
			inspectObject(stack, nextPath.appendIndex(seq++), object, visitor);
		}
	}

	private void logInspection(final BeanPropertyPath path, final String loggedType, final Object instance) {
		LOG.trace("Inspect Path [{}]. {} [{}:{}]", new Object[] {
				path.fullPath(), loggedType, instance.getClass().getSimpleName(), identityHashCode(instance)
		});
	}

	public void setNamingStrategy(final BeanNamingStrategy strategy) {
		this.naming = strategy;
	}
}
