/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.lang.reflect.Method;
import uk.co.it.modular.beans.BeanNamingStrategy;

/**
 * Implementation of a {@link BeanNamingStrategy} which decorates an existing naming strategy but forces the return of {@link #describeRoot(Class)}
 * 
 * @author Stewart Bissett
 */
public class ForceRootNameNamingStrategy implements BeanNamingStrategy {

	private final BeanNamingStrategy delegate;
	private final String rootName;

	public ForceRootNameNamingStrategy(final BeanNamingStrategy delegate, final String rootName) {
		this.delegate = delegate;
		this.rootName = rootName;
	}

	public String describeRoot(final Class<?> type) {
		return rootName;
	}

	public String describeType(final Class<?> type) {
		return delegate.describeType(type);
	}

	public String describeProperty(final Method method, final String prefix) {
		return delegate.describeProperty(method, prefix);
	}

}
