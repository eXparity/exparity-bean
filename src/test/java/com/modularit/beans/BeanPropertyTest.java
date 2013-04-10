/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import com.modularit.beans.testutils.BeanUtilTestFixture.AllTypes;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class BeanPropertyTest {

	@Test
	public void canAccessStringProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "stringValue");
		doPropertyTest(property, String.class, "sample");
		assertTrue(property.isString());
	}

	@Test
	public void canAccessDateProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "dateValue");
		doPropertyTest(property, Date.class, new Date());
		assertTrue(property.isDate());
	}

	@Test
	public void canAccessLongProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "longObjectValue");
		doPropertyTest(property, Long.class, 1L);
		assertTrue(property.isLong());
	}

	@Test
	public void canAccessBooleanProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "booleanObjectValue");
		doPropertyTest(property, Boolean.class, false);
		assertTrue(property.isBoolean());
	}

	@Test
	public void canAccessIntegerProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "integerObjectValue");
		doPropertyTest(property, Integer.class, 1);
		assertTrue(property.isInteger());
	}

	@Test
	public void canAccessShortProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "shortObjectValue");
		doPropertyTest(property, Short.class, Short.MIN_VALUE);
		assertTrue(property.isShort());
	}

	@Test
	public void canAccessDoubleProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "doubleObjectValue");
		doPropertyTest(property, Double.class, 1.0);
		assertTrue(property.isDouble());
	}

	@Test
	public void canAccessFloatProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "floatObjectValue");
		doPropertyTest(property, Float.class, Float.MIN_VALUE);
		assertTrue(property.isFloat());
	}

	@Test
	public void canAccessCharacterProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "charObjectValue");
		doPropertyTest(property, Character.class, 'a');
		assertTrue(property.isCharacter());
	}

	@Test
	public void canAccessByteProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "byteObjectValue");
		doPropertyTest(property, Byte.class, Byte.MIN_VALUE);
		assertTrue(property.isByte());
	}

	@Test
	public void canAccessArrayProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "array");
		doPropertyTest(property, int[].class, new int[] {
			0
		});
		assertTrue(property.isArray());
	}

	@Test
	public void canAccessListProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "list");
		doPropertyTest(property, List.class, singletonList(""));
		assertTrue(property.isCollection());
		assertTrue(property.isIterable());
		assertTrue(property.isList());
		assertTrue(property.isGeneric());
		assertThat(property.getTypeParameters(), hasItem(String.class));
	}

	@Test
	public void canAccessSetProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "set");
		doPropertyTest(property, Set.class, Collections.singleton(""));
		assertTrue(property.isCollection());
		assertTrue(property.isIterable());
		assertTrue(property.isSet());
		assertTrue(property.isGeneric());
		assertThat(property.getTypeParameters(), hasItem(String.class));
	}

	@Test
	public void canAccessMapProperty() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "map");
		doPropertyTest(property, Map.class, Collections.singletonMap(1L, ""));
		assertTrue(property.isMap());
		assertTrue(property.isGeneric());
		assertThat(property.getTypeParameters(), hasItem(Long.class));
		assertThat(property.getTypeParameter(0), equalTo((Object) Long.class));
		assertThat(property.getTypeParameters(), hasItem(String.class));
		assertThat(property.getTypeParameter(1), equalTo((Object) String.class));
	}

	@SuppressWarnings("rawtypes")
	private <T> void doPropertyTest(final BeanProperty property, final Class<T> type, final T newValue) {
		assertThat(property.getType(), equalTo((Class) type));
		assertThat(property.isType(type), equalTo(true));
		assertThat(property.getTypeCanonicalName(), equalTo(type.getCanonicalName()));
		assertThat(property.getTypeSimpleName(), equalTo(type.getSimpleName()));
		assertThat(property.getValue(), nullValue());
		assertThat(property.setValue(newValue), equalTo(true));
		assertThat(property.getValue(), equalTo((Object) newValue));
		assertThat(property.getValue(type), equalTo(newValue));
	}

	@Test
	public void areEqualIfSamePropertyOnSameInstance() throws Exception {
		String propertyName = "stringValue";
		AllTypes instance = new AllTypes();
		BeanProperty property = BeanUtils.property(instance, propertyName);
		BeanProperty other = BeanUtils.property(instance, propertyName);
		assertThat(property, equalTo(other));
		assertThat(property.hashCode(), equalTo(other.hashCode()));
	}

	@Test
	public void areUnequalIfSamePropertyOnDifferentInstance() throws Exception {
		BeanProperty property = BeanUtils.property(new AllTypes(), "stringValue");
		BeanProperty other = BeanUtils.property(new AllTypes(), "stringValue");
		assertThat(property, not(equalTo(other)));
		assertThat(property.hashCode(), not(equalTo(other.hashCode())));
	}

	@Test
	public void areUnequalIfDifferentPropertyOnSameInstance() throws Exception {
		AllTypes instance = new AllTypes();
		BeanProperty property = BeanUtils.property(instance, "stringValue");
		BeanProperty other = BeanUtils.property(instance, "longValue");
		assertThat(property, not(equalTo(other)));
		assertThat(property.hashCode(), not(equalTo(other.hashCode())));
	}

	@Test(expected = BeanPropertyException.class)
	public void throwsExceptionIfSetWithWrongType() throws Exception {
		BeanUtils.property(new AllTypes(), "stringValue").setValue(1L);
	}

}
