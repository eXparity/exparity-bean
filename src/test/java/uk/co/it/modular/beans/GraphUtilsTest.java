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
import static uk.co.it.modular.beans.BeanPredicates.withValue;
import static uk.co.it.modular.beans.GraphUtils.find;
import static uk.co.it.modular.beans.GraphUtils.propertyNamed;
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
import org.hamcrest.Matchers;
import org.junit.Test;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Car;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Engine;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Wheel;

/**
 * Unit Tests for Bean Utils
 * 
 * @author Stewart.Bissett
 */
public class GraphUtilsTest {

	@Test
	public void canReadStringProperty() {
		doGetPropertyTests(new AllTypes(), "stringValue", String.class, null, "sample");
		assertTrue(propertyNamed(new AllTypes(), "stringValue").isString());
		assertFalse(propertyNamed(new AllTypes(), "stringValue").isPrimitive());
	}

	@Test
	public void canReadShortProperty() {

		doGetPropertyTests(new AllTypes(), "shortValue", short.class, (short) 0, Short.MAX_VALUE);
		assertTrue(propertyNamed(new AllTypes(), "shortValue").isShort());
		assertTrue(propertyNamed(new AllTypes(), "shortValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "shortObjectValue", Short.class, null, Short.MAX_VALUE);
		assertTrue(propertyNamed(new AllTypes(), "shortObjectValue").isShort());
		assertFalse(propertyNamed(new AllTypes(), "shortObjectValue").isPrimitive());
	}

	@Test
	public void canReadIntegerProperty() {

		doGetPropertyTests(new AllTypes(), "integerValue", int.class, 0, 12345);
		assertTrue(propertyNamed(new AllTypes(), "integerValue").isInteger());
		assertTrue(propertyNamed(new AllTypes(), "integerValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "integerObjectValue", Integer.class, null, 12345);
		assertTrue(propertyNamed(new AllTypes(), "integerObjectValue").isInteger());
		assertFalse(propertyNamed(new AllTypes(), "integerObjectValue").isPrimitive());
	}

	@Test
	public void canReadLongProperty() {

		doGetPropertyTests(new AllTypes(), "longValue", long.class, 0L, 12345L);
		assertTrue(propertyNamed(new AllTypes(), "longValue").isLong());
		assertTrue(propertyNamed(new AllTypes(), "longValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "longObjectValue", Long.class, null, 12345L);
		assertTrue(propertyNamed(new AllTypes(), "longObjectValue").isLong());
		assertFalse(propertyNamed(new AllTypes(), "longObjectValue").isPrimitive());
	}

	@Test
	public void canReadDoubleProperty() {

		doGetPropertyTests(new AllTypes(), "doubleValue", double.class, 0.0, 1.1);
		assertTrue(propertyNamed(new AllTypes(), "doubleValue").isDouble());
		assertTrue(propertyNamed(new AllTypes(), "doubleValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "doubleObjectValue", Double.class, null, 1.1);
		assertTrue(propertyNamed(new AllTypes(), "doubleObjectValue").isDouble());
		assertFalse(propertyNamed(new AllTypes(), "doubleObjectValue").isPrimitive());
	}

	@Test
	public void canReadFloatProperty() {

		doGetPropertyTests(new AllTypes(), "floatValue", float.class, 0.0f, 1.1f);
		assertTrue(propertyNamed(new AllTypes(), "floatValue").isFloat());
		assertTrue(propertyNamed(new AllTypes(), "floatValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "floatObjectValue", Float.class, null, 1.1f);
		assertTrue(propertyNamed(new AllTypes(), "floatObjectValue").isFloat());
		assertFalse(propertyNamed(new AllTypes(), "floatObjectValue").isPrimitive());
	}

	@Test
	public void canReadBooleanProperty() {

		doGetPropertyTests(new AllTypes(), "booleanValue", boolean.class, false, true);
		assertTrue(propertyNamed(new AllTypes(), "booleanValue").isBoolean());
		assertTrue(propertyNamed(new AllTypes(), "booleanValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "booleanObjectValue", Boolean.class, null, Boolean.TRUE);
		assertTrue(propertyNamed(new AllTypes(), "booleanObjectValue").isBoolean());
		assertFalse(propertyNamed(new AllTypes(), "booleanObjectValue").isPrimitive());
	}

	@Test
	public void canReadByteProperty() {

		doGetPropertyTests(new AllTypes(), "byteValue", byte.class, (byte) 0, (byte) 1);
		assertTrue(propertyNamed(new AllTypes(), "byteValue").isByte());
		assertTrue(propertyNamed(new AllTypes(), "byteValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "byteObjectValue", Byte.class, null, Byte.MAX_VALUE);
		assertTrue(propertyNamed(new AllTypes(), "byteObjectValue").isByte());
		assertFalse(propertyNamed(new AllTypes(), "byteObjectValue").isPrimitive());
	}

	@Test
	public void canReadCharProperty() {

		doGetPropertyTests(new AllTypes(), "charValue", char.class, (char) 0, 'a');
		assertTrue(propertyNamed(new AllTypes(), "charValue").isCharacter());
		assertTrue(propertyNamed(new AllTypes(), "charValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "charObjectValue", Character.class, null, Character.MAX_VALUE);
		assertTrue(propertyNamed(new AllTypes(), "charObjectValue").isCharacter());
		assertFalse(propertyNamed(new AllTypes(), "charObjectValue").isPrimitive());
	}

	@Test
	public void canReadDateProperty() {
		doGetPropertyTests(new AllTypes(), "dateValue", Date.class, null, new Date());
		assertTrue(propertyNamed(new AllTypes(), "dateValue").isDate());
		assertFalse(propertyNamed(new AllTypes(), "dateValue").isPrimitive());
	}

	@Test
	public void canReadBigDecimalProperty() {
		doGetPropertyTests(new AllTypes(), "bigDecimalValue", BigDecimal.class, null, new BigDecimal(0.0));
		assertFalse(propertyNamed(new AllTypes(), "bigDecimalValue").isPrimitive());
	}

	@Test
	public void canReadArrayProperty() {
		doGetPropertyTests(new AllTypes(), "array", int[].class, null, new int[] {
			0
		});
	}

	@Test
	public void canReadCollectionProperty() {
		doGetGenericPropertyTests(new AllTypes(), "collection", Collection.class, null, asList("sample"), String.class);
	}

	@Test
	public void canReadListProperty() {
		doGetGenericPropertyTests(new AllTypes(), "list", List.class, null, asList("sample"), String.class);
	}

	@Test
	public void canReadSetProperty() {
		doGetGenericPropertyTests(new AllTypes(), "set", Set.class, null, new HashSet<String>(asList("sample")), String.class);
	}

	@Test
	public void canReadMapProperty() {
		doGetGenericPropertyTests(new AllTypes(), "map", Map.class, null, singletonMap(1L, "value"), Long.class, String.class);
	}

	@Test
	public void canVisitAString() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		GraphUtils.visit(new String(), visitor);
		verifyZeroInteractions(visitor);
	}

	@Test
	public void canVisitASimpleObject() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel wheel = new Wheel();
		GraphUtils.visit(wheel, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(wheel), eq("diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitNull() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		GraphUtils.visit(null, visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitAGraphAndNotOverflow() {
		Person brother = new Person(), sister = new Person();
		brother.setSiblings(asList(sister));
		sister.setSiblings(asList(brother));
		BeanVisitor visitor = mock(BeanVisitor.class);
		GraphUtils.visit(brother, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(brother), eq("siblings"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(sister), eq("siblings[0].siblings"), any(Object[].class));
	}

	@Test
	public void canVisitWithNullProperties() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel wheel = new Wheel();
		wheel.setDiameter(null);
		Car car = new Car(null, Arrays.asList(wheel));
		GraphUtils.visit(car, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("engine"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("wheels"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(wheel), eq("wheels[0].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canFindPropertyInGraph() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanPropertyInstance> found = find(car, withValue(4));
		assertThat(found, hasSize(1));
		assertThat(found.get(0).getInstance(), equalTo((Object) last));
	}

	@Test
	public void canFindWithPredicate() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanPropertyInstance> found = find(car, withValue("diameter", 4));
		assertThat(found, hasSize(1));
		assertThat(found.get(0).getInstance(), equalTo((Object) last));
	}

	@Test
	public void canFindNothing() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanPropertyInstance> found = find(car, withValue(100));
		assertThat(found, hasSize(0));
	}

	@Test
	public void canIgnoreMissingHasProperty() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(GraphUtils.hasProperty(car, withValue(100)), equalTo(false));
	}

	@Test
	public void canIgnoreMissingPropertyType() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(GraphUtils.propertyType(car, "hubcap"), nullValue());
	}

	@Test
	public void canIgnoreMissingPropertyValue() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(GraphUtils.propertyValue(car, "hubcap"), nullValue());
	}

	@Test
	public void canIgnoreMissingSetProperty() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		GraphUtils.setProperty(car, "hubcap", 20);
	}

	@Test
	public void canFindFirst() {
		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);
		BeanPropertyInstance found = GraphUtils.findFirst(car, BeanPredicates.withType(Engine.class));
		assertThat(found.getValue(), Matchers.equalTo((Object) engine));
	}

	@Test
	public void canVisitACollection() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel first = new Wheel(), second = new Wheel();
		List<Wheel> list = Arrays.asList(first, second);
		GraphUtils.visit(list, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(first), eq("collection[0].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(second), eq("collection[1].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitAnArray() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel first = new Wheel(), second = new Wheel();
		Wheel[] wheels = {
				first, second
		};
		GraphUtils.visit(wheels, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(first), eq("array[0].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(second), eq("array[1].diameter"), any(Object[].class));
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
	public void canVisitAWholeGraph() {

		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);

		BeanVisitor visitor = mock(BeanVisitor.class);
		GraphUtils.visit(car, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("engine"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(engine), eq("engine.capacity"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("wheels"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(wheels.get(0)), eq("wheels[0].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(wheels.get(1)), eq("wheels[1].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(wheels.get(2)), eq("wheels[2].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(wheels.get(3)), eq("wheels[3].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitAGraphWithoutOverflow() {

		Person oldest = new Person(), middle = new Person(), youngest = new Person();
		oldest.setSiblings(asList(middle, youngest));
		middle.setSiblings(asList(youngest, oldest));
		youngest.setSiblings(asList(middle, oldest));

		BeanVisitor visitor = mock(BeanVisitor.class);
		GraphUtils.visit(oldest, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(oldest), eq("siblings"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(middle), eq("siblings[0].siblings"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(youngest), eq("siblings[0].siblings[0].siblings"), any(Object[].class));
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
	public void isEqualIfSamePropertyOnSameInstance() throws Exception {
		String propertyName = "stringValue";
		AllTypes instance = new AllTypes();
		BeanPropertyInstance property = propertyNamed(instance, propertyName);
		BeanPropertyInstance other = propertyNamed(instance, propertyName);
		assertThat(property, equalTo(other));
		assertThat(property.hashCode(), equalTo(other.hashCode()));
	}

	@Test
	public void isNotEqualIfBeanPropertyIfSamePropertyOnDifferentInstance() throws Exception {
		BeanPropertyInstance property = propertyNamed(new AllTypes(), "stringValue");
		BeanPropertyInstance other = propertyNamed(new AllTypes(), "stringValue");
		assertThat(property, not(equalTo(other)));
		assertThat(property.hashCode(), not(equalTo(other.hashCode())));
	}

	@Test
	public void isNotEqualIfBeanPropertyIfDifferentPropertyOnSameInstance() throws Exception {
		AllTypes instance = new AllTypes();
		BeanPropertyInstance property = propertyNamed(instance, "stringValue");
		BeanPropertyInstance other = propertyNamed(instance, "longValue");
		assertThat(property, not(equalTo(other)));
		assertThat(property.hashCode(), not(equalTo(other.hashCode())));
	}

	@Test(expected = BeanPropertyException.class)
	public void canThrowsExceptionIfSetWithWrongType() throws Exception {
		GraphUtils.setProperty(new AllTypes(), "stringValue", 1L);
	}

	@Test
	public void canApplyAFunctionToAGraph() {
		final int newSize = 3;
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		GraphUtils.apply(car, new BeanPropertyFunction() {

			public void apply(final BeanPropertyInstance property) {
				if (property.hasName("diameter")) {
					property.setValue(newSize);
				}
			}
		});
		assertThat(first.getDiameter(), equalTo(newSize));
		assertThat(last.getDiameter(), equalTo(newSize));
	}

	@Test
	public void canApplyAFunctionToMatchingProperties() {
		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);
		GraphUtils.apply(car, changeWheelSize(20), withName("diameter"));
	}

	private BeanPropertyFunction changeWheelSize(final int newSize) {
		return new BeanPropertyFunction() {

			public void apply(final BeanPropertyInstance property) {
				property.setValue(newSize);
			}
		};
	}

	@Test
	public void canSetPropertyViaPredicate() {
		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);
		int newSize = 20;
		GraphUtils.setProperty(car, withName("diameter"), newSize);
		for (Wheel wheel : wheels) {
			assertThat(wheel.getDiameter(), equalTo(newSize));
		}
	}

	@SuppressWarnings("rawtypes")
	private <T> void doGetPropertyTests(final Object sample, final String name, final Class<T> type, final T currentValue, final T newValue) {

		List<BeanPropertyInstance> properties = GraphUtils.propertyList(sample);
		assertThat(properties, hasItem(aBeanPropertyInstance(name, type)));

		Map<String, BeanPropertyInstance> propertyMap = GraphUtils.propertyMap(sample);
		assertThat(propertyMap, hasEntry(equalTo(name), aBeanPropertyInstance(name, type)));

		assertThat(GraphUtils.hasProperty(sample, name), equalTo(true));
		assertThat(GraphUtils.hasProperty(sample, BeanPredicates.withName(name)), equalTo(true));
		assertThat(GraphUtils.propertyNamed(sample, name), aBeanPropertyInstance(name, type));
		assertThat(GraphUtils.isPropertyType(sample, name, type), equalTo(true));
		assertThat(GraphUtils.isPropertyType(sample, BeanPredicates.withName(name), type), equalTo(true));
		assertThat(GraphUtils.propertyType(sample, name), equalTo((Class) type));
		assertThat(GraphUtils.propertyValue(sample, name, type), equalTo(currentValue));
		assertThat(GraphUtils.propertyValue(sample, name), equalTo((Object) currentValue));
		assertThat(GraphUtils.setProperty(sample, name, newValue), equalTo(true));
	}

	private <T> void doGetGenericPropertyTests(final Object sample, final String name, final Class<T> type, final T currentValue, final T newValue, final Class<?>... genericTypes) {
		doGetPropertyTests(sample, name, type, currentValue, newValue);
		BeanPropertyInstance property = propertyNamed(new AllTypes(), name);
		assertThat(property.isGeneric(), equalTo(true));
		assertThat(property.getTypeParameters(), hasSize(genericTypes.length));
		for (Class<?> genericType : genericTypes) {
			assertThat(property.getTypeParameters(), hasItem(genericType));
		}
	}

	class Person {

		private List<Person> siblings;

		public List<Person> getSiblings() {
			return siblings;
		}

		public void setSiblings(final List<Person> siblings) {
			this.siblings = siblings;
		}
	}

}
