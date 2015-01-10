package org.exparity.beans.testutils.types;

import java.util.List;

public class Car {

	private Engine engine;
	private List<Wheel> wheels;

	public Car(final Engine engine, final List<Wheel> wheels) {
		this.engine = engine;
		this.wheels = wheels;
	}

	public Car() {
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(final Engine engine) {
		this.engine = engine;
	}

	public List<Wheel> getWheels() {
		return wheels;
	}

	public void setWheels(final List<Wheel> wheels) {
		this.wheels = wheels;
	}
}