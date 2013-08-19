
package uk.co.it.modular.beans;

import static uk.co.it.modular.beans.Bean.bean;
import static uk.co.it.modular.beans.Type.type;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A {@link BeanProperty} which is bound to a particular instance
 * 
 * @author Stewart Bissett
 */
public class BeanPropertyInstance {

	/**
	 * Static factory method for constructing a {@link BeanPropertyInstance} for the property name on the given instance.</p> Returns <code>null</code> if the property is not
	 * present.</p>
	 */
	public static final BeanPropertyInstance beanProperty(final Object instance, final String name) {
		return bean(instance).propertyNamed(name);
	}

	private final BeanProperty property;
	private final Object instance;

	public BeanPropertyInstance(final BeanProperty property, final Object instance) {
		this.property = property;
		this.instance = instance;
	}

	/**
	 * Return the object instance this property is bound to
	 */
	public Object getInstance() {
		return instance;
	}

	/**
	 * Return the value of this property from the given object cast to the given type. Will throw a {@link ClassCastException} if the value is not of the given type.
	 * 
	 * @param type the type to return the value as
	 */
	public <T> T getValue(final Class<T> type) {
		return property.getValue(instance, type);
	}

	/**
	 * Return the value of this property from the contained instance
	 * 
	 * @param type the type to return the value as
	 */
	public Object getValue() {
		return property.getValue(instance);
	}

	/**
	 * Return <code>true</code> if the value of this property on this instance matches the supplied value
	 */
	public boolean hasValue(final Object value) {
		return value == null ? this.isNull() : value.equals(property.getValue(instance));
	}

	/**
	 * Return the property details
	 */
	public BeanProperty getProperty() {
		return property;
	}

	/**
	 * Return <code>true</code> if the value of this property on this instance is null
	 */
	public boolean isNull() {
		return property.getValue(instance) == null;
	}

	/**
	 * Set the value of this property on the object to the given value. Will throw a {@link RuntimeException} if the property does not exist or return <code>true</code> if the
	 * property was successfullly set.
	 * 
	 * @param value the value to set this property to on the instance
	 */
	public boolean setValue(final Object value) {
		return property.setValue(instance, value);
	}

	public String getName() {
		return property.getName();
	}

	public Method getAccessor() {
		return property.getAccessor();
	}

	public Method getMutator() {
		return property.getMutator();
	}

	public boolean hasName(final String name) {
		return property.hasName(name);
	}

	public Class<?> getDeclaringType() {
		return property.getDeclaringType();
	}

	public String getDeclaringTypeCanonicalName() {
		return property.getDeclaringTypeCanonicalName();
	}

	public String getDeclaringTypeSimpleName() {
		return property.getDeclaringTypeSimpleName();
	}

	public Class<?> getType() {
		return property.getType();
	}

	public String getTypeCanonicalName() {
		return property.getTypeCanonicalName();
	}

	public String getTypeSimpleName() {
		return property.getTypeSimpleName();
	}

	public Class<?> getTypeParameter(final int n) {
		return property.getTypeParameter(n);
	}

	public List<Class<?>> getTypeParameters() {
		return property.getTypeParameters();
	}

	public boolean isIterable() {
		return property.isIterable();
	}

	public boolean isType(final Class<?> type) {
		return property.isType(type);
	}

	public boolean isType(final Class<?>... types) {
		return property.isType(types);
	}

	public boolean isGeneric() {
		return property.isGeneric();
	}

	public boolean isPrimitive() {
		return property.isPrimitive();
	}

	public boolean isArray() {
		return property.isArray();
	}

	public boolean isString() {
		return property.isString();
	}

	public boolean isCharacter() {
		return property.isCharacter();
	}

	public boolean isByte() {
		return property.isByte();
	}

	public boolean isInteger() {
		return property.isInteger();
	}

	public boolean isDouble() {
		return property.isDouble();
	}

	public boolean isFloat() {
		return property.isFloat();
	}

	public boolean isShort() {
		return property.isShort();
	}

	public boolean isLong() {
		return property.isLong();
	}

	public boolean isBoolean() {
		return property.isBoolean();
	}

	public boolean isDate() {
		return property.isDate();
	}

	public boolean isMap() {
		return property.isMap();
	}

	public boolean isList() {
		return property.isList();
	}

	public boolean isSet() {
		return property.isSet();
	}

	public boolean isCollection() {
		return property.isCollection();
	}

	public boolean isEnum() {
		return property.isEnum();
	}

	public boolean hasTypeParameter(final Class<?> type) {
		return property.hasTypeParameter(type);
	}

	public boolean hasAnyTypeParameters(final Class<?>... types) {
		return property.hasAnyTypeParameters(types);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BeanPropertyInstance)) {
			return false;
		}
		BeanPropertyInstance rhs = (BeanPropertyInstance) obj;
		return new EqualsBuilder().append(property, rhs.property).append(instance, rhs.instance).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(23, 35).append(property).append(instance).toHashCode();
	}

	@Override
	public String toString() {
		return "BeanPropertyInstance [" + type(this.property.getType()).camelName() + "." + this.property.getName() + ". [" + instance + "]]";
	}
}
