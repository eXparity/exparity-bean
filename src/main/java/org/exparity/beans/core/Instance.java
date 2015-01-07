
package org.exparity.beans.core;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.exparity.beans.core.visitors.Print;
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
	 * Return a list of the publicly exposes get/set properties on this instance. For example to list the properties on :
	 * <p/>
	 * 
	 * <pre>
	 * List&lt;BeanPropertyInstance&gt; properties = bean(myObject).propertyList()
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

	/**
	 * Return a map of the publicly exposes get/set properties on the Bean with the property name as the key and the initial character lowercased For example:
	 * <p/>
	 * 
	 * <pre>
	 * Map&lt;String, BeanPropertyInstance&gt; propertyMap = bean(myObject).propertyMap()
	 * </pre>
	 */
	public Map<String, BeanProperty> propertyMap() {
		final Map<String, BeanProperty> propertyMap = new HashMap<String, BeanProperty>();
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
				propertyMap.put(property.getName(), property);
			}
		});
		return propertyMap;
	}

	/**
	 * Set the property which matches the predicate on the given instance. For example:</p>
	 * 
	 * <pre>
	 * bean(myObject).setProperty(BeanPredicates.named(&quot;surname&quot;), &quot;Smith&quot;)
	 * </pre>
	 * 
	 * @param predicate a predicate to match the properties
	 * @param value the property value
	 */
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

	/**
	 * Return the first property value on the instance which matches the predicate or return <code>null</code> if the no matching properties are not present on the instance. For
	 * example:</p>
	 * 
	 * <pre>
	 * Object value = bean(myObject).propertyValue(BeanPredicates.named("surname")))
	 * </pre>
	 * 
	 * @param instance an object to get the property from
	 * @param name the property name
	 */
	public Object propertyValue(final BeanPropertyPredicate predicate) {
		BeanProperty property = findAny(predicate);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	/**
	 * Return the first property value on the instance which matches the predicate or return <code>null</code> if the no matching properties are not present on the instance. For
	 * example:</p>
	 * 
	 * <pre>
	 * String value = bean(myObject).propertyValue(BeanPredicates.named(&quot;surname&quot;)), String.class)
	 * </pre>
	 * 
	 * @param predicate a predicate to match the properties
	 * @param type the type to return the property value as
	 */
	public <T> T propertyValue(final BeanPropertyPredicate predicate, final Class<T> type) {
		BeanProperty property = findAny(predicate);
		if (property != null) {
			return property.getValue(type);
		}
		return null;
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance. For example:</p>
	 * 
	 * <pre>
	 * Object value = bean(myObject).propertyValue("surname"))
	 * </pre>
	 * 
	 * @param name the property name
	 */
	public Object propertyValue(final String name) {
		return propertyNamed(name).getValue();
	}

	/**
	 * Return the property value on the instance for the supplied property name or return <code>null</code> if the property is not present on the instance. For example:</p>
	 * 
	 * <pre>
	 * String value = bean(myObject).propertyValue("surname", String.class))
	 * </pre>
	 * 
	 * @param name the property name
	 * @param type the type to return the property value as
	 */
	public <T> T propertyValue(final String name, final Class<T> type) {
		return propertyNamed(name).getValue(type);
	}

	/**
	 * Test if the supplied instance has a Bean property which matches the given predicate. For example:
	 * <p/>
	 * 
	 * <pre>
	 * if ( bean(myObject).hasProperty(aUser, BeanPredicates.named("surname")))) {
	 * 	// Do Something
	 * }
	 * </pre>
	 * 
	 * @param predicate the predicate to select the property with
	 */
	public boolean hasProperty(final BeanPropertyPredicate predicate) {
		return findAny(predicate) != null;
	}

	/**
	 * Get the requested property from the type or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = bean(myObject).get("surname")
	 * </pre>
	 * 
	 * @param name the property name
	 */
	public BeanProperty get(final String name) {
		return propertyNamed(name);
	}

	/**
	 * Get the property which matches the predicate from the instance or return <code>null</code> if not matching property is found. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = bean(myObject).get(BeanPredicates.named("surname"))
	 * </pre>
	 * 
	 * @param predicate a predicate to match the property
	 */
	public BeanProperty get(final BeanPropertyPredicate predicate) {
		return findAny(predicate);
	}

	/**
	 * Get the requested property from the instance or return <code>null</code> if the property is not present. For example:</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance surname = bean(myObject).propertyNamed("surname")
	 * </pre>
	 * 
	 * @param name the property name
	 */
	public BeanProperty propertyNamed(final String name) {
		BeanProperty property = findAny(named(name));
		if (property == null) {
			throw new BeanPropertyNotFoundException(this.instance.getClass(), name);
		}
		return property;
	}

	/**
	 * Return the property type on the instance for the supplied property name or <code>null</code> if the property doesn't exist. For example:</p>
	 * 
	 * <pre>
	 * if (String.class.equals(bean(myObject).propertyType(&quot;surname&quot;))) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param name the property name
	 */
	public Class<?> propertyType(final String name) {
		return propertyNamed(name).getType();
	}

	/**
	 * Return the property type on the instance for the first property which matches the supplied predicate or <code>null</code> if no matching properties are found. For
	 * example:</p>
	 * 
	 * <pre>
	 * if (String.class.equals(bean(myObject).propertyType(BeanPredicates.named(&quot;surname&quot;)))) {
	 * 	// Do something
	 * }
	 * </pre>
	 * 
	 * @param predicate a predicate to match the properties
	 */
	public Class<?> propertyType(final BeanPropertyPredicate predicate) {
		BeanProperty found = findAny(predicate);
		if (found != null) {
			return found.getType();
		}
		return null;
	}

	/**
	 * Find the first instance of the property which matches the given predicate in the instance. For example</p>
	 * 
	 * <pre>
	 * BeanPropertyInstance property = bean(myObject).findAny(BeanPredicates.withValue(&quot;name&quot; "Bob"))
	 * </pre>
	 * 
	 * @param predicate a predicate to match the properties
	 */
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

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties on the supplied instance. For example</p>
	 * 
	 * <pre>
	 * bean(myObject).apply(BeanFunctions.setValue(null))
	 * </pre>
	 * 
	 * @param function the function to apply to the matching properties
	 */
	public void apply(final BeanPropertyFunction function) {
		apply(function, anyProperty());
	}

	/**
	 * Apply the {@link BeanPropertyFunction} to all properties which match the predicate in the supplied instances. For example</p>
	 * 
	 * <pre>
	 * bean(myObject).apply(BeanPredicates.ofType(String.class), BeanFunctions.setValue(null))
	 * </pre>
	 * 
	 * @param function the function to apply to the matching properties
	 * @param predicate a predicate to match the properties
	 */
	public void apply(final BeanPropertyFunction function, final BeanPropertyPredicate predicate) {
		visit(new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
				if (predicate.matches(property)) {
					function.apply(property);
				}
			}
		});
	}

	/**
	 * Find all property properties which match the predicate. For example</p>
	 * 
	 * <pre>
	 * for ( BeanPropertyInstance property : bean(myObject).find(BeanPredicates.ofType(String.class)) {
	 *   property.setValue(null);
	 * }
	 * </pre>
	 * 
	 * @param predicate a predicate to match the properties
	 */
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

	/**
	 * Visit the supplied bean or graph instance and notify the visitor for each bean property found. For example:</p>
	 * 
	 * <pre>
	 * bean(myObject).visit(new BeanVisitor() {
	 * 
	 * 	public void visit(final BeanPropertyInstance property, final Object current, final BeanPropertyPath path, final Object[] stack) {
	 * 		System.out.println(path.fullPath());
	 * 	}
	 * });
	 * </pre>
	 * @param visitor the visitor which will be notified of every bean property encountered
	 */
	public void visit(final BeanVisitor visitor) {
		inspector.inspect(instance, naming, visitor);
	}

	/**
	 * Test if the supplied instance has a Bean property with the given name. For example</p>
	 * 
	 * <pre>
	 * if ( bean(myObject).hasProperty("surname"))) {
	 * 	// Do Something
	 * }
	 * </pre>
	 * @param name the property name
	 */
	public boolean hasProperty(final String name) {
		return hasProperty(named(name));
	}

	/**
	 * Test if the supplied instance has a Bean property with the given name and value. For example</p>
	 * 
	 * <pre>
	 * if ( bean(myObject).hasProperty("surname","Smith"))) {
	 * 	// Do Something
	 * }
	 * </pre>
	 * @param name the property name
	 * @param value the value to test for
	 */
	public boolean hasProperty(final String name, final Object value) {
		return hasProperty(hasPropertyValue(name, value));
	}

	/**
	 * Set the requested property on the given instance. For example:</p>
	 * 
	 * <pre>
	 * bean(myObject).setProperty(&quot;surname&quot;, &quot;Smith&quot;)
	 * </pre>
	 * 
	 * @param name the property name
	 * @param value the property value
	 */
	public boolean setProperty(final String name, final Object value) {
		return propertyNamed(name).setValue(value);
	}

	/**
	 * Test if the property on the supplied instance is of the supplied type. For example:</p>
	 * 
	 * <pre>
	 * if (bean(myObject).isPropertyType(&quot;surname&quot;, String.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * @param name the property name
	 * @param type the expected type of the property
	 */
	public boolean isPropertyType(final String name, final Class<?> type) {
		return propertyNamed(name).isType(type);
	}

	/**
	 * Test if the property which matches the predicate on the supplied instance is of the supplied type. For example:</p>
	 * 
	 * <pre>
	 * if (bean(myObject).isPropertyType(&quot;surname&quot;, String.class)) {
	 * 	// Do something
	 * }
	 * </pre>
	 * @param predicate a predicate to match the properties
	 * @param type the expected type of the property
	 */
	public boolean isPropertyType(final BeanPropertyPredicate predicate, final Class<?> type) {
		return hasProperty(matchesAll(predicate, ofType(type)));
	}

	/**
	 * Override the default naming strategy
	 */
	public void setNamingStrategy(final BeanNamingStrategy naming) {
		this.naming = naming;
	}

	/**
	 * Write out all properties and their values on this object.
	 * @param writer the writer to write the output to
	 */
	public void dump(final Writer writer) {
		this.visit(new Print(writer));
	}

	/**
	 * Write out all properties and their values on this object.
	 * @param os the stream to write the output to
	 */
	public void dump(final OutputStream os) {
		this.dump(new OutputStreamWriter(os));
	}

	/**
	 * Write out all properties and their values on this object.
	 * @param buffer the buffer to append the output to
	 */
	public void dump(final StringBuffer buffer) {
		StringWriter writer = new StringWriter();
		this.dump(writer);
		buffer.append(writer);
	}

}
