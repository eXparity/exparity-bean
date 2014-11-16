
package org.exparity.beans;

import static org.exparity.beans.InstanceInspector.beanInspector;
import static org.exparity.beans.Type.type;

/**
 * @author Stewart Bissett
 */
public class Bean extends Instance implements Typed {

	private final Type type;

	public static Bean bean(final Object instance) {
		return new Bean(instance);
	}

	public Bean(final Object instance) {
		super(beanInspector(), instance);
		this.type = type(instance);
	}

	public String camelName() {
		return type.camelName();
	}

	public String simpleName() {
		return type.simpleName();
	}

	public String canonicalName() {
		return type.canonicalName();
	}

	public Class<?>[] typeHierachy() {
		return type.typeHierachy();
	}

	public Class<?>[] superTypes() {
		return type.superTypes();
	}

	public boolean is(final Class<?> otherType) {
		return type.is(otherType);
	}

	public boolean isArray() {
		return type.isArray();
	}

	public String packageName() {
		return type.packageName();
	}

	public boolean isPrimitive() {
		return type.isPrimitive();
	}

	public boolean isEnum() {
		return type.isEnum();
	}

	public Class<?> getType() {
		return type.getType();
	}
}
