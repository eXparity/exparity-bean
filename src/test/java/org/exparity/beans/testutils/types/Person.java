
package org.exparity.beans.testutils.types;

import java.util.List;

public class Person {

	private List<Person> siblings;
	private String firstname, surname;

	public Person(final String firstname, final String surname) {
		this.firstname = firstname;
		this.surname = surname;
	}

	public Person() {}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public List<Person> getSiblings() {
		return siblings;
	}

	public void setSiblings(final List<Person> siblings) {
		this.siblings = siblings;
	}
}