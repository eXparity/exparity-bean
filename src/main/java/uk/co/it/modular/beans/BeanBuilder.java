
package uk.co.it.modular.beans;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.System.identityHashCode;
import static org.apache.commons.lang.math.RandomUtils.nextInt;
import static uk.co.it.modular.beans.Graph.graph;
import static uk.co.it.modular.beans.Type.type;
import static uk.co.it.modular.beans.ValueFactories.*;

/**
 * Builder object for instantiating and populating objects which follow the Java beans standards conventions for getter/setters
 * 
 * @author Stewart Bissett
 */
public class BeanBuilder<T> {

	private static final Logger LOG = LoggerFactory.getLogger(BeanBuilder.class);
	private static final int OVERFLOW_LIMIT = 1;

	private static final Map<Class<?>, Object> NULL_FACTORIES = new HashMap<Class<?>, Object>() {

		private static final long serialVersionUID = 1L;

	};

	private static final Map<Class<?>, Object> RANDOM_FACTORIES = new HashMap<Class<?>, Object>() {

		private static final long serialVersionUID = 1L;

		{
			put(Short.class, aRandomShort());
			put(short.class, aRandomShort());
			put(Integer.class, aRandomInteger());
			put(int.class, aRandomInteger());
			put(Long.class, aRandomLong());
			put(long.class, aRandomLong());
			put(Double.class, aRandomDouble());
			put(double.class, aRandomDouble());
			put(Float.class, aRandomFloat());
			put(float.class, aRandomFloat());
			put(Boolean.class, aRandomBoolean());
			put(boolean.class, aRandomBoolean());
			put(Byte.class, aRandomByte());
			put(byte.class, aRandomByte());
			put(Character.class, aRandomChar());
			put(char.class, aRandomChar());
			put(String.class, aRandomString());
			put(BigDecimal.class, aRandomDecimal());
			put(Date.class, aRandomDate());
			put(List.class, aList());
			put(Set.class, aSet());
			put(Map.class, aMap());
			put(Object.class, aNewInstance());
			put(Enum.class, aRandomEnum());
			put(Array.class, anArray());
		}
	};

	private static final Map<Class<?>, Object> EMPTY_FACTORIES = new HashMap<Class<?>, Object>() {

		private static final long serialVersionUID = 1L;

		{
			put(Short.class, aNullValue());
			put(short.class, theValue(0));
			put(Integer.class, aNullValue());
			put(int.class, theValue(0));
			put(Long.class, aNullValue());
			put(long.class, theValue(0));
			put(Double.class, aNullValue());
			put(double.class, theValue(0.0));
			put(Float.class, aNullValue());
			put(float.class, theValue(0.0));
			put(Boolean.class, aNullValue());
			put(boolean.class, theValue(false));
			put(Byte.class, aNullValue());
			put(byte.class, theValue(0));
			put(Character.class, aNullValue());
			put(char.class, theValue(0));
			put(String.class, aNullValue());
			put(BigDecimal.class, aNullValue());
			put(Date.class, aNullValue());
			put(Enum.class, aNullValue());
			put(List.class, aList());
			put(Set.class, aSet());
			put(Map.class, aMap());
			put(Object.class, aNewInstance());
			put(Array.class, anArray());
		}
	};

	/**
	 * Return an instance of a {@link BeanBuilder} for the given type which can then be populated with values either manually or automatically. For example:
	 * 
	 * <pre>
	 * BeanUtils.anInstanceOf(Person.class).populatedWith(BeanUtils.randomValues()).build();
	 * </pre>
	 * @param type
	 *            the type to return the {@link BeanBuilder} for
	 */
	public static <T> BeanBuilder<T> anInstanceOf(final Class<T> type) {
		return new BeanBuilder<T>(type);
	}

	/**
	 * Return an instance of a {@link BeanBuilder} for the given type which is populated with empty objects but collections, maps, etc which have empty objects. For example:
	 * 
	 * <pre>
	 * BeanUtils.anEmptyInstanceOf(Person.class).build();
	 * </pre>
	 * @param type
	 *            the type to return the {@link BeanBuilder} for
	 */
	public static <T> BeanBuilder<T> anEmptyInstanceOf(final Class<T> type) {
		return new BeanBuilder<T>(type, EMPTY_FACTORIES);
	}

	/**
	 * Return an instance of a {@link BeanBuilder} for the given type which is populated with random values. For example:
	 * 
	 * <pre>
	 * BeanUtils.aRandomInstanceOf(Person.class).build();
	 * </pre>
	 * @param type
	 *            the type to return the {@link BeanBuilder} for
	 */
	public static <T> BeanBuilder<T> aRandomInstanceOf(final Class<T> type) {
		return new BeanBuilder<T>(type, RANDOM_FACTORIES);
	}

	private final Set<String> excludedProperties = new HashSet<String>();
	private final Set<String> excludedPaths = new HashSet<String>();
	private final Map<String, Object> paths = new HashMap<String, Object>();
	private final Map<String, Object> properties = new HashMap<String, Object>();
	private final Map<Class<?>, Object> types = new HashMap<Class<?>, Object>();
	private final Class<T> type;
	private int collectionMin = 1, collectionMax = 5;;

	public BeanBuilder(final Class<T> type) {
		this(type, NULL_FACTORIES);
	}

	private BeanBuilder(final Class<T> type, final Map<Class<?>, Object> valueFactories) {
		this.type = type;
		this.types.putAll(valueFactories);
	}

	public BeanBuilder<T> with(final String propertyOrPathName, final Object value) {
		return with(propertyOrPathName, theValue(value));
	}

	public BeanBuilder<T> with(final Class<?> type, final ValueFactory factory) {
		this.types.put(type, factory);
		return this;
	}

	public BeanBuilder<T> with(final Class<?> type, final ArrayFactory factory) {
		this.types.put(type, factory);
		return this;
	}

	public BeanBuilder<T> with(final String propertyOrPathName, final ValueFactory factory) {
		withPath(propertyOrPathName, factory);
		withProperty(propertyOrPathName, factory);
		return this;
	}

	/**
	 * @deprecated See {@link #property(String, Object)}
	 */
	@Deprecated
	public BeanBuilder<T> withPropertyValue(final String propertyName, final Object value) {
		return withProperty(propertyName, value);
	}

	public BeanBuilder<T> withProperty(final String propertyName, final Object value) {
		return withProperty(propertyName, theValue(value));
	}

	public BeanBuilder<T> withProperty(final String propertyName, final ValueFactory factory) {
		properties.put(propertyName, factory);
		return this;
	}

	public BeanBuilder<T> excludeProperty(final String propertyName) {
		this.excludedProperties.add(propertyName);
		return this;
	}

	/**
	 * @deprecated See {@link #path(String, Object)}
	 */
	@Deprecated
	public BeanBuilder<T> withPathValue(final String path, final Object value) {
		return withPath(path, value);
	}

	public BeanBuilder<T> withPath(final String path, final Object value) {
		return withPath(path, theValue(value));
	}

	public BeanBuilder<T> withPath(final String path, final ValueFactory factory) {
		this.paths.put(path, factory);
		return this;
	}

	public BeanBuilder<T> excludePath(final String path) {
		this.excludedPaths.add(path);
		return this;
	}

	public BeanBuilder<T> aCollectionSizeOf(final int size) {
		return aCollectionSizeRangeOf(size, size);
	}

