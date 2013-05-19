
package uk.co.it.modular.beans;

import static java.lang.Character.toLowerCase;
import static java.lang.System.identityHashCode;
import static org.apache.commons.lang.ArrayUtils.contains;
import static uk.co.it.modular.beans.BeanInspectorProperty.INSPECT_CHILDREN;
import static uk.co.it.modular.beans.BeanInspectorProperty.STOP_OVERFLOW;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
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

	BeanInspector(final BeanInspectorProperty... properties) {
		this.recurse = contains(properties, INSPECT_CHILDREN);
		this.stopOverflow = contains(properties, STOP_OVERFLOW);
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

	private static final String MUTATOR_PROPERTY_NAME = "set";
	private static final String[] ACCESSOR_PROPERTY_NAMES = new String[] {
			"is", "get"
	};

	private List<BeanProperty> propertyList(final Object instance) {

		Map<String, List<Method>> mutatorMap = createMutatorMap(instance);

		List<BeanProperty> properties = new ArrayList<BeanProperty>();
		for (Method accessor : instance.getClass().getMethods()) {
			final String methodName = accessor.getName();
			for (String prefix : ACCESSOR_PROPERTY_NAMES) {
				if (methodName.startsWith(prefix) && accessor.getParameterTypes().length == 0) {
					String propertyName = convertToPropertyName(methodName, prefix.length());
					Method mutator = getMutatorFor(propertyName, accessor.getReturnType(), mutatorMap);
					if (mutator != null) {
						properties.add(new BeanProperty(propertyName, accessor, mutator));
					}
					break;
				}
			}
		}
		return properties;
	}

	private Method getMutatorFor(final String propertyName, final Class<?> type, final Map<String, List<Method>> mutatorMap) {
		List<Method> mutatorList = mutatorMap.get(propertyName);
		if (mutatorList != null && !mutatorList.isEmpty()) {
			for (Method mutator : mutatorList) {
				if (mutator.getParameterTypes()[0].isAssignableFrom(type)) {
					return mutator;
				}
			}
		}
		return null;
	}

	private Map<String, List<Method>> createMutatorMap(final Object instance) {
		Map<String, List<Method>> mutatorMap = new HashMap<String, List<Method>>();
		for (Method method : instance.getClass().getMethods()) {
			String methodName = method.getName();
			if (isMutator(method, methodName)) {
				String propertyName = convertToPropertyName(methodName, MUTATOR_PROPERTY_NAME.length());
				List<Method> list = mutatorMap.get(propertyName);
				if (list == null) {
					list = new ArrayList<Method>();
					list.add(method);
					mutatorMap.put(propertyName, list);
				} else {
					list.add(method);
				}
			}
		}
		return mutatorMap;
	}

	private boolean isMutator(final Method method, final String methodName) {
		return methodName.startsWith(MUTATOR_PROPERTY_NAME) && method.getParameterTypes().length == 1;
	}

	private static String convertToPropertyName(final String methodName, final int startPos) {
		return toLowerCase(methodName.charAt(startPos)) + methodName.substring(startPos + 1);
	}
}
