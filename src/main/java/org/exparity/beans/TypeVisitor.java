
package org.exparity.beans;

/**
 * A callback interface which is fired for each property found by {@link BeanUtils#visit(Object, BeanVisitor)}
 * 
 * @author Stewart Bissett
 */
public interface TypeVisitor {

	/**
	 * Callback fired for each {@link BeanProperty} found on an object
	 * 
	 * @param property the property being visited
	 */
	public void visit(final TypeProperty property);
}
