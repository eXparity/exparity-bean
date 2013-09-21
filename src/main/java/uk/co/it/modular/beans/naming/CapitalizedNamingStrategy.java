/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.lang.reflect.Method;
import uk.co.it.modular.beans.BeanNamingStrategy;
import static uk.co.it.modular.beans.Type.type;

/**
 * @author Stewart Bissett
 */
public class CapitalizedNamingStrategy implements BeanNamingStrategy {

	public String describeType(final Class<?> type) {
		return type(type).simpleName();
	}

	public String describeProperty(final Method method, final String prefix) {
		return method.getName().substring(prefix.length());
	}

}
