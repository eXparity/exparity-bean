/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Utility methods for accessing {@link java.lang.reflect.Method} properties
 * 
 * @author Stewart Bissett
 */
class MethodUtils {

	public static Class<?>[] genericArgs(final Method accessor) {
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

	public static Object invoke(final Method accessor, final Object instance) {
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

	public static boolean invoke(final Method mutator, final Object instance, final Object value) {
		try {
			mutator.invoke(instance, value);
		} catch (IllegalArgumentException e) {
			throw new BeanPropertyException("Method '" + mutator.getName()
					+ " on '"
					+ instance.getClass().getCanonicalName()
					+ "' expected arguments '"
					+ StringUtils.join(mutator.getParameterTypes())
					+ "'  but was supplied a '"
					+ value.getClass().getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new BeanPropertyException("Illegal Access exception encountered whilst calling '" + mutator.getName() + " on '" + instance.getClass().getCanonicalName() + "'", e);
		} catch (InvocationTargetException e) {
			throw new BeanPropertyException("Unexpected exception whilst calling '" + mutator.getName() + " on '" + instance.getClass().getCanonicalName() + "'",
					e.getTargetException());
		}
		return true;
	}

}
