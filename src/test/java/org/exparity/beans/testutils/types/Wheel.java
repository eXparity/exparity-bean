
package org.exparity.beans.testutils.types;

public class Wheel {

	private Integer diameter;

	public Wheel(final Integer diameter) {
		this.diameter = diameter;
	}

	public Wheel() {}

	public Integer getDiameter() {
		return diameter;
	}

	public void setDiameter(final Integer diameter) {
		this.diameter = diameter;
	}

	@Override
	public String toString() {
		return "Wheel [" + diameter + "]";
	}
}