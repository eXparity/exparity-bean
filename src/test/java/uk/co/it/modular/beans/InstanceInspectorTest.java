/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.arrayContaining;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.co.it.modular.beans.Bean.bean;
import static uk.co.it.modular.beans.BeanBuilder.aRandomInstanceOf;
import static uk.co.it.modular.beans.BeanBuilder.anEmptyInstanceOf;
import static uk.co.it.modular.beans.BeanBuilder.anInstanceOf;
import static uk.co.it.modular.beans.InstanceInspector.beanInspector;
import static uk.co.it.modular.beans.InstanceInspector.graphInspector;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Car;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Engine;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.GetterWithArgs;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.NameMismatch;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.OverloadedSetter;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.SetterWithNotArgs;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.TypeMismatch;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Wheel;

/**
 * @author Stewart Bissett
 */
public class InstanceInspectorTest {

	@Test
	public void canInspectABean() {
		Person instance = aRandomInstanceOf(Person.class).build();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(instance, visitor);
		verify(visitor).visit(eq(bean(instance).propertyNamed("firstname")), eq(instance), eq(new BeanPropertyPath("person.firstname")), aStackOf(instance));
		verify(visitor).visit(eq(bean(instance).propertyNamed("surname")), eq(instance), eq(new BeanPropertyPath("person.surname")), aStackOf(instance));
		verify(visitor).visit(eq(bean(instance).propertyNamed("siblings")), eq(instance), eq(new BeanPropertyPath("person.siblings")), aStackOf(instance));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAnEmptyBean() {
		Person instance = anEmptyInstanceOf(Person.class).build();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(instance, visitor);
		verify(visitor).visit(eq(bean(instance).propertyNamed("firstname")), eq(instance), eq(new BeanPropertyPath("person.firstname")), aStackOf(instance));
		verify(visitor).visit(eq(bean(instance).propertyNamed("surname")), eq(instance), eq(new BeanPropertyPath("person.surname")), aStackOf(instance));
		verify(visitor).visit(eq(bean(instance).propertyNamed("siblings")), eq(instance), eq(new BeanPropertyPath("person.siblings")), aStackOf(instance));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectANull() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(null, visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAGraph() {
		Car car = aRandomInstanceOf(Car.class).aCollectionSizeOf(4).build();
		Engine engine = car.getEngine();
		List<Wheel> wheels = car.getWheels();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		graphInspector().inspect(car, visitor);
		verify(visitor).visit(eq(bean(car).get("engine")), eq(car), eq(new BeanPropertyPath("car.engine")), aStackOf(car));
		verify(visitor).visit(eq(bean(engine).get("capacity")), eq(engine), eq(new BeanPropertyPath("car.engine.capacity")), aStackOf(car, engine));
		verify(visitor).visit(eq(bean(car).get("wheels")), eq(car), eq(new BeanPropertyPath("car.wheels")), aStackOf(car));
		verify(visitor).visit(eq(bean(wheels.get(0)).get("diameter")), eq(wheels.get(0)), eq(new BeanPropertyPath("car.wheels[0].diameter")), aStackOf(car, wheels.get(0)));
		verify(visitor).visit(eq(bean(wheels.get(1)).get("diameter")), eq(wheels.get(1)), eq(new BeanPropertyPath("car.wheels[1].diameter")), aStackOf(car, wheels.get(1)));
		verify(visitor).visit(eq(bean(wheels.get(2)).get("diameter")), eq(wheels.get(2)), eq(new BeanPropertyPath("car.wheels[2].diameter")), aStackOf(car, wheels.get(2)));
		verify(visitor).visit(eq(bean(wheels.get(3)).get("diameter")), eq(wheels.get(3)), eq(new BeanPropertyPath("car.wheels[3].diameter")), aStackOf(car, wheels.get(3)));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAGraphAndNotOverflow() {
		Person brother = anEmptyInstanceOf(Person.class).build(), sister = anEmptyInstanceOf(Person.class).build();
		brother.setSiblings(asList(sister));
		sister.setSiblings(asList(brother));
		BeanVisitor visitor = mock(BeanVisitor.class);
		graphInspector().inspect(brother, visitor);
		verify(visitor).visit(eq(bean(brother).get("firstname")), eq(brother), eq(new BeanPropertyPath("person.firstname")), aStackOf(brother));
		verify(visitor).visit(eq(bean(brother).get("surname")), eq(brother), eq(new BeanPropertyPath("person.surname")), aStackOf(brother));
		verify(visitor).visit(eq(bean(brother).get("siblings")), eq(brother), eq(new BeanPropertyPath("person.siblings")), aStackOf(brother));
		verify(visitor).visit(eq(bean(sister).get("firstname")), eq(sister), eq(new BeanPropertyPath("person.siblings[0].firstname")), aStackOf(brother, sister));
		verify(visitor).visit(eq(bean(sister).get("surname")), eq(sister), eq(new BeanPropertyPath("person.siblings[0].surname")), aStackOf(brother, sister));
		verify(visitor).visit(eq(bean(sister).get("siblings")), eq(sister), eq(new BeanPropertyPath("person.siblings[0].siblings")), aStackOf(brother, sister));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWithOverloadedSetter() {
		OverloadedSetter instance = aRandomInstanceOf(OverloadedSetter.class).build();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(instance, visitor);
		verify(visitor).visit(eq(bean(instance).propertyNamed("property")),
				eq(instance),
				eq(new BeanPropertyPath("overloadedSetter.property")),
				argThat(arrayContaining(((Object) instance))));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAllTypes() {
		AllTypes instance = aRandomInstanceOf(AllTypes.class).build();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(instance, visitor);
		// verify(visitor).visit(eq(bean(instance).propertyNamed("property")), eq(instance), eq(new BeanPropertyPath("overloadedSetter.property"), argThat(arrayContaining(((Object)
		// instance))));
		// verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAllTypesAreEmpty() {
		AllTypes instance = anEmptyInstanceOf(AllTypes.class).build();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(instance, visitor);
		// verify(visitor).visit(eq(bean(instance).propertyNamed("property")), eq(instance), eq(new BeanPropertyPath("overloadedSetter.property"), argThat(arrayContaining(((Object)
		// instance))));
		// verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAllTypesAreNull() {
		AllTypes instance = anInstanceOf(AllTypes.class).build();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(instance, visitor);
		// verify(visitor).visit(eq(bean(instance).propertyNamed("property")), eq(instance), eq(new BeanPropertyPath("overloadedSetter.property"), argThat(arrayContaining(((Object)
		// instance))));
		// verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAMap() {
		Person bob = aRandomInstanceOf(Person.class).with("surname", "Onion").build();
		Person tina = aRandomInstanceOf(Person.class).with("surname", "Melon").build();
		Map<String, Person> instance = new HashMap<String, Person>();
		instance.put("Bob", bob);
		instance.put("Tina", tina);
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(instance, visitor);
		verify(visitor).visit(eq(bean(bob).propertyNamed("firstname")), eq(bob), eq(new BeanPropertyPath("map[Bob].firstname")), aStackOf(bob));
		verify(visitor).visit(eq(bean(bob).propertyNamed("surname")), eq(bob), eq(new BeanPropertyPath("map[Bob].surname")), aStackOf(bob));
		verify(visitor).visit(eq(bean(bob).propertyNamed("siblings")), eq(bob), eq(new BeanPropertyPath("map[Bob].siblings")), aStackOf(bob));
		verify(visitor).visit(eq(bean(tina).propertyNamed("firstname")), eq(tina), eq(new BeanPropertyPath("map[Tina].firstname")), aStackOf(tina));
		verify(visitor).visit(eq(bean(tina).propertyNamed("surname")), eq(tina), eq(new BeanPropertyPath("map[Tina].surname")), aStackOf(tina));
		verify(visitor).visit(eq(bean(tina).propertyNamed("siblings")), eq(tina), eq(new BeanPropertyPath("map[Tina].siblings")), aStackOf(tina));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAnArrray() {
		Person bob = aRandomInstanceOf(Person.class).with("surname", "Onion").build();
		Person tina = aRandomInstanceOf(Person.class).with("surname", "Melon").build();
		Person[] people = {
				bob, tina
		};
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(people, visitor);
		verify(visitor).visit(eq(bean(bob).propertyNamed("firstname")), eq(bob), eq(new BeanPropertyPath("array[0].firstname")), aStackOf(bob));
		verify(visitor).visit(eq(bean(bob).propertyNamed("surname")), eq(bob), eq(new BeanPropertyPath("array[0].surname")), aStackOf(bob));
		verify(visitor).visit(eq(bean(bob).propertyNamed("siblings")), eq(bob), eq(new BeanPropertyPath("array[0].siblings")), aStackOf(bob));
		verify(visitor).visit(eq(bean(tina).propertyNamed("firstname")), eq(tina), eq(new BeanPropertyPath("array[1].firstname")), aStackOf(tina));
		verify(visitor).visit(eq(bean(tina).propertyNamed("surname")), eq(tina), eq(new BeanPropertyPath("array[1].surname")), aStackOf(tina));
		verify(visitor).visit(eq(bean(tina).propertyNamed("siblings")), eq(tina), eq(new BeanPropertyPath("array[1].siblings")), aStackOf(tina));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectACollection() {
		Person bob = aRandomInstanceOf(Person.class).with("surname", "Onion").build();
		Person tina = aRandomInstanceOf(Person.class).with("surname", "Melon").build();
		List<Person> people = Arrays.asList(bob, tina);
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(people, visitor);
		verify(visitor).visit(eq(bean(bob).propertyNamed("firstname")), eq(bob), eq(new BeanPropertyPath("collection[0].firstname")), aStackOf(bob));
		verify(visitor).visit(eq(bean(bob).propertyNamed("surname")), eq(bob), eq(new BeanPropertyPath("collection[0].surname")), aStackOf(bob));
		verify(visitor).visit(eq(bean(bob).propertyNamed("siblings")), eq(bob), eq(new BeanPropertyPath("collection[0].siblings")), aStackOf(bob));
		verify(visitor).visit(eq(bean(tina).propertyNamed("firstname")), eq(tina), eq(new BeanPropertyPath("collection[1].firstname")), aStackOf(tina));
		verify(visitor).visit(eq(bean(tina).propertyNamed("surname")), eq(tina), eq(new BeanPropertyPath("collection[1].surname")), aStackOf(tina));
		verify(visitor).visit(eq(bean(tina).propertyNamed("siblings")), eq(tina), eq(new BeanPropertyPath("collection[1].siblings")), aStackOf(tina));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasGetterWithArgs() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(new GetterWithArgs(), visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasSetterWithNoArgs() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(SetterWithNotArgs.class, visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasMismatchedTypes() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(new TypeMismatch(), visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasANameMismatch() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		beanInspector().inspect(new NameMismatch(), visitor);
		verifyNoMoreInteractions(visitor);
	}

	private Object[] aStackOf(final Object... instance) {
		return argThat(arrayContaining(instance));
	}

}
