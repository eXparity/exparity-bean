/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static uk.co.it.modular.beans.Type.type;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.NotABean;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
@SuppressWarnings("rawtypes")
public class TypeTest {

	@Test
	public void canGetAPropertyByName() {
		assertThat(type(Person.class).propertyNamed("firstname"), notNullValue());
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameIncorrectProperty() {
		type(Person.class).propertyNamed("missing");
	}

	@Test
	public void canGetAPropertyByNameShortForm() {
		assertThat(type(Person.class).get("firstname"), notNullValue());
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyByNameShortFormIncorrectProperty() {
		type(Person.class).get("missing");
	}

	@Test
	public void canGetAPropertyType() {
		assertThat(type(Person.class).propertyType("firstname"), equalTo((Class) String.class));
	}

	@Test(expected = BeanPropertyNotFoundException.class)
	public void canGetAPropertyTypeMissingProperty() {
		type(Person.class).propertyType("missing");
	}

	@Test
	public void canCheckIfPropertyExists() {
		assertThat(type(Person.class).hasProperty("firstname"), equalTo(true));
	}

	@Test
	public void canCheckIfPropertyExistsNotFound() {
		assertThat(type(Person.class).hasProperty("missing"), equalTo(false));
	}

	@Test
	public void canCheckPropertyType() {
		assertThat(type(Person.class).isPropertyType("firstname", String.class), equalTo(true));
	}

	@Test
	public void canCheckPropertyTypeDifferent() {
		assertThat(type(Person.class).isPropertyType("firstname", Integer.class), equalTo(false));
	}

	@Test(expected = BeanPropertyException.class)
	public void canCheckPropertyTypeMissingProperty() {
		type(Person.class).isPropertyType("missing", String.class);
	}

	@Test
	public void canVisitAType() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		Type type = type(Person.class);
		type.visit(visitor);
		Mockito.verify(visitor).visit(type.propertyNamed("firstname"));
		Mockito.verify(visitor).visit(type.propertyNamed("surname"));
		Mockito.verify(visitor).visit(type.propertyNamed("siblings"));
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitATypeNotABean() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		type(NotABean.class).visit(visitor);
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canGetAListOfProperties() {
		Type type = type(Person.class);
		List<BeanProperty> properties = type.propertyList();
		assertThat(properties, hasSize(3));
		assertThat(properties, hasItem(equalTo(type.propertyNamed("firstname"))));
		assertThat(properties, hasItem(equalTo(type.propertyNamed("surname"))));
		assertThat(properties, hasItem(equalTo(type.propertyNamed("siblings"))));
	}

	@Test
	public void canGetAListOfPropertiesNotABean() {
		assertThat(type(NotABean.class).propertyList().size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfProperties() {
		Type type = type(Person.class);
		Map<String, BeanProperty> properties = type.propertyMap();
		assertThat(properties.size(), equalTo(3));
		assertThat(properties, hasEntry("firstname", type.propertyNamed("firstname")));
		assertThat(properties, hasEntry("surname", type.propertyNamed("surname")));
		assertThat(properties, hasEntry("siblings", type.propertyNamed("siblings")));
	}

	@Test
	public void canGetAMapOfPropertiesNotABean() {
		assertThat(type(NotABean.class).propertyMap().size(), equalTo(0));
	}

}
