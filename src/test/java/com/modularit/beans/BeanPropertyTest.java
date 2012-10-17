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
		Sample instance = new Sample();
		BeanProperty property = new BeanProperty(instance, propertyName);

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

		String newValue = "sample";

		assertThat(property.getValue(), nullValue());
		assertThat(property.setValue(newValue), equalTo(true));
		assertThat(property.getValue(), equalTo((Object) newValue));
		assertThat(property.getValue(String.class), equalTo(newValue));
	}
}
