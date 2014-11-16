/*
 * Copyright (c) Modular IT Limited.
 */

package org.exparity.beans.naming;

import static org.exparity.beans.Type.type;

import java.util.Collection;
import java.util.Map;

import org.exparity.beans.BeanNamingStrategy;
import org.exparity.beans.Type;

/**
 * @author Stewart Bissett
 */
public abstract class AbstractNamingStrategy implements BeanNamingStrategy {

	protected String typeName(final Class<?> klass) {
		Type type = type(klass);
		if (type.is(Map.class)) {
			return "Map";
		} else if (type.is(Collection.class)) {
			return "Collection";
		} else {
			return type.componentSimpleName();
		}
	}
}