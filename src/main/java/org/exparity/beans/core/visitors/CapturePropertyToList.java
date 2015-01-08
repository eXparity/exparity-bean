package org.exparity.beans.core.visitors;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPath;
import org.exparity.beans.core.BeanVisitor;

/**
 * @author Stewart Bissett
 */
public class CapturePropertyToList implements BeanVisitor {

	private final List<BeanProperty> propertyList;

	public CapturePropertyToList(final List<BeanProperty> propertyList) {
		this.propertyList = propertyList;
	}

	public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack, AtomicBoolean stop) {
		propertyList.add(property);
	}
}