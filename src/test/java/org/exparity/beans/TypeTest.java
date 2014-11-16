/*
 * Copyright (c) Modular IT Limited.
 */

package org.exparity.beans;

import java.util.List;
import java.util.Map;

import org.exparity.beans.BeanPropertyException;
import org.exparity.beans.BeanPropertyNotFoundException;
import org.exparity.beans.Type;
import org.exparity.beans.TypeProperty;
import org.exparity.beans.TypeVisitor;
import org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes;
import org.exparity.beans.testutils.BeanUtilTestFixture.Employee;
import org.exparity.beans.testutils.BeanUtilTestFixture.Manager;
import org.exparity.beans.testutils.BeanUtilTestFixture.NameMismatch;
import org.exparity.beans.testutils.BeanUtilTestFixture.Person;
import org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes.EnumValues;
import org.junit.Test;
import org.mockito.Mockito;
import static org.exparity.beans.Type.type;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Stewart Bissett
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
		verify(visitor).visit(type.propertyNamed("firstname"));
		verify(visitor).visit(type.propertyNamed("surname"));
		verify(visitor).visit(type.propertyNamed("siblings"));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canVisitATypeNotABean() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		type(NameMismatch.class).visit(visitor);
		Mockito.verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canGetAListOfProperties() {
		Type type = type(Person.class);
		List<TypeProperty> properties = type.propertyList();
		assertThat(properties, hasSize(3));
		assertThat(properties, hasItem(equalTo(type.propertyNamed("firstname"))));
		assertThat(properties, hasItem(equalTo(type.propertyNamed("surname"))));
		assertThat(properties, hasItem(equalTo(type.propertyNamed("siblings"))));
	}

	@Test
	public void canGetAListOfPropertiesNotABean() {
		assertThat(type(NameMismatch.class).propertyList().size(), equalTo(0));
	}

	@Test
	public void canGetAMapOfProperties() {
		Type type = type(Person.class);
		Map<String, TypeProperty> properties = type.propertyMap();
		assertThat(properties.size(), equalTo(3));
		assertThat(properties, hasEntry("firstname", type.propertyNamed("firstname")));
		assertThat(properties, hasEntry("surname", type.propertyNamed("surname")));
		assertThat(properties, hasEntry("siblings", type.propertyNamed("siblings")));
	}

	@Test
	public void canGetAMapOfPropertiesNotABean() {
		assertThat(type(NameMismatch.class).propertyMap().size(), equalTo(0));
	}

	@Test
	public void canGetASimpleNameForAType() {
		assertThat(type(AllTypes.class).simpleName(), equalTo("AllTypes"));
	}

	@Test
	public void canGetASimpleNameInCamelCaseForAType() {
		assertThat(type(AllTypes.class).camelName(), equalTo("allTypes"));
	}

	@Test
	public void canGetACanonicalNameForAType() {
		assertThat(type(AllTypes.class).canonicalName(), equalTo("org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes"));
	}

	@Test
	public void canGetAPackageNameForAType() {
		assertThat(type(AllTypes.class).packageName(), equalTo("org.exparity.beans.testutils"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowIllegalArgumentExceptionIfConstructedWithNull() {
		type(null);
	}

	@Test
	public void canInstantiateTypeFromAnInstance() {
		assertThat(type(new Person()).simpleName(), equalTo("Person"));
	}

	@Test
	public void canGetSuperTypesForSuperType() {
		Class<?>[] superTypes = type(Person.class).superTypes();
		assertThat(superTypes, arrayWithSize(0));
	}

	@Test
	public void canGetSuperTypesForSubclass() {
		Class<?>[] superTypes = type(Employee.class).superTypes();
		assertThat(superTypes, arrayContaining((Object) Person.class));
	}

	@Test
	public void canGetTypeHierachyForSubclass() {
		Class<?>[] superTypes = type(Employee.class).typeHierachy();
		assertThat(superTypes, arrayContaining((Object) Employee.class, (Object) Person.class));
	}

	@Test
	public void canTestIfTypeIsSame() {
		assertThat(type(Person.class).is(Person.class), equalTo(true));
	}

	@Test
	public void canTestIfTypeIsASubclass() {
		assertThat(type(Manager.class).is(Person.class), equalTo(true));
	}

	@Test
	public void canTestIfTypeIsASuperclass() {
		assertThat(type(Person.class).is(Manager.class), equalTo(false));
	}

	@Test
	public void canTestIfTypeIsDifferent() {
		assertThat(type(Person.class).is(AllTypes.class), equalTo(false));
	}

	@Test
	public void canTestIfTypeIsEnum() {
		assertThat(type(EnumValues.class).isEnum(), equalTo(true));
	}

	@Test
	public void canTestIfTypeIsNotEnum() {
		assertThat(type(Person.class).isEnum(), equalTo(false));
	}

	@Test
	public void canTestIfTypeIsPrimitive() {
		assertThat(type(int.class).isPrimitive(), equalTo(true));
	}

	@Test
	public void canTestIfTypeIsNotPrimitive() {
		assertThat(type(Integer.class).isPrimitive(), equalTo(false));
	}

	@Test
	public void canTestIfTypeIsArray() {
		assertThat(type(int[].class).isArray(), equalTo(true));
	}

	@Test
	public void canTestIfTypeIsNotArray() {
		assertThat(type(Integer.class).isArray(), equalTo(false));
	}

}
