/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.lang.reflect.Method;
import org.junit.Test;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class BeanPropertyTest {

	@Test
	@SuppressWarnings("rawtypes")
	public void supportsSimpleProperties() throws Exception {

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

		Method accessor = Sample.class.getMethod("getProperty"), mutator = Sample.class.getMethod("setProperty", String.class);
		String propertyName = "property";
		BeanProperty property = new BeanProperty(propertyName, accessor, mutator);

		assertThat(property.getName(), equalTo(propertyName));
		assertThat(property.getType(), equalTo((Class) String.class));
		assertThat(property.isType(String.class), equalTo(true));
		assertThat(property.isType(Integer.class), equalTo(false));
		assertThat(property.getTypeCanonicalName(), equalTo("java.lang.String"));
		assertThat(property.getTypeSimpleName(), equalTo("String"));
		assertThat(property.getAccessor(), equalTo(accessor));
		assertThat(property.getMutator(), equalTo(mutator));
		assertThat(property.isArray(), equalTo(false));
		assertThat(property.isIterable(), equalTo(false));
		assertThat(property.isMap(), equalTo(false));

		Sample instance = new Sample();
		String newValue = "sample";

		assertThat(property.getValue(instance), nullValue());
		assertThat(property.setValue(instance, newValue), equalTo(true));
		assertThat(property.getValue(instance), equalTo((Object) newValue));
		assertThat(property.getValue(instance, String.class), equalTo(newValue));
	}

	@Test(expected = BeanPropertyException.class)
	public void throwsIfInvokedOnWrongObject() throws Exception {

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

		Method accessor = Sample.class.getMethod("getProperty"), mutator = Sample.class.getMethod("setProperty", String.class);
		String propertyName = "property";
		BeanProperty property = new BeanProperty(propertyName, accessor, mutator);
		property.getValue(new String(""));
	}

	@Test(expected = BeanPropertyException.class)
	public void throwsIfInvokedOnWrongObjectWithSameMethodName() throws Exception {

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

		Method accessor = Sample.class.getMethod("getProperty"), mutator = Sample.class.getMethod("setProperty", String.class);
		String propertyName = "property";
		BeanProperty property = new BeanProperty(propertyName, accessor, mutator);

		@SuppressWarnings("unused")
		class NotSample {

			private String property;

			public String getProperty() {
				return property;
			}

			public void setProperty(final String property) {
				this.property = property;
			}
		}

		property.getValue(new NotSample());
	}
}
