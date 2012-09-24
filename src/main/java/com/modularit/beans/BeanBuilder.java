/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder object for instantiating and populating objects which follow the Java beans standards conventions for getter/setters
 * 
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class BeanBuilder<T> {

	/**
	 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
	 */
	public interface Values {

		public <T> T createValue(final Class<T> type);

	}

	public static <T> BeanBuilder<T> anInstanceOf(final Class<T> type) {
		return new BeanBuilder<T>(type);
	}

	private final Map<String, Object> properties = new HashMap<String, Object>();
	private final Map<String, Object> paths = new HashMap<String, Object>();
	private final Class<T> type;
	private Values values;

	public BeanBuilder(final Class<T> type) {
		this.type = type;
	}

	public BeanBuilder<T> filledWith(final Values values) {
		this.values = values;
		return this;
	}

	public BeanBuilder<T> withPropertyValue(final String propertyName, final Object value) {
		this.properties.put(propertyName, value);
		return this;
	}

	public BeanBuilder<T> withPathValue(final String path, final Object value) {
		this.paths.put(path, value);
		return this;
	}

	public BeanBuilder<T> withMaxCollectionSize(final int size) {
		return this;
	}

	public T build() {
		try {
			T instance = type.newInstance();
			BeanUtils.visitAll(instance, new BeanVisitor() {

				public void visit(final Object[] stack, final String path, final Object current, final BeanProperty property) {
					Object value = paths.get(path);
					if (value == null) {
						value = properties.get(property.getName());
						if (value == null) {
							value = values.createValue(property.getType());
						}
					}
					property.setValue(current, value);
				}
			});
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
