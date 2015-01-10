package org.exparity.beans.testutils.types;

public class Thrower {

	int property = 0;

	public void setProperty(final int property) {
		throw new RuntimeException();
	}

	public int getProperty() {
		throw new RuntimeException();
	}
}