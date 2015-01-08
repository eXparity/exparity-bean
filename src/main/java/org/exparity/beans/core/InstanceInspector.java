
package org.exparity.beans.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.System.identityHashCode;
import static org.exparity.beans.Type.type;
import static org.exparity.beans.core.InstanceInspector.InspectionDepth.GRAPH;
import static org.exparity.beans.core.InstanceInspector.Overflow.DENY_OVERFLOW;

/**
 * Helper class which inspects the bean and exposes the properties of the bean to support the visitor pattern
 * 
 * @author Stewart Bissett
 */
public class InstanceInspector {

	public enum InspectionDepth {
		BEAN, GRAPH
	};

	public enum Overflow {
		ALLOW_OVERFLOW, DENY_OVERFLOW
	};

	public static InstanceInspector beanInspector() {
		return new InstanceInspector(InspectionDepth.BEAN, Overflow.DENY_OVERFLOW);
	}

	public static InstanceInspector graphInspector() {
		return new InstanceInspector(InspectionDepth.GRAPH, Overflow.DENY_OVERFLOW);
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
	 * @param naming the naming strategy to use for the Java Bean properties
	 * @param visitor the visitor to raise events when Java Bean properties are found
	 */
	public void inspect(final Object instance, final BeanNamingStrategy naming, final BeanVisitor visitor) {
		try {
			if (instance != null) {
				inspectObject(new ArrayList<Object>(), new BeanPropertyPath(naming.describeRoot(instance.getClass())), naming, instance, visitor, new AtomicBoolean());
			}
		} finally {
			inspected.get().clear();
		}
	}

	@SuppressWarnings("rawtypes")
	private void inspectObject(final List<Object> currentStack,
			final BeanPropertyPath path,
			final BeanNamingStrategy naming,
			final Object instance,
			final BeanVisitor visitor,
			final AtomicBoolean stop) {

		if (instance == null) {
			return;
		} else if (stop.get()) {
			LOG.debug("Stopped Visit of {}. Stop set to true", path);
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

		Type type = type(instance.getClass(), naming);
		if (type.isArray()) {
			inspectArray(new ArrayList<Object>(), path, naming, instance, visitor, stop);
		} else if (type.is(Iterable.class)) {
			inspectIterable(new ArrayList<Object>(), path, naming, (Iterable) instance, visitor, stop);
		} else if (type.is(Map.class)) {
			inspectMap(new ArrayList<Object>(), path, naming, (Map) instance, visitor, stop);
		} else {
			BeanPropertyPath rootPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(instance.getClass())) : path;
			stack.add(instance);
			for (TypeProperty property : type.propertyList()) {
				BeanPropertyPath nextPath = rootPath.append(property.getName());
				visitor.visit(new BeanProperty(property.getName(), property.getAccessorWrapper(), property.getMutatorWrapper(), instance),
						instance,
						nextPath,
						stack.toArray(),
						stop);
				if (stop.get()) {
					LOG.debug("Stopped Visit of {}. Stop set to true", nextPath);
					return;
				}
				if (property.isArray()) {
					Object value = property.getValue(instance);
					if (value != null) {
						inspectArray(stack, nextPath, naming, value, visitor, stop);
					}
				} else if (property.isIterable()) {
					Iterable value = property.getValue(instance, Iterable.class);
					if (value != null) {
						inspectIterable(stack, nextPath, naming, value, visitor, stop);
					}
				} else if (property.isMap()) {
					Map value = property.getValue(instance, Map.class);
					if (value != null) {
						inspectMap(stack, nextPath, naming, value, visitor, stop);
					}
				} else {
					try {
						Object propertyValue = property.getValue(instance);
						if (propertyValue != null) {
							inspectObject(stack, nextPath, naming, propertyValue, visitor, stop);
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
		return GRAPH.equals(depth);
	}

	private void inspectMap(final List<Object> stack,
			final BeanPropertyPath path,
			final BeanNamingStrategy naming,
			final Map<?, ?> instance,
			final BeanVisitor visitor,
			final AtomicBoolean stop) {
		logInspection(path, "Map", instance);
		for (Map.Entry<?, ?> entry : instance.entrySet()) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Map.class)) : path;
			inspectObject(stack, nextPath.appendIndex(entry.getKey().toString()), naming, entry.getValue(), visitor, stop);
		}
	}

	private void inspectArray(final List<Object> stack,
			final BeanPropertyPath path,
			final BeanNamingStrategy naming,
			final Object instance,
			final BeanVisitor visitor,
			final AtomicBoolean stop) {
		logInspection(path, "Array", instance);
		for (int i = 0; i < Array.getLength(instance); ++i) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Array.class)) : path;
			inspectObject(stack, nextPath.appendIndex(i), naming, Array.get(instance, i), visitor, stop);
		}
	}

	private void inspectIterable(final List<Object> stack,
			final BeanPropertyPath path,
			final BeanNamingStrategy naming,
			final Iterable<?> instance,
			final BeanVisitor visitor,
			final AtomicBoolean stop) {
		logInspection(path, "Iterable", instance);
		int seq = 0;
		for (Object object : instance) {
			BeanPropertyPath nextPath = path.isEmpty() ? new BeanPropertyPath(naming.describeType(Collection.class)) : path;
			inspectObject(stack, nextPath.appendIndex(seq++), naming, object, visitor, stop);
		}
	}

	private void logInspection(final BeanPropertyPath path, final String loggedType, final Object instance) {
		LOG.trace("Inspect Path [{}]. {} [{}:{}]", new Object[] {
				path.fullPath(), loggedType, instance.getClass().getSimpleName(), identityHashCode(instance)
		});
	}
}
