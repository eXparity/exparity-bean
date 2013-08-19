
package uk.co.it.modular.beans;

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
	 * @param path a dot notation path of the properties navigated to get to this property
	 * @param stack a stack of the objects that have been visited to get to the current object. The latest object is last in the array.
	 */
	public void visit(final BeanPropertyInstance property, final Object current, final BeanPropertyPath path, final Object[] stack);
}
