package org.exparity.beans.testutils.types;

public class Employee extends Person {

	private Person manager;

	public Person getManager() {
		return manager;
	}

	public void setManager(final Person manager) {
		this.manager = manager;
	}

}