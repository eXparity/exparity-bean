/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static uk.co.it.modular.beans.BeanPredicates.anyProperty;
import static uk.co.it.modular.beans.BeanPredicates.matchesAll;
import static uk.co.it.modular.beans.BeanPredicates.withName;
import static uk.co.it.modular.beans.BeanPredicates.withType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stewart Bissett
 */
public class Bean {

	public static Bean bean(final Object instance) {
		return new Bean(instance);
	}

	private final BeanInspector beanInspector = new BeanInspector();
	private final Object instance;

	public Bean(final Object instance) {
		this.instance = instance;
	}

	public List<BeanPropertyInstance> propertyList() {
		final List<BeanPropertyInstance> propertyList = new ArrayList<BeanPropertyInstance>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				propertyList.add(new BeanPropertyInstance(property, current));
			}
		});
		return propertyList;
	}

	public Map<String, BeanPropertyInstance> propertyMap() {
		final Map<String, BeanPropertyInstance> propertyMap = new HashMap<String, BeanPropertyInstance>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				propertyMap.put(property.getName(), new BeanPropertyInstance(property, current));
			}
		});
		return propertyMap;
	}

	public boolean setProperty(final BeanPropertyPredicate predicate, final Object value) {
		final List<BeanProperty> valuesSet = new ArrayList<BeanProperty>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property, current)) {
					property.setValue(current, value);
					valuesSet.add(property);
				}
			}
		});
		return !valuesSet.isEmpty();
	}

	public Object propertyValue(final BeanPropertyPredicate predicate) {
		BeanPropertyInstance property = findAny(predicate);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	public Object propertyValue(final String name) {
		return propertyValue(withName(name));
	}

	public <T> T propertyValue(final String name, final Class<T> type) {
		BeanPropertyInstance property = findAny(withName(name));
		if (property != null) {
			return property.getValue(type);
		}
		return null;
	}

	public boolean hasProperty(final BeanPropertyPredicate predicate) {
		return findAny(predicate) != null;
	}

	public BeanPropertyInstance propertyNamed(final String propertyName) {
		return findAny(withName(propertyName));
	}

	public Class<?> propertyType(final BeanPropertyPredicate predicate) {
		BeanPropertyInstance found = findAny(predicate);
		if (found != null) {
			return found.getType();
		}
		return null;
	}

	public BeanPropertyInstance findAny(final BeanPropertyPredicate predicate) {

		@SuppressWarnings("serial")
		class HaltVisitException extends RuntimeException {

			BeanPropertyInstance property;

			private HaltVisitException(final BeanPropertyInstance property) {
				this.property = property;
			}
		}

		try {
			visit(new BeanVisitor() {

				public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
					if (predicate.matches(property, current)) {
						throw new HaltVisitException(new BeanPropertyInstance(property, current));
					}
				}
			});
		} catch (HaltVisitException e) {
			return e.property;
		}
		return null;
	}

	public void apply(final BeanPropertyFunction function) {
		apply(function, anyProperty());
	}

	public void apply(final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				BeanPropertyInstance propertyInstance = new BeanPropertyInstance(property, current);
				if (predicate.matches(property, current)) {
					function.apply(propertyInstance);
				}
			}
		});
	}

	public List<BeanPropertyInstance> find(final BeanPropertyPredicate predicate) {
		final List<BeanPropertyInstance> collection = new ArrayList<BeanPropertyInstance>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				BeanPropertyInstance propertyInstance = new BeanPropertyInstance(property, current);
				if (predicate.matches(property, current)) {
					collection.add(propertyInstance);
				}
			}
		});
		return collection;
	}

	public void visit(final BeanVisitor visitor) {
		beanInspector.inspect(instance, visitor);
	}

	public boolean hasProperty(final String name) {
		return hasProperty(withName(name));
	}

	public boolean setProperty(final String name, final Object value) {
		return setProperty(withName(name), value);
	}

	public boolean isPropertyType(final String name, final Class<?> type) {
		return isPropertyType(withName(name), type);
	}

	public boolean isPropertyType(final BeanPropertyPredicate predicate, final Class<?> type) {
		return hasProperty(matchesAll(predicate, withType(type)));
	}

	public Class<?> propertyType(final String name) {
		return propertyType(withName(name));
	}

}