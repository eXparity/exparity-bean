
package org.exparity.beans.core;

import java.util.List;

/**
 * The strategy used to sort the properties read from a bean or type
 * @author Stewart Bissett
 */
public interface BeanPropertyOrderingStrategy {

	public void sort(final List<TypeProperty> propertyList);
}
