/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;
import uk.co.it.modular.beans.BeanNamingStrategy;
import uk.co.it.modular.beans.Type;
import static uk.co.it.modular.beans.Type.type;

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