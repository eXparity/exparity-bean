
package org.exparity.beans.core;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * A callback interface which is fired for each property found by {@link BeanUtils#visit(Object, BeanVisitor)}
 * 
 * @author Stewart Bissett
 */
public interface BeanVisitor {

	/**
	 * Callback fired for each {@link BeanProperty} found on an object
	 * 
	 * @param property the property being visited
	 * @param current the object currently being visited
	 * @param stack a stack of the objects that have been visited to get to the current object. The latest object is last in the array.
	 * @param stop set the value to <code>true</code> if the visit is to be stopped
	 */
	public void visit(final BeanProperty property, final Object current, final Object[] stack, AtomicBoolean stop);
}
