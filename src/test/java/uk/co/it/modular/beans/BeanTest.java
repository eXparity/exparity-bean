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
import static uk.co.it.modular.beans.Bean.bean;
import static uk.co.it.modular.beans.BeanFunctions.setValue;
import static uk.co.it.modular.beans.BeanPredicates.withName;
import static uk.co.it.modular.beans.BeanPredicates.withType;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.NameMismatch;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
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
	public void canGetAPropertyValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(bean(instance).propertyValue("firstname"), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyTypesValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(bean(instance).propertyValue("firstname", String.class), equalTo("Tina"));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyValueByNameIncorrectProperty() {
		bean(new Person()).propertyValue("missing");
	}

	@Test
	public void canGetAPropertyValueByPredicate() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(bean(instance).propertyValue(withName("firstname")), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyValueByPredicateNoMatch() {
		assertThat(bean(new Person()).propertyValue(withName("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyTypedValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(bean(instance).propertyValue(withName("firstname"), String.class), equalTo("Tina"));
	}

	@Test
	public void canGetAPropertyTypedValueByPredicateNoMatch() {
		assertThat(bean(new Person()).propertyValue(withName("missing"), String.class), nullValue());
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
		assertThat(bean(new Person()).setProperty(withName("firstname"), "Bob"), equalTo(true));
	}

	@Test
	public void canSetAPropertyByPredicateNoMatch() {
		assertThat(bean(new Person()).setProperty(withName("missing"), "Bob"), equalTo(false));
	}

	@Test
	public void canGetAPropertyByNameShortForm() {
		assertThat(bean(new Person()).get("firstname"), notNullValue());
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameShortFormIncorrectProperty() {
		bean(new Person()).get("missing");
	}

	@Test
	public void canGetAPropertyByPredicate() {
		assertThat(bean(new Person()).get(withName("firstname")), notNullValue());
	}

	@Test
	public void canGetAPropertyByPredicateNoMatch() {
		assertThat(bean(new Person()).get(withName("missing")), nullValue());
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
	public void canGetAPropertyTypeByPredicate() {
		assertThat(bean(new Person()).propertyType(withName("firstname")), equalTo((Class) String.class));
	}

	@Test
	public void canGetAPropertyTypeByPredicateNoMatch() {
		assertThat(bean(new Person()).propertyType(withName("missing")), nullValue());
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
		assertThat(bean(new Person()).hasProperty(withName("firstname")), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsByPredicateNoMatch() {
		assertThat(bean(new Person()).hasProperty(withName("missing")), equalTo(false));
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
		assertThat(bean(new Person()).isPropertyType(withName("firstname"), String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeByPredicateNoMatch() {
		assertThat(bean(new Person()).isPropertyType(withName("missing"), String.class), equalTo(false));
	}

	@Test
	public void canVisitABean() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		Person instance = new Person();
		Bean bean = bean(instance);
		bean.visit(visitor);
		verify(visitor).visit(eq(bean.propertyNamed("firstname")), eq(instance), eq("person.firstname"), any(Object[].class));
		verify(visitor).visit(eq(bean.propertyNamed("surname")), eq(instance), eq("person.surname"), any(Object[].class));
		verify(visitor).visit(eq(bean.propertyNamed("siblings")), eq(instance), eq("person.siblings"), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitABeanWithNoProperties() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		bean(new NameMismatch()).visit(visitor);
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canGetAListOfProperties() {
		Bean bean = bean(new Person());
		List<BeanPropertyInstance> properties = bean.propertyList();
		assertThat(properties, hasSize(3));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("firstname"))));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("surname"))));
		assertThat(properties, hasItem(equalTo(bean.propertyNamed("siblings"))));
	}

	@Test
	public void canGetAListOfPropertiesNotABean() {
		assertThat(bean(NameMismatch.class).propertyList().size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfProperties() {
		Bean bean = bean(new Person());
		Map<String, BeanPropertyInstance> properties = bean.propertyMap();
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
	public void canApplyAFunctionToABean() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		bean(instance).apply(new BeanPropertyFunction() {

			public void apply(final BeanPropertyInstance property) {
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
		Bean bean = bean(instance);
		assertThat(bean.find(withName("firstname")), hasItem(equalTo(bean.propertyNamed("firstname"))));
	}

	@Test
	public void canNotFindAPropertyOnABean() {
		assertThat(bean(new Person()).find(withName("missing")), hasSize(0));
	}

	@Test
	public void canApplyAFunctionToABeanWithPredicate() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		bean(instance).apply(setValue("Applied"), withType(String.class));
		assertThat(instance.getFirstname(), equalTo("Applied"));
		assertThat(instance.getSurname(), equalTo("Applied"));
	}
}
