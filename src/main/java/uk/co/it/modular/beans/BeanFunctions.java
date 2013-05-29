/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public abstract class BeanFunctions {

	public static BeanPropertyFunction setValue(final Object value) {
		return new BeanPropertyFunction() {

			public void apply(final BeanPropertyInstance property) {
				property.setValue(value);
			}
		};

	}
}
