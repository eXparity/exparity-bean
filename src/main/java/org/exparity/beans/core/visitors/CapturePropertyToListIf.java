
package org.exparity.beans.core.visitors;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPredicate;

/**
 * @author Stewart Bissett
 */
public class CapturePropertyToListIf extends CapturePropertyToList {

	private final BeanPropertyPredicate predicate;

	public CapturePropertyToListIf(final BeanPropertyPredicate predicate, final List<BeanProperty> collection) {
		super(collection);
		this.predicate = predicate;
	}

	@Override
	public void visit(final BeanProperty property, final Object current, final Object[] stack, final AtomicBoolean stop) {
		if (predicate.matches(property)) {
			super.visit(property, current, stack, stop);
		}
	}
}