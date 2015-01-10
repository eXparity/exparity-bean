package org.exparity.beans;

import static org.exparity.beans.Bean.bean;
import static org.exparity.beans.BeanPredicates.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;
import org.exparity.beans.testutils.types.Car;
import org.exparity.beans.testutils.types.Person;
import org.junit.Test;

/**
 * @author Stewart Bissett
 */
public class BeanPredicatesTest {

	@Test
	public void canMatchAll() {
		assertMatch(matchesAll(named("firstname"), ofType(String.class)), true);
	}

	@Test
	public void canMatchAllWhichIsDifferent() {
		assertMatch(matchesAll(named("firstname"), ofType(Integer.class)), false);
	}

	@Test
	public void canMatchAny() {
		assertMatch(matchesOneOf(named("firstname"), ofType(Integer.class)), true);
	}

	@Test
	public void canMatchAnyWhichIsDifferent() {
		assertMatch(matchesOneOf(named("lastname"), ofType(Integer.class)), false);
	}

	@Test
	public void canAnyProperty() {
		assertMatch(anyProperty(), true);
	}

	@Test
	public void canMatchAName() {
		assertMatch(named("firstname"), true);
	}

	@Test
	public void canMatchANameWhichIsDifferent() {
		assertMatch(named("lastname"), false);
	}

	@Test
	public void canMatchAValue() {
		assertMatch(hasValue("Bob"), true);
	}

	@Test
	public void canMatchAValueWhichIsDifferent() {
		assertMatch(hasValue("Tina"), false);
	}

	@Test
	public void canMatchANameAndValue() {
		assertMatch(hasPropertyValue("firstname", "Bob"), true);
	}

	@Test
	public void canMatchANameAndValueWhichIsDifferent() {
		assertMatch(hasPropertyValue("firstname", "Tina"), false);
	}

	@Test
	public void canMatchAType() {
		assertMatch(ofType(String.class), true);
	}

	@Test
	public void canMatchATypeWhichIsDifferent() {
		assertMatch(ofType(Integer.class), false);
	}

	@Test
	public void canMatchWithDeclaringType() {
		assertMatch(ofDeclaringType(Person.class), true);
	}

	@Test
	public void canMatchWithDeclaringTypeWhichIsDifferent() {
		assertMatch(ofDeclaringType(Car.class), false);
	}

	@Test
	public void canMatchWithNameAndType() {
		assertMatch(named("firstname", String.class), true);
	}

	@Test
	public void canMatchWithNameAndTypeWrongType() {
		assertMatch(named("firstname", Integer.class), false);
	}

	@Test
	public void canMatchWithNameAndTypeWrongName() {
		assertMatch(named("lastname", String.class), false);
	}

	@Test
	public void canMatchPattern() {
		assertMatch(BeanPredicates.matchesPattern("firstname", "B.*"), true);
	}

	@Test
	public void canFailToMatchPattern() {
		assertMatch(BeanPredicates.matchesPattern("firstname", "St.*"), false);
	}

	private void assertMatch(final BeanPropertyPredicate predicate, final boolean expected) {
		Person person = new Person();
		person.setFirstname("Bob");
		BeanProperty firstName = bean(person).get("firstname");
		assertThat(predicate.matches(firstName), equalTo(expected));
	}

}
