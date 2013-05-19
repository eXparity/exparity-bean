
package uk.co.it.modular.beans;

import static java.lang.Character.toLowerCase;
import static java.lang.System.identityHashCode;
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
class TypeInspector {

	private static final Logger LOG = LoggerFactory.getLogger(TypeInspector.class);

	private static final String MUTATOR_PROPERTY_NAME = "set";
	private static final String[] ACCESSOR_PROPERTY_NAMES = new String[] {
			"is", "get"
	};

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object
	 * 
	 * @param instance
	 *            an object instance to inspect for Java Bean properties
	 * @param visitor
	 *            the visitor to raise events when Java Bean properties are found
	 */
	void inspect(final Class<?> type, final TypeVisitor visitor) {
		inspect(type, "", visitor);
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
	void inspect(final Class<?> type, final String rootPath, final TypeVisitor visitor) {
		inspectType(new ArrayList<Class<?>>(), rootPath, type, visitor);
	}

	private void inspectType(final List<Class<?>> currentStack, final String path, final Class<?> type, final TypeVisitor visitor) {
		final List<Class<?>> stack = new ArrayList<Class<?>>(currentStack);
		logInspection(path, "Object", type);
		for (BeanProperty property : propertyList(type)) {
			visitor.visit(property, type, nextPath(path, property), (Class<?>[]) stack.toArray());
		}
	}

	private String nextPath(final String path, final BeanProperty property) {
		return path.isEmpty() ? property.getName() : path + "." + property.getName();
	}

	private void logInspection(final String path, final String loggedType, final Object instance) {
		LOG.trace("Inspect Path [{}]. {} [{}:{}]", new Object[] {
				path, loggedType, instance.getClass().getSimpleName(), identityHashCode(instance)
		});
	}

	private List<BeanProperty> propertyList(final Class<?> type) {
		Map<String, List<Method>> mutatorMap = createMutatorMap(type);
		List<BeanProperty> properties = new ArrayList<BeanProperty>();
		for (Method accessor : type.getClass().getMethods()) {
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

	private Map<String, List<Method>> createMutatorMap(final Class<?> instance) {
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
