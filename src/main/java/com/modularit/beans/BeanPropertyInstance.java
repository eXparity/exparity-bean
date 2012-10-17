/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BeanPropertyInstance {

	private final BeanProperty property;
	private final String path;
	private final Object[] stack;

	public BeanPropertyInstance(final BeanProperty property, final String path, final Object[] stack) {
		this.property = property;
		this.path = path;
		this.stack = stack;
	}

	public Object getValue() {
		return property.getValue();
	}

	public BeanProperty getProperty() {
		return property;
	}

	public String getPath() {
		return path;
	}

	public Object[] getStack() {
		return stack;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BeanPropertyInstance)) {
			return false;
		}
		BeanPropertyInstance rhs = (BeanPropertyInstance) obj;
		return new EqualsBuilder().append(path, rhs.path).append(property, rhs.property).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(33, 55).append(path).append(property).toHashCode();
	}

	@Override
	public String toString() {
		return "BeanPropertyInstance [" + property + ":" + path + "]";
	}
}