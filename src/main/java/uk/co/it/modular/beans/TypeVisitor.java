/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * A callback interface which is fired for each property found by {@link BeanUtils#visit(Object, BeanVisitor)}
 * 
 * @author Stewart Bissett
 */
public interface TypeVisitor {

	/**
	 * Callback fired for each {@link BeanProperty} found on an object
	 * 
	 * @param property
	 *            the property being visited
	 * @param current
	 *            the type currently being visited
	 * @param path
	 *            a dot notation path of the properties navigated to get to this property
	 * @param stack
	 *            a stack of the types that have been visited to get to the current type. The latest type is last in the array.
	 */
	public void visit(final BeanProperty property, final Class<?> current, final String path, final Class<?>[] stack);
}
