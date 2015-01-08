
package org.exparity.beans.core;

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
class MethodWrapper {

	private final Method method;

	MethodWrapper(final Method method) {
		this.method = method;
	}

	Class<?>[] genericArgs() {
		Type type = method.getGenericReturnType();
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

	Object invoke(final Object instance) {
		try {
			return method.invoke(instance);
		} catch (IllegalArgumentException e) {
			throw new BeanPropertyException("Method '" + method.getName() + "' does not exist on '" + instance.getClass() + "'");
		} catch (IllegalAccessException e) {
			throw new BeanPropertyException("Illegal Access exception encountered whilst calling '" + method.getName() + " on '" + instance.getClass().getCanonicalName() + "'",
					e);
		} catch (InvocationTargetException e) {
			throw new BeanPropertyException("Unexpected exception whilst calling '" + method.getName() + " on '" + instance.getClass().getCanonicalName() + "'", e.getCause());
		}
	}

	boolean invoke(final Object instance, final Object value) {
		try {
			method.invoke(instance, value);
		} catch (IllegalArgumentException e) {
			throw new BeanPropertyException("Method '" + method.getName()
					+ " on '"
					+ instance.getClass().getCanonicalName()
					+ "' expected arguments '"
					+ StringUtils.join(method.getParameterTypes())
					+ "'  but was supplied a '"
					+ value.getClass().getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new BeanPropertyException("Illegal Access exception encountered whilst calling '" + method.getName() + " on '" + instance.getClass().getCanonicalName() + "'", e);
		} catch (InvocationTargetException e) {
			throw new BeanPropertyException("Unexpected exception whilst calling '" + method.getName() + " on '" + instance.getClass().getCanonicalName() + "'",
					e.getTargetException());
		}
		return true;
	}

	Class<?> getDeclaringClass() {
		return method.getDeclaringClass();
	}

	Method getMethod() {
		return method;
	}

	Class<?> getReturnType() {
		return method.getReturnType();
	}
}
