package org.exparity.beans.core.visitors;

import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyFunction;
import org.exparity.beans.core.BeanPropertyPredicate;
import org.exparity.beans.core.BeanVisitor;

/**
 * @author Stewart Bissett
 */
public class ApplyFunctionIf implements BeanVisitor {

	private final BeanPropertyFunction function;
	private final BeanPropertyPredicate predicate;

	public ApplyFunctionIf(BeanPropertyFunction function, BeanPropertyPredicate predicate) {
		this.function = function;
		this.predicate = predicate;
	}

	public void visit(final BeanProperty property, final Object current, final Object[] stack, final AtomicBoolean stop) {
		if (predicate.matches(property)) {
			function.apply(property);
		}
	}
}