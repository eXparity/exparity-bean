
package org.exparity.beans.core;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.exparity.beans.core.TypeProperty.typeProperty;
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
import org.exparity.beans.core.BeanPropertyException;
import org.exparity.beans.core.BeanPropertyNotFoundException;
import org.exparity.beans.core.TypeProperty;
import org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes;
import org.exparity.beans.testutils.BeanUtilTestFixture.Person;
import org.exparity.beans.testutils.BeanUtilTestFixture.Thrower;
import org.junit.Test;

public class TypePropertyTest {

	@Test
	public void canWrapAStringProperty() {
		verifyProperty(new AllTypes(), "stringValue", String.class, null, "sample");
		TypeProperty property = typeProperty(AllTypes.class, "stringValue");
		assertThat(property.isString(), equalTo(true));
		assertThat(property.isPrimitive(), equalTo(false));
	}

	@Test
	public void canWrapAShortProperty() {
		verifyProperty(new AllTypes(), "shortValue", short.class, (short) 0, Short.MAX_VALUE);
		assertTrue(typeProperty(AllTypes.class, "shortValue").isShort());
		assertTrue(typeProperty(AllTypes.class, "shortValue").isPrimitive());
		verifyProperty(new AllTypes(), "shortObjectValue", Short.class, null, Short.MAX_VALUE);
		assertTrue(typeProperty(AllTypes.class, "shortObjectValue").isShort());
		assertFalse(typeProperty(AllTypes.class, "shortObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAIntegerProperty() {
		verifyProperty(new AllTypes(), "integerValue", int.class, 0, 12345);
		assertTrue(typeProperty(AllTypes.class, "integerValue").isInteger());
		assertTrue(typeProperty(AllTypes.class, "integerValue").isPrimitive());
		verifyProperty(new AllTypes(), "integerObjectValue", Integer.class, null, 12345);
		assertTrue(typeProperty(AllTypes.class, "integerObjectValue").isInteger());
		assertFalse(typeProperty(AllTypes.class, "integerObjectValue").isPrimitive());
	}

	@Test
	public void canWrapALongProperty() {
		verifyProperty(new AllTypes(), "longValue", long.class, 0L, 12345L);
		assertTrue(typeProperty(AllTypes.class, "longValue").isLong());
		assertTrue(typeProperty(AllTypes.class, "longValue").isPrimitive());
		verifyProperty(new AllTypes(), "longObjectValue", Long.class, null, 12345L);
		assertTrue(typeProperty(AllTypes.class, "longObjectValue").isLong());
		assertFalse(typeProperty(AllTypes.class, "longObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADoubleProperty() {
		verifyProperty(new AllTypes(), "doubleValue", double.class, 0.0, 1.1);
		assertTrue(typeProperty(AllTypes.class, "doubleValue").isDouble());
		assertTrue(typeProperty(AllTypes.class, "doubleValue").isPrimitive());
		verifyProperty(new AllTypes(), "doubleObjectValue", Double.class, null, 1.1);
		assertTrue(typeProperty(AllTypes.class, "doubleObjectValue").isDouble());
		assertFalse(typeProperty(AllTypes.class, "doubleObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAFloatProperty() {
		verifyProperty(new AllTypes(), "floatValue", float.class, 0.0f, 1.1f);
		assertTrue(typeProperty(AllTypes.class, "floatValue").isFloat());
		assertTrue(typeProperty(AllTypes.class, "floatValue").isPrimitive());
		verifyProperty(new AllTypes(), "floatObjectValue", Float.class, null, 1.1f);
		assertTrue(typeProperty(AllTypes.class, "floatObjectValue").isFloat());
		assertFalse(typeProperty(AllTypes.class, "floatObjectValue").isPrimitive());
	}

	@Test
	public void canWrapABooleanProperty() {
		verifyProperty(new AllTypes(), "booleanValue", boolean.class, false, true);
		assertTrue(typeProperty(AllTypes.class, "booleanValue").isBoolean());
		assertTrue(typeProperty(AllTypes.class, "booleanValue").isPrimitive());
		verifyProperty(new AllTypes(), "booleanObjectValue", Boolean.class, null, Boolean.TRUE);
		assertTrue(typeProperty(AllTypes.class, "booleanObjectValue").isBoolean());
		assertFalse(typeProperty(AllTypes.class, "booleanObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAByteProperty() {
		verifyProperty(new AllTypes(), "byteValue", byte.class, (byte) 0, (byte) 1);
		assertTrue(typeProperty(AllTypes.class, "byteValue").isByte());
		assertTrue(typeProperty(AllTypes.class, "byteValue").isPrimitive());
		verifyProperty(new AllTypes(), "byteObjectValue", Byte.class, null, Byte.MAX_VALUE);
		assertTrue(typeProperty(AllTypes.class, "byteObjectValue").isByte());
		assertFalse(typeProperty(AllTypes.class, "byteObjectValue").isPrimitive());
	}

	@Test
	public void canWrapACharProperty() {
		verifyProperty(new AllTypes(), "charValue", char.class, (char) 0, 'a');
		assertTrue(typeProperty(AllTypes.class, "charValue").isCharacter());
		assertTrue(typeProperty(AllTypes.class, "charValue").isPrimitive());
		verifyProperty(new AllTypes(), "charObjectValue", Character.class, null, Character.MAX_VALUE);
		assertTrue(typeProperty(AllTypes.class, "charObjectValue").isCharacter());
		assertFalse(typeProperty(AllTypes.class, "charObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADateProperty() {
		verifyProperty(new AllTypes(), "dateValue", Date.class, null, new Date());
		assertTrue(typeProperty(AllTypes.class, "dateValue").isDate());
		assertFalse(typeProperty(AllTypes.class, "dateValue").isPrimitive());
	}

	@Test
	public void canWrapABigDecimalProperty() {
		verifyProperty(new AllTypes(), "bigDecimalValue", BigDecimal.class, null, new BigDecimal(0.0));
		assertFalse(typeProperty(AllTypes.class, "bigDecimalValue").isPrimitive());
	}

	@Test
	public void canWrapAArrayProperty() {
		verifyProperty(new AllTypes(), "array", int[].class, null, new int[] {
			0
		});
		TypeProperty instance = typeProperty(AllTypes.class, "array");
		assertThat(instance.isArray(), equalTo(true));
	}

	@Test
	public void canWrapACollectionProperty() {
		verifyProperty(new AllTypes(), "collection", Collection.class, null, asList("sample"), String.class);
		TypeProperty instance = typeProperty(AllTypes.class, "collection");
		assertThat(instance.isCollection(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAListProperty() {
		verifyProperty(new AllTypes(), "list", List.class, null, asList("sample"), String.class);
		TypeProperty instance = typeProperty(AllTypes.class, "list");
		assertThat(instance.isList(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapASetProperty() {
		verifyProperty(new AllTypes(), "set", Set.class, null, new HashSet<String>(asList("sample")), String.class);
		TypeProperty instance = typeProperty(AllTypes.class, "set");
		assertThat(instance.isSet(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAMapProperty() {
		verifyProperty(new AllTypes(), "map", Map.class, null, singletonMap(1L, "value"), Long.class, String.class);
		TypeProperty instance = typeProperty(AllTypes.class, "map");
		assertThat(instance.isMap(), equalTo(true));
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleIllegalArgumentExceptionOnSet() throws Exception {
		typeProperty(AllTypes.class, "stringValue").setValue(new AllTypes(), Boolean.FALSE);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleIllegalArgumentExceptionOnGet() throws Exception {
		typeProperty(AllTypes.class, "stringValue").getValue(Boolean.FALSE);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleInvocationTargetExceptionOnSet() throws Exception {
		typeProperty(Thrower.class, "property").setValue(new Thrower(), 1);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleInvocationTargetExceptionOnGet() throws Exception {
		typeProperty(Thrower.class, "property").getValue(new Thrower());
	}

	@Test
	public void canTestForInstanceEquality() {
		TypeProperty lhs = typeProperty(AllTypes.class, "stringValue");
		assertThat(lhs.equals(lhs), equalTo(true));
		assertThat(lhs.hashCode(), equalTo(lhs.hashCode()));
	}

	@Test
	public void canTestForEquality() {
		TypeProperty lhs = typeProperty(AllTypes.class, "stringValue"), rhs = typeProperty(AllTypes.class, "stringValue");
		assertThat(lhs.equals(rhs), equalTo(true));
		assertThat(lhs.hashCode(), equalTo(rhs.hashCode()));
	}

	@Test
	public void canTestForInequalityOnMethodName() {
		TypeProperty lhs = typeProperty(AllTypes.class, "stringValue"), rhs = typeProperty(AllTypes.class, "integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test
	public void canTestForInequalityOnTypeName() {
		TypeProperty lhs = typeProperty(AllTypes.class, "stringValue"), rhs = typeProperty(Person.class, "firstname");
		assertThat(lhs.equals(rhs), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnNull() {
		assertThat(typeProperty(AllTypes.class, "stringValue").equals(null), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnType() {
		TypeProperty lhs = typeProperty(AllTypes.class, "stringValue"), rhs = typeProperty(AllTypes.class, "integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowForIncorrectTypeParameterIndex() {
		typeProperty(AllTypes.class, "stringValue").getTypeParameter(1);
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canThrowPropertyNotFoundForUnknownProperty() {
		typeProperty(AllTypes.class, "unknownValue");
	}

	@Test
	public void canDetectInvalidTypeParameters() {
		TypeProperty property = typeProperty(AllTypes.class, "collection");
		assertThat(property.hasAnyTypeParameters(Integer.class, Boolean.class), equalTo(false));
		assertThat(property.hasTypeParameter(Integer.class), equalTo(false));
	}

	@SuppressWarnings("rawtypes")
	private <T> void verifyProperty(final Object instance, final String propertyName, final Class<T> propertyType, final Object currentValue, final T newValue,
			final Class<?>... genericTypes) {
		TypeProperty property = typeProperty(AllTypes.class, propertyName);
		assertThat(property.getDeclaringType(), equalTo((Class) instance.getClass()));
		assertThat(property.getDeclaringTypeCanonicalName(), equalTo(instance.getClass().getCanonicalName()));
		assertThat(property.getDeclaringTypeSimpleName(), equalTo(instance.getClass().getSimpleName()));
		assertThat(property.getName(), equalTo(propertyName));
		assertThat(property.getType(), equalTo((Class) propertyType));
		assertThat(property.getTypeCanonicalName(), equalTo(propertyType.getCanonicalName()));
		assertThat(property.getTypeSimpleName(), equalTo(propertyType.getSimpleName()));
		assertThat(property.getValue(instance), equalTo(currentValue));
		assertThat(property.getValue(instance, propertyType), equalTo(currentValue));
		assertThat(property.hasName(propertyName), equalTo(true));
		assertThat(property.isType(propertyType), equalTo(true));
		assertThat(property.isType(), equalTo(false));
		assertThat(property.setValue(instance, newValue), equalTo(true));
		assertThat(property.getValue(instance, propertyType), equalTo(newValue));
		assertThat(property.isGeneric(), equalTo(genericTypes.length > 0));
		if (property.isGeneric()) {
			assertThat(property.getTypeParameters().size(), equalTo(genericTypes.length));
			for (int i = 0; i < genericTypes.length; ++i) {
				assertThat(property.getTypeParameter(i), equalTo((Class) genericTypes[i]));
				assertThat(property.hasTypeParameter(genericTypes[i]), equalTo(true));
			}
		}
	}
}
