
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
import org.exparity.beans.testutils.types.AllTypes;
import org.exparity.beans.testutils.types.Person;
import org.exparity.beans.testutils.types.Thrower;
import org.junit.Test;
import static org.exparity.beans.Bean.beanProperty;

public class BeanPropertyTest {

	@Test
	public void canTestForName() {
		BeanProperty property = beanProperty(new AllTypes(), "stringValue");
		assertThat(property.hasName("stringValue"), equalTo(true));
	}

	@Test
	public void canTestForNameUsingAnyCase() {
		BeanProperty property = beanProperty(new AllTypes(), "stringValue");
		assertThat(property.hasName("STRingValUE"), equalTo(true));
	}

	@Test
	public void canTestForWrongName() {
		BeanProperty property = beanProperty(new AllTypes(), "stringValue");
		assertThat(property.hasName("wrongName"), equalTo(false));
	}

	@Test
	public void canWrapAStringProperty() {
		verifyProperty(new AllTypes(), "stringValue", String.class, null, "sample");
		BeanProperty property = beanProperty(new AllTypes(), "stringValue");
		assertThat(property.isString(), equalTo(true));
		assertThat(property.isPrimitive(), equalTo(false));
	}

	@Test
	public void canWrapAShortProperty() {
		verifyProperty(new AllTypes(), "shortValue", short.class, (short) 0, Short.MAX_VALUE);
		assertTrue(beanProperty(new AllTypes(), "shortValue").isShort());
		assertTrue(beanProperty(new AllTypes(), "shortValue").isPrimitive());
		verifyProperty(new AllTypes(), "shortObjectValue", Short.class, null, Short.MAX_VALUE);
		assertTrue(beanProperty(new AllTypes(), "shortObjectValue").isShort());
		assertFalse(beanProperty(new AllTypes(), "shortObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAIntegerProperty() {
		verifyProperty(new AllTypes(), "integerValue", int.class, 0, 12345);
		assertTrue(beanProperty(new AllTypes(), "integerValue").isInteger());
		assertTrue(beanProperty(new AllTypes(), "integerValue").isPrimitive());
		verifyProperty(new AllTypes(), "integerObjectValue", Integer.class, null, 12345);
		assertTrue(beanProperty(new AllTypes(), "integerObjectValue").isInteger());
		assertFalse(beanProperty(new AllTypes(), "integerObjectValue").isPrimitive());
	}

	@Test
	public void canWrapALongProperty() {
		verifyProperty(new AllTypes(), "longValue", long.class, 0L, 12345L);
		assertTrue(beanProperty(new AllTypes(), "longValue").isLong());
		assertTrue(beanProperty(new AllTypes(), "longValue").isPrimitive());
		verifyProperty(new AllTypes(), "longObjectValue", Long.class, null, 12345L);
		assertTrue(beanProperty(new AllTypes(), "longObjectValue").isLong());
		assertFalse(beanProperty(new AllTypes(), "longObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADoubleProperty() {
		verifyProperty(new AllTypes(), "doubleValue", double.class, 0.0, 1.1);
		assertTrue(beanProperty(new AllTypes(), "doubleValue").isDouble());
		assertTrue(beanProperty(new AllTypes(), "doubleValue").isPrimitive());
		verifyProperty(new AllTypes(), "doubleObjectValue", Double.class, null, 1.1);
		assertTrue(beanProperty(new AllTypes(), "doubleObjectValue").isDouble());
		assertFalse(beanProperty(new AllTypes(), "doubleObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAFloatProperty() {
		verifyProperty(new AllTypes(), "floatValue", float.class, 0.0f, 1.1f);
		assertTrue(beanProperty(new AllTypes(), "floatValue").isFloat());
		assertTrue(beanProperty(new AllTypes(), "floatValue").isPrimitive());
		verifyProperty(new AllTypes(), "floatObjectValue", Float.class, null, 1.1f);
		assertTrue(beanProperty(new AllTypes(), "floatObjectValue").isFloat());
		assertFalse(beanProperty(new AllTypes(), "floatObjectValue").isPrimitive());
	}

	@Test
	public void canWrapABooleanProperty() {
		verifyProperty(new AllTypes(), "booleanValue", boolean.class, false, true);
		assertTrue(beanProperty(new AllTypes(), "booleanValue").isBoolean());
		assertTrue(beanProperty(new AllTypes(), "booleanValue").isPrimitive());
		verifyProperty(new AllTypes(), "booleanObjectValue", Boolean.class, null, Boolean.TRUE);
		assertTrue(beanProperty(new AllTypes(), "booleanObjectValue").isBoolean());
		assertFalse(beanProperty(new AllTypes(), "booleanObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAByteProperty() {
		verifyProperty(new AllTypes(), "byteValue", byte.class, (byte) 0, (byte) 1);
		assertTrue(beanProperty(new AllTypes(), "byteValue").isByte());
		assertTrue(beanProperty(new AllTypes(), "byteValue").isPrimitive());
		verifyProperty(new AllTypes(), "byteObjectValue", Byte.class, null, Byte.MAX_VALUE);
		assertTrue(beanProperty(new AllTypes(), "byteObjectValue").isByte());
		assertFalse(beanProperty(new AllTypes(), "byteObjectValue").isPrimitive());
	}

	@Test
	public void canWrapACharProperty() {
		verifyProperty(new AllTypes(), "charValue", char.class, (char) 0, 'a');
		assertTrue(beanProperty(new AllTypes(), "charValue").isCharacter());
		assertTrue(beanProperty(new AllTypes(), "charValue").isPrimitive());
		verifyProperty(new AllTypes(), "charObjectValue", Character.class, null, Character.MAX_VALUE);
		assertTrue(beanProperty(new AllTypes(), "charObjectValue").isCharacter());
		assertFalse(beanProperty(new AllTypes(), "charObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADateProperty() {
		verifyProperty(new AllTypes(), "dateValue", Date.class, null, new Date());
		assertTrue(beanProperty(new AllTypes(), "dateValue").isDate());
		assertFalse(beanProperty(new AllTypes(), "dateValue").isPrimitive());
	}

	@Test
	public void canWrapABigDecimalProperty() {
		verifyProperty(new AllTypes(), "bigDecimalValue", BigDecimal.class, null, new BigDecimal(0.0));
		assertFalse(beanProperty(new AllTypes(), "bigDecimalValue").isPrimitive());
	}

	@Test
	public void canWrapAArrayProperty() {
		verifyProperty(new AllTypes(), "array", int[].class, null, new int[] {
				0
		});
		BeanProperty instance = beanProperty(new AllTypes(), "array");
		assertThat(instance.isArray(), equalTo(true));
	}

	@Test
	public void canWrapACollectionProperty() {
		verifyProperty(new AllTypes(), "collection", Collection.class, null, asList("sample"), String.class);
		BeanProperty instance = beanProperty(new AllTypes(), "collection");
		assertThat(instance.isCollection(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAListProperty() {
		verifyProperty(new AllTypes(), "list", List.class, null, asList("sample"), String.class);
		BeanProperty instance = beanProperty(new AllTypes(), "list");
		assertThat(instance.isList(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapASetProperty() {
		verifyProperty(new AllTypes(), "set", Set.class, null, new HashSet<String>(asList("sample")), String.class);
		BeanProperty instance = beanProperty(new AllTypes(), "set");
		assertThat(instance.isSet(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAMapProperty() {
		verifyProperty(new AllTypes(), "map", Map.class, null, singletonMap(1L, "value"), Long.class, String.class);
		BeanProperty instance = beanProperty(new AllTypes(), "map");
		assertThat(instance.isMap(), equalTo(true));
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleIllegalArgumentExceptionOnSet() throws Exception {
		beanProperty(new AllTypes(), "stringValue").setValue(Boolean.FALSE);
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
		BeanProperty lhs = beanProperty(new AllTypes(), "stringValue");
		assertThat(lhs.equals(lhs), equalTo(true));
		assertThat(lhs.hashCode(), equalTo(lhs.hashCode()));
	}

	@Test
	public void canTestForInequalityOnInstance() {
		BeanProperty lhs = beanProperty(new AllTypes(), "stringValue"), rhs = beanProperty(new AllTypes(), "stringValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test
	public void canTestForInequalityOnMethodName() {
		BeanProperty lhs = beanProperty(new AllTypes(), "stringValue"), rhs = beanProperty(new AllTypes(), "integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test
	public void canTestForInequalityOnTypeName() {
		BeanProperty lhs = beanProperty(new AllTypes(), "stringValue"), rhs = Bean.bean(new Person()).propertyNamed("firstname");
		assertThat(lhs.equals(rhs), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnNull() {
		assertThat(beanProperty(new AllTypes(), "stringValue").equals(null), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnType() {
		BeanProperty lhs = beanProperty(new AllTypes(), "stringValue"), rhs = beanProperty(new AllTypes(), "integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowForIncorrectTypeParameterIndex() {
		beanProperty(new AllTypes(), "stringValue").getTypeParameter(1);
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canThrowPropertyNotFoundForUnknownProperty() {
		beanProperty(new AllTypes(), "unknownValue");
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
