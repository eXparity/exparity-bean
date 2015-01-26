
package org.exparity.beans;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyException;
import org.exparity.beans.core.BeanPropertyFunction;
import org.exparity.beans.core.BeanPropertyNotFoundException;
import org.exparity.beans.core.BeanVisitor;
import org.exparity.beans.core.functions.SetValue;
import org.exparity.beans.core.naming.CapitalizedNamingStrategy;
import org.exparity.beans.testutils.types.Car;
import org.exparity.beans.testutils.types.Engine;
import org.exparity.beans.testutils.types.NameMismatch;
import org.exparity.beans.testutils.types.Person;
import org.exparity.beans.testutils.types.Thrower;
import org.exparity.beans.testutils.types.Wheel;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import static org.exparity.beans.Bean.bean;
import static org.exparity.beans.BeanPredicates.named;
import static org.exparity.beans.BeanPredicates.ofType;
import static org.exparity.beans.Graph.graph;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Stewart.Bissett
 */
@SuppressWarnings("rawtypes")
public class GraphTest {

	@Test
	public void canGetAPropertyByName() {
		BeanProperty property = graph(new Person()).propertyNamed("firstname");
		assertThat(property, notNullValue());
		assertThat(property.getName(), equalTo("firstname"));
		assertThat(property.getPath().fullPath(), equalTo("person.firstname"));
	}

