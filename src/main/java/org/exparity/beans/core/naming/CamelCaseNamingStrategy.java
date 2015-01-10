package org.exparity.beans.core.naming;

import java.lang.reflect.Method;
import org.exparity.beans.core.BeanNamingStrategy;
import static org.apache.commons.lang.StringUtils.lowerCase;
import static org.apache.commons.lang.StringUtils.uncapitalize;

/**
 * @author Stewart Bissett
 */
public class CamelCaseNamingStrategy extends AbstractNamingStrategy implements BeanNamingStrategy {

	public String describeRoot(final Class<?> type) {
		return describeType(type);
	}

	public String describeType(final Class<?> type) {
		return uncapitalize(typeName(type));
	}

	public String describeProperty(final Method method, final String prefix) {
		int startPos = prefix.length();
		String methodName = method.getName();
		return lowerCase(methodName.charAt(startPos) + "") + methodName.substring(startPos + 1);
	}

}
