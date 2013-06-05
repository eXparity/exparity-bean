/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static org.apache.commons.lang.StringUtils.uncapitalize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stewart.Bissett
 */
public class Type {

	public static Type type(final Class<?> type) {
		return new Type(type);
	}

	private final TypeInspector inspector = new TypeInspector();
	private final Class<?> type;

	public Type(final Class<?> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null");
		}
		this.type = type;
	}

	public String camelName() {
		return uncapitalize(type.getSimpleName());
	}

	public String simpleName() {
		return type.getSimpleName();
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
		inspector.inspect(type, visitor);
	}

	public List<BeanProperty> propertyList() {
		final List<BeanProperty> propertyList = new ArrayList<BeanProperty>();
		visit(new TypeVisitor() {

			public void visit(final BeanProperty property) {
				propertyList.add(property);
			}
		});
		return propertyList;
	}

	public Map<String, BeanProperty> propertyMap() {
		final Map<String, BeanProperty> propertyMap = new HashMap<String, BeanProperty>();
		visit(new TypeVisitor() {

			public void visit(final BeanProperty property) {
				propertyMap.put(property.getName(), property);
			}
		});
		return propertyMap;
	}

	/**
	 * @throws BeanPropertyNotFoundException
	 */
	public BeanProperty propertyNamed(final String propertyName) {
		BeanProperty property = propertyMap().get(propertyName);
		if (property == null) {
			throw new BeanPropertyNotFoundException(type, propertyName);
		}
		return property;
	}

	/**
	 * @throws BeanPropertyNotFoundException
	 */
	public BeanProperty get(final String propertyName) {
		return propertyNamed(propertyName);
	}

	/**
	 * @throws BeanPropertyNotFoundException
	 */
	public Class<?> propertyType(final String propertyName) {
		return propertyNamed(propertyName).getType();
	}
}
