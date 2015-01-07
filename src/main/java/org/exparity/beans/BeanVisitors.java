
package org.exparity.beans;

import java.io.OutputStreamWriter;
import java.io.Writer;
import org.exparity.beans.core.BeanVisitor;
import org.exparity.beans.core.visitors.Print;

/**
 * Static repository of {@link BeanVisitor} implementations
 * 
 * @author Stewart Bissett
 */
public abstract class BeanVisitors {

	/**
	 * Print all the properties visited to {@link System#out}
	 */
	public static BeanVisitor print() {
		return print(new OutputStreamWriter(System.out));
	}

	/**
	 * Print all the properties visited to the {@link Writer}
	 */
	public static BeanVisitor print(final Writer writer) {
		return new Print(writer);
	}
}
