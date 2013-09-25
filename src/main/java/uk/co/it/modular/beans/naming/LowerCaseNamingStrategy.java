/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.lang.reflect.Method;
import uk.co.it.modular.beans.BeanNamingStrategy;
import static org.apache.commons.lang.StringUtils.lowerCase;

/**
 * @author Stewart Bissett
 */
public class LowerCaseNamingStrategy extends AbstractNamingStrategy implements BeanNamingStrategy {

	public String describeRoot(final Class<?> type) {
		return describeType(type);
	}

	public String describeType(final Class<?> type) {
		return lowerCase(typeName(type));
	}

	public String describeProperty(final Method method, final String prefix) {
		return lowerCase(method.getName().substring(prefix.length()));
	}

}
