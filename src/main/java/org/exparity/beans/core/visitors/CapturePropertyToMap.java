package org.exparity.beans.core.visitors;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanVisitor;

/**
 * @author Stewart Bissett
 */
public class CapturePropertyToMap implements BeanVisitor {

	private final Map<String, BeanProperty> propertyMap;

	public CapturePropertyToMap(final Map<String, BeanProperty> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public void visit(final BeanProperty property, final Object current, final Object[] stack, AtomicBoolean stop) {
		propertyMap.put(property.getName(), property);
	}
}