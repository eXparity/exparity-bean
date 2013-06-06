
package uk.co.it.modular.beans;

import static java.lang.System.identityHashCode;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.math.RandomUtils.*;
import static org.apache.commons.lang.time.DateUtils.addSeconds;
import static uk.co.it.modular.beans.Type.type;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder object for instantiating and populating objects which follow the Java beans standards conventions for getter/setters
 * 
 * @author Stewart Bissett
 */
public class BeanBuilder<T> {

	private static final int OVERFLOW_LIMIT = 1;
	private static final Logger LOG = LoggerFactory.getLogger(BeanBuilder.class);

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
		return new BeanBuilder<T>(type).populatedWithEmptyValues();
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
		return new BeanBuilder<T>(type).populatedWithRandomValues();
	}

	/**
	 * Interface to be implement by classes which can return values to the requested types
	 * 
	 * @author Stewart Bissett
	 */
	public interface BeanBuilderPropertySource {

		public Object createArray(final Class<?> type, final int size);

		public <T> List<T> createList(final Class<T> type);

		public <T> Set<T> createSet(final Class<T> type);

		public <K, V> Map<K, V> createMap(final Class<K> keyType, Class<V> valueType);

		/**
		 * Return a {@link String} instance or <code>null</code>
		 */
		public String createString();

		/**
		 * Return an {@link Integer} instance or <code>null</code>
		 */
		public Integer createInt();

		/**
		 * Return a {@link Short} instance or <code>null</code>
		 */
		public Short createShort();

		/**
		 * Return a {@link Long} instance or <code>null</code>
		 */
		public Long createLong();

		/**
		 * Return a {@link Double} instance or <code>null</code>
		 */
		public Double createDouble();

		/**
		 * Return a {@link Float} instance or <code>null</code>
		 */
		public Float createFloat();

		/**
		 * Return a {@link Boolean} instance or <code>null</code>
		 */
		public Boolean createBoolean();

		/**
		 * Return a {@link Date} instance or <code>null</code>
		 */
		public Date createDate();

		/**
		 * Return a {@link BigDecimal} instance or <code>null</code>
		 */
		public BigDecimal createDecimal();

		/**
		 * Return a {@link Byte} instance or <code>null</code>
		 */
		public Byte createByte();

		/**
		 * Return a {@link Character} instance or <code>null</code>
		 */
		public Character createChar();

		/**
		 * Return one other enumeration values or <code>null</code>
		 */
		public <E> E createEnum(final Class<E> enumType);

	}

	private final Map<String, Object> properties = new HashMap<String, Object>();
	private final Set<String> excludedProperties = new HashSet<String>();
	private final Map<String, Object> paths = new HashMap<String, Object>();
	private final Set<String> excludedPaths = new HashSet<String>();
	private final Map<Class<?>, List<Class<?>>> subtypes = new HashMap<Class<?>, List<Class<?>>>();
	private final Class<T> type;
	private BeanBuilderPropertySource values = new NullValuePropertySource();
	private int minCollectionSize = 1, maxCollectionSize = 5;

	public BeanBuilder(final Class<T> type) {
		this.type = type;
	}

	public BeanBuilder<T> populatedWith(final BeanBuilderPropertySource values) {
		this.values = values;
		return this;
	}

	public BeanBuilder<T> populatedWithRandomValues() {
		return populatedWith(new RandomValuePropertySource());
	}

	public BeanBuilder<T> populatedWithEmptyValues() {
		return populatedWith(new EmptyValuePropertySource());
	}

	public BeanBuilder<T> with(final String propertyOrPathName, final Object value) {
		this.properties.put(propertyOrPathName, value);
		this.paths.put(propertyOrPathName, value);
		return this;
	}

	public BeanBuilder<T> withPropertyValue(final String propertyName, final Object value) {
		this.properties.put(propertyName, value);
		return this;
	}

	public BeanBuilder<T> excludeProperty(final String propertyName) {
		this.excludedProperties.add(propertyName);
		return this;
	}

	public BeanBuilder<T> withPathValue(final String path, final Object value) {
		this.paths.put(path, value);
		return this;
	}

	public BeanBuilder<T> excludePath(final String path) {
		this.excludedPaths.add(path);
		return this;
	}

	public BeanBuilder<T> withCollectionSize(final int size) {
		return withCollectionSize(size, size);
	}

	public BeanBuilder<T> withCollectionSize(final int min, final int max) {
		this.minCollectionSize = min;
		this.maxCollectionSize = max;
		return this;
	}

	public <X> BeanBuilder<T> withSubtype(final Class<X> klass, final Class<? extends X> subtype) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(subtype);
		this.subtypes.put(klass, list);
		return this;
	}

	public T build() {
		T instance = createNewInstance();
		GraphUtils.visit(instance, new BeanVisitor() {

			public void visit(final BeanPropertyInstance property, final Object current, final String path, final Object[] stack) {

				String pathNoIndexes = path.replaceAll("\\[\\w*\\]\\.", ".");

				if (excludedPaths.contains(pathNoIndexes) || excludedProperties.contains(property.getName())) {
					LOG.trace("Ignore  Path [{}]. Explicity excluded", path);
					return;
				}

				Object value = null;
				if (paths.containsKey(pathNoIndexes)) {
					value = paths.get(pathNoIndexes);
				} else if (properties.containsKey(property.getName())) {
					value = properties.get(property.getName());
				} else {
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
					value = createValue(property);
				}

				if (value != null) {
					LOG.trace("Assign  Path [{}] value [{}:{}]", new Object[] {
							path, value.getClass().getSimpleName(), identityHashCode(value)
					});
					property.setValue(value);
				} else {
					LOG.trace("Assign  Path [{}] value [null]", path);
				}
			}
		});
		return instance;
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
			throw new BeanBuilderException(e);
		}
	}

	private Object createValue(final BeanPropertyInstance property) {
		if (property.isArray()) {
			return createArray(property.getType().getComponentType());
		} else if (property.isMap()) {
			return createMap(property.getTypeParameter(0), property.getTypeParameter(1));
		} else if (property.isSet()) {
			return createSet(property.getTypeParameter(0));
		} else if (property.isList() || property.isCollection()) {
			return createList(property.getTypeParameter(0));
		} else {
			return createValue(property.getType());
		}
	}

	@SuppressWarnings("unchecked")
	private <V> V createValue(final Class<V> type) {
		if (isType(type, String.class)) {
			return (V) values.createString();
		} else if (isType(type, Integer.class)) {
			return (V) values.createInt();
		} else if (isType(type, int.class)) {
			return (V) ObjectUtils.defaultIfNull(values.createInt(), 0);
		} else if (isType(type, Short.class)) {
			return (V) values.createShort();
		} else if (isType(type, Long.class)) {
			return (V) values.createLong();
		} else if (isType(type, Double.class)) {
			return (V) values.createDouble();
		} else if (isType(type, Float.class)) {
			return (V) values.createFloat();
		} else if (isType(type, Boolean.class)) {
			return (V) values.createBoolean();
		} else if (isType(type, Byte.class)) {
			return (V) values.createByte();
		} else if (isType(type, Character.class)) {
			return (V) values.createChar();
		} else if (isType(type, Date.class)) {
			return (V) values.createDate();
		} else if (isType(type, BigDecimal.class)) {
			return (V) values.createDecimal();
		} else if (type.isEnum()) {
			return values.createEnum(type);
		} else {
			List<Class<?>> candidates = subtypes.get(type);
			if (candidates != null && !candidates.isEmpty()) {
				return (V) newInstance(candidates.get(nextInt(candidates.size())));
			} else {
				return newInstance(type);
			}
		}
	}

	private <I> I newInstance(final Class<I> type) {
		try {
			return type.newInstance();
		} catch (Exception e) {
			throw new BeanBuilderException("Failed to instantiate instance of '" + type.getCanonicalName() + "'", e);
		}
	}

	private <E> Object createArray(final Class<E> type) {
		int length = collectionSize();
		Object array = values.createArray(type, length);
		if (array != null) {
			for (int i = 0; i < length; ++i) {
				Array.set(array, i, createValue(type));
			}
		}
		return array;
	}

	private <E> Set<E> createSet(final Class<E> type) {
		Set<E> set = values.createSet(type);
		if (set != null) {
			for (int i = 0; i < collectionSize(); ++i) {
				set.add(createValue(type));
			}
		}
		return set;
	}

	private <E> List<E> createList(final Class<E> type) {
		List<E> list = values.createList(type);
		if (list != null) {
			for (int i = 0; i < collectionSize(); ++i) {
				list.add(createValue(type));
			}
		}
		return list;
	}

	private <K, V> Map<K, V> createMap(final Class<K> keyType, final Class<V> valueType) {
		Map<K, V> map = values.createMap(keyType, valueType);
		if (map != null) {
			for (int i = 0; i < collectionSize(); ++i) {
				map.put(createValue(keyType), createValue(valueType));
			}
		}
		return map;
	}

	private int collectionSize() {
		if (minCollectionSize == maxCollectionSize) {
			return minCollectionSize;
		} else {
			int size = Integer.MIN_VALUE;
			while (size < minCollectionSize) {
				size = nextInt(maxCollectionSize);
			}
			return size;
		}
	}

	private boolean isType(final Class<?> type, final Class<?>... options) {
		for (Class<?> option : options) {
			if (option.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}

	private static class RandomValuePropertySource implements BeanBuilderPropertySource {

		private static final int MAX_STRING_LENGTH = 50;
		private static final int MINUTES_PER_HOUR = 60;
		private static final int HOURS_PER_DAY = 24;
		private static final int DAYS_PER_YEAR = 365;
		private static final int SECONDS_IN_A_YEAR = MINUTES_PER_HOUR * HOURS_PER_DAY * DAYS_PER_YEAR;

		public String createString() {
			return randomAlphanumeric(MAX_STRING_LENGTH);
		}

		public Integer createInt() {
			return Integer.valueOf(nextInt());
		}

		public Short createShort() {
			return Short.valueOf((short) nextInt(Short.MAX_VALUE));
		}

		public Long createLong() {
			return Long.valueOf(nextLong());
		}

		public Double createDouble() {
			return Double.valueOf(nextDouble());
		}

		public Float createFloat() {
			return Float.valueOf(nextFloat());
		}

		public Boolean createBoolean() {
			return Boolean.valueOf(nextBoolean());
		}

		public Date createDate() {
			return addSeconds(new Date(), nextInt(SECONDS_IN_A_YEAR));
		}

		public BigDecimal createDecimal() {
			return new BigDecimal(createDouble());
		}

		public Byte createByte() {
			return (byte) nextInt(Byte.MAX_VALUE);
		}

		public Character createChar() {
			return randomAlphabetic(1).charAt(0);
		}

		public <E> E createEnum(final Class<E> enumType) {
			E[] enumerationValues = enumType.getEnumConstants();
			if (enumerationValues.length == 0) {
				return null;
			} else {
				return enumerationValues[nextInt(enumerationValues.length)];
			}
		}

		public Object createArray(final Class<?> type, final int size) {
			return Array.newInstance(type, size);
		}

		public <T> List<T> createList(final Class<T> type) {
			return new ArrayList<T>();
		}

		public <T> Set<T> createSet(final Class<T> type) {
			return new HashSet<T>();
		}

		public <K, V> Map<K, V> createMap(final Class<K> keyType, final Class<V> valueType) {
			return new HashMap<K, V>();
		}

	}

	private static class EmptyValuePropertySource implements BeanBuilderPropertySource {

		public String createString() {
			return null;
		}

		public Integer createInt() {
			return null;
		}

		public Short createShort() {
			return null;
		}

		public Long createLong() {
			return null;
		}

		public Double createDouble() {
			return null;
		}

		public Float createFloat() {
			return null;
		}

		public Boolean createBoolean() {
			return null;
		}

		public Date createDate() {
			return null;
		}

		public BigDecimal createDecimal() {
			return null;
		}

		public Byte createByte() {
			return null;
		}

		public Character createChar() {
			return null;
		}

		public <E> E createEnum(final Class<E> enumType) {
			return null;
		}

		public Object createArray(final Class<?> type, final int size) {
			return Array.newInstance(type, size);
		}

		public <T> List<T> createList(final Class<T> type) {
			return new ArrayList<T>();
		}

		public <T> Set<T> createSet(final Class<T> type) {
			return new HashSet<T>();
		}

		public <K, V> Map<K, V> createMap(final Class<K> keyType, final Class<V> valueType) {
			return new HashMap<K, V>();
		}

	}

	private static class NullValuePropertySource implements BeanBuilderPropertySource {

		public String createString() {
			return null;
		}

		public Integer createInt() {
			return null;
		}

		public Short createShort() {
			return null;
		}

		public Long createLong() {
			return null;
		}

		public Double createDouble() {
			return null;
		}

		public Float createFloat() {
			return null;
		}

		public Boolean createBoolean() {
			return null;
		}

		public Date createDate() {
			return null;
		}

		public BigDecimal createDecimal() {
			return null;
		}

		public Byte createByte() {
			return null;
		}

		public Character createChar() {
			return null;
		}

		public Object createArray(final Class<?> type, final int size) {
			return null;
		}

		public <T> List<T> createList(final Class<T> type) {
			return null;
		}

		public <T> Set<T> createSet(final Class<T> type) {
			return null;
		}

		public <K, V> Map<K, V> createMap(final Class<K> keyType, final Class<V> valueType) {
			return null;
		}

		public <E> E createEnum(final Class<E> enumType) {
			return null;
		}
	}

}
