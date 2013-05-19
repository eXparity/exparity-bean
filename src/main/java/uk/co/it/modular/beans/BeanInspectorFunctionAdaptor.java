/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static uk.co.it.modular.beans.BeanPredicates.withName;
import static uk.co.it.modular.beans.BeanPropertyInstancePredicateAdaptor.adapted;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public/**
 * Adaptor class which converts the {@link BeanInspector} inspection methods into functions which can be shared between the utility classes
 * 
 * @author Stewart Bissett
 */
class BeanInspectorFunctionAdaptor {

	private final BeanInspector beanInspector;

	public BeanInspectorFunctionAdaptor(final BeanInspector beanInspector) {
		this.beanInspector = beanInspector;
	}

	public List<BeanProperty> propertyList(final Object instance) {
		final List<BeanProperty> propertyList = new ArrayList<BeanProperty>();
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				propertyList.add(property);
			}
		});
		return propertyList;
	}

	public Map<String, BeanProperty> propertyMap(final Object instance) {
		final Map<String, BeanProperty> propertyMap = new HashMap<String, BeanProperty>();
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				propertyMap.put(property.getName(), property);
			}
		});
		return propertyMap;
	}

	public boolean setProperty(final Object instance, final BeanPropertyPredicate predicate, final Object value) {
		final List<BeanProperty> valuesSet = new ArrayList<BeanProperty>();
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				if (predicate.matches(property)) {
					property.setValue(current, value);
					valuesSet.add(property);
				}
			}
		});
		return !valuesSet.isEmpty();
	}

	public Object propertyValue(final Object instance, final BeanPropertyInstancePredicate predicate) {
		BeanPropertyInstance property = findFirst(instance, predicate);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	public boolean hasProperty(final Object instance, final BeanPropertyInstancePredicate predicate) {
		return findFirst(instance, predicate) != null;
	}

	public BeanPropertyInstance propertyNamed(final Object instance, final String propertyName) {
		return findFirst(instance, adapted(withName(propertyName)));
	}

	public Class<?> propertyType(final Object instance, final BeanPropertyPredicate withName) {
		return findFirst(instance, adapted(withName(propertyName)));
	}

	public BeanPropertyInstance findFirst(final Object instance, final BeanPropertyInstancePredicate predicate) {

		@SuppressWarnings("serial")
		class HaltVisitException extends RuntimeException {

			BeanPropertyInstance property;

			private HaltVisitException(final BeanPropertyInstance property) {
				this.property = property;
			}
		}

		try {
			visit(instance, new BeanVisitor() {

				public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
					BeanPropertyInstance propertyInstance = new BeanPropertyInstance(property, current);
					if (predicate.matches(propertyInstance)) {
						throw new HaltVisitException(propertyInstance);
					}
				}
			});
		} catch (HaltVisitException e) {
			return e.property;
		}
		return null;
	}

	public void apply(final Object instance, final BeanPropertyFunction function, final BeanPropertyInstancePredicate predicate) {
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				BeanPropertyInstance propertyInstance = new BeanPropertyInstance(property, current);
				if (predicate.matches(propertyInstance)) {
					function.apply(propertyInstance);
				}
			}
		});
	}

	public List<BeanPropertyInstance> find(final Object instance, final BeanPropertyInstancePredicate predicate) {
		final List<BeanPropertyInstance> collection = new ArrayList<BeanPropertyInstance>();
		visit(instance, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				BeanPropertyInstance propertyInstance = new BeanPropertyInstance(property, current);
				if (predicate.matches(propertyInstance)) {
					collection.add(propertyInstance);
				}
			}
		});
		return collection;
	}

	public void visit(final Object instance, final BeanVisitor visitor) {
		beanInspector.inspect(instance, visitor);
	}
}
