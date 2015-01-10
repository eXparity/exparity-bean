package org.exparity.beans.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.core.naming.CamelCaseNamingStrategy;
import org.exparity.beans.testutils.BeanUtilTestFixture;
import org.exparity.beans.testutils.types.AllTypes;
import org.exparity.beans.testutils.types.Car;
import org.exparity.beans.testutils.types.Engine;
import org.exparity.beans.testutils.types.GetterWithArgs;
import org.exparity.beans.testutils.types.NameMismatch;
import org.exparity.beans.testutils.types.OverloadedSetter;
import org.exparity.beans.testutils.types.Person;
import org.exparity.beans.testutils.types.SetterWithNoArgs;
import org.exparity.beans.testutils.types.TypeMismatch;
import org.exparity.beans.testutils.types.Wheel;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.Mockito;
import static java.util.Arrays.asList;
import static org.exparity.beans.Bean.bean;
import static org.exparity.beans.core.InstanceInspector.beanInspector;
import static org.exparity.beans.core.InstanceInspector.graphInspector;
import static org.exparity.beans.testutils.BeanUtilTestFixture.aPopulatedCar;
import static org.exparity.beans.testutils.BeanUtilTestFixture.aPopulatedPerson;
import static org.hamcrest.Matchers.arrayContaining;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Stewart Bissett
 */
public class InstanceInspectorTest {

