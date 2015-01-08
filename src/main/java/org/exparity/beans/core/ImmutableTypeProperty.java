
package org.exparity.beans.core;

import java.lang.reflect.Method;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import static org.exparity.beans.Type.type;
import static org.exparity.beans.core.MethodUtils.genericArgs;

/**
 * Immutable value object to encapsulate an property on an Object which follows the get/is Java beans standard for accessors.</p>
 * <p>
 * An instance of this class is not bound to a specific instance of an object, rather it represents a re-usable defintion of a get/set pair on a given class
 * </p>
 * 
 * @author Stewart Bissett
 */
public class ImmutableTypeProperty extends AbstractProperty {

	private final Method accessor;

	public ImmutableTypeProperty(final String propertyName, final Method accessor) {
		super(accessor.getDeclaringClass(), propertyName, type(accessor.getReturnType()), genericArgs(accessor));
		this.accessor = accessor;
	}

	/**
	 * Return the accessor {@link Method} for this property
	 */
	public Method getAccessor() {
		return accessor;
	}

	/**
	 * Return the value of this property. Will throw a {@link BeanPropertyException} if the property is not found on the given instance
	 */
	public Object getValue(final Object instance) {
		return MethodUtils.invoke(getAccessor(), instance);
	}

	/**
	 * Return the value of this property from the given object cast to the given type. Will throw a {@link ClassCastException} if the value is not of the given type.
	 * 
	 * @param type the type to return the value as
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(final Object instance, final Class<T> type) {
		return (T) getValue(instance);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ImmutableTypeProperty)) {
			return false;
		}
		ImmutableTypeProperty rhs = (ImmutableTypeProperty) obj;
		return new EqualsBuilder().append(getDeclaringType(), rhs.getDeclaringType()).append(getName(), rhs.getName()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(23, 35).append(getDeclaringType()).append(getName()).toHashCode();
	}

	@Override
	public String toString() {
		return "ImmutableTypeProperty [" + getDeclaringType() + "." + getName() + "]";
	}
}