	@Test
	public void canGetAGraphPropertyByName() {
		Car person = new Car(new Engine(new BigDecimal("3.8")), Arrays.asList(new Wheel(1)));
		BeanProperty property = graph(person).propertyNamed("diameter");
		assertThat(property, notNullValue());
		assertThat(property.getName(), equalTo("diameter"));
		assertThat(property.getPath().fullPath(), equalTo("car.wheels[0].diameter"));
		assertThat(property.getPath().fullPathWithNoIndexes(), equalTo("car.wheels.diameter"));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameIncorrectProperty() {
		graph(new Person()).propertyNamed("missing");
	}

	@Test
	public void canGetAPropertyByPath() {
		BeanProperty property = graph(new Person()).propertyAtPath("person.firstname");
		assertThat(property, notNullValue());
		assertThat(property.getName(), equalTo("firstname"));
		assertThat(property.getPath().fullPath(), equalTo("person.firstname"));
	}

	@Test
	public void canGetAGraphPropertyByPath() {
		Car person = new Car(new Engine(new BigDecimal("3.8")), Arrays.asList(new Wheel(1)));
		BeanProperty property = graph(person).propertyAtPath("car.wheels[0].diameter");
		assertThat(property, notNullValue());
		assertThat(property.getName(), equalTo("diameter"));
		assertThat(property.getPath().fullPath(), equalTo("car.wheels[0].diameter"));
		assertThat(property.getPath().fullPathWithNoIndexes(), equalTo("car.wheels.diameter"));
	}

	@Test
	public void canGetAGraphPropertyByPathIgnoreOrdinal() {
		Car person = new Car(new Engine(new BigDecimal("3.8")), Arrays.asList(new Wheel(1)));
		assertThat(graph(person).propertyAtPath("car.wheels.diameter"), nullValue());
		BeanProperty property = graph(person).propertyAtPathIgnoreOrdinal("car.wheels.diameter");
		assertThat(property, notNullValue());
		assertThat(property.getName(), equalTo("diameter"));
		assertThat(property.getPath().fullPath(), equalTo("car.wheels[0].diameter"));
		assertThat(property.getPath().fullPathWithNoIndexes(), equalTo("car.wheels.diameter"));
	}

	@Test
	public void canGetAPropertyValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(graph(instance1).propertyValue("firstname"), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyTypesValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(graph(instance1).propertyValue("firstname", String.class), equalTo("Tina"));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyValueByNameIncorrectProperty() {
		graph(new Person()).propertyValue("missing");
	}

	@Test
	public void canGetAPropertyValueByPredicate() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(graph(instance1).propertyValue(named("firstname")), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyValueByPredicateNoMatch() {
		assertThat(graph(new Person()).propertyValue(named("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyTypedValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(graph(instance1).propertyValue(named("firstname"), String.class), equalTo("Tina"));
	}

	@Test
	public void canGetAPropertyTypedValueByPredicateNoMatch() {
		assertThat(graph(new Person()).propertyValue(named("missing"), String.class), nullValue());
	}

	@Test
	public void canSetAPropertyByName() {
		assertThat(graph(new Person()).setProperty("firstname", "Bob"), equalTo(true));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canSetAPropertyByNameIncorrectProperty() {
		graph(new Person()).setProperty("missing", "Bob");
	}

	@Test
	public void canSetAPropertyByPredicate() {
		Person instance = new Person();
		graph(instance).setProperty(named("firstname"), "Bob");
		assertThat(instance.getFirstname(), Matchers.equalTo("Bob"));
	}

	@Test
	public void canGetAPropertyByNameShortForm() {
		assertThat(graph(new Person()).get("firstname"), notNullValue());
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameShortFormIncorrectProperty() {
		graph(new Person()).get("missing");
	}

	@Test
	public void canGetAPropertyByPredicate() {
		assertThat(graph(new Person()).get(named("firstname")), notNullValue());
	}

	@Test
	public void canGetAPropertyByPredicateNoMatch() {
		assertThat(graph(new Person()).get(named("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyType() {
		assertThat(graph(new Person()).propertyType("firstname"), equalTo((Class) String.class));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyTypeMissingProperty() {
		graph(new Person()).propertyType("missing");
	}

	@Test
	public void canGetAPropertyTypeByPredicate() {
		assertThat(graph(new Person()).propertyType(named("firstname")), equalTo((Class) String.class));
	}

	@Test
	public void canGetAPropertyTypeByPredicateNoMatch() {
		assertThat(graph(new Person()).propertyType(named("missing")), nullValue());
	}

	@Test
	public void canCheckIfPropertyExists() {
		assertThat(graph(new Person()).hasProperty("firstname"), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsNotFound() {
		assertThat(graph(new Person()).hasProperty("missing"), equalTo(false));
	}

	@Test
	public void canCheckIfPropertyExistsByPredicate() {
		assertThat(graph(new Person()).hasProperty(named("firstname")), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsByPredicateNoMatch() {
		assertThat(graph(new Person()).hasProperty(named("missing")), equalTo(false));
	}

	@Test
	public void canCheckIfPropertyExistsWithValue() {
		assertThat(graph(new Person("Bob", "Onion")).hasProperty("firstname", "Bob"), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsWithValueNoMatch() {
		assertThat(graph(new Person("Bob", "Onion")).hasProperty("firstname", "Tina"), equalTo(false));
	}

	@Test
	public void canCheckPropertyType() {
		assertThat(graph(new Person()).isPropertyType("firstname", String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeDifferent() {
		assertThat(graph(new Person()).isPropertyType("firstname", Integer.class), equalTo(false));
	}

	@Test(expected = BeanPropertyException.class)
	public void canCheckPropertyTypeMissingProperty() {
		graph(new Person()).isPropertyType("missing", String.class);
	}

	@Test
	public void canCheckPropertyTypeByPredicate() {
		assertThat(graph(new Person()).isPropertyType(named("firstname"), String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeByPredicateNoMatch() {
		assertThat(graph(new Person()).isPropertyType(named("missing"), String.class), equalTo(false));
	}

	@Test
	public void canVisitABean() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		Person instance = new Person();
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		graph(instance1).visit(visitor1);
		verify(visitor).visit(eq(bean(instance).propertyNamed("firstname")),
				eq(instance),
				any(Object[].class),
				any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("surname")), eq(instance), any(Object[].class), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("siblings")),
				eq(instance),
				any(Object[].class),
				any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitABeanWithNoProperties() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final BeanVisitor visitor1 = visitor;
		graph(new NameMismatch()).visit(visitor1);
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitABeanWhichThrowsAnException() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		Thrower instance = new Thrower();
		final Object instance1 = instance;
		final BeanVisitor visitor1 = visitor;
		graph(instance1).visit(visitor1);
		verify(visitor).visit(eq(bean(instance).propertyNamed("property")),
				eq(instance),
				any(Object[].class),
				any(AtomicBoolean.class));
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canGetAListOfProperties() {
		Person instance = new Person();
		final Object instance1 = instance;
		List<BeanProperty> properties = graph(instance1).propertyList();
		assertThat(properties, hasSize(3));
		assertThat(properties, hasItem(equalTo(bean(instance).propertyNamed("firstname"))));
		assertThat(properties, hasItem(equalTo(bean(instance).propertyNamed("surname"))));
		assertThat(properties, hasItem(equalTo(bean(instance).propertyNamed("siblings"))));
	}

	@Test
	public void canGetAListOfPropertiesNotABean() {
		assertThat(graph(NameMismatch.class).propertyList().size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfProperties() {
		Person instance = new Person();
		final Object instance1 = instance;
		Map<String, BeanProperty> properties = graph(instance1).propertyMap();
		assertThat(properties.size(), equalTo(3));
		assertThat(properties, hasEntry("firstname", bean(instance).propertyNamed("firstname")));
		assertThat(properties, hasEntry("surname", bean(instance).propertyNamed("surname")));
		assertThat(properties, hasEntry("siblings", bean(instance).propertyNamed("siblings")));
	}

	@Test
	public void canGetAMapOfPropertiesNotABean() {
		assertThat(graph(new NameMismatch()).propertyMap().size(), equalTo(0));
	}

	@Test
	public void canApplyAFunctionToABean() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		final Object instance1 = instance;
		graph(instance1).apply(new BeanPropertyFunction() {

			public void apply(final BeanProperty property) {
				if (property.isType(String.class)) {
					property.setValue("Applied");
				}
			}
		});
		assertThat(instance.getFirstname(), equalTo("Applied"));
		assertThat(instance.getSurname(), equalTo("Applied"));
	}

	@Test
	public void canFindAPropertyOnABean() {
		Person instance = new Person();
		final Object instance1 = instance;
		assertThat(graph(instance1).find(named("firstname")), hasItem(equalTo(bean(instance).propertyNamed("firstname"))));
	}

	@Test
	public void canNotFindAPropertyOnABean() {
		assertThat(graph(new Person()).find(named("missing")), hasSize(0));
	}

	@Test
	public void canFindTheFirstPropertyOnABean() {
		Person instance = new Person();
		final Object instance1 = instance;
		assertThat(graph(instance1).findAny(named("firstname")), equalTo(bean(instance).propertyNamed("firstname")));
	}

	@Test
	public void canNotFindTheFirstPropertyOnABean() {
		assertThat(graph(new Person()).findAny(named("missing")), nullValue());
	}

	@Test
	public void canApplyAFunctionToABeanWithPredicate() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		final Object instance1 = instance;
		graph(instance1).apply(new SetValue("Applied"), ofType(String.class));
		assertThat(instance.getFirstname(), equalTo("Applied"));
		assertThat(instance.getSurname(), equalTo("Applied"));
	}

	@Test
	public void canOverrideNaming() {
		Car person = new Car(new Engine(new BigDecimal("3.8")), Arrays.asList(new Wheel(1)));
		BeanProperty property = graph(person, new CapitalizedNamingStrategy()).propertyNamed("diameter");
		assertThat(property, notNullValue());
		assertThat(property.getName(), equalTo("Diameter"));
		assertThat(property.getPath().fullPath(), equalTo("Car.Wheels[0].Diameter"));
		assertThat(property.getPath().fullPathWithNoIndexes(), equalTo("Car.Wheels.Diameter"));
	}

}
