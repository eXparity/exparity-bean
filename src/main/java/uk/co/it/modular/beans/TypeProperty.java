
package uk.co.it.modular.beans;

import java.lang.reflect.Method;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import static uk.co.it.modular.beans.Type.type;

/**
 * Immutable value object to encapsulate a property on an Object which follows the get/set Java beans standard.</p>
 * <p>
 * An instance of this class is not bound to a specific instance of an object, rather it represents a re-usable defintion of a get/set pair on a given class
 * </p>
 * 
 * @author Stewart Bissett
 */
public class TypeProperty extends InstanceProperty {

	/**
	 * Static factory method for constructing a {@link BeanProperty} for the property name on the given class.</p> Returns <code>null</code> if the property is not present.</p>
	 */
	public static final TypeProperty typeProperty(final Class<?> instance, final String name) {
		return type(instance).propertyNamed(name);
	}

	public TypeProperty(final String propertyName, final Method accessor, final Method mutator) {
		super(propertyName, accessor, mutator);
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

	/**
	 * Set the value of this property on the object to the given value. Will throw a {@link RuntimeException} if the property does not exist or return <code>true</code> if the
	 * property was successfullly set.
	 * 
	 * @param value the value to set this property to on the instance
	 */
	public boolean setValue(final Object instance, final Object value) {
		return MethodUtils.invoke(getMutator(), instance, value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof TypeProperty)) {
			return false;
		}
		TypeProperty rhs = (TypeProperty) obj;
		return new EqualsBuilder().append(getDeclaringType(), rhs.getDeclaringType()).append(getName(), rhs.getName()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(23, 35).append(getDeclaringType()).append(getName()).toHashCode();
	}

	@Override
	public String toString() {
		return "TypeProperty [" + getDeclaringType() + "." + getName() + "]";
	}
}
