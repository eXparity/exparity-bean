
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

	/**
	 * Test if the supplied type has a property with the given name. For example:</p>
	 * 
	 * <pre>
	 * if ( type(MyObject.class).hasProperty("surname"))) {
	 * 	// Do Something;
	 * }
	 * </pre>
	 * 
	 * @param name the property name
	 */
	public boolean hasProperty(final String name) {
		return propertyMap().containsKey(name);
	}

	/**
	 * Test if the property on the type is of the supplied type. For example:</p>
	 * 
	 * <pre>
	 * if (bean(MyObject.class).isPropertyType(&quot;surname&quot;, String.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public boolean isPropertyType(final String name, final Class<?> type) {
		return propertyNamed(name).isType(type);
	}

	/**
	 * Visit the supplied class and notify the visitor for each bean property found. For example:</p>
	 * 
	 * <pre>
	 * type(MyObject.class).visit(new TypeVisitor() {
	 * 
	 * 	public void visit(final TypeProperty property) {
	 * 		System.out.println(property.getName());
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @param type the type to get the property from
	 * @param visitor the visitor which will be notified of every bean property encountered
	 */
	public void visit(final TypeVisitor visitor) {
		inspector.inspect(type, naming, visitor);
	}

	public Type setNamingStrategy(final BeanNamingStrategy naming) {
		this.naming = naming;
		return this;
	}

	/**
	 * Return a list of the publicly exposes get/set properties on a class. For example:
	 * <p/>
	 * 
	 * <pre>
	 * List&lt;TypeProperty&gt; properties = Type.type(MyObject.class).propertyList()
	 * </pre>
	 */
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

	/**
	 * Return a map of the publicly exposes get/set properties on the type with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <pre>
	 * Map&lt;String, BeanProperty&gt; propertyMap = Bean.bean(MyObject.class).propertyMap()
	 * </pre>
	 */
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
	 * Get the requested property from the type or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * TypeProperty surname = type(MyObject.class).propertyNamed(MyObject.class, &quot;surname&quot;)
	 * </pre>
	 * 
	 * @param name the property name
	 */
	public TypeProperty propertyNamed(final String propertyName) {
		TypeProperty property = propertyMap().get(propertyName);
		if (property == null) {
			throw new BeanPropertyNotFoundException(type, propertyName);
		}
		return property;
	}

	/**
	 * Get the requested property from the type or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * TypeProperty surname = type(MyObject.class).get(&quot;surname&quot;)
	 * </pre>
	 * 
	 * @param propertyName the property name
	 */
	public TypeProperty get(final String propertyName) {
		return propertyNamed(propertyName);
	}

	/**
	 * Return the property type on the type for the supplied property name or <code>null</code> if the property doesn't exist. For example:</p>
	 * 
	 * <pre>
	 * if (String.class.equals(type(MyObject.class).propertyType(&quot;surname&quot;))) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param name the property name
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
