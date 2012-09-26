
package com.modularit.beans;

import static com.modularit.beans.BeanUtils.*;

/**
 * A re-usable definition of a property which can be used to re-usably inspect and mutate objects for a given property name. For Example:
 * 
 * <pre>
 *  BeanPropertyDescription surname = BeanUtils.property("surname");
 *  if ( surname.existsOn(aPerson) ) {
 *     System.out.println("Hello " + surname.from(aPerson) 
 *  }
 * </pre>
 * @author Stewart Bissett
 */
public class BeanPropertyDescriptor {

	/**
	 * Return a {@link BeanPropertyDescriptor} instance which allows for re-usable inspection and mutation of objects for a given property name. For Example:
	 * 
	 * <pre>
	 *  BeanPropertyDescription surname = BeanPropertyDescriptor.property("surname");
	 *  if ( surname.existsOn(aPerson) ) {
	 *     System.out.println("Hello " + surname.from(aPerson) 
	 *  }
	 * </pre>
	 */
	public static BeanPropertyDescriptor property(final String propertyName) {
		return new BeanPropertyDescriptor(propertyName);
	}

	private final String name;

	public BeanPropertyDescriptor(final String name) {
		this.name = name;
	}

	/**
	 * Test if this property exists on the supplied object instance. For Example: </p>
	 * 
	 * <pre>
	 *  BeanPropertyDescription surname = BeanUtils.property("surname");
	 *  if ( surname.existsOn(aPerson) ) {
	 *     System.out.println("Hello " + surname.from(aPerson) 
	 *  }
	 * </pre>
	 * 
	 * @param instance
	 *            the object instance to test to see if it has this property
	 */
	public boolean existsOn(final Object instance) {
		return hasProperty(instance, name);
	}

	/**
	 * Read the property from the supplied supplied object instance. For Example: </p>
	 * 
	 * <pre>
	 *  BeanPropertyDescription surname = BeanUtils.property("surname");
	 *  if ( surname.existsOn(aPerson) ) {
	 *     System.out.println("Hello " + surname.from(aPerson) 
	 *  }
	 * </pre>
	 * @param instance
	 *            the object instance to read this property from
	 */
	public Object from(final Object instance) {
		BeanProperty property = getProperty(instance, name);
		if (property != null) {
			return property.getValue(instance);
		}
		return null;
	}

	/**
	 * Set the property on the supplied supplied object instance. For Example: </p>
	 * 
	 * <pre>
	 * BeanPropertyDescription surname = BeanUtils.property(&quot;surname&quot;);
	 * if (surname.existsOn(aPerson)) {
	 * 	surname.changeTo(aPerson, &quot;Smith&quot;);
	 * }
	 * </pre>
	 * @param instance
	 *            the object instance to set this property from
	 */
	public void change(final Object instance, final Object value) {
		BeanProperty property = getProperty(instance, name);
		if (property != null) {
			property.setValue(instance, value);
		} else {
			throw new BeanPropertyNotFound("Property '" + name + " was not found on '" + instance + "'");
		}
	}

	/**
	 * Set the property on the supplied supplied object instance to null. For Example: </p>
	 * 
	 * <pre>
	 * BeanPropertyDescription surname = BeanUtils.property(&quot;surname&quot;);
	 * if (surname.existsOn(aPerson)) {
	 * 	surname.changeToNull(aPerson);
	 * }
	 * </pre>
	 * @param instance
	 *            the object instance to set this property from
	 */
	public void changeToNull(final Object instance) {
		BeanProperty property = getProperty(instance, name);
		if (property != null) {
			property.setValue(instance, null);
		} else {
			throw new BeanPropertyNotFound("Property '" + name + " was not found on '" + instance + "'");
		}
	}
}
