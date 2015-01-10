package org.exparity.beans.core.naming;

import static org.exparity.beans.Type.type;
import java.util.Collection;
import java.util.Map;
import org.exparity.beans.Type;
import org.exparity.beans.core.BeanNamingStrategy;

/**
 * @author Stewart Bissett
 */
public abstract class AbstractNamingStrategy implements BeanNamingStrategy {

	protected String typeName(final Class<?> klass) {
		Type type = type(klass);
		if (type.is(Map.class)) {
			return "Map";
		} else if (type.is(Collection.class)) {
			return "Collection";
		} else {
			return type.componentSimpleName();
		}
	}
}