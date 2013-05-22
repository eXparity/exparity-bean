/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.co.it.modular.beans.BeanPredicates.withName;
import static uk.co.it.modular.beans.testutils.BeanPropertyMatchers.aBeanProperty;
import static uk.co.it.modular.beans.testutils.BeanPropertyMatchers.aBeanPropertyInstance;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Car;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Engine;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Wheel;

/**
 * Unit Tests for Bean Utils
 * 
 * @author Stewart.Bissett
 */
public class BeanUtilsTest {

	@Test
	public void canReadStringProperty() {
		doGetPropertyTests(new AllTypes(), "stringValue", String.class, null, "sample");
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "stringValue").isString());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "stringValue").isPrimitive());
	}

	@Test
	public void canReadShortProperty() {
		doGetPropertyTests(new AllTypes(), "shortValue", short.class, (short) 0, Short.MAX_VALUE);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "shortValue").isShort());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "shortValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "shortObjectValue", Short.class, null, Short.MAX_VALUE);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "shortObjectValue").isShort());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "shortObjectValue").isPrimitive());
	}

	@Test
	public void canReadIntegerProperty() {
		doGetPropertyTests(new AllTypes(), "integerValue", int.class, 0, 12345);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "integerValue").isInteger());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "integerValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "integerObjectValue", Integer.class, null, 12345);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "integerObjectValue").isInteger());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "integerObjectValue").isPrimitive());
	}

	@Test
	public void canReadLongProperty() {
		doGetPropertyTests(new AllTypes(), "longValue", long.class, 0L, 12345L);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "longValue").isLong());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "longValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "longObjectValue", Long.class, null, 12345L);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "longObjectValue").isLong());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "longObjectValue").isPrimitive());
	}

	@Test
	public void canReadDoubleProperty() {
		doGetPropertyTests(new AllTypes(), "doubleValue", double.class, 0.0, 1.1);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "doubleValue").isDouble());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "doubleValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "doubleObjectValue", Double.class, null, 1.1);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "doubleObjectValue").isDouble());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "doubleObjectValue").isPrimitive());
	}

	@Test
	public void canReadFloatProperty() {
		doGetPropertyTests(new AllTypes(), "floatValue", float.class, 0.0f, 1.1f);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "floatValue").isFloat());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "floatValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "floatObjectValue", Float.class, null, 1.1f);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "floatObjectValue").isFloat());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "floatObjectValue").isPrimitive());
	}

	@Test
	public void canReadBooleanProperty() {
		doGetPropertyTests(new AllTypes(), "booleanValue", boolean.class, false, true);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "booleanValue").isBoolean());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "booleanValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "booleanObjectValue", Boolean.class, null, Boolean.TRUE);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "booleanObjectValue").isBoolean());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "booleanObjectValue").isPrimitive());
	}

	@Test
	public void canReadByteProperty() {
		doGetPropertyTests(new AllTypes(), "byteValue", byte.class, (byte) 0, (byte) 1);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "byteValue").isByte());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "byteValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "byteObjectValue", Byte.class, null, Byte.MAX_VALUE);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "byteObjectValue").isByte());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "byteObjectValue").isPrimitive());
	}

	@Test
	public void canReadCharProperty() {
		doGetPropertyTests(new AllTypes(), "charValue", char.class, (char) 0, 'a');
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "charValue").isCharacter());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "charValue").isPrimitive());
		doGetPropertyTests(new AllTypes(), "charObjectValue", Character.class, null, Character.MAX_VALUE);
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "charObjectValue").isCharacter());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "charObjectValue").isPrimitive());
	}

	@Test
	public void canReadDateProperty() {
		doGetPropertyTests(new AllTypes(), "dateValue", Date.class, null, new Date());
		assertTrue(BeanUtils.propertyNamed(new AllTypes(), "dateValue").isDate());
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "dateValue").isPrimitive());
	}

	@Test
	public void canReadBigDecimalProperty() {
		doGetPropertyTests(new AllTypes(), "bigDecimalValue", BigDecimal.class, null, new BigDecimal(0.0));
		assertFalse(BeanUtils.propertyNamed(new AllTypes(), "bigDecimalValue").isPrimitive());
	}

	@Test
	public void canReadArrayProperty() {
		doGetPropertyTests(new AllTypes(), "array", int[].class, null, new int[] {
			0
		});
		BeanPropertyInstance instance = BeanUtils.propertyNamed(new AllTypes(), "array");
		assertThat(instance.isArray(), equalTo(true));
	}

	@Test
	public void canReadCollectionProperty() {
		doGetGenericPropertyTests(new AllTypes(), "collection", Collection.class, null, asList("sample"), String.class);
		BeanPropertyInstance instance = BeanUtils.propertyNamed(new AllTypes(), "collection");
		assertThat(instance.isCollection(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canReadListProperty() {
		doGetGenericPropertyTests(new AllTypes(), "list", List.class, null, asList("sample"), String.class);
		BeanPropertyInstance instance = BeanUtils.propertyNamed(new AllTypes(), "list");
		assertThat(instance.isList(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canReadSetProperty() {
		doGetGenericPropertyTests(new AllTypes(), "set", Set.class, null, new HashSet<String>(asList("sample")), String.class);
		BeanPropertyInstance instance = BeanUtils.propertyNamed(new AllTypes(), "set");
		assertThat(instance.isSet(), equalTo(true));
		assertThat(instance.isIterable(), equalTo(true));
	}

	@Test
	public void canReadMapProperty() {
		doGetGenericPropertyTests(new AllTypes(), "map", Map.class, null, singletonMap(1L, "value"), Long.class, String.class);
		BeanPropertyInstance instance = BeanUtils.propertyNamed(new AllTypes(), "map");
		assertThat(instance.isMap(), equalTo(true));
	}

	@Test
	public void canVisitAString() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visit(new String(), visitor);
		verifyZeroInteractions(visitor);
	}

	@Test
	public void canFindNothing() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanPropertyInstance> found = BeanUtils.find(car, BeanPredicates.withValue(100));
		assertThat(found, hasSize(0));
	}

	@Test
	public void canFindFirst() {
		Person bob = new Person("Bob", "Onion");
		BeanPropertyInstance found = BeanUtils.findAny(bob, BeanPredicates.withType(String.class));
		assertThat(found, aBeanPropertyInstance("firstname", String.class));
	}

	@Test
	public void canVisitACollection() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel first = new Wheel(), second = new Wheel();
		List<Wheel> list = Arrays.asList(first, second);
		BeanUtils.visit(list, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(first), eq("collection[0].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(second), eq("collection[1].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitAMap() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel first = new Wheel(), second = new Wheel();
		Map<String, Wheel> wheelMap = new HashMap<String, Wheel>();
		wheelMap.put("frontLeft", first);
		wheelMap.put("frontRight", second);
		GraphUtils.visit(wheelMap, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(first), eq("map[frontLeft].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(second), eq("map[frontRight].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitAnArray() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel first = new Wheel(), second = new Wheel();
		Wheel[] wheels = {
				first, second
		};
		BeanUtils.visit(wheels, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(first), eq("array[0].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(second), eq("array[1].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitABean() {

		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);

		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visit(car, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("engine"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("wheels"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canSetAPropertyWithOverloadedProperties() {

		@SuppressWarnings("unused")
		class MixedUp {

			private Boolean value;

			public void setValue(final String value) {
				this.value = Boolean.valueOf(value);
			}

			public void setValue(final Boolean value) {
				this.value = value;
			}

			public Boolean getValue() {
				return value;
			}
		}

		doGetPropertyTests(new MixedUp(), "value", Boolean.class, null, Boolean.TRUE);
	}

	@Test
	public void canCreateEqualBeanPropertyIfSamePropertyOnSameInstance() throws Exception {
		String propertyName = "stringValue";
		AllTypes instance = new AllTypes();
		BeanPropertyInstance property = BeanUtils.propertyNamed(instance, propertyName);
		BeanPropertyInstance other = BeanUtils.propertyNamed(instance, propertyName);
		assertThat(property, equalTo(other));
		assertThat(property.hashCode(), equalTo(other.hashCode()));
	}

	@Test
	public void canCreateUnequalBeanPropertyIfSamePropertyOnDifferentInstance() throws Exception {
		BeanPropertyInstance property = BeanUtils.propertyNamed(new AllTypes(), "stringValue");
		BeanPropertyInstance other = BeanUtils.propertyNamed(new AllTypes(), "stringValue");
		assertThat(property, not(equalTo(other)));
		assertThat(property.hashCode(), not(equalTo(other.hashCode())));
	}

	@Test
	public void canCreateUnequalBeanPropertyIfDifferentPropertyOnSameInstance() throws Exception {
		AllTypes instance = new AllTypes();
		BeanPropertyInstance property = BeanUtils.propertyNamed(instance, "stringValue");
		BeanPropertyInstance other = BeanUtils.propertyNamed(instance, "longValue");
		assertThat(property, not(equalTo(other)));
		assertThat(property.hashCode(), not(equalTo(other.hashCode())));
	}

	@Test(expected = BeanPropertyException.class)
	public void canThrowsExceptionIfSetWithWrongType() throws Exception {
		BeanUtils.setProperty(new AllTypes(), "stringValue", 1L);
	}

	@Test
	public void canApplyAFunctionToABean() {
		final BigDecimal newSize = new BigDecimal(4.0);
		Wheel first = new Wheel(2), last = new Wheel(4);
		Engine engine = new Engine(new BigDecimal(3.8));
		Car car = new Car(engine, Arrays.asList(first, last));
		BeanUtils.apply(car, new BeanPropertyFunction() {

			public void apply(final BeanPropertyInstance property) {
				if (property.hasName("engine")) {
					property.setValue(new Engine(newSize));
				}
			}
		});
		assertThat(car.getEngine().getCapacity(), equalTo(newSize));
	}

	@Test
	public void canApplyAFunctionToMatchingProperties() {

		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);

		BigDecimal newSize = new BigDecimal(4.0);
		BeanUtils.apply(car, changeEngine(new Engine(newSize)), withName("engine"));
		assertThat(car.getEngine().getCapacity(), equalTo(newSize));
	}

	private BeanPropertyFunction changeEngine(final Engine engine) {
		return new BeanPropertyFunction() {

			public void apply(final BeanPropertyInstance property) {
				property.setValue(engine);
			}
		};
	}

	@SuppressWarnings("rawtypes")
	private <T> void doGetPropertyTests(final Object sample, final String name, final Class<T> type, final T currentValue, final T newValue) {

		BeanPropertyInstance instance = BeanUtils.propertyNamed(sample, name);
		assertThat(instance, notNullValue());
		assertThat(instance.isType(type), equalTo(true));
		assertThat(instance.hasValue(currentValue), equalTo(true));
		assertThat(instance.getDeclaringTypeCanonicalName(), equalTo(sample.getClass().getCanonicalName()));
		assertThat(instance.getDeclaringTypeSimpleName(), equalTo(sample.getClass().getSimpleName()));
		assertThat(instance.getTypeCanonicalName(), equalTo(type.getCanonicalName()));
		assertThat(instance.getTypeSimpleName(), equalTo(type.getSimpleName()));

		List<BeanPropertyInstance> properties = BeanUtils.propertyList(sample);
		assertThat(properties, hasItem(aBeanPropertyInstance(name, type)));

		Map<String, BeanPropertyInstance> propertyMap = BeanUtils.propertyMap(sample);
		assertThat(propertyMap, hasEntry(equalTo(name), aBeanPropertyInstance(name, type)));

		assertThat(BeanUtils.hasProperty(sample, name), equalTo(true));
		assertThat(BeanUtils.hasProperty(sample, BeanPredicates.withName(name)), equalTo(true));
		assertThat(BeanUtils.propertyNamed(sample, name), aBeanPropertyInstance(name, type));
		assertThat(BeanUtils.isPropertyType(sample, name, type), equalTo(true));
		assertThat(BeanUtils.isPropertyType(sample, BeanPredicates.withName(name), type), equalTo(true));
		assertThat(BeanUtils.propertyType(sample, name), equalTo((Class) type));
		assertThat(BeanUtils.propertyValue(sample, name, type), equalTo(currentValue));
		assertThat(BeanUtils.propertyValue(sample, name), equalTo((Object) currentValue));
		assertThat(BeanUtils.setProperty(sample, name, newValue), equalTo(true));
		assertThat(BeanUtils.propertyValue(sample, name, type), equalTo(newValue));
		assertThat(BeanUtils.setProperty(sample, BeanPredicates.withName(name), currentValue), equalTo(true));

		List<BeanProperty> typeProperties = BeanUtils.propertyList(sample.getClass());
		assertThat(typeProperties, hasItem(aBeanProperty(name, type)));
		Map<String, BeanProperty> typePropertyMap = BeanUtils.propertyMap(sample.getClass());
		assertThat(typePropertyMap, hasEntry(equalTo(name), aBeanProperty(name, type)));
		assertThat(BeanUtils.hasProperty(sample.getClass(), name), equalTo(true));
		assertThat(BeanUtils.propertyNamed(sample.getClass(), name), aBeanProperty(name, type));
		assertThat(BeanUtils.isPropertyType(sample.getClass(), name, type), equalTo(true));
		assertThat(BeanUtils.propertyType(sample.getClass(), name), equalTo((Class) type));

	}

	private <T> void doGetGenericPropertyTests(final Object sample, final String name, final Class<T> type, final T currentValue, final T newValue, final Class<?>... genericTypes) {
		doGetPropertyTests(sample, name, type, currentValue, newValue);
		BeanPropertyInstance property = BeanUtils.propertyNamed(new AllTypes(), name);
		assertThat(property.isGeneric(), equalTo(true));
		assertThat(property.getTypeParameters(), hasSize(genericTypes.length));
		for (Class<?> genericType : genericTypes) {
			assertThat(property.getTypeParameters(), hasItem(genericType));
		}
	}
}
