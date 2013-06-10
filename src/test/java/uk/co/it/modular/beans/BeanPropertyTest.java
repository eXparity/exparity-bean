
package uk.co.it.modular.beans;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static uk.co.it.modular.beans.BeanProperty.beanProperty;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Thrower;

public class BeanPropertyTest {

	@Test
	public void canWrapAStringProperty() {
		verifyProperty(new AllTypes(), "stringValue", String.class, null, "sample");
		BeanProperty property = beanProperty(AllTypes.class, "stringValue");
		assertThat(property.isString(), equalTo(true));
		assertThat(property.isPrimitive(), equalTo(false));
	}

	@Test
	public void canWrapAShortProperty() {
		verifyProperty(new AllTypes(), "shortValue", short.class, (short) 0, Short.MAX_VALUE);
		assertTrue(beanProperty(AllTypes.class, "shortValue").isShort());
		assertTrue(beanProperty(AllTypes.class, "shortValue").isPrimitive());
		verifyProperty(new AllTypes(), "shortObjectValue", Short.class, null, Short.MAX_VALUE);
		assertTrue(beanProperty(AllTypes.class, "shortObjectValue").isShort());
		assertFalse(beanProperty(AllTypes.class, "shortObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAIntegerProperty() {
		verifyProperty(new AllTypes(), "integerValue", int.class, 0, 12345);
		assertTrue(beanProperty(AllTypes.class, "integerValue").isInteger());
		assertTrue(beanProperty(AllTypes.class, "integerValue").isPrimitive());
		verifyProperty(new AllTypes(), "integerObjectValue", Integer.class, null, 12345);
		assertTrue(beanProperty(AllTypes.class, "integerObjectValue").isInteger());
		assertFalse(beanProperty(AllTypes.class, "integerObjectValue").isPrimitive());
	}

	@Test
	public void canWrapALongProperty() {
		verifyProperty(new AllTypes(), "longValue", long.class, 0L, 12345L);
		assertTrue(beanProperty(AllTypes.class, "longValue").isLong());
		assertTrue(beanProperty(AllTypes.class, "longValue").isPrimitive());
		verifyProperty(new AllTypes(), "longObjectValue", Long.class, null, 12345L);
		assertTrue(beanProperty(AllTypes.class, "longObjectValue").isLong());
		assertFalse(beanProperty(AllTypes.class, "longObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADoubleProperty() {
		verifyProperty(new AllTypes(), "doubleValue", double.class, 0.0, 1.1);
		assertTrue(beanProperty(AllTypes.class, "doubleValue").isDouble());
		assertTrue(beanProperty(AllTypes.class, "doubleValue").isPrimitive());
		verifyProperty(new AllTypes(), "doubleObjectValue", Double.class, null, 1.1);
		assertTrue(beanProperty(AllTypes.class, "doubleObjectValue").isDouble());
		assertFalse(beanProperty(AllTypes.class, "doubleObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAFloatProperty() {
		verifyProperty(new AllTypes(), "floatValue", float.class, 0.0f, 1.1f);
		assertTrue(beanProperty(AllTypes.class, "floatValue").isFloat());
		assertTrue(beanProperty(AllTypes.class, "floatValue").isPrimitive());
		verifyProperty(new AllTypes(), "floatObjectValue", Float.class, null, 1.1f);
		assertTrue(beanProperty(AllTypes.class, "floatObjectValue").isFloat());
		assertFalse(beanProperty(AllTypes.class, "floatObjectValue").isPrimitive());
	}

	@Test
	public void canWrapABooleanProperty() {
		verifyProperty(new AllTypes(), "booleanValue", boolean.class, false, true);
		assertTrue(beanProperty(AllTypes.class, "booleanValue").isBoolean());
		assertTrue(beanProperty(AllTypes.class, "booleanValue").isPrimitive());
		verifyProperty(new AllTypes(), "booleanObjectValue", Boolean.class, null, Boolean.TRUE);
		assertTrue(beanProperty(AllTypes.class, "booleanObjectValue").isBoolean());
		assertFalse(beanProperty(AllTypes.class, "booleanObjectValue").isPrimitive());
	}

	@Test
	public void canWrapAByteProperty() {
		verifyProperty(new AllTypes(), "byteValue", byte.class, (byte) 0, (byte) 1);
		assertTrue(beanProperty(AllTypes.class, "byteValue").isByte());
		assertTrue(beanProperty(AllTypes.class, "byteValue").isPrimitive());
		verifyProperty(new AllTypes(), "byteObjectValue", Byte.class, null, Byte.MAX_VALUE);
		assertTrue(beanProperty(AllTypes.class, "byteObjectValue").isByte());
		assertFalse(beanProperty(AllTypes.class, "byteObjectValue").isPrimitive());
	}

	@Test
	public void canWrapACharProperty() {
		verifyProperty(new AllTypes(), "charValue", char.class, (char) 0, 'a');
		assertTrue(beanProperty(AllTypes.class, "charValue").isCharacter());
		assertTrue(beanProperty(AllTypes.class, "charValue").isPrimitive());
		verifyProperty(new AllTypes(), "charObjectValue", Character.class, null, Character.MAX_VALUE);
		assertTrue(beanProperty(AllTypes.class, "charObjectValue").isCharacter());
		assertFalse(beanProperty(AllTypes.class, "charObjectValue").isPrimitive());
	}

	@Test
	public void canWrapADateProperty() {
		verifyProperty(new AllTypes(), "dateValue", Date.class, null, new Date());
		assertTrue(beanProperty(AllTypes.class, "dateValue").isDate());
		assertFalse(beanProperty(AllTypes.class, "dateValue").isPrimitive());
	}

	@Test
	public void canWrapABigDecimalProperty() {
		verifyProperty(new AllTypes(), "bigDecimalValue", BigDecimal.class, null, new BigDecimal(0.0));
		assertFalse(beanProperty(AllTypes.class, "bigDecimalValue").isPrimitive());
	}

	@Test
	public void canWrapAArrayProperty() {
		verifyProperty(new AllTypes(), "array", int[].class, null, new int[] {
			0
		});
		BeanProperty instance = beanProperty(AllTypes.class, "array");
		assertThat(instance.isArray(), equalTo(true));
	}

	@Test
	public void canWrapACollectionProperty() {
		verifyProperty(new AllTypes(), "collection", Collection.class, null, asList("sample"), String.class);
		BeanProperty instance = beanProperty(AllTypes.class, "collection");
		assertThat(instance.isCollection(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAListProperty() {
		verifyProperty(new AllTypes(), "list", List.class, null, asList("sample"), String.class);
		BeanProperty instance = beanProperty(AllTypes.class, "list");
		assertThat(instance.isList(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapASetProperty() {
		verifyProperty(new AllTypes(), "set", Set.class, null, new HashSet<String>(asList("sample")), String.class);
		BeanProperty instance = beanProperty(AllTypes.class, "set");
		assertThat(instance.isSet(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canWrapAMapProperty() {
		verifyProperty(new AllTypes(), "map", Map.class, null, singletonMap(1L, "value"), Long.class, String.class);
		BeanProperty instance = beanProperty(AllTypes.class, "map");
		assertThat(instance.isMap(), equalTo(true));
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleIllegalArgumentExceptionOnSet() throws Exception {
		beanProperty(AllTypes.class, "stringValue").setValue(new AllTypes(), Boolean.FALSE);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleIllegalArgumentExceptionOnGet() throws Exception {
		beanProperty(AllTypes.class, "stringValue").getValue(Boolean.FALSE);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleInvocationTargetExceptionOnSet() throws Exception {
		beanProperty(Thrower.class, "property").setValue(new Thrower(), 1);
	}

	@Test(expected = BeanPropertyException.class)
	public void canHandleInvocationTargetExceptionOnGet() throws Exception {
		beanProperty(Thrower.class, "property").getValue(new Thrower());
	}

	@Test
	public void canTestForInstanceEquality() {
		BeanProperty lhs = beanProperty(AllTypes.class, "stringValue");
		assertThat(lhs.equals(lhs), equalTo(true));
		assertThat(lhs.hashCode(), equalTo(lhs.hashCode()));
	}

	@Test
	public void canTestForEquality() {
		BeanProperty lhs = beanProperty(AllTypes.class, "stringValue"), rhs = beanProperty(AllTypes.class, "stringValue");
		assertThat(lhs.equals(rhs), equalTo(true));
		assertThat(lhs.hashCode(), equalTo(rhs.hashCode()));
	}

	@Test
	public void canTestForInequalityOnMethodName() {
		BeanProperty lhs = beanProperty(AllTypes.class, "stringValue"), rhs = beanProperty(AllTypes.class, "integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test
	public void canTestForInequalityOnTypeName() {
		BeanProperty lhs = beanProperty(AllTypes.class, "stringValue"), rhs = beanProperty(Person.class, "firstname");
		assertThat(lhs.equals(rhs), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnNull() {
		assertThat(beanProperty(AllTypes.class, "stringValue").equals(null), equalTo(false));
	}

	@Test
	public void canTestForInequalityOnType() {
		BeanProperty lhs = beanProperty(AllTypes.class, "stringValue"), rhs = beanProperty(AllTypes.class, "integerValue");
		assertThat(lhs.equals(rhs), equalTo(false));
		assertThat(lhs.hashCode(), not(equalTo(rhs.hashCode())));
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowForIncorrectTypeParameterIndex() {
		beanProperty(AllTypes.class, "stringValue").getTypeParameter(1);
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canThrowPropertyNotFoundForUnknownProperty() {
		beanProperty(AllTypes.class, "unknownValue");
	}

	@Test
	public void canDetectInvalidTypeParameters() {
		BeanProperty property = beanProperty(AllTypes.class, "collection");
		assertThat(property.hasAnyTypeParameters(Integer.class, Boolean.class), equalTo(false));
		assertThat(property.hasTypeParameter(Integer.class), equalTo(false));
	}

	@SuppressWarnings("rawtypes")
	private <T> void verifyProperty(final Object instance, final String propertyName, final Class<T> propertyType, final Object currentValue, final T newValue,
			final Class<?>... genericTypes) {
		BeanProperty property = beanProperty(AllTypes.class, propertyName);
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