	public BeanBuilder<T> aCollectionSizeRangeOf(final int min, final int max) {
		this.collectionMin = min;
		this.collectionMax = max;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <X> BeanBuilder<T> usingType(final Class<X> klass, final Class<? extends X> subtypes) {
		return with(klass, oneOf(createInstanceOfFactoriesForTypes(subtypes)));
	}

	public <X> BeanBuilder<T> usingType(final Class<X> klass, final Class<? extends X>... subtypes) {
		return with(klass, oneOf(createInstanceOfFactoriesForTypes(subtypes)));
	}

	public T build() {
		T instance = createNewInstance();
		graph(instance).visit(new BeanVisitor() {

			public void visit(final BeanPropertyInstance property, final Object current, final String path, final Object[] stack) {

				String pathNoIndexes = path.replaceAll("\\[\\w*\\]\\.", ".");

				if (excludedPaths.contains(pathNoIndexes) || excludedProperties.contains(property.getName())) {
					LOG.trace("Ignore  Path [{}]. Explicity excluded", path);
					return;
				}

				Object factory = paths.get(path);
				if (factory == null) {
					factory = paths.get(pathNoIndexes);
					if (factory == null) {
						factory = properties.get(property.getName());
					}
				}

				if (factory != null) {
					assignValue(property, path, factory, type);
					return;
				}

				for (String assignedPath : paths.keySet()) {
					if (pathNoIndexes.startsWith(assignedPath)) {
						LOG.trace("Ignore  Path [{}]. Child of assigned path {}", path, assignedPath);
						return;
					}
				}

				int hits = 0;
				for (Object object : stack) {
					Class<?>[] typeHierachy = type(object).typeHierachy();
					if (property.isType(typeHierachy) || property.hasAnyTypeParameters(typeHierachy)) {
						if ((++hits) > OVERFLOW_LIMIT) {
							LOG.trace("Ignore  Path [{}]. Avoids stack overflow caused by type {}", path, object.getClass().getSimpleName());
							return;
						}
					}
				}

				Object currentPropertyValue = property.getValue();
				if (currentPropertyValue != null && !isEmptyCollection(currentPropertyValue)) {
					LOG.trace("Ignore  Path [{}]. Already set", path);
					return;
				}

				if (property.isArray()) {
					assignValue(property, path, createArray(property.getType().getComponentType()));
				} else if (property.isMap()) {
					assignValue(property, path, createMap(property.getTypeParameter(0), property.getTypeParameter(1), collectionSize()));
				} else if (property.isSet()) {
					assignValue(property, path, createSet(property.getTypeParameter(0), collectionSize()));
				} else if (property.isList() || property.isCollection()) {
					assignValue(property, path, createList(property.getTypeParameter(0), collectionSize()));
				} else {
					assignValue(property, path, createValue(property.getType()));
				}
			}
		});
		return instance;
	}

	private void assignValue(final BeanPropertyInstance property, final String path, final Object factory, final Class<?> type) {
		assignValue(property, path, createValue(factory, type));
	}

	private void assignValue(final BeanPropertyInstance property, final String path, final Object value) {
		if (value != null) {
			LOG.trace("Assign  Path [{}] value [{}:{}]", new Object[] {
					path, value.getClass().getSimpleName(), identityHashCode(value)
			});
			property.setValue(value);
		} else {
			LOG.trace("Assign  Path [{}] value [null]", path);
		}
	}

	private <E> E createValue(final Class<E> type) {
		return createValue(factoryFor(type), type);
	}

	private <E> E createValue(final Object factory, final Class<E> type) {
		if (factory == null) {
			return null;
		} else if (factory instanceof ValueFactory) {
			return ((ValueFactory) factory).createValue(type);
		} else if (factory instanceof ArrayFactory) {
			return ((ArrayFactory) factory).createValue(type, collectionSize());
		} else {
			throw new IllegalArgumentException("Unknown Factory type '" + type(factory).canonicalName() + "'");
		}
	}

	private <I> Object factoryFor(final Class<I> type) {

		for (Entry<Class<?>, Object> keyedFactory : types.entrySet()) {
			if (type.isAssignableFrom(keyedFactory.getKey())) {
				return keyedFactory.getValue();
			}
		}

		if (type.isEnum()) {
			Object factory = types.get(Enum.class);
			if (factory != null) {
				return factory;
			}
		}

		Object factory = types.get(Object.class);
		if (factory != null) {
			return factory;
		}
		return null;
	}

	private <E> Object createArray(final Class<E> type) {
		Object array = createValue(types.get(Array.class), type);
		if (array != null) {
			for (int i = 0; i < Array.getLength(array); ++i) {
				Array.set(array, i, createValue(type));
			}
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private <E> Set<E> createSet(final Class<E> type, final int length) {
		Set<E> set = (Set<E>) createValue(types.get(Set.class), type);
		if (set != null) {
			for (int i = 0; i < length; ++i) {
				set.add(createValue(type));
			}
		}
		return set;
	}

	@SuppressWarnings("unchecked")
	private <E> List<E> createList(final Class<E> type, final int length) {
		List<E> list = (List<E>) createValue(types.get(List.class), type);
		if (list != null) {
			for (int i = 0; i < length; ++i) {
				list.add(createValue(type));
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private <K, V> Map<K, V> createMap(final Class<K> keyType, final Class<V> valueType, final int length) {
		Map<K, V> map = (Map<K, V>) createValue(types.get(Map.class), type);
		if (map != null) {
			for (int i = 0; i < length; ++i) {
				map.put(createValue(keyType), createValue(valueType));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private boolean isEmptyCollection(final Object obj) {
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		} else if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		} else {
			return false;
		}
	}

	private T createNewInstance() {
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			throw new BeanBuilderException("Failed to instantiate '" + type + "'. Error [" + e.getMessage() + "]", e);
		} catch (IllegalAccessException e) {
			throw new BeanBuilderException("Failed to instantiate '" + type + "'. Error [" + e.getMessage() + "]", e);
		}
	}

	private int collectionSize() {
		int min = collectionMin, max = collectionMax;
		if (min == max) {
			return min;
		} else {
			int size = Integer.MIN_VALUE;
			while (size < min) {
				size = nextInt(max);
			}
			return size;
		}
	}

	private <X> List<ValueFactory> createInstanceOfFactoriesForTypes(final Class<? extends X>... subtypes) {
		List<ValueFactory> factories = new ArrayList<ValueFactory>();
		for (Class<?> subtype : subtypes) {
			factories.add(aNewInstanceOf(subtype));
		}
		return factories;
	}

}
