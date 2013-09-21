/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.lang.reflect.Method;
import uk.co.it.modular.beans.BeanNamingStrategy;
import static org.apache.commons.lang.StringUtils.lowerCase;
import static uk.co.it.modular.beans.Type.type;

/**
 * @author Stewart Bissett
 */
public class LowerCaseNamingStrategy implements BeanNamingStrategy {

	public String describeType(final Class<?> type) {
		return lowerCase(type(type).simpleName());
	}

	public String describeProperty(final Method method, final String prefix) {
		return lowerCase(method.getName().substring(prefix.length()));
	}

}
