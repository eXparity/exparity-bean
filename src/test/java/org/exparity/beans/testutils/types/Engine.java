package org.exparity.beans.testutils.types;

import java.math.BigDecimal;

public class Engine {

	private BigDecimal capacity;

	public Engine(final BigDecimal capacity) {
		this.capacity = capacity;
	}

	public Engine() {
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(final BigDecimal capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Engine [" + capacity + "]";
	}
}