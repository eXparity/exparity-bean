/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

/**
 * A callback interface which is fired for each property found by {@link BeanUtils#visit(Object, BeanVisitor)}
 * 
 * @author Stewart Bissett
 */
public interface BeanVisitor {

	/**
	 * Callback fired for each {@link BeanProperty} found on an object
	 * 
	 * @param stack a stack of the objects that have been visited to get to the current object. The latest object is last in the array.
	 * @param path a dot notation path of the properties navigated to get to this property
	 * @param current the object currently being visited
	 * @param property the property being visited
	 */
	public void visit(final Object[] stack, final String path, final Object current, final BeanProperty property);
}
