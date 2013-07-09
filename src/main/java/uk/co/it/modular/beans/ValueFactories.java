/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.math.RandomUtils.*;
import static org.apache.commons.lang.time.DateUtils.addSeconds;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public abstract class ValueFactories {

	private static final int MAX_STRING_LENGTH = 50;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int HOURS_PER_DAY = 24;
	private static final int DAYS_PER_YEAR = 365;
	private static final int SECONDS_IN_A_YEAR = MINUTES_PER_HOUR * HOURS_PER_DAY * DAYS_PER_YEAR;

	public static <T> ValueFactory<T> theValue(final T value) {
		return new ValueFactory<T>() {

			public T createValue(final Class<T> type) {
				return value;
			}
		};
	}

	public static ValueFactory<Object> aNullValue() {
		return new ValueFactory<Object>() {

			public Object createValue(final Class<Object> type) {
				return null;
			}
		};
	}

	public static ValueFactory<String> aRandomString() {
		return new ValueFactory<String>() {

			public String createValue(final Class<String> type) {
				return randomAlphanumeric(MAX_STRING_LENGTH);
			}
		};
	}

	public static ValueFactory<Integer> aRandomInteger() {
		return new ValueFactory<Integer>() {

			public Integer createValue(final Class<Integer> type) {
				return Integer.valueOf(nextInt());
			}
		};
	}

	public static ValueFactory<Short> aRandomShort() {
		return new ValueFactory<Short>() {

			public Short createValue(final Class<Short> type) {
				return Short.valueOf((short) nextInt(Short.MAX_VALUE));
			}
		};
	}

	public static ValueFactory<Long> aRandomLong() {
		return new ValueFactory<Long>() {

			public Long createValue(final Class<Long> type) {
				return Long.valueOf(nextLong());
			}
		};
	}

	public static ValueFactory<Double> aRandomDouble() {
		return new ValueFactory<Double>() {

			public Double createValue(final Class<Double> type) {
				return Double.valueOf(nextDouble());
			}
		};
	}

	public static ValueFactory<Float> aRandomFloat() {
		return new ValueFactory<Float>() {

			public Float createValue(final Class<Float> type) {
				return Float.valueOf(nextFloat());
			}
		};
	}

	public static ValueFactory<Boolean> aRandomBoolean() {
		return new ValueFactory<Boolean>() {

			public Boolean createValue(final Class<Boolean> type) {
				return Boolean.valueOf(nextBoolean());
			}
		};
	}

	public static ValueFactory<Date> aRandomDate() {
		return new ValueFactory<Date>() {

			public Date createValue(final Class<Date> type) {
				return addSeconds(new Date(), nextInt(SECONDS_IN_A_YEAR));
			}
		};
	}

	public static ValueFactory<BigDecimal> aRandomDecimal() {
		return new ValueFactory<BigDecimal>() {

			public BigDecimal createValue(final Class<BigDecimal> type) {
				return BigDecimal.valueOf(nextDouble());
			}
		};
	}

	public static ValueFactory<Byte> aRandomByte() {
		return new ValueFactory<Byte>() {

			public Byte createValue(final Class<Byte> type) {
				return (byte) nextInt(Byte.MAX_VALUE);
			}
		};
	}

	public static ValueFactory<Object> aRandomInstance() {
		return new ValueFactory<Object>() {

			public Object createValue(final Class<Object> type) {
				return BeanBuilder.aRandomInstanceOf(type).build();
			}
		};
	}

	public static ValueFactory<Character> aRandomChar() {
		return new ValueFactory<Character>() {

			public Character createValue(final Class<Character> type) {
				return randomAlphabetic(1).charAt(0);
			}
		};
	}

	public static <E> ValueFactory<E> aRandomEnum(final Class<E> enumType) {
		return new ValueFactory<E>() {

			public E createValue(final Class<E> type) {
				E[] enumerationValues = enumType.getEnumConstants();
				if (enumerationValues.length == 0) {
					return null;
				} else {
					return enumerationValues[nextInt(enumerationValues.length)];
				}
			}
		};
	}

	public static <A> ArrayFactory<A> aRandomArrayOf(final ValueFactory<A> typeFactory) {
		return new ArrayFactory<A>() {

			@SuppressWarnings("unchecked")
			public A[] createValue(final Class<A> type, final int size) {
				Object array = Array.newInstance(type, size);
				if (array != null) {
					for (int i = 0; i < size; ++i) {
						Array.set(array, i, typeFactory.createValue(type));
					}
				}
				return (A[]) array;
			}
		};
	}

	public static <T> ValueFactory<T> aNewInstanceOf(final Class<T> type) {
		return new ValueFactory<T>() {

			public T createValue(final Class<T> type) {
				try {
					return type.newInstance();
				} catch (Exception e) {
					throw new BeanBuilderException("Failed to instantiate instance of '" + type.getCanonicalName() + "'", e);
				}
			}
		};
	}

	public static <T> ValueFactory<T> oneOf(final ValueFactory<T>... factories) {
		return oneOf(Arrays.asList(factories));
	}

	public static <T> ValueFactory<T> oneOf(final Collection<ValueFactory<T>> factories) {
		return new ValueFactory<T>() {

			private final List<ValueFactory<T>> candidates = new ArrayList<ValueFactory<T>>(factories);

			public T createValue(final Class<T> type) {
				return candidates.get(nextInt(candidates.size())).createValue(type);
			}
		};
	}
}
