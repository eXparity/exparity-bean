/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import static com.modularit.beans.BeanBuilder.anInstanceOf;
import static com.modularit.beans.BeanBuilder.randomValues;
import static com.modularit.beans.BeanUtils.property;
import static com.modularit.beans.BeanUtils.visitAll;
import static com.modularit.beans.testutils.BeanPropertyMatchers.aBeanProperty;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.Test;
import com.modularit.beans.testutils.BeanUtilTestFixture.AllTypes;
import com.modularit.beans.testutils.BeanUtilTestFixture.Car;
import com.modularit.beans.testutils.BeanUtilTestFixture.Engine;
import com.modularit.beans.testutils.BeanUtilTestFixture.Wheel;

/**
 * Unit Tests for Bean Utils
 * 
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class BeanUtilsTest {

	@Test
	public void canReadStringProperty() {
		doGetPropertyTests(new AllTypes(), "stringValue", String.class, null, "sample");
		assertTrue(property(new AllTypes(), "stringValue").isString());
		assertFalse(property(new AllTypes(), "stringValue").isPrimitive());
	}

	@Test
	public void canReadShortProperty() {

		doGetPropertyTests(new AllTypes(), "shortValue", short.class, (short) 0, Short.MAX_VALUE);
		assertTrue(property(new AllTypes(), "shortValue").isShort());
		assertTrue(property(new AllTypes(), "shortValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "shortObjectValue", Short.class, null, Short.MAX_VALUE);
		assertTrue(property(new AllTypes(), "shortObjectValue").isShort());
		assertFalse(property(new AllTypes(), "shortObjectValue").isPrimitive());
	}

	@Test
	public void canReadIntegerProperty() {

		doGetPropertyTests(new AllTypes(), "integerValue", int.class, 0, 12345);
		assertTrue(property(new AllTypes(), "integerValue").isInteger());
		assertTrue(property(new AllTypes(), "integerValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "integerObjectValue", Integer.class, null, 12345);
		assertTrue(property(new AllTypes(), "integerObjectValue").isInteger());
		assertFalse(property(new AllTypes(), "integerObjectValue").isPrimitive());
	}

	@Test
	public void canReadLongProperty() {

		doGetPropertyTests(new AllTypes(), "longValue", long.class, 0L, 12345L);
		assertTrue(property(new AllTypes(), "longValue").isLong());
		assertTrue(property(new AllTypes(), "longValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "longObjectValue", Long.class, null, 12345L);
		assertTrue(property(new AllTypes(), "longObjectValue").isLong());
		assertFalse(property(new AllTypes(), "longObjectValue").isPrimitive());
	}

	@Test
	public void canReadDoubleProperty() {

		doGetPropertyTests(new AllTypes(), "doubleValue", double.class, 0.0, 1.1);
		assertTrue(property(new AllTypes(), "doubleValue").isDouble());
		assertTrue(property(new AllTypes(), "doubleValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "doubleObjectValue", Double.class, null, 1.1);
		assertTrue(property(new AllTypes(), "doubleObjectValue").isDouble());
		assertFalse(property(new AllTypes(), "doubleObjectValue").isPrimitive());
	}

	@Test
	public void canReadFloatProperty() {

		doGetPropertyTests(new AllTypes(), "floatValue", float.class, 0.0f, 1.1f);
		assertTrue(property(new AllTypes(), "floatValue").isFloat());
		assertTrue(property(new AllTypes(), "floatValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "floatObjectValue", Float.class, null, 1.1f);
		assertTrue(property(new AllTypes(), "floatObjectValue").isFloat());
		assertFalse(property(new AllTypes(), "floatObjectValue").isPrimitive());
	}

	@Test
	public void canReadBooleanProperty() {

		doGetPropertyTests(new AllTypes(), "booleanValue", boolean.class, false, true);
		assertTrue(property(new AllTypes(), "booleanValue").isBoolean());
		assertTrue(property(new AllTypes(), "booleanValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "booleanObjectValue", Boolean.class, null, Boolean.TRUE);
		assertTrue(property(new AllTypes(), "booleanObjectValue").isBoolean());
		assertFalse(property(new AllTypes(), "booleanObjectValue").isPrimitive());
	}

	@Test
	public void canReadByteProperty() {

		doGetPropertyTests(new AllTypes(), "byteValue", byte.class, (byte) 0, (byte) 1);
		assertTrue(property(new AllTypes(), "byteValue").isByte());
		assertTrue(property(new AllTypes(), "byteValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "byteObjectValue", Byte.class, null, Byte.MAX_VALUE);
		assertTrue(property(new AllTypes(), "byteObjectValue").isByte());
		assertFalse(property(new AllTypes(), "byteObjectValue").isPrimitive());
	}

	@Test
	public void canReadCharProperty() {

		doGetPropertyTests(new AllTypes(), "charValue", char.class, (char) 0, 'a');
		assertTrue(property(new AllTypes(), "charValue").isCharacter());
		assertTrue(property(new AllTypes(), "charValue").isPrimitive());

		doGetPropertyTests(new AllTypes(), "charObjectValue", Character.class, null, Character.MAX_VALUE);
		assertTrue(property(new AllTypes(), "charObjectValue").isCharacter());
		assertFalse(property(new AllTypes(), "charObjectValue").isPrimitive());
	}

	@Test
	public void canReadDateProperty() {
		doGetPropertyTests(new AllTypes(), "dateValue", Date.class, null, new Date());
		assertTrue(property(new AllTypes(), "dateValue").isDate());
		assertFalse(property(new AllTypes(), "dateValue").isPrimitive());
	}

	@Test
	public void canReadBigDecimalProperty() {
		doGetPropertyTests(new AllTypes(), "bigDecimalValue", BigDecimal.class, null, new BigDecimal(0.0));
		assertFalse(property(new AllTypes(), "bigDecimalValue").isPrimitive());
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
		BeanUtils.visit(new String(), visitor);
		verifyZeroInteractions(visitor);
	}

	@Test
	public void canVisitASimpleObject() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel wheel = new Wheel();
		BeanUtils.visit(wheel, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(wheel), eq("diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitWithNullProperties() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel wheel = new Wheel();
		wheel.setDiameter(null);
		Car car = new Car(null, Arrays.asList(wheel));
		BeanUtils.visitAll(car, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("engine"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(car), eq("wheels"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(wheel), eq("wheels[0].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitACollection() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel first = new Wheel(), second = new Wheel();
		List<Wheel> list = Arrays.asList(first, second);
		BeanUtils.visitAll(list, visitor);
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
		BeanUtils.visitAll(wheels, visitor);
		verify(visitor).visit(any(BeanProperty.class), eq(first), eq("array[0].diameter"), any(Object[].class));
		verify(visitor).visit(any(BeanProperty.class), eq(second), eq("array[1].diameter"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void visitWillNotFollowAGraph() {

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
	public void visitAllWillFollowAGraph() {

		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);

		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visitAll(car, visitor);
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
	public void visitAllWillPreventStackOverflow() {

		Person oldest = new Person(), middle = new Person(), youngest = new Person();
		oldest.setSiblings(asList(middle, youngest));
		middle.setSiblings(asList(youngest, oldest));
		youngest.setSiblings(asList(middle, oldest));

		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visitAll(oldest, visitor);
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

	@Test(expected = StackOverflowError.class)
	public void visitAllAllowOverflowWillOverflow() {

		Person brother = new Person(), sister = new Person();
		brother.setSiblings(asList(sister));
		sister.setSiblings(asList(brother));

		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visitAllAllowOverflow(brother, visitor);
	}

	@Test
	public void canInstantiateASimpleObject() {
		AllTypes allTypes = anInstanceOf(AllTypes.class).populatedWith(randomValues()).build();
		visitAll(allTypes, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				assertThat("Expected " + property + " to not be null", property.getValue(), Matchers.notNullValue());
			}
		});
	}

	@Test
	public void canInstantiateAndFillAComplexObject() {
		Car car = anInstanceOf(Car.class).populatedWith(randomValues()).build();
		assertThat(car.getEngine(), Matchers.notNullValue());
		assertThat(car.getEngine().getCapacity(), Matchers.notNullValue());
		assertThat(car.getWheels().size(), Matchers.greaterThan(0));
		assertThat(car.getWheels().get(0).getDiameter(), Matchers.notNullValue());
	}

	@SuppressWarnings("rawtypes")
	private <T> void doGetPropertyTests(final Object sample, final String name, final Class<T> type, final T currentValue, final T newValue) {

		List<BeanProperty> properties = BeanUtils.propertyList(sample);
		assertThat(properties, hasItem(aBeanProperty(name, type)));

		Map<String, BeanProperty> propertyMap = BeanUtils.propertyMap(sample);
		assertThat(propertyMap, hasEntry(equalTo(name), aBeanProperty(name, type)));

		assertThat(BeanUtils.hasProperty(sample, name), equalTo(true));
		assertThat(BeanUtils.property(sample, name), aBeanProperty(name, type));
		assertThat(BeanUtils.isPropertyType(sample, name, type), equalTo(true));
		assertThat(BeanUtils.propertyType(sample, name), equalTo((Class) type));
		assertThat(BeanUtils.propertyValue(sample, name, type), equalTo(currentValue));
		assertThat(BeanUtils.setProperty(sample, name, newValue), equalTo(true));
		assertThat(BeanUtils.propertyValue(sample, name, type), equalTo(newValue));
	}

	private <T> void doGetGenericPropertyTests(final Object sample, final String name, final Class<T> type, final T currentValue, final T newValue, final Class<?>... genericTypes) {
		doGetPropertyTests(sample, name, type, currentValue, newValue);
		BeanProperty property = property(new AllTypes(), name);
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
