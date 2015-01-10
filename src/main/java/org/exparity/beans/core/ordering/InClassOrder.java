
package org.exparity.beans.core.ordering;

import java.util.List;
import org.exparity.beans.core.BeanPropertyOrderingStrategy;
import org.exparity.beans.core.TypeProperty;

/**
 * Implementation of a BeanPropertyOrderingStrategy which orders the properties as they're returned from the class
 * 
 * @author Stewart Bissett
 */
public class InClassOrder implements BeanPropertyOrderingStrategy {

	public int compare(final TypeProperty o1, final TypeProperty o2) {
		return o1.getName().compareTo(o2.getName());
	}

	public void sort(final List<TypeProperty> propertyList) {
		// nothing to do
	}

}
