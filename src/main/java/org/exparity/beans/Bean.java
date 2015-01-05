
package org.exparity.beans;

import org.exparity.beans.core.BeanNamingStrategy;
import org.exparity.beans.core.Instance;
import org.exparity.beans.core.Typed;
import org.exparity.beans.naming.CamelCaseNamingStrategy;
import static org.exparity.beans.Type.type;
import static org.exparity.beans.core.InstanceInspector.beanInspector;

/**
 * @author Stewart Bissett
 */
public class Bean extends Instance implements Typed {

	private final Type type;

	public static Bean bean(final Object instance) {
		return bean(instance, new CamelCaseNamingStrategy());
	}

	public static Bean bean(final Object instance, final BeanNamingStrategy naming) {
		return new Bean(instance, naming);
	}

	public Bean(final Object instance) {
		this(instance, new CamelCaseNamingStrategy());
	}

	public Bean(final Object instance, final BeanNamingStrategy naming) {
		super(beanInspector(), instance, naming);
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
