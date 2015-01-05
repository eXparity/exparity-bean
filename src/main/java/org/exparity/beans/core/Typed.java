
package org.exparity.beans.core;

/**
 * Any BeanUtils class which can be considered to represent a single class and therefore can have it's type information examined should inherit the {@link Typed} interface so as to
 * expose a consistent group of methods for examining type information
 * 
 * @author Stewart Bissett
 */
public interface Typed {

	/**
	 * Return the {@link Class} for this type
	 */
	public abstract Class<?> getType();

	/**
	 * Return the name of the type using camel notation. For example the class <code>my.package.MyObject</code> would have the camelName <code>myObject</code>.
	 */
	public abstract String camelName();

	/**
	 * Return the name of the type without any package information. For example the class <code>my.package.MyObject</code> would have the name <code>MyObject</code>.
	 */
	public abstract String simpleName();

	/**
	 * Return the full name of the type. For example the class <code>my.package.MyObject</code> would have the name <code>my.package.MyObject</code>.
	 */
	public abstract String canonicalName();

	/**
	 * Return the package name for this type. For example the class <code>my.package.MyObject</code> would have the name <code>my.package</code>
	 */
	public abstract String packageName();

	/**
	 * Return this type and it's super types as an array of {@link Class} instances
	 */
	public abstract Class<?>[] typeHierachy();

	/**
	 * Return this type's super types as an array of {@link Class} instances
	 */
	public abstract Class<?>[] superTypes();

	/**
	 * Return <code>true</code> if this type is assignable from the other type.
	 */
	public abstract boolean is(final Class<?> otherType);

	/**
	 * Return <code>true</code> if this type is an array
	 */
	public abstract boolean isArray();

	/**
	 * Return <code>true</code> if this type is a primitive type
	 */
	public abstract boolean isPrimitive();

	/**
	 * Return <code>true</code> if this type is a Java enumeration
	 */
	public abstract boolean isEnum();

}