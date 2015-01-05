
package org.exparity.beans.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.exparity.beans.naming.CamelCaseNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.System.identityHashCode;
import static org.exparity.beans.core.MethodUtils.isAccessor;
import static org.exparity.beans.core.MethodUtils.isSetter;
import static org.exparity.beans.core.MethodUtils.toPropertyName;

/**
 * Helper class which inspects the bean and exposes the properties of the bean to support the visitor pattern
 * 
 * @author Stewart Bissett
 */
public class TypeInspector {

	private static final Logger LOG = LoggerFactory.getLogger(TypeInspector.class);

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object
	 * 
	 * @param instance an object instance to inspect for Java Bean properties
	 * @param visitor the visitor to raise events when Java Bean properties are found
	 */
	public void inspect(final Class<?> type, final TypeVisitor visitor) {
		inspect(type, new CamelCaseNamingStrategy(), visitor);
	}

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object
	 * 
	 * @param instance an object instance to inspect for Java Bean properties
	 * @param naming the naming strategy to use for the Java Bean properties
	 * @param visitor the visitor to raise events when Java Bean properties are found
	 */
	public void inspect(final Class<?> type, final BeanNamingStrategy naming, final TypeVisitor visitor) {
		inspectType(type, naming, visitor);
	}

	public List<TypeProperty> propertyList(final Class<?> type, final BeanNamingStrategy naming) {
		Map<String, List<Method>> mutatorMap = createMutatorMap(type, naming);
		List<TypeProperty> properties = new ArrayList<TypeProperty>();
		for (Method accessor : type.getMethods()) {
			if (isAccessor(accessor)) {
				String propertyName = toPropertyName(accessor, naming);
				Method mutator = getMutatorFor(propertyName, accessor.getReturnType(), mutatorMap);
				if (mutator != null) {
					properties.add(new TypeProperty(propertyName, accessor, mutator));
				}
			}
		}
		return properties;
	}

	public List<ImmutableTypeProperty> accessorList(final Class<?> type, final BeanNamingStrategy naming) {
		List<ImmutableTypeProperty> properties = new ArrayList<ImmutableTypeProperty>();
		for (Method accessor : type.getMethods()) {
			if (isAccessor(accessor)) {
				properties.add(new ImmutableTypeProperty(toPropertyName(accessor, naming), accessor));
			}
		}
		return properties;
	}

	private void inspectType(final Class<?> type, final BeanNamingStrategy naming, final TypeVisitor visitor) {
		logInspection(naming.describeRoot(type), "Object", type);
		for (TypeProperty property : propertyList(type, naming)) {
			visitor.visit(property);
		}
	}

	private void logInspection(final String path, final String loggedType, final Object instance) {
		LOG.trace("Inspect Path [{}]. {} [{}:{}]", new Object[] {
				path, loggedType, instance.getClass().getSimpleName(), identityHashCode(instance)
		});
	}

	private Method getMutatorFor(final String propertyName, final Class<?> type, final Map<String, List<Method>> mutatorMap) {
		List<Method> mutatorList = mutatorMap.get(propertyName);
		if (mutatorList != null) {
			for (Method mutator : mutatorList) {
				if (mutator.getParameterTypes()[0].isAssignableFrom(type)) {
					return mutator;
				}
			}
		}
		return null;
	}

	private Map<String, List<Method>> createMutatorMap(final Class<?> type, final BeanNamingStrategy naming) {
		Map<String, List<Method>> mutatorMap = new HashMap<String, List<Method>>();
		for (Method method : type.getMethods()) {
			if (isSetter(method)) {
				String propertyName = toPropertyName(method, naming);
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
}