
package com.modularit.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Value object to encapsulate a property on an Object which follows the get/set Java beans standard
 * 
 * @author Stewart Bissett
 */
public class BeanProperty {

	private final Class<?> declaringType;
	private final String name;
	private final Class<?> type;
	private final Class<?>[] params;
	private final Method accessor, mutator;

	public BeanProperty(final String propertyName, final Class<?> declaringType) {
		this(propertyName, BeanUtils.getAccessor(propertyName, declaringType), BeanUtils.getMutator(propertyName, declaringType));
	}

	public BeanProperty(final String propertyName, final Method accessor, final Method mutator) {
		this.declaringType = accessor.getDeclaringClass();
		this.name = propertyName;
		this.accessor = accessor;
		this.mutator = mutator;
		this.type = accessor.getReturnType();
		this.params = genericArgs(accessor);
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
		synchronized (new Integer(1)) {

		}
		return accessor;
	}

	/**
	 * Return the value of this property from the given object instance. Will throw a {@link BeanPropertyException} if the property is not found on the given instance
	 * 
	 * @param instance
	 *            an object to get the value for this property
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
	 * @param instance
	 *            an object to get the value for this property
	 * @param type
	 *            the type to return the value as
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
	 * @param instance
	 *            an object to get the value for this property
	 * @param value
	 *            the value to set this property to on the instance
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
	 * Return the declaring type of the property
	 */
	public Class<?> getDeclaringType() {
		return declaringType;
	}

	/**
	 * Return the declaring type of the property
	 */
	public String getDeclaringTypeSimpleName() {
		return declaringType.getSimpleName();
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
	 * Return the nth type parameter or throw {@link IllegalArgumentException} if there is no nth type parameter
	 */
	public Class<?> getTypeParameter(final int n) {
		if (params.length > n) {
			return params[n];
		} else {
			throw new IllegalArgumentException("Unknown type parameter with index '" + n + "'");
		}
	}

	/**
	 * Return the collection of type paramaters or an empty {@link List} if this property is not generic
	 */
	public List<Class<?>> getTypeParameters() {
		return Arrays.asList(params);
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
	 * @param type
	 *            any type to check to see if this properties type is assignable to it
	 */
	public boolean isType(final Class<?> type) {
		return type.isAssignableFrom(this.type);
	}

	/**
	 * Test if the property type is assignable from any one of the supplied types
	 * 
	 * @param types
	 *            types to check to see if this properties type is assignable to it
	 */
	public boolean isType(final Class<?>... types) {
		for (Class<?> type : types) {
			if (type.isAssignableFrom(this.type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Test if the property is a generic type
	 */
	public boolean isGeneric() {
		return this.params.length > 0;
	}

	/**
	 * Test if the property is primitive
	 */
	public boolean isPrimitive() {
		return type.isPrimitive();
	}

	/**
	 * Test if the property is an array
	 */
	public boolean isArray() {
		return type.isArray();
	}

	/**
	 * Test if the property is an array
	 */
	public boolean isString() {
		return isType(String.class);
	}

	/**
	 * Test if the property is an {@link Integer} or int
	 */
	public boolean isInteger() {
		return isType(Integer.class, int.class);
	}

	/**
	 * Test if the property is an {@link Double} or double
	 */
	public boolean isDouble() {
		return isType(Double.class, double.class);
	}

	/**
	 * Test if the property is an {@link Float} or float
	 */
	public boolean isFloat() {
		return isType(Float.class, float.class);
	}

	/**
	 * Test if the property is an {@link Short} or short
	 */
	public boolean isShort() {
		return isType(Short.class, short.class);
	}

	/**
	 * Test if the property is an {@link Long} or long
	 */
	public boolean isLong() {
		return isType(Long.class, long.class);
	}

	/**
	 * Test if the property is an {@link Boolean} or boolean
	 */
	public boolean isBoolean() {
		return isType(Boolean.class, boolean.class);
	}

	/**
	 * Test if the property is an {@link Date} or long
	 */
	public boolean isDate() {
		return isType(Date.class);
	}

	/**
	 * Test if the property implements {@link Map}
	 */
	public boolean isMap() {
		return isType(Map.class);
	}

	/**
	 * Test if the property implements {@link List}
	 */
	public boolean isList() {
		return isType(List.class);
	}

	/**
	 * Test if the property implements {@link Set}
	 */
	public boolean isSet() {
		return isType(Set.class);
	}

	/**
	 * Test if the property implements {@link Collection}
	 */
	public boolean isCollection() {
		return isType(Collection.class);
	}

	private Class<?>[] genericArgs(final Method accessor) {
		Type type = accessor.getGenericReturnType();
		if (type instanceof ParameterizedType) {
			List<Class<?>> params = new ArrayList<Class<?>>();
			for (Type arg : ((ParameterizedType) type).getActualTypeArguments()) {
				if (arg instanceof Class<?>) {
					params.add((Class<?>) arg);
				}
			}
			return params.toArray(new Class<?>[0]);
		} else {
			return new Class<?>[0];
		}
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
		return "BeanProperty [" + declaringType.getSimpleName() + "." + name + "]";
	}
}
