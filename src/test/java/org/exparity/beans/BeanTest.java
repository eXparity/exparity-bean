
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
import org.exparity.beans.core.BeanPropertyPath;
import org.exparity.beans.core.BeanVisitor;
import org.exparity.beans.core.TypeProperty;
import org.exparity.beans.core.functions.SetValue;
import org.exparity.beans.testutils.types.AllTypes;
import org.exparity.beans.testutils.types.AllTypes.EnumValues;
import org.exparity.beans.testutils.types.Car;
import org.exparity.beans.testutils.types.Engine;
import org.exparity.beans.testutils.types.NameMismatch;
import org.exparity.beans.testutils.types.Person;
import org.exparity.beans.testutils.types.Wheel;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import static org.exparity.beans.Bean.bean;
import static org.exparity.beans.BeanPredicates.named;
import static org.exparity.beans.BeanPredicates.ofType;
import static org.exparity.beans.Type.type;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Stewart Bissett
 */
@SuppressWarnings("rawtypes")
public class BeanTest {

	@Test
	public void canGetAPropertyByName() {
		assertThat(bean(new Person()).propertyNamed("firstname"), notNullValue());
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameIncorrectProperty() {
		bean(new Person()).propertyNamed("missing");
	}

	@Test
	public void canGetAPropertyByNameOnType() {
		assertThat(type(Person.class).propertyNamed("firstname"), equalTo(type(Person.class).get("firstname")));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameIncorrectPropertyOnType() {
		type(Person.class).propertyNamed("missing");
	}

	@Test
	public void canGetAPropertyValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(bean(instance1).propertyValue("firstname"), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyTypesValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(bean(instance1).propertyValue("firstname", String.class), equalTo("Tina"));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyValueByNameIncorrectProperty() {
		bean(new Person()).propertyValue("missing");
	}

	@Test
	public void canGetAPropertyValueByPredicate() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(bean(instance1).propertyValue(named("firstname")), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyValueByPredicateNoMatch() {
		assertThat(bean(new Person()).propertyValue(named("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyTypedValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		final Object instance1 = instance;
		assertThat(bean(instance1).propertyValue(named("firstname"), String.class), equalTo("Tina"));
	}

	@Test
	public void canGetAPropertyTypedValueByPredicateNoMatch() {
		assertThat(bean(new Person()).propertyValue(named("missing"), String.class), nullValue());
	}

	@Test
	public void canSetAPropertyByName() {
		assertThat(bean(new Person()).setProperty("firstname", "Bob"), equalTo(true));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canSetAPropertyByNameIncorrectProperty() {
		bean(new Person()).setProperty("missing", "Bob");
	}

	@Test
	public void canSetAPropertyByPredicate() {
		Person instance = new Person();
		bean(instance).setProperty(named("firstname"), "Bob");
		assertThat(instance.getFirstname(), Matchers.equalTo("Bob"));
	}

	@Test
	public void canGetAPropertyByNameShortForm() {
		assertThat(bean(new Person()).get("firstname"), notNullValue());
	}

	@Test
	public void canGetAPropertyByNameShortFormOnType() {
		assertThat(type(Person.class).get("firstname"), equalTo(type(Person.class).get("firstname")));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameShortFormIncorrectProperty() {
		bean(new Person()).get("missing");
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameShortFormIncorrectPropertyOnType() {
		type(Person.class).get("missing");
	}

	@Test
	public void canGetAPropertyByPredicate() {
		assertThat(bean(new Person()).get(named("firstname")), notNullValue());
	}

	@Test
	public void canGetAPropertyByPredicateNoMatch() {
		assertThat(bean(new Person()).get(named("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyType() {
		assertThat(bean(new Person()).propertyType("firstname"), equalTo((Class) String.class));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyTypeMissingProperty() {
		bean(new Person()).propertyType("missing");
	}

	@Test
	public void canGetAPropertyTypeOnType() {
		assertThat(type(Person.class).propertyType("firstname"), equalTo((Class) String.class));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyTypeMissingPropertyOnType() {
		type(Person.class).propertyType("missing");
	}

	@Test
	public void canGetAPropertyTypeByPredicate() {
		assertThat(bean(new Person()).propertyType(named("firstname")), equalTo((Class) String.class));
	}

	@Test
	public void canGetAPropertyTypeByPredicateNoMatch() {
		assertThat(bean(new Person()).propertyType(named("missing")), nullValue());
	}

	@Test
	public void canCheckIfPropertyExists() {
		assertThat(bean(new Person()).hasProperty("firstname"), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsNotFound() {
		assertThat(bean(new Person()).hasProperty("missing"), equalTo(false));
	}

	@Test
	public void canCheckIfPropertyExistsByPredicate() {
		assertThat(bean(new Person()).hasProperty(named("firstname")), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsByPredicateNoMatch() {
		assertThat(bean(new Person()).hasProperty(named("missing")), equalTo(false));
	}

	@Test
	public void canCheckIfPropertyExistsOnType() {
		assertThat(type(Person.class).hasProperty("firstname"), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsNotFoundOnType() {
		assertThat(type(Person.class).hasProperty("missing"), equalTo(false));
	}

	@Test
	public void canCheckPropertyType() {
		assertThat(bean(new Person()).isPropertyType("firstname", String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeDifferent() {
		assertThat(bean(new Person()).isPropertyType("firstname", Integer.class), equalTo(false));
	}

	@Test(expected = BeanPropertyException.class)
	public void canCheckPropertyTypeMissingProperty() {
		bean(new Person()).isPropertyType("missing", String.class);
	}

	@Test
	public void canCheckPropertyTypeByPredicate() {
		assertThat(bean(new Person()).isPropertyType(named("firstname"), String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeByPredicateNoMatch() {
		assertThat(bean(new Person()).isPropertyType(named("missing"), String.class), equalTo(false));
	}

	@Test
	public void canCheckPropertyTypeOnType() {
		assertThat(type(Person.class).isPropertyType("firstname", String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeDifferentOnType() {
		assertThat(type(Person.class).isPropertyType("firstname", Integer.class), equalTo(false));
	}

	@Test
	public void canVisitABean() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		Person instance = new Person();
		Bean bean = Bean.bean(instance);
		bean(instance).visit(visitor);
		verify(visitor).visit(eq(bean.propertyNamed("firstname")), eq(instance), eq(new BeanPropertyPath("person.firstname")), any(Object[].class), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean.propertyNamed("surname")), eq(instance), eq(new BeanPropertyPath("person.surname")), any(Object[].class), any(AtomicBoolean.class));
		verify(visitor).visit(eq(bean.propertyNamed("siblings")), eq(instance), eq(new BeanPropertyPath("person.siblings")), any(Object[].class), any(AtomicBoolean.class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitABeanWithNoProperties() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		final BeanVisitor visitor1 = visitor;
		bean(new NameMismatch()).visit(visitor1);
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canGetAListOfProperties() {
		Person person = new Person();
		final Object instance = person;
		List<BeanProperty> properties = bean(instance).propertyList();
		Bean bean = bean(person);
		assertThat(properties, hasSize(3));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("firstname"))));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("surname"))));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("siblings"))));
	}

	@Test
	public void canGetAListOfPropertiesNotABean() {
		assertThat(type(NameMismatch.class).propertyList().size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfProperties() {
		Person instance = new Person();
		final Object instance1 = instance;
		Map<String, BeanProperty> properties = bean(instance1).propertyMap();
		Bean bean = bean(instance);
		assertThat(properties.size(), equalTo(3));
		assertThat(properties, hasEntry("firstname", bean.propertyNamed("firstname")));
		assertThat(properties, hasEntry("surname", bean.propertyNamed("surname")));
		assertThat(properties, hasEntry("siblings", bean.propertyNamed("siblings")));
	}

	@Test
	public void canGetAMapOfPropertiesNotABean() {
		assertThat(bean(new NameMismatch()).propertyMap().size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfPropertiesForType() {
		Map<String, TypeProperty> properties = type(Person.class).propertyMap();
		Type bean = Type.type(Person.class);
		assertThat(properties.size(), equalTo(3));
		assertThat(properties, hasEntry("firstname", bean.propertyNamed("firstname")));
		assertThat(properties, hasEntry("surname", bean.propertyNamed("surname")));
		assertThat(properties, hasEntry("siblings", bean.propertyNamed("siblings")));
	}

	@Test
	public void canApplyAFunctionToABean() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		final Object instance1 = instance;
		bean(instance1).apply(new BeanPropertyFunction() {

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
		assertThat(bean(instance1).find(named("firstname")), hasItem(equalTo(bean(instance).propertyNamed("firstname"))));
	}

	@Test
	public void canNotFindAPropertyOnABean() {
		assertThat(bean(new Person()).find(named("missing")), hasSize(0));
	}

	@Test
	public void canFindFirstInstanceOfAPropertyOnABean() {
		Person instance = new Person();
		final Object instance1 = instance;
		assertThat(bean(instance1).findAny(named("firstname")), equalTo(bean(instance).propertyNamed("firstname")));
	}

	@Test
	public void canNotFindFirstInstanceOfAPropertyOnABean() {
		assertThat(bean(new Person()).findAny(named("missing")), nullValue());
	}

	@Test
	public void canApplyAFunctionToABeanWithPredicate() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		final Object instance1 = instance;
		bean(instance1).apply(new SetValue("Applied"), ofType(String.class));
		assertThat(instance.getFirstname(), equalTo("Applied"));
		assertThat(instance.getSurname(), equalTo("Applied"));
	}

	@Test
	public void canGetNameInCamelCase() {
		assertThat(bean(new AllTypes()).camelName(), equalTo("allTypes"));
	}

	@Test
	public void canGetSimpleName() {
		assertThat(bean(new AllTypes()).simpleName(), equalTo("AllTypes"));
	}

	@Test
	public void canGetCanonicalName() {
		assertThat(bean(new AllTypes()).canonicalName(), equalTo("org.exparity.beans.testutils.types.AllTypes"));
	}

	@Test
	public void canGetPackageName() {
		assertThat(bean(new AllTypes()).packageName(), equalTo("org.exparity.beans.testutils.types"));
	}

	@Test
	public void canGetType() {
		assertThat(bean(new AllTypes()).getType(), Matchers.<Class> equalTo(AllTypes.class));
	}

	@Test
	public void canCheckType() {
		assertThat(bean(new AllTypes()).is(AllTypes.class), equalTo(true));
	}

	@Test
	public void canCheckIsNotType() {
		assertThat(bean(new AllTypes()).is(String.class), equalTo(false));
	}

	@Test
	public void canCheckIsEnum() {
		assertThat(bean(EnumValues.VALUE_1).isEnum(), equalTo(true));
	}

	@Test
	public void canCheckIsNotEnum() {
		assertThat(bean(new AllTypes()).isEnum(), equalTo(false));
	}

	@Test
	public void canCheckIsNotPrimitiveEvenWithPrimitive() {
		assertThat(bean(1L).isPrimitive(), equalTo(false));
	}

	@Test
	public void canCheckIsNotPrimitive() {
		assertThat(bean("AString").isPrimitive(), equalTo(false));
	}

	@Test
	public void canDumpPropertiesToStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		Engine engine = new Engine(new BigDecimal("1.1"));
		Wheel wheel1 = new Wheel(5), wheel2 = new Wheel(6);
		Car car = new Car(engine, Arrays.asList(wheel1, wheel2));
		bean(car).dump(buffer);
		assertThat(buffer.toString(), equalTo("car.engine='Engine [1.1]'\r\ncar.wheels='[Wheel [5], Wheel [6]]'\r\n"));
	}
}
