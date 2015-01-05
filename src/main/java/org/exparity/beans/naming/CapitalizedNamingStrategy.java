package org.exparity.beans.naming;

import java.lang.reflect.Method;
import org.exparity.beans.core.BeanNamingStrategy;

/**
 * @author Stewart Bissett
 */
public class CapitalizedNamingStrategy extends AbstractNamingStrategy implements BeanNamingStrategy {

	public String describeRoot(final Class<?> type) {
		return describeType(type);
	}

	public String describeType(final Class<?> type) {
		return typeName(type);
	}

	public String describeProperty(final Method method, final String prefix) {
		return method.getName().substring(prefix.length());
	}

}
