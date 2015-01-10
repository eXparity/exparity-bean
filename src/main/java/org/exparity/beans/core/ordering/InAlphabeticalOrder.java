
package org.exparity.beans.core.ordering;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.exparity.beans.core.TypeProperty;
import org.exparity.beans.core.BeanPropertyOrderingStrategy;

/**
 * Implementation of a BeanPropertyOrderingStrategy which orders the properties alphabetically
 * 
 * @author Stewart Bissett
 */
public class InAlphabeticalOrder implements BeanPropertyOrderingStrategy {

	public void sort(final List<TypeProperty> propertyList) {
		Collections.sort(propertyList, new Comparator<TypeProperty>() {

			public int compare(final TypeProperty o1, final TypeProperty o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

}
