package org.exparity.beans.core.visitors;

import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPath;
import org.exparity.beans.core.BeanPropertyPredicate;
import org.exparity.beans.core.BeanVisitor;

/**
 * @author Stewart Bissett
 */
public class CapturePropertyIf implements BeanVisitor {

	private final BeanPropertyPredicate predicate;
	private BeanProperty property;

	public CapturePropertyIf(final BeanPropertyPredicate predicate) {
		this.predicate = predicate;
	}

	public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack, final AtomicBoolean stop) {
		if (predicate.matches(property)) {
			this.property = property;
			stop.set(true);
		}
	}

	public BeanProperty getMatchedProperty() {
		return property;
	}
}