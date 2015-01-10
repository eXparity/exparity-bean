package org.exparity.beans.testutils.types;

import java.util.List;

public class Manager extends Person {

	private List<Person> employees;

	public List<Person> getEmployees() {
		return employees;
	}

	public void setEmployees(final List<Person> employees) {
		this.employees = employees;
	}

}