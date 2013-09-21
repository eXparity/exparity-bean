/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.lang.reflect.Method;
import uk.co.it.modular.beans.BeanNamingStrategy;
import static uk.co.it.modular.beans.Type.type;
import static org.apache.commons.lang.StringUtils.lowerCase;
import static org.apache.commons.lang.StringUtils.uncapitalize;

/**
 * @author Stewart Bissett
 */
public class CamelCaseNamingStrategy implements BeanNamingStrategy {

	public String describeType(final Class<?> type) {
		return uncapitalize(type(type).simpleName());
	}

	public String describeProperty(final Method method, final String prefix) {
		int startPos = prefix.length();
		String methodName = method.getName();
		return lowerCase(methodName.charAt(startPos) + "") + methodName.substring(startPos + 1);
	}

}