	@Test
	public void canInspectABean() {
		Person instance = BeanUtilTestFixture.aPopulatedPerson();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance1, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(instance).propertyNamed("firstname")),
				eq(instance),
				eq(new BeanPropertyPath("person.firstname")),
				aStackOf(instance),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("surname")), eq(instance), eq(new BeanPropertyPath("person.surname")), aStackOf(instance), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("siblings")), eq(instance), eq(new BeanPropertyPath("person.siblings")), aStackOf(instance), any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAnEmptyBean() {
		Person instance = new Person();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance1, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(instance).propertyNamed("firstname")),
				eq(instance),
				eq(new BeanPropertyPath("person.firstname")),
				aStackOf(instance),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("surname")), eq(instance), eq(new BeanPropertyPath("person.surname")), aStackOf(instance), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("siblings")), eq(instance), eq(new BeanPropertyPath("person.siblings")), aStackOf(instance), any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectANull() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(null, new CamelCaseNamingStrategy(), visitor1);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAGraph() {
		Car car = aPopulatedCar();
		Engine engine = car.getEngine();
		List<Wheel> wheels = car.getWheels();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance = car;
		final BeanVisitor visitor1 = visitor;
		graphInspector().inspect(instance, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(car).get("engine")), eq(car), eq(new BeanPropertyPath("car.engine")), aStackOf(car), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(engine).get("capacity")), eq(engine), eq(new BeanPropertyPath("car.engine.capacity")), aStackOf(car, engine), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(car).get("wheels")), eq(car), eq(new BeanPropertyPath("car.wheels")), aStackOf(car), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(wheels.get(0)).get("diameter")),
				eq(wheels.get(0)),
				eq(new BeanPropertyPath("car.wheels[0].diameter")),
				aStackOf(car, wheels.get(0)),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(wheels.get(1)).get("diameter")),
				eq(wheels.get(1)),
				eq(new BeanPropertyPath("car.wheels[1].diameter")),
				aStackOf(car, wheels.get(1)),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(wheels.get(2)).get("diameter")),
				eq(wheels.get(2)),
				eq(new BeanPropertyPath("car.wheels[2].diameter")),
				aStackOf(car, wheels.get(2)),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(wheels.get(3)).get("diameter")),
				eq(wheels.get(3)),
				eq(new BeanPropertyPath("car.wheels[3].diameter")),
				aStackOf(car, wheels.get(3)),
				any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAGraphAndNotOverflow() {
		Person brother = aPopulatedPerson(), sister = aPopulatedPerson();
		brother.setSiblings(asList(sister));
		sister.setSiblings(asList(brother));
		BeanVisitor visitor = mock(BeanVisitor.class);
		final Object instance = brother;
		final BeanVisitor visitor1 = visitor;
		graphInspector().inspect(instance, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(brother).get("firstname")), eq(brother), eq(new BeanPropertyPath("person.firstname")), aStackOf(brother), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(brother).get("surname")), eq(brother), eq(new BeanPropertyPath("person.surname")), aStackOf(brother), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(brother).get("siblings")), eq(brother), eq(new BeanPropertyPath("person.siblings")), aStackOf(brother), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(sister).get("firstname")),
				eq(sister),
				eq(new BeanPropertyPath("person.siblings[0].firstname")),
				aStackOf(brother, sister),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(sister).get("surname")),
				eq(sister),
				eq(new BeanPropertyPath("person.siblings[0].surname")),
				aStackOf(brother, sister),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(sister).get("siblings")),
				eq(sister),
				eq(new BeanPropertyPath("person.siblings[0].siblings")),
				aStackOf(brother, sister),
				any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWithOverloadedSetter() {
		OverloadedSetter instance = new OverloadedSetter();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance1, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(instance).propertyNamed("property")),
				eq(instance),
				eq(new BeanPropertyPath("overloadedSetter.property")),
				argThat(arrayContaining(((Object) instance))), any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAllTypes() {
		AllTypes instance = BeanUtilTestFixture.aPopulatedAllTypes();
		final Object instance1 = instance;
		beanInspector().inspect(instance1, new CamelCaseNamingStrategy(), new BeanVisitor() {
		
			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack, final AtomicBoolean stop) {
				MatcherAssert.assertThat(property.getValue(), org.hamcrest.Matchers.notNullValue());
			}
		});
	}

	@Test
	public void canInspectAllTypesAreEmpty() {
		AllTypes instance = BeanUtilTestFixture.anEmptyAllTypes();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance1, new CamelCaseNamingStrategy(), visitor1);
		final Object instance2 = instance;
		beanInspector().inspect(instance2, new CamelCaseNamingStrategy(), new BeanVisitor() {
		
			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack, final AtomicBoolean stop) {
				if (property.isCollection() || property.isMap() || property.isArray()) {
					MatcherAssert.assertThat(property.getValue(), org.hamcrest.Matchers.notNullValue());
				}
			}
		});
	}

	@Test
	public void canInspectAllTypesAreNull() {
		AllTypes instance = new AllTypes();
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance1, new CamelCaseNamingStrategy(), visitor1);
		final Object instance2 = instance;
		beanInspector().inspect(instance2, new CamelCaseNamingStrategy(), new BeanVisitor() {
		
			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack, final AtomicBoolean stop) {
				if (!property.isPrimitive()) {
					MatcherAssert.assertThat(property.getValue(), org.hamcrest.Matchers.nullValue());
				}
			}
		});
	}

	@Test
	public void canInspectAMap() {
		Person bob = aPopulatedPerson();
		bob.setSurname("Onion");
		Person tina = aPopulatedPerson();
		tina.setSurname("Melon");
		Map<String, Person> instance = new HashMap<String, Person>();
		instance.put("Bob", bob);
		instance.put("Tina", tina);
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance1, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(bob).propertyNamed("firstname")), eq(bob), eq(new BeanPropertyPath("map[Bob].firstname")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(bob).propertyNamed("surname")), eq(bob), eq(new BeanPropertyPath("map[Bob].surname")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(bob).propertyNamed("siblings")), eq(bob), eq(new BeanPropertyPath("map[Bob].siblings")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("firstname")), eq(tina), eq(new BeanPropertyPath("map[Tina].firstname")), aStackOf(tina), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("surname")), eq(tina), eq(new BeanPropertyPath("map[Tina].surname")), aStackOf(tina), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("siblings")), eq(tina), eq(new BeanPropertyPath("map[Tina].siblings")), aStackOf(tina), any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectAnArrray() {
		Person bob = BeanUtilTestFixture.aPopulatedPerson();
		bob.setSurname("Onion");
		Person tina = BeanUtilTestFixture.aPopulatedPerson();
		tina.setSurname("Melon");
		Person[] people = {
				bob, tina
		};
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance = people;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(bob).propertyNamed("firstname")), eq(bob), eq(new BeanPropertyPath("person[0].firstname")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(bob).propertyNamed("surname")), eq(bob), eq(new BeanPropertyPath("person[0].surname")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(bob).propertyNamed("siblings")), eq(bob), eq(new BeanPropertyPath("person[0].siblings")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("firstname")), eq(tina), eq(new BeanPropertyPath("person[1].firstname")), aStackOf(tina), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("surname")), eq(tina), eq(new BeanPropertyPath("person[1].surname")), aStackOf(tina), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("siblings")), eq(tina), eq(new BeanPropertyPath("person[1].siblings")), aStackOf(tina), any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectACollection() {
		Person bob = aPopulatedPerson();
		bob.setSurname("Onion");
		Person tina = aPopulatedPerson();
		tina.setSurname("Melon");
		List<Person> people = Arrays.asList(bob, tina);
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final Object instance = people;
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(instance, new CamelCaseNamingStrategy(), visitor1);
		verify(visitor).visit(eq(bean(bob).propertyNamed("firstname")), eq(bob), eq(new BeanPropertyPath("collection[0].firstname")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(bob).propertyNamed("surname")), eq(bob), eq(new BeanPropertyPath("collection[0].surname")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(bob).propertyNamed("siblings")), eq(bob), eq(new BeanPropertyPath("collection[0].siblings")), aStackOf(bob), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("firstname")), eq(tina), eq(new BeanPropertyPath("collection[1].firstname")), aStackOf(tina), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("surname")), eq(tina), eq(new BeanPropertyPath("collection[1].surname")), aStackOf(tina), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(tina).propertyNamed("siblings")), eq(tina), eq(new BeanPropertyPath("collection[1].siblings")), aStackOf(tina), any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasGetterWithArgs() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(new GetterWithArgs(), new CamelCaseNamingStrategy(), visitor1);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasSetterWithNoArgs() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(SetterWithNoArgs.class, new CamelCaseNamingStrategy(), visitor1);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasMismatchedTypes() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(new TypeMismatch(), new CamelCaseNamingStrategy(), visitor1);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectABeanWhichHasANameMismatch() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final BeanVisitor visitor1 = visitor;
		beanInspector().inspect(new NameMismatch(), new CamelCaseNamingStrategy(), visitor1);
		verifyNoMoreInteractions(visitor);
	}

	private Object[] aStackOf(final Object... instance) {
		return argThat(arrayContaining(instance));
	}

}
