/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class Type {

	public static Type type(final Class<?> type) {
		return new Type(type);
	}

	private final TypeInspector inspector = new TypeInspector();
	private final Class<?> type;

	public Type(final Class<?> type) {
		this.type = type;
	}

	public boolean hasProperty(final String name) {
		return propertyMap().containsKey(name);
	}

	public boolean isPropertyType(final String propertyName, final Class<?> expectedType) {
		BeanProperty found = propertyMap().get(propertyName);
		return found != null && found.isType(expectedType);
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

	public BeanProperty propertyNamed(final String name) {
		return propertyMap().get(name);
	}

	public Class<?> propertyType(final String propertyName) {
		BeanProperty property = propertyNamed(propertyName);
		if (property != null) {
			return property.getType();
		}
		return null;
	}
}
