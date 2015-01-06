
package org.exparity.beans.core;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.exparity.beans.Bean;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyException;
import org.exparity.beans.core.BeanPropertyNotFoundException;
import org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes;
import org.exparity.beans.testutils.BeanUtilTestFixture.Person;
import org.exparity.beans.testutils.BeanUtilTestFixture.Thrower;
import org.junit.Test;

public class BeanPropertyTest {

	@Test
	public void canWrapAStringProperty() {
		verifyProperty(new AllTypes(), "stringValue", String.class, null, "sample");
		BeanProperty property = Bean.bean(new AllTypes()).propertyNamed("stringValue");
		assertThat(property.isString(), equalTo(true));
		assertThat(property.isPrimitive(), equalTo(false));
	}

	@Test
	public void canWrapAShortProperty() {
		verifyProperty(new AllTypes(), "shortValue", short.class, (short) 0, Short.MAX_VALUE);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("shortValue").isShort());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("shortValue").isPrimitive());
		verifyProperty(new AllTypes(), "shortObjectValue", Short.class, null, Short.MAX_VALUE);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("shortObjectValue").isShort());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("shortObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAIntegerProperty() {
		verifyProperty(new AllTypes(), "integerValue", int.class, 0, 12345);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("integerValue").isInteger());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("integerValue").isPrimitive());
		verifyProperty(new AllTypes(), "integerObjectValue", Integer.class, null, 12345);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("integerObjectValue").isInteger());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("integerObjectValue").isPrimitive());
	}

	@Test
	public void canWrapALongProperty() {
		verifyProperty(new AllTypes(), "longValue", long.class, 0L, 12345L);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("longValue").isLong());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("longValue").isPrimitive());
		verifyProperty(new AllTypes(), "longObjectValue", Long.class, null, 12345L);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("longObjectValue").isLong());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("longObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADoubleProperty() {
		verifyProperty(new AllTypes(), "doubleValue", double.class, 0.0, 1.1);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("doubleValue").isDouble());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("doubleValue").isPrimitive());
		verifyProperty(new AllTypes(), "doubleObjectValue", Double.class, null, 1.1);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("doubleObjectValue").isDouble());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("doubleObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAFloatProperty() {
		verifyProperty(new AllTypes(), "floatValue", float.class, 0.0f, 1.1f);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("floatValue").isFloat());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("floatValue").isPrimitive());
		verifyProperty(new AllTypes(), "floatObjectValue", Float.class, null, 1.1f);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("floatObjectValue").isFloat());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("floatObjectValue").isPrimitive());
	}

	@Test
	public void canWrapABooleanProperty() {
		verifyProperty(new AllTypes(), "booleanValue", boolean.class, false, true);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("booleanValue").isBoolean());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("booleanValue").isPrimitive());
		verifyProperty(new AllTypes(), "booleanObjectValue", Boolean.class, null, Boolean.TRUE);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("booleanObjectValue").isBoolean());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("booleanObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAByteProperty() {
		verifyProperty(new AllTypes(), "byteValue", byte.class, (byte) 0, (byte) 1);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("byteValue").isByte());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("byteValue").isPrimitive());
		verifyProperty(new AllTypes(), "byteObjectValue", Byte.class, null, Byte.MAX_VALUE);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("byteObjectValue").isByte());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("byteObjectValue").isPrimitive());
	}

	@Test
	public void canWrapACharProperty() {
		verifyProperty(new AllTypes(), "charValue", char.class, (char) 0, 'a');
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("charValue").isCharacter());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("charValue").isPrimitive());
		verifyProperty(new AllTypes(), "charObjectValue", Character.class, null, Character.MAX_VALUE);
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("charObjectValue").isCharacter());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("charObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADateProperty() {
		verifyProperty(new AllTypes(), "dateValue", Date.class, null, new Date());
		assertTrue(Bean.bean(new AllTypes()).propertyNamed("dateValue").isDate());
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("dateValue").isPrimitive());
	}

	@Test
	public void canWrapABigDecimalProperty() {
		verifyProperty(new AllTypes(), "bigDecimalValue", BigDecimal.class, null, new BigDecimal(0.0));
		assertFalse(Bean.bean(new AllTypes()).propertyNamed("bigDecimalValue").isPrimitive());
	}

	@Test
	public void canWrapAArrayProperty() {
		verifyProperty(new AllTypes(), "array", int[].class, null, new int[] {
			0
		});
		BeanProperty instance = Bean.bean(new AllTypes()).propertyNamed("array");
		assertThat(instance.isArray(), equalTo(true));
	}

	@Test
	public void canWrapACollectionProperty() {
		verifyProperty(new AllTypes(), "collection", Collection.class, null, asList("sample"), String.class);
		BeanProperty instance = Bean.bean(new AllTypes()).propertyNamed("collection");
		assertThat(instance.isCollection(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAListProperty() {
		verifyProperty(new AllTypes(), "list", List.class, null, asList("sample"), String.class);
		BeanProperty instance = Bean.bean(new AllTypes()).propertyNamed("list");
		assertThat(instance.isList(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapASetProperty() {
		verifyProperty(new AllTypes(), "set", Set.class, null, new HashSet<String>(asList("sample")), String.class);
		BeanProperty instance = Bean.bean(new AllTypes()).propertyNamed("set");
		assertThat(instance.isSet(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAMapProperty() {
		verifyProperty(new AllTypes(), "map", Map.class, null, singletonMap(1L, "value"), Long.class, String.class);
		BeanProperty instance = Bean.bean(new AllTypes()).propertyNamed("map");
		assertThat(instance.isMap(), equalTo(true));
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleIllegalArgumentExceptionOnSet() throws Exception {
		Bean.bean(new AllTypes()).propertyNamed("stringValue").setValue(Boolean.FALSE);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleInvocationTargetExceptionOnSet() throws Exception {
		Bean.bean(new Thrower()).propertyNamed("property").setValue(1);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleInvocationTargetExceptionOnGet() throws Exception {
		Bean.bean(new Thrower()).propertyNamed("property").getValue();
	}

	@Test
	public void canTestForInstanceEquality() {
		BeanProperty lhs = Bean.bean(new AllTypes()).propertyNamed("stringValue");
		assertThat(lhs.equals(lhs), equalTo(true));
		assertThat(lhs.hashCode(), equalTo(lhs.hashCode()));
	}

	@Test
	public void canTestForInequalityOnInstance() {
		BeanProperty lhs = Bean.bean(new AllTypes()).propertyNamed("stringValue"), rhs = Bean.bean(new AllTypes()).propertyNamed("stringValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test
	public void canTestForInequalityOnMethodName() {
		BeanProperty lhs = Bean.bean(new AllTypes()).propertyNamed("stringValue"), rhs = Bean.bean(new AllTypes()).propertyNamed("integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test
	public void canTestForInequalityOnTypeName() {
		BeanProperty lhs = Bean.bean(new AllTypes()).propertyNamed("stringValue"), rhs = Bean.bean(new Person()).propertyNamed("firstname");
		assertThat(lhs.equals(rhs), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnNull() {
		assertThat(Bean.bean(new AllTypes()).propertyNamed("stringValue").equals(null), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnType() {
		BeanProperty lhs = Bean.bean(new AllTypes()).propertyNamed("stringValue"), rhs = Bean.bean(new AllTypes()).propertyNamed("integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowForIncorrectTypeParameterIndex() {
		Bean.bean(new AllTypes()).propertyNamed("stringValue").getTypeParameter(1);
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canThrowPropertyNotFoundForUnknownProperty() {
		Bean.bean(new AllTypes()).propertyNamed("unknownValue");
	}

	@SuppressWarnings("rawtypes")
	private <T> void verifyProperty(final Object instance, final String propertyName, final Class<T> propertyType, final Object currentValue, final T newValue,
			final Class<?>... genericTypes) {
		BeanProperty property = Bean.bean(new AllTypes()).propertyNamed(propertyName);
		assertThat(property.getDeclaringType(), equalTo((Class) instance.getClass()));
		assertThat(property.getDeclaringTypeCanonicalName(), equalTo(instance.getClass().getCanonicalName()));
		assertThat(property.getDeclaringTypeSimpleName(), equalTo(instance.getClass().getSimpleName()));
		assertThat(property.getName(), equalTo(propertyName));
		assertThat(property.getType(), equalTo((Class) propertyType));
		assertThat(property.getTypeCanonicalName(), equalTo(propertyType.getCanonicalName()));
		assertThat(property.getTypeSimpleName(), equalTo(propertyType.getSimpleName()));
		assertThat(property.isNull(), equalTo(currentValue == null));
		assertThat(property.hasValue(currentValue), equalTo(true));
		assertThat(property.getValue(), equalTo(currentValue));
		assertThat(property.getValue(propertyType), equalTo(currentValue));
		assertThat(property.hasName(propertyName), equalTo(true));
		assertThat(property.isType(propertyType), equalTo(true));
		assertThat(property.isType(), equalTo(false));
		assertThat(property.setValue(newValue), equalTo(true));
		assertThat(property.hasValue(newValue), equalTo(true));
		assertThat(property.isNull(), equalTo(newValue == null));
		assertThat(property.getValue(propertyType), equalTo(newValue));
		assertThat(property.isGeneric(), equalTo(genericTypes.length > 0));
		if (property.isGeneric()) {
			assertThat(property.getTypeParameters().size(), equalTo(genericTypes.length));
			for (int i = 0; i < genericTypes.length; ++i) {
				assertThat(property.getTypeParameter(i), equalTo((Class) genericTypes[i]));
			}
		}
	}
}
