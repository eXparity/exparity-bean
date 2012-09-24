/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import static java.util.Arrays.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class BeanInspectorTest {

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
		verify(visitor).visit(any(Object[].class), eq("diameter"), eq(wheel), any(BeanProperty.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitACollection() {
		BeanVisitor visitor = mock(BeanVisitor.class);
		Wheel first = new Wheel(), second = new Wheel();
		List<Wheel> list = Arrays.asList(first, second);
		BeanUtils.visitAll(list, visitor);
		verify(visitor).visit(any(Object[].class), eq("collection[0].diameter"), eq(first), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("collection[1].diameter"), eq(second), any(BeanProperty.class));
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
		verify(visitor).visit(any(Object[].class), eq("array[0].diameter"), eq(first), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("array[1].diameter"), eq(second), any(BeanProperty.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void visitWillNotFollowAGraph() {

		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);

		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visit(car, visitor);
		verify(visitor).visit(any(Object[].class), eq("engine"), eq(car), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("wheels"), eq(car), any(BeanProperty.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void visitAllWillFollowAGraph() {

		Engine engine = new Engine(new BigDecimal(3.8));
		List<Wheel> wheels = asList(new Wheel(18), new Wheel(18), new Wheel(18), new Wheel(18));
		Car car = new Car(engine, wheels);

		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visitAll(car, visitor);
		verify(visitor).visit(any(Object[].class), eq("engine"), eq(car), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("engine.capacity"), eq(engine), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("wheels"), eq(car), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("wheels[0].diameter"), eq(wheels.get(0)), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("wheels[1].diameter"), eq(wheels.get(1)), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("wheels[2].diameter"), eq(wheels.get(2)), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("wheels[3].diameter"), eq(wheels.get(3)), any(BeanProperty.class));
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
		verify(visitor).visit(any(Object[].class), eq("siblings"), eq(oldest), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("siblings[0].siblings"), eq(middle), any(BeanProperty.class));
		verify(visitor).visit(any(Object[].class), eq("siblings[0].siblings[0].siblings"), eq(youngest), any(BeanProperty.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test(expected = StackOverflowError.class)
	public void vvisitAllAllowOverflowWillOverflow() {

		Person brother = new Person(), sister = new Person();
		brother.setSiblings(asList(sister));
		sister.setSiblings(asList(brother));

		BeanVisitor visitor = mock(BeanVisitor.class);
		BeanUtils.visitAllAllowOverflow(brother, visitor);
	}

	class Wheel {

		private Integer diameter;

		public Wheel(final Integer diameter) {
			this.diameter = diameter;
		}

		public Wheel() {
		}

		public Integer getDiameter() {
			return diameter;
		}

		public void setDiameter(final Integer diameter) {
			this.diameter = diameter;
		}
	}

	class Engine {

		private BigDecimal capacity;

		public Engine(final BigDecimal capacity) {
			this.capacity = capacity;
		}

		public Engine() {
		}

		public BigDecimal getCapacity() {
			return capacity;
		}

		public void setCapacity(final BigDecimal capacity) {
			this.capacity = capacity;
		}
	}

	class Car {

		private Engine engine;
		private List<Wheel> wheels;

		public Car(final Engine engine, final List<Wheel> wheels) {
			this.engine = engine;
			this.wheels = wheels;
		}

		public Car() {
		}

		public Engine getEngine() {
			return engine;
		}

		public void setEngine(final Engine engine) {
			this.engine = engine;
		}

		public List<Wheel> getWheels() {
			return wheels;
		}

		public void setWheels(final List<Wheel> wheels) {
			this.wheels = wheels;
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
