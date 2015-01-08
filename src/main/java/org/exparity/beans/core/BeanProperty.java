
package org.exparity.beans.core;

import java.lang.reflect.Method;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import static org.exparity.beans.Type.type;
import static org.exparity.beans.core.MethodUtils.genericArgs;

/**
 * A {@link BeanProperty} which is bound to a particular instance
 * 
 * @author Stewart Bissett
 */
public class BeanProperty extends AbstractProperty {

	private final Object instance;
	private final Method accessor, mutator;

	public BeanProperty(final String propertyName, final Method accessor, final Method mutator, final Object instance) {
		super(accessor.getDeclaringClass(), propertyName, type(accessor.getReturnType()), genericArgs(accessor));
		this.instance = instance;
		this.accessor = accessor;
		this.mutator = mutator;
	}

	/**
	 * Return the object instance this property is bound to
	 */
	public Object getInstance() {
		return instance;
	}

	/**
	 * Return the accessor {@link Method} for this property
	 */
	public Method getAccessor() {
		return accessor;
	}

	/**
	 * Return the mutator {@link Method} for this property
	 */
	public Method getMutator() {
		return mutator;
	}

	/**
	 * Return the value of this property from the given object cast to the given type. Will throw a {@link ClassCastException} if the value is not of the given type.
	 * 
	 * @param type the type to return the value as
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(final Class<T> type) {
		return (T) getValue();
	}

	/**
	 * Return the value of this property from the contained instance
	 * 
	 * @param type the type to return the value as
	 */
	public Object getValue() {
		return MethodUtils.invoke(getAccessor(), instance);
	}

	/**
	 * Return <code>true</code> if the value of this property on this instance matches the supplied value
	 */
	public boolean hasValue(final Object value) {
		return value == null ? this.isNull() : value.equals(getValue());
	}

	/**
	 * Return <code>true</code> if the value of this property on this instance is null
	 */
	public boolean isNull() {
		return getValue() == null;
	}

	/**
	 * Set the value of this property on the object to the given value. Will throw a {@link RuntimeException} if the property does not exist or return <code>true</code> if the
	 * property was successfullly set.
	 * 
	 * @param value the value to set this property to on the instance
	 */
	public boolean setValue(final Object value) {
		return MethodUtils.invoke(getMutator(), instance, value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BeanProperty)) {
			return false;
		}
		BeanProperty rhs = (BeanProperty) obj;
		return new EqualsBuilder().append(getDeclaringType(), rhs.getDeclaringType()).append(getName(), rhs.getName()).append(instance, rhs.instance).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(23, 35).append(getDeclaringType()).append(getName()).append(instance).toHashCode();
	}

	@Override
	public String toString() {
		return "BeanProperty [" + getDeclaringType() + "." + getName() + " [" + instance + "]]";
	}
}
