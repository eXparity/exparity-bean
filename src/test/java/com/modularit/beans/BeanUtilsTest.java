/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import static com.modularit.beans.BeanPropertyMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * Unit Tests for Bean Utils
 * 
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class BeanUtilsTest {

	@Test
	public void canReadStringProperty() {

		@SuppressWarnings("unused")
		class Sample {

			private String property;

			public String getProperty() {
				return property;
			}

			public void setProperty(final String property) {
				this.property = property;
			}
		}

		doTests(new Sample(), "property", String.class, null, "sample");
	}

	@Test
	public void canReadDoubleProperty() {

		@SuppressWarnings("unused")
		class Sample {

			private double property;

			public double getProperty() {
				return property;
			}

			public void setProperty(final double property) {
				this.property = property;
			}
		}

		doTests(new Sample(), "property", double.class, 0.0, 1.1);
	}

	@Test
	public void canReadLongProperty() {

		@SuppressWarnings("unused")
		class Sample {

			private long property;

			public long getProperty() {
				return property;
			}

			public void setProperty(final long property) {
				this.property = property;
			}
		}

		doTests(new Sample(), "property", long.class, 0L, 12345L);
	}

	@Test
	public void canReadBooleanProperty() {

		@SuppressWarnings("unused")
		class Sample {

			private boolean property;

			public boolean isProperty() {
				return property;
			}

			public void setProperty(final boolean property) {
				this.property = property;
			}
		}

		doTests(new Sample(), "property", boolean.class, false, true);
	}

	@Test
	public void canReadArrayProperty() {

		@SuppressWarnings("unused")
		class Sample {

			private int[] property;

			public int[] getProperty() {
				return property;
			}

			public void setProperty(final int[] property) {
				this.property = property;
			}
		}

		doTests(new Sample(), "property", int[].class, null, new int[] {
			0
		});
	}

	@Test
	public void canReadCollectionProperty() {

		@SuppressWarnings("unused")
		class Sample {

			private Collection<String> property;

			public Collection<String> getProperty() {
				return property;
			}

			public void setProperty(final Collection<String> property) {
				this.property = property;
			}
		}

		doTests(new Sample(), "property", Collection.class, null, Arrays.asList("sample"));
		// assertThat(BeanUtils.isGenericType(new Sample(), "property"), equalTo(true));
		// assertThat(BeanUtils.isPropertyGenericType(new Sample(), "property", List.class), equalTo(true));
	}

	@SuppressWarnings("rawtypes")
	private <T> void doTests(final Object sample, final String name, final Class<T> type, final T currentValue, final T newValue) {

		List<BeanProperty> properties = BeanUtils.getProperties(sample);
		assertThat(properties, hasSize(1));
		assertThat(properties, hasItem(aBeanProperty(name, type)));

		Map<String, BeanProperty> propertyMap = BeanUtils.getPropertyMap(sample);
		assertThat(propertyMap, hasEntry(equalTo("property"), aBeanProperty(name, type)));

		assertThat(BeanUtils.hasProperty(sample, name), equalTo(true));
		assertThat(BeanUtils.getProperty(sample, name), aBeanProperty(name, type));
		assertThat(BeanUtils.isPropertyType(sample, name, type), equalTo(true));
		assertThat(BeanUtils.getPropertyType(sample, name), equalTo((Class) type));
		assertThat(BeanUtils.getPropertyValue(sample, name, type), equalTo(currentValue));
		assertThat(BeanUtils.setPropertyValue(sample, name, newValue), equalTo(true));
		assertThat(BeanUtils.getPropertyValue(sample, name, type), equalTo(newValue));
	}
}
