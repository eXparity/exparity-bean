
package com.modularit.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Value object to encapsulate a property on an Object which follows the get/set Java beans standard
 * 
 * @author Stewart Bissett
 */
public class BeanProperty {

	private final Method accessor, mutator;
	private final String name;
	private final Class<?> type;

	public BeanProperty(final String propertyName, final Method accessor, final Method mutator) {
		this.name = propertyName;
		this.accessor = accessor;
		this.mutator = mutator;
		this.type = accessor.getReturnType();
	}

	/**
	 * Return the name of the property
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the accessor or getter {@link Method} for this property
	 */
	public Method getAccessor() {
		return accessor;
	}

	/**
	 * Return the value of this property from the given object instance. Will throw a {@link BeanPropertyException} if the property is not found on the given instance
	 * 
	 * @param instance an object to get the value for this property
	 */
	public Object getValue(final Object instance) {
		try {
			return accessor.invoke(instance);
		} catch (IllegalArgumentException e) {
			throw new BeanPropertyException("Method '" + accessor.getName() + "' does not exist on '" + instance.getClass() + "'");
		} catch (IllegalAccessException e) {
			throw new BeanPropertyException("Illegal Access exception encountered whilst calling '" + accessor.getName() + " on '" + instance.getClass().getCanonicalName() + "'",
					e);
		} catch (InvocationTargetException e) {
			throw new BeanPropertyException("Unexpected exception whilst calling '" + accessor.getName() + " on '" + instance.getClass().getCanonicalName() + "'", e.getCause());
		}
	}

	/**
	 * Return the value of this property from the given object cast to the given type. Will throw a {@link ClassCastException} if the value is not of the given type.
	 * 
	 * @param instance an object to get the value for this property
	 * @param type the type to return the value as
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(final Object instance, final Class<T> type) {
		return (T) getValue(instance);
	}

	/**
	 * Return the mutator or setter {@link Method} for this property
	 */
	public Method getMutator() {
		return mutator;
	}

	/**
	 * Set the value of this property on the object to the given value. Will throw a {@link RuntimeException} if the property does not exist or return <code>true</code> if the
	 * property was successfullly set.
	 * 
	 * @param instance an object to get the value for this property
	 * @param value the value to set this property to on the instance
	 */
	public boolean setValue(final Object instance, final Object value) {
		try {
			mutator.invoke(instance, value);
		} catch (IllegalArgumentException e) {
			throw new BeanPropertyException("Mutator property '" + name + " on '" + instance.getClass().getCanonicalName() + "' expect arguments", e);
		} catch (IllegalAccessException e) {
			throw new BeanPropertyException("Illegal Access exception encountered whilst calling '" + mutator.getName() + " on '" + instance.getClass().getCanonicalName() + "'", e);
		} catch (InvocationTargetException e) {
			throw new BeanPropertyException("Unexpected exception whilst calling '" + mutator.getName() + " on '" + instance.getClass().getCanonicalName() + "'", e.getCause());
		}
		return true;
	}

	/**
	 * Return the type of the property
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Return the canonical name of the type
	 */
	public String getTypeCanonicalName() {
		return type.getCanonicalName();
	}

	/**
	 * Return the canonical name of the type
	 */
	public String getTypeSimpleName() {
		return type.getSimpleName();
	}

	/**
	 * Test if the property implements {@link Iterable}
	 */
	public boolean isIterable() {
		return isType(Iterable.class);
	}

	/**
	 * Test if the property type is assignable from the supplied type
	 * 
	 * @param type any type to check to see if this properties type is assignable to it
	 */
	public boolean isType(final Class<?> type) {
		return type.isAssignableFrom(this.type);
	}

	/**
	 * Test if the property is an array
	 */
	public boolean isArray() {
		return type.isArray();
	}

	/**
	 * Test if the property implements {@link Map}
	 */
	public boolean isMap() {
		return isType(Map.class);
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
		return new EqualsBuilder().append(type, rhs.type).append(name, rhs.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(23, 35).append(this.type).append(this.name).toHashCode();
	}

	@Override
	public String toString() {
		return "BeanProperty [" + name + ":" + type.getCanonicalName() + "]";
	}
}
