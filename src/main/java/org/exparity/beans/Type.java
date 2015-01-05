
package org.exparity.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.exparity.beans.core.BeanNamingStrategy;
import org.exparity.beans.core.BeanPropertyNotFoundException;
import org.exparity.beans.core.ImmutableTypeProperty;
import org.exparity.beans.core.TypeInspector;
import org.exparity.beans.core.TypeProperty;
import org.exparity.beans.core.TypeVisitor;
import org.exparity.beans.core.Typed;
import org.exparity.beans.naming.CamelCaseNamingStrategy;
import org.exparity.beans.naming.CapitalizedNamingStrategy;
import static org.apache.commons.lang.StringUtils.uncapitalize;

/**
 * @author Stewart.Bissett
 */
public class Type implements Typed {

	public static Type type(final Class<?> type) {
		return new Type(type, new CamelCaseNamingStrategy());
	}

	public static Type type(final Class<?> type, final BeanNamingStrategy namingStrategy) {
		return new Type(type, namingStrategy);
	}

	public static Type type(final Object instance) {
		return new Type(instance.getClass(), new CamelCaseNamingStrategy());
	}

	public static Type type(final Object instance, final BeanNamingStrategy namingStrategy) {
		return new Type(instance.getClass(), namingStrategy);
	}

	private final TypeInspector inspector = new TypeInspector();
	private final Class<?> type;
	private BeanNamingStrategy naming;

	public Type(final Class<?> type) {
		this(type, new CapitalizedNamingStrategy());
	}

	public Type(final Class<?> type, final BeanNamingStrategy namingStrategy) {
		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null");
		}
		this.type = type;
		this.naming = namingStrategy;
	}

	public String camelName() {
		return uncapitalize(type.getSimpleName());
	}

	public String simpleName() {
		return type.getSimpleName();
	}

	/**
	 * Return the name for the {@link Class#getComponentType()}, For a scalar Class this returns the same as {@link #canonicalName()} but for an array it this return the class
	 * simple name but without the [].
	 */
	public String componentName() {
		return isArray() ? type.getComponentType().getName() : canonicalName();
	}

	/**
	 * Return the simple name for the {@link Class#getComponentType()}, For a scalar Class this returns the same as {@link #simpleName()} but for an array it this return the class
	 * simple name but without the [].
	 */
	public String componentSimpleName() {
		return isArray() ? type.getComponentType().getSimpleName() : simpleName();
	}

	public String canonicalName() {
		return type.getCanonicalName();
	}

	public boolean hasProperty(final String name) {
		return propertyMap().containsKey(name);
	}

	/**
	 * @throws BeanPropertyNotFoundException
	 */
	public boolean isPropertyType(final String propertyName, final Class<?> expectedType) {
		return propertyNamed(propertyName).isType(expectedType);
	}

	public void visit(final TypeVisitor visitor) {
		inspector.inspect(type, naming, visitor);
	}

	public Type setNamingStrategy(final BeanNamingStrategy naming) {
		this.naming = naming;
		return this;
	}

	public List<TypeProperty> propertyList() {
		final List<TypeProperty> propertyList = new ArrayList<TypeProperty>();
		visit(new TypeVisitor() {

			public void visit(final TypeProperty property) {
				propertyList.add(property);
			}
		});
		return propertyList;
	}

	/**
	 * Return a list of the accessors exposed on this type
	 */
	public List<ImmutableTypeProperty> accessorList() {
		return inspector.accessorList(type, naming);
	}

	public Map<String, TypeProperty> propertyMap() {
		final Map<String, TypeProperty> propertyMap = new HashMap<String, TypeProperty>();
		visit(new TypeVisitor() {

			public void visit(final TypeProperty property) {
				propertyMap.put(property.getName(), property);
			}
		});
		return propertyMap;
	}

	/**
	 * @throws BeanPropertyNotFoundException
	 */
	public TypeProperty propertyNamed(final String propertyName) {
		TypeProperty property = propertyMap().get(propertyName);
		if (property == null) {
			throw new BeanPropertyNotFoundException(type, propertyName);
		}
		return property;
	}

	/**
	 * @throws BeanPropertyNotFoundException
	 */
	public TypeProperty get(final String propertyName) {
		return propertyNamed(propertyName);
	}

	/**
	 * @throws BeanPropertyNotFoundException
	 */
	public Class<?> propertyType(final String propertyName) {
		return propertyNamed(propertyName).getType();
	}

	public Class<?>[] typeHierachy() {
		return (Class<?>[]) ArrayUtils.addAll(new Class<?>[] {
				this.type
		}, superTypes());
	}

	public Class<?>[] superTypes() {

		Class<?> superType = type.getSuperclass();
		if (superType.equals(Object.class)) {
			return new Class<?>[0];
		}

		List<Class<?>> superTypes = new ArrayList<Class<?>>();
		while (!superType.equals(Object.class)) {
			superTypes.add(superType);
			superType = superType.getSuperclass();
		}
		return superTypes.toArray(new Class<?>[0]);
	}

	/**
	 * Return <code>true</code> if this type is the same type or a subclass of the other type.
	 */
	public boolean is(final Class<?> otherType) {
		return otherType != null && otherType.isAssignableFrom(this.type);
	}

	public boolean isArray() {
		return this.type.isArray();
	}

	public String packageName() {
		return type.getPackage().getName();
	}

	public boolean isPrimitive() {
		return type.isPrimitive();
	}

	public boolean isEnum() {
		return type.isEnum();
	}

	public Class<?> getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Type [" + simpleName() + "]";
	}
}
