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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import static uk.co.it.modular.beans.BeanBuilder.aRandomInstanceOf;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.math.RandomUtils.*;
import static org.apache.commons.lang.time.DateUtils.addSeconds;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
@SuppressWarnings({
		"rawtypes", "unchecked"
})
public abstract class ValueFactories {

	private static final int MAX_STRING_LENGTH = 50;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int HOURS_PER_DAY = 24;
	private static final int DAYS_PER_YEAR = 365;
	private static final int SECONDS_IN_A_YEAR = MINUTES_PER_HOUR * HOURS_PER_DAY * DAYS_PER_YEAR;

	public static ValueFactory theValue(final Object value) {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) value;
			}
		};
	}

	public static ValueFactory aNullValue() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return null;
			}
		};
	}

	public static ValueFactory aRandomString() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) randomAlphanumeric(MAX_STRING_LENGTH);
			}
		};
	}

	public static ValueFactory aRandomInteger() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) Integer.valueOf(nextInt());
			}
		};
	}

	public static ValueFactory aRandomShort() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) Short.valueOf((short) nextInt(Short.MAX_VALUE));
			}
		};
	}

	public static ValueFactory aRandomLong() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) Long.valueOf(nextLong());
			}
		};
	}

	public static ValueFactory aRandomDouble() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) Double.valueOf(nextDouble());
			}
		};
	}

	public static ValueFactory aRandomFloat() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) Float.valueOf(nextFloat());
			}
		};
	}

	public static ValueFactory aRandomBoolean() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) Boolean.valueOf(nextBoolean());
			}
		};
	}

	public static ValueFactory aRandomDate() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) addSeconds(new Date(), nextInt(SECONDS_IN_A_YEAR));
			}
		};
	}

	public static ValueFactory aRandomDecimal() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) BigDecimal.valueOf((Double) aRandomDouble().createValue(type));
			}
		};
	}

	public static ValueFactory aRandomByte() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				Byte value = (byte) nextInt(Byte.MAX_VALUE);
				return (T) value;
			}
		};
	}

	public static ValueFactory aRandomInstance() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return aRandomInstanceOf(type).build();
			}
		};
	}

	public static ValueFactory aRandomChar() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				Character value = randomAlphabetic(1).charAt(0);
				return (T) value;
			}
		};
	}

	public static ValueFactory aRandomEnum() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {

				if (!type.isEnum()) {
					throw new IllegalArgumentException("Cannot instantiate non-enum type '" + type.getCanonicalName() + "'");
				}

				Object[] enumerationValues = type.getEnumConstants();
				if (enumerationValues.length == 0) {
					return null;
				} else {
					return (T) enumerationValues[nextInt(enumerationValues.length)];
				}
			}
		};
	}

	public static <A> ValueFactory aRandomArrayOf(final Class<A> type, final ValueFactory typeFactory) {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				int size = 1;
				Object array = Array.newInstance(type, size);
				if (array != null) {
					for (int i = 0; i < size; ++i) {
						Array.set(array, i, typeFactory.createValue(type));
					}
				}
				return (T) array;
			}
		};
	}

	public static ValueFactory aMap() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) new HashMap();
			}
		};
	}

	public static ValueFactory aSet() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) new HashSet<T>();
			}
		};
	}

	public static ValueFactory aList() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				return (T) new ArrayList<T>();
			}
		};
	}

	public static ArrayFactory anArray() {
		return new ArrayFactory() {

			public <T> T createValue(final Class<T> type, final int size) {
				return (T) Array.newInstance(type, size);
			}
		};
	}

	public static ValueFactory aNewInstance() {
		return new ValueFactory() {

			public <T> T createValue(final Class<T> type) {
				try {
					return type.newInstance();
				} catch (Exception e) {
					throw new BeanBuilderException("Failed to instantiate instance of '" + type.getCanonicalName() + "'", e);
				}
			}
		};
	}

	public static <T> ValueFactory aNewInstanceOf(final Class<T> type) {
		return new ValueFactory() {

			public <I> I createValue(final Class<I> inner) {
				try {
					if (!inner.isAssignableFrom(type)) {
						throw new IllegalArgumentException("Cannot create instance of '" + type.getCanonicalName() + "' from '" + inner.getCanonicalName() + "'");
					}
					return (I) type.newInstance();
				} catch (Exception e) {
					throw new BeanBuilderException("Failed to instantiate instance of '" + type.getCanonicalName() + "'", e);
				}
			}
		};
	}

	public static ValueFactory oneOf(final ValueFactory... factories) {
		return oneOf(Arrays.asList(factories));
	}

	public static ValueFactory oneOf(final Collection<ValueFactory> factories) {
		return new ValueFactory() {

			private final List<ValueFactory> candidates = new ArrayList<ValueFactory>(factories);

			public <T> T createValue(final Class<T> type) {
				return candidates.get(nextInt(candidates.size())).createValue(type);
			}
		};
	}
}
