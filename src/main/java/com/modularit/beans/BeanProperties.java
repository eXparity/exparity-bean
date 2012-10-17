
package com.modularit.beans;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Static repository of {@link BeanPropertyDescriptor} instances
 * 
 * @author Stewart Bissett
 */
public abstract class BeanProperties {

	/**
	 * Return a {@link BeanPropertyDescriptor} instance which allows for re-usable inspection and mutation of objects for a given property name. For Example:</p>
	 * 
	 * <pre>
	 *  BeanPropertyDescription surname = BeanPropertyDescriptor.beanProperty("surname");
	 *  if ( surname.existsOn(aPerson) ) {
	 * 		System.out.println("Hello " + surname.getValue(aPerson) 
	 *  }
	 * </pre>
	 */
	public static <P> BeanPropertyDescriptor<P> beanProperty(final String propertyName, final Class<P> propertyType) {
		return new BeanPropertyDescriptor<P>(propertyName, propertyType);
	}

	/**
	 * Return a {@link BeanPropertyDescriptor} for a {@link Long} property
	 */
	public static BeanPropertyDescriptor<Long> longProperty(final String propertyName) {
		return beanProperty(propertyName, Long.class);
	}

	/**
	 * Return a {@link BeanPropertyDescriptor} for an {@link Integer} property
	 */
	public static BeanPropertyDescriptor<Integer> intProperty(final String propertyName) {
		return beanProperty(propertyName, Integer.class);
	}

	/**
	 * Return a {@link BeanPropertyDescriptor} for a {@link Double} property
	 */
	public static BeanPropertyDescriptor<Double> doubleProperty(final String propertyName) {
		return beanProperty(propertyName, Double.class);
	}

	/**
	 * Return a {@link BeanPropertyDescriptor} for a {@link BigDecimal} property
	 */
	public static BeanPropertyDescriptor<BigDecimal> decimalProperty(final String propertyName) {
		return beanProperty(propertyName, BigDecimal.class);
	}

	/**
	 * Return a {@link BeanPropertyDescriptor} for a {@link Date} property
	 */
	public static BeanPropertyDescriptor<Date> dateProperty(final String propertyName) {
		return beanProperty(propertyName, Date.class);
	}

	/**
	 * Return a {@link BeanPropertyDescriptor} for a {@link String} property
	 */
	public static BeanPropertyDescriptor<String> stringProperty(final String propertyName) {
		return beanProperty(propertyName, String.class);
	}
}
