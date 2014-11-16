
package org.exparity.beans;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exparity.beans.naming.CamelCaseNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.System.identityHashCode;
import static org.exparity.beans.InstanceInspector.InspectionDepth.DEEP;
import static org.exparity.beans.InstanceInspector.Overflow.DENY_OVERFLOW;
import static org.exparity.beans.Type.type;

/**
 * Helper class which inspects the bean and exposes the properties of the bean to support the visitor pattern
 * 
 * @author Stewart Bissett
 */
class InstanceInspector {

	enum InspectionDepth {
		SHALLOW, DEEP
	};

	enum Overflow {
		ALLOW_OVERFLOW, DENY_OVERFLOW
	};

	static InstanceInspector beanInspector() {
		return new InstanceInspector(InspectionDepth.SHALLOW, Overflow.DENY_OVERFLOW);
	}

	static InstanceInspector graphInspector() {
		return new InstanceInspector(InspectionDepth.DEEP, Overflow.DENY_OVERFLOW);
	};

	private static final Logger LOG = LoggerFactory.getLogger(InstanceInspector.class);
	private static final Integer OBJECT_HITS_BEFORE_OVERFLOW = 0;

	private final ThreadLocal<Map<Object, Integer>> inspected = new ThreadLocal<Map<Object, Integer>>() {

		@Override
		protected Map<Object, Integer> initialValue() {
			return new HashMap<Object, Integer>();
		}
	};
	private final InspectionDepth depth;
	private final Overflow overflow;

	InstanceInspector(final InspectionDepth depth, final Overflow overflow) {
		this.depth = depth;
		this.overflow = overflow;
	}

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object
	 * 
	 * @param instance an object instance to inspect for Java Bean properties
	 * @param visitor the visitor to raise events when Java Bean properties are found
	 */
	void inspect(final Object instance, final BeanVisitor visitor) {
		inspect(instance, new CamelCaseNamingStrategy(), visitor);
	}

	/**
	 * Inspect the supplied object and fire callbacks on the supplied {@link BeanVisitor} for every property exposed on the object
	 * 
	 * @param instance an object instance to inspect for Java Bean properties
	 * @param naming the naming strategy to use for the Java Bean properties
	 * @param visitor the visitor to raise events when Java Bean properties are found
	 */
	void inspect(final Object instance, final BeanNamingStrategy naming, final BeanVisitor visitor) {
		try {
			if (instance != null) {
				inspectObject(new ArrayList<Object>(), new BeanPropertyPath(naming.describeRoot(instance.getClass())), naming, instance, visitor);
			}
		} finally {
			inspected.get().clear();
		}
	}

	@SuppressWarnings("rawtypes")
	private void inspectObject(final List<Object> currentStack, final BeanPropertyPath path, final BeanNamingStrategy naming, final Object instance, final BeanVisitor visitor) {

		if (instance == null) {
			return;
		}

		final List<Object> stack = new ArrayList<Object>(currentStack);
		logInspection(path, "Object", instance);

		if (isDenyOverflow()) {
			int instanceKey = identityHashCode(instance);
			Integer hits = inspected.get().get(instanceKey);
			if (hits != null) {
				if (hits > OBJECT_HITS_BEFORE_OVERFLOW) {
					return;
				} else {
					inspected.get().put(instanceKey, ++hits);
				}
			} else {
				inspected.get().put(instanceKey, 1);
			}
		}

		if (!isInspectChildren() && currentStack.size() > 0) {
			return;
		}

		Type type = type(instance.getClass());
		if (type.isArray()) {
			inspectArray(new ArrayList<Object>(), path, naming, instance, visitor);
		} else if (type.is(Iterable.class)) {
			inspectIterable(new ArrayList<Object>(), path, naming, (Iterable) instance, visitor);
		} else if (type.is(Map.class)) {
			inspectMap(new ArrayList<Object>(), path, naming, (Map) instance, visitor);
		} else {
			BeanPropertyPath rootPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(instance.getClass())) : path;
			stack.add(instance);
			for (TypeProperty property : type.setNamingStrategy(naming).propertyList()) {
				BeanPropertyPath nextPath = rootPath.append(property.getName());
				visitor.visit(new BeanProperty(property.getName(), property.getAccessor(), property.getMutator(), instance), instance, nextPath, stack.toArray());
				if (property.isArray()) {
					Object value = property.getValue(instance);
					if (value != null) {
						inspectArray(stack, nextPath, naming, value, visitor);
					}
				} else if (property.isIterable()) {
					Iterable value = property.getValue(instance, Iterable.class);
					if (value != null) {
						inspectIterable(stack, nextPath, naming, value, visitor);
					}
				} else if (property.isMap()) {
					Map value = property.getValue(instance, Map.class);
					if (value != null) {
						inspectMap(stack, nextPath, naming, value, visitor);
					}
				} else {
					try {
						Object propertyValue = property.getValue(instance);
						if (propertyValue != null) {
							inspectObject(stack, nextPath, naming, propertyValue, visitor);
						}
					} catch (Exception e) {
						LOG.trace("Skip {}. Exception thrown on calling get", property);
					}
				}
			}
		}
	}

	private boolean isDenyOverflow() {
		return DENY_OVERFLOW.equals(overflow);
	}

	private boolean isInspectChildren() {
		return DEEP.equals(depth);
	}

	private void inspectMap(final List<Object> stack, final BeanPropertyPath path, final BeanNamingStrategy naming, final Map<?, ?> instance, final BeanVisitor visitor) {
		logInspection(path, "Map", instance);
		for (Map.Entry<?, ?> entry : instance.entrySet()) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Map.class)) : path;
			inspectObject(stack, nextPath.appendIndex(entry.getKey().toString()), naming, entry.getValue(), visitor);
		}
	}

	private void inspectArray(final List<Object> stack, final BeanPropertyPath path, final BeanNamingStrategy naming, final Object instance, final BeanVisitor visitor) {
		logInspection(path, "Array", instance);
		for (int i = 0; i < Array.getLength(instance); ++i) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Array.class)) : path;
			inspectObject(stack, nextPath.appendIndex(i), naming, Array.get(instance, i), visitor);
		}
	}

	private void inspectIterable(final List<Object> stack, final BeanPropertyPath path, final BeanNamingStrategy naming, final Iterable<?> instance, final BeanVisitor visitor) {
		logInspection(path, "Iterable", instance);
		int seq = 0;
		for (Object object : instance) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Collection.class)) : path;
			inspectObject(stack, nextPath.appendIndex(seq++), naming, object, visitor);
		}
	}

	private void logInspection(final BeanPropertyPath path, final String loggedType, final Object instance) {
		LOG.trace("Inspect Path [{}]. {} [{}:{}]", new Object[] {
				path.fullPath(), loggedType, instance.getClass().getSimpleName(), identityHashCode(instance)
		});
	}
}