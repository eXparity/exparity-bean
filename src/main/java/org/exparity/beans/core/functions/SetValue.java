package org.exparity.beans.core.functions;

import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyFunction;

/**
 * Implementation of a BeanPropertyFunction which sets a value of a given BeanProperty
 */
public class SetValue implements BeanPropertyFunction {

	private final Object value;

	public SetValue(final Object value) {
		this.value = value;
	}

	public void apply(final BeanProperty property) {
		property.setValue(value);
	}
}