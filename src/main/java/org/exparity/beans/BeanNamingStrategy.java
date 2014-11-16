/*
 * Copyright (c) Modular IT Limited.
 */

package org.exparity.beans;

import java.lang.reflect.Method;

/**
 * Classes which implement the {@link BeanNamingStrategy} strategy interface should define the strategy for turning the types and methods discovered by the bean utils into names
 * 
 * @author Stewart Bissett
 */
public interface BeanNamingStrategy {

	/**
	 * Return a textual description of the type specific to the root type
	 * 
	 * @param type the type to describe
	 */
	public String describeRoot(final Class<?> type);

	/**
	 * Return a textual description of the type
	 * 
	 * @param type the type to describe
	 */
	public String describeType(final Class<?> type);

	/**
	 * Return a textual description of the method
	 * 
	 * @param method the method to describe
	 * @param prefix the bean property prefix, e.g. get, set, is, which is assoscaited with this method
	 */
	public String describeProperty(final Method method, final String prefix);

}
