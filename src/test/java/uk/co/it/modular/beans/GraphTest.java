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
import static uk.co.it.modular.beans.Graph.graph;
import static uk.co.it.modular.beans.BeanFunctions.setValue;
import static uk.co.it.modular.beans.BeanPredicates.named;
import static uk.co.it.modular.beans.BeanPredicates.ofType;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.NameMismatch;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;

/**
 * @author Stewart.Bissett
 */
@SuppressWarnings("rawtypes")
public class GraphTest {

	@Test
	public void canGetAPropertyByName() {
		assertThat(graph(new Person()).propertyNamed("firstname"), notNullValue());
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameIncorrectProperty() {
		graph(new Person()).propertyNamed("missing");
	}

	@Test
	public void canGetAPropertyValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(graph(instance).propertyValue("firstname"), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyTypesValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(graph(instance).propertyValue("firstname", String.class), equalTo("Tina"));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyValueByNameIncorrectProperty() {
		graph(new Person()).propertyValue("missing");
	}

	@Test
	public void canGetAPropertyValueByPredicate() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(graph(instance).propertyValue(named("firstname")), equalTo((Object) "Tina"));
	}

	@Test
	public void canGetAPropertyValueByPredicateNoMatch() {
		assertThat(graph(new Person()).propertyValue(named("missing")), nullValue());
	}

	@Test
	public void canGetAPropertyTypedValueByName() {
		Person instance = new Person();
		instance.setFirstname("Tina");
		assertThat(graph(instance).propertyValue(named("firstname"), String.class), equalTo("Tina"));
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
		assertThat(graph(new Person()).setProperty(named("firstname"), "Bob"), equalTo(true));
	}

	@Test
	public void canSetAPropertyByPredicateNoMatch() {
		assertThat(graph(new Person()).setProperty(named("missing"), "Bob"), equalTo(false));
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
		graph(instance).visit(visitor);
		verify(visitor).visit(eq(bean(instance).propertyNamed("firstname")), eq(instance), eq(new BeanPropertyPath("person.firstname")), any(Object[].class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("surname")), eq(instance), eq(new BeanPropertyPath("person.surname")), any(Object[].class));
		verify(visitor).visit(eq(bean(instance).propertyNamed("siblings")), eq(instance), eq(new BeanPropertyPath("person.siblings")), any(Object[].class));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitABeanWithNoProperties() {
		BeanVisitor visitor = Mockito.mock(BeanVisitor.class);
		graph(new NameMismatch()).visit(visitor);
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canGetAListOfProperties() {
		Person instance = new Person();
		List<BeanPropertyInstance> properties = graph(instance).propertyList();
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
		Map<String, BeanPropertyInstance> properties = graph(instance).propertyMap();
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
		graph(instance).apply(new BeanPropertyFunction() {

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
		assertThat(graph(instance).find(named("firstname")), hasItem(equalTo(bean(instance).propertyNamed("firstname"))));
	}

	@Test
	public void canNotFindAPropertyOnABean() {
		assertThat(graph(new Person()).find(named("missing")), hasSize(0));
	}

	@Test
	public void canApplyAFunctionToABeanWithPredicate() {
		Person instance = new Person();
		assertThat(instance.getFirstname(), not(equalTo("Applied")));
		assertThat(instance.getSurname(), not(equalTo("Applied")));
		graph(instance).apply(setValue("Applied"), ofType(String.class));
		assertThat(instance.getFirstname(), equalTo("Applied"));
		assertThat(instance.getSurname(), equalTo("Applied"));
	}
}
