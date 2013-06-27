/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static uk.co.it.modular.beans.Bean.bean;
import static uk.co.it.modular.beans.BeanBuilder.aRandomInstanceOf;
import static uk.co.it.modular.beans.BeanBuilder.anEmptyInstanceOf;
import static uk.co.it.modular.beans.BeanBuilder.anInstanceOf;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
import org.junit.Test;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Car;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Employee;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Engine;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Manager;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.NoDefaultConstructor;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;

/**
 * @author Stewart.Bissett
 */
public class BeanBuilderTest {

	@Test
	public void canRandomlyFillAGraph() {
		Car car = aRandomInstanceOf(Car.class).build();
		assertThat(car.getEngine(), Matchers.notNullValue());
		assertThat(car.getEngine().getCapacity(), Matchers.notNullValue());
		assertThat(car.getWheels().size(), Matchers.greaterThan(0));
		assertThat(car.getWheels().get(0).getDiameter(), Matchers.notNullValue());
	}

	@Test
	public void canRandomlyFillASimpleObject() {
		AllTypes allTypes = aRandomInstanceOf(AllTypes.class).build();
		bean(allTypes).visit(new BeanVisitor() {

			public void visit(final BeanPropertyInstance property, final Object current, final String path, final Object[] stack) {
				assertThat("Expected " + property + " to not be null", property.getValue(), notNullValue());
			}
		});
	}

	@Test
	public void canCreateAnNullSimpleObject() {
		AllTypes allTypes = anInstanceOf(AllTypes.class).build();
		bean(allTypes).visit(new BeanVisitor() {

			public void visit(final BeanPropertyInstance property, final Object current, final String path, final Object[] stack) {
				if (!property.isPrimitive()) {
					assertThat("Expected " + property + " to not be null", property.getValue(), nullValue());
				}
			}
		});
	}

	@Test
	public void canCreateAnEmptySimpleObject() {
		AllTypes allTypes = anEmptyInstanceOf(AllTypes.class).build();
		bean(allTypes).visit(new BeanVisitor() {

			public void visit(final BeanPropertyInstance property, final Object current, final String path, final Object[] stack) {
				if (!property.isCollection() && !property.isMap() && !property.isPrimitive() && !property.isArray() && !property.isEnum()) {
					assertThat("Expected " + property + " to not be null", property.getValue(), nullValue());
				}
			}
		});
	}

	@Test
	public void canCreateAnEmptyGraph() {
		Car car = anEmptyInstanceOf(Car.class).build();
		assertThat(car.getEngine().getCapacity(), Matchers.nullValue());
		assertThat(car.getWheels().size(), Matchers.greaterThan(0));
		assertThat(car.getWheels().get(0).getDiameter(), Matchers.nullValue());
	}

	@Test
	public void canRandomlyFillAGraphOverrideProperty() {
		BigDecimal overrideValue = new BigDecimal("4.0");
		Car car = aRandomInstanceOf(Car.class).with("capacity", overrideValue).build();
		assertThat(car.getEngine().getCapacity(), comparesEqualTo(overrideValue));
	}

	@Test
	public void canRandomlyFillAGraphWithOveridePropertyOnOverride() {
		BigDecimal capacity = new BigDecimal("4.0");
		Engine engine = aRandomInstanceOf(Engine.class).build();
		Car car = aRandomInstanceOf(Car.class).with("engine", engine).with("capacity", capacity).build();
		assertThat(car.getEngine(), theInstance(engine));
		assertThat(car.getEngine().getCapacity(), comparesEqualTo(capacity));
	}

	@Test
	public void canRandomlyFillAGraphOverridePropertyShortForm() {
		BigDecimal overrideValue = new BigDecimal("4.0");
		Car car = aRandomInstanceOf(Car.class).with("capacity", overrideValue).build();
		assertThat(car.getEngine().getCapacity(), comparesEqualTo(overrideValue));
	}

	@Test
	public void canRandomlyFillAGraphWithoutOverflow() {
		Person person = aRandomInstanceOf(Person.class).aCollectionSizeOf(1).build();
		assertThat(person.getFirstname(), notNullValue());
		assertThat(person.getSurname(), notNullValue());
		assertThat(person.getSiblings(), hasSize(1));
		assertThat(person.getSiblings().get(0).getFirstname(), notNullValue());
		assertThat(person.getSiblings().get(0).getSurname(), notNullValue());
		assertThat(person.getSiblings().get(0).getSiblings(), nullValue());
	}

	@Test
	public void canRandomlyFillAGraphOverridePropertyByPath() {
		BigDecimal overrideValue = new BigDecimal("4.0");
		Car car = aRandomInstanceOf(Car.class).with("car.engine.capacity", overrideValue).build();
		assertThat(car.getEngine().getCapacity(), comparesEqualTo(overrideValue));
	}

	@Test
	public void canRandomlyFillAGraphOverridePropertyByIndexedPath() {
		int overrideDiameter = 1234;
		Car car = aRandomInstanceOf(Car.class).aCollectionSizeOf(4).with("car.wheels[1].diameter", overrideDiameter).build();
		assertThat(car.getWheels().get(1).getDiameter(), equalTo(overrideDiameter));
	}

	@Test
	public void canRandomlyFillAGraphOverridePropertyByPathShortForm() {
		BigDecimal overrideValue = new BigDecimal("4.0");
		Car car = aRandomInstanceOf(Car.class).with("car.engine.capacity", overrideValue).build();
		assertThat(car.getEngine().getCapacity(), comparesEqualTo(overrideValue));
	}

	@Test
	public void canRandomlyFillAGraphExcludeProperty() {
		Car car = aRandomInstanceOf(Car.class).excludeProperty("capacity").build();
		assertThat(car.getEngine().getCapacity(), nullValue());
	}

	@Test
	public void canRandomlyFillAGraphExcludePath() {
		Car car = aRandomInstanceOf(Car.class).excludePath("car.engine.capacity").build();
		assertThat(car.getEngine().getCapacity(), nullValue());
	}

	@Test
	public void canRandomlyFillAGraphControlCollectionSize() {
		int expectedSize = 1;
		Car car = aRandomInstanceOf(Car.class).aCollectionSizeOf(expectedSize).build();
		assertThat(car.getWheels(), hasSize(expectedSize));
	}

	@Test
	public void canFillAGraphWithSubTypes() {
		Employee employee = aRandomInstanceOf(Employee.class).usingType(Person.class, Manager.class).build();
		assertThat(employee.getManager(), instanceOf(Manager.class));
	}

	@Test(expected = BeanBuilderException.class)
	public void canNotCreateAnInstanceWithNoDefaultConstructor() {
		aRandomInstanceOf(NoDefaultConstructor.class).build();
	}

}
