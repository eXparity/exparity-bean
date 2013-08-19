/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.co.it.modular.beans.BeanUtils.*;
import static uk.co.it.modular.beans.Bean.bean;
import static uk.co.it.modular.beans.BeanFunctions.setValue;
import static uk.co.it.modular.beans.BeanPredicates.named;
import static uk.co.it.modular.beans.BeanPredicates.ofType;
import static uk.co.it.modular.beans.Type.type;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.NameMismatch;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;

/**
 * @author Stewart Bissett
 */
@SuppressWarnings("rawtypes")
public class BeanUtilsTest {

	@Test
	public void canGetAPropertyByName() {
		assertThat(propertyNamed(new Person(), "firstname"), notNullValue());
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameIncorrectProperty() {
		propertyNamed(new Person(), "missing");
	}

	@Test
	public void canGetAPropertyByNameOnType() {
		assertThat(propertyNamed(Person.class, "firstname"), equalTo(type(Person.class).get("firstname")));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameIncorrectPropertyOnType() {
		propertyNamed(Person.class, "missing");
	}

	@Test
	public void canGetAPropertyValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(propertyValue(instance, "firstname"), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyTypesValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(propertyValue(instance, "firstname", String.class), equalTo("Tina"));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyValueByNameIncorrectProperty() {
		propertyValue(new Person(), "missing");
	}

	@Test
	public void canGetAPropertyValueByPredicate() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(propertyValue(instance, named("firstname")), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyValueByPredicateNoMatch() {
		assertThat(propertyValue(new Person(), named("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyTypedValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(propertyValue(instance, named("firstname"), String.class), equalTo("Tina"));
	}

	@Test
	public void canGetAPropertyTypedValueByPredicateNoMatch() {
		assertThat(propertyValue(new Person(), named("missing"), String.class), nullValue());
	}

	@Test
	public void canSetAPropertyByName() {
		assertThat(setProperty(new Person(), "firstname", "Bob"), equalTo(true));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canSetAPropertyByNameIncorrectProperty() {
		setProperty(new Person(), "missing", "Bob");
	}

	@Test
	public void canSetAPropertyByPredicate() {
		assertThat(setProperty(new Person(), named("firstname"), "Bob"), equalTo(true));
	}

	@Test
	public void canSetAPropertyByPredicateNoMatch() {
		assertThat(setProperty(new Person(), named("missing"), "Bob"), equalTo(false));
	}

	@Test
	public void canGetAPropertyByNameShortForm() {
		assertThat(get(new Person(), "firstname"), notNullValue());
	}

	@Test
	public void canGetAPropertyByNameShortFormOnType() {
		assertThat(get(Person.class, "firstname"), equalTo(type(Person.class).get("firstname")));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameShortFormIncorrectProperty() {
		get(new Person(), "missing");
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameShortFormIncorrectPropertyOnType() {
		get(Person.class, "missing");
	}

	@Test
	public void canGetAPropertyByPredicate() {
		assertThat(BeanUtils.get(new Person(), named("firstname")), notNullValue());
	}

	@Test
	public void canGetAPropertyByPredicateNoMatch() {
		assertThat(BeanUtils.get(new Person(), named("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyType() {
		assertThat(propertyType(new Person(), "firstname"), equalTo((Class) String.class));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyTypeMissingProperty() {
		propertyType(new Person(), "missing");
	}

	@Test
	public void canGetAPropertyTypeOnType() {
		assertThat(propertyType(Person.class, "firstname"), equalTo((Class) String.class));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyTypeMissingPropertyOnType() {
		propertyType(Person.class, "missing");
	}

	@Test
	public void canGetAPropertyTypeByPredicate() {
		assertThat(propertyType(new Person(), named("firstname")), equalTo((Class) String.class));
	}

	@Test
	public void canGetAPropertyTypeByPredicateNoMatch() {
		assertThat(propertyType(new Person(), named("missing")), nullValue());
	}

	@Test
	public void canCheckIfPropertyExists() {
		assertThat(hasProperty(new Person(), "firstname"), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsNotFound() {
		assertThat(hasProperty(new Person(), "missing"), equalTo(false));
	}

	@Test
	public void canCheckIfPropertyExistsByPredicate() {
		assertThat(hasProperty(new Person(), named("firstname")), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsByPredicateNoMatch() {
		assertThat(hasProperty(new Person(), named("missing")), equalTo(false));
	}

	@Test
	public void canCheckIfPropertyExistsOnType() {
		assertThat(hasProperty(Person.class, "firstname"), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsNotFoundOnType() {
		assertThat(hasProperty(Person.class, "missing"), equalTo(false));
	}

	@Test
	public void canCheckPropertyType() {
		assertThat(isPropertyType(new Person(), "firstname", String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeDifferent() {
		assertThat(isPropertyType(new Person(), "firstname", Integer.class), equalTo(false));
	}

	@Test(expected = BeanPropertyException.class)
	public void canCheckPropertyTypeMissingProperty() {
		isPropertyType(new Person(), "missing", String.class);
	}

	@Test
	public void canCheckPropertyTypeByPredicate() {
		assertThat(isPropertyType(new Person(), named("firstname"), String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeByPredicateNoMatch() {
		assertThat(isPropertyType(new Person(), named("missing"), String.class), equalTo(false));
	}

	@Test
	public void canCheckPropertyTypeOnType() {
		assertThat(isPropertyType(Person.class, "firstname", String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeDifferentOnType() {
		assertThat(isPropertyType(Person.class, "firstname", Integer.class), equalTo(false));
	}

	@Test
	public void canVisitABean() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		Person instance = new Person();
		Bean bean = Bean.bean(instance);
		visit(instance, visitor);
		verify(visitor).visit(eq(bean.propertyNamed("firstname")), eq(instance), eq(new BeanPropertyPath("person.firstname")), any(Object[].class));
		verify(visitor).visit(eq(bean.propertyNamed("surname")), eq(instance), eq(new BeanPropertyPath("person.surname")), any(Object[].class));
		verify(visitor).visit(eq(bean.propertyNamed("siblings")), eq(instance), eq(new BeanPropertyPath("person.siblings")), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitABeanWithNoProperties() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		visit(new NameMismatch(), visitor);
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canGetAListOfProperties() {
		Person person = new Person();
		List<BeanProperty> properties = propertyList(person);
		Bean bean = bean(person);
		assertThat(properties, hasSize(3));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("firstname"))));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("surname"))));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("siblings"))));
	}

	@Test
	public void canGetAListOfPropertiesNotABean() {
		assertThat(propertyList(NameMismatch.class).size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfProperties() {
		Person instance = new Person();
		Map<String, BeanProperty> properties = propertyMap(instance);
		Bean bean = bean(instance);
		assertThat(properties.size(), equalTo(3));
		assertThat(properties, hasEntry("firstname", bean.propertyNamed("firstname")));
		assertThat(properties, hasEntry("surname", bean.propertyNamed("surname")));
		assertThat(properties, hasEntry("siblings", bean.propertyNamed("siblings")));
	}

	@Test
	public void canGetAMapOfPropertiesNotABean() {
		assertThat(propertyMap(new NameMismatch()).size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfPropertiesForType() {
		Map<String, TypeProperty> properties = propertyMap(Person.class);
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
		apply(instance, new BeanPropertyFunction() {

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
		assertThat(find(instance, named("firstname")), hasItem(equalTo(bean(instance).propertyNamed("firstname"))));
	}

	@Test
	public void canNotFindAPropertyOnABean() {
		assertThat(find(new Person(), named("missing")), hasSize(0));
	}

	@Test
	public void canFindFirstInstanceOfAPropertyOnABean() {
		Person instance = new Person();
		assertThat(findAny(instance, named("firstname")), equalTo(bean(instance).propertyNamed("firstname")));
	}

	@Test
	public void canNotFindFirstInstanceOfAPropertyOnABean() {
		assertThat(findAny(new Person(), named("missing")), nullValue());
	}

	@Test
	public void canApplyAFunctionToABeanWithPredicate() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		apply(instance, setValue("Applied"), ofType(String.class));
		assertThat(instance.getFirstname(), equalTo("Applied"));
		assertThat(instance.getSurname(), equalTo("Applied"));
	}
}
