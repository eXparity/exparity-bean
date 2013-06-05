/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static uk.co.it.modular.beans.InstanceInspector.beanInspector;

/**
 * @author Stewart Bissett
 */
public class Bean extends Instance {

	public static Bean bean(final Object instance) {
		return new Bean(instance);
	}

	public Bean(final Object instance) {
		super(beanInspector(), instance);
	}
}
