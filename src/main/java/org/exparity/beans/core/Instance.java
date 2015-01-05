
package org.exparity.beans.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.exparity.beans.BeanPredicates.*;

/**
 * @author Stewart Bissett
 */
public abstract class Instance {

	private final InstanceInspector inspector;
	private final Object instance;
	private BeanNamingStrategy naming;

	protected Instance(final InstanceInspector inspector, final Object instance, final BeanNamingStrategy naming) {
		this.inspector = inspector;
		this.instance = instance;
		this.naming = naming;
	}

	/**
	 * Return a list of the publicly exposes get/set properties on the Bean. For example:
	 * <p/>
	 * 
	 * <pre>
	 * List&lt;BeanPropertyInstance&gt; properties = Bean.bean(myObject).propertyList()
	 * </pre>
	 */
	public List<BeanProperty> propertyList() {
		final List<BeanProperty> propertyList = new ArrayList<BeanProperty>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
				propertyList.add(property);
			}
		});
		return propertyList;
	}

	public Map<String, BeanProperty> propertyMap() {
		final Map<String, BeanProperty> propertyMap = new HashMap<String, BeanProperty>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
				propertyMap.put(property.getName(), property);
			}
		});
		return propertyMap;
	}

	public boolean setProperty(final BeanPropertyPredicate predicate, final Object value) {
		final List<BeanProperty> valuesSet = new ArrayList<BeanProperty>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
				if (predicate.matches(property)) {
					property.setValue(value);
					valuesSet.add(property);
				}
			}
		});
		return !valuesSet.isEmpty();
	}

	public Object propertyValue(final BeanPropertyPredicate predicate) {
		BeanProperty property = findAny(predicate);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	public <T> T propertyValue(final BeanPropertyPredicate predicate, final Class<T> type) {
		BeanProperty property = findAny(predicate);
		if (property != null) {
			return property.getValue(type);
		}
		return null;
	}

	public Object propertyValue(final String name) {
		return propertyNamed(name).getValue();
	}

	public <T> T propertyValue(final String name, final Class<T> type) {
		return propertyNamed(name).getValue(type);
	}

	public boolean hasProperty(final BeanPropertyPredicate predicate) {
		return findAny(predicate) != null;
	}

	public BeanProperty get(final String propertyName) {
		return propertyNamed(propertyName);
	}

	public BeanProperty get(final BeanPropertyPredicate predicate) {
		return findAny(predicate);
	}

	public BeanProperty propertyNamed(final String propertyName) {
		BeanProperty property = findAny(named(propertyName));
		if (property == null) {
			throw new BeanPropertyNotFoundException(this.instance.getClass(), propertyName);
		}
		return property;
	}

	public Class<?> propertyType(final String name) {
		return propertyNamed(name).getType();
	}

	public Class<?> propertyType(final BeanPropertyPredicate predicate) {
		BeanProperty found = findAny(predicate);
		if (found != null) {
			return found.getType();
		}
		return null;
	}

	public BeanProperty findAny(final BeanPropertyPredicate predicate) {

		@SuppressWarnings("serial")
		class HaltVisitException extends RuntimeException {

			BeanProperty property;

			private HaltVisitException(final BeanProperty property) {
				this.property = property;
			}
		}

		try {
			visit(new BeanVisitor() {

				public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
					if (predicate.matches(property)) {
						throw new HaltVisitException(property);
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

			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
				if (predicate.matches(property)) {
					function.apply(property);
				}
			}
		});
	}

	public List<BeanProperty> find(final BeanPropertyPredicate predicate) {
		final List<BeanProperty> collection = new ArrayList<BeanProperty>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
				if (predicate.matches(property)) {
					collection.add(property);
				}
			}
		});
		return collection;
	}

	public void visit(final BeanVisitor visitor) {
		inspector.inspect(instance, naming, visitor);
	}

	public boolean hasProperty(final String name) {
		return hasProperty(named(name));
	}

	public boolean hasProperty(final String name, final Object value) {
		return hasProperty(withValue(name, value));
	}

	public boolean setProperty(final String name, final Object value) {
		return propertyNamed(name).setValue(value);
	}

	public boolean isPropertyType(final String name, final Class<?> type) {
		return propertyNamed(name).isType(type);
	}

	public boolean isPropertyType(final BeanPropertyPredicate predicate, final Class<?> type) {
		return hasProperty(matchesAll(predicate, ofType(type)));
	}

	public void setNamingStrategy(final BeanNamingStrategy naming) {
		this.naming = naming;
	}

}
