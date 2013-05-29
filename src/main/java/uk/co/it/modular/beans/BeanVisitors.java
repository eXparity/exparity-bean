
package uk.co.it.modular.beans;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

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
		return new BeanVisitor() {

			final PrintWriter printer = new PrintWriter(writer);

			public void visit(final BeanPropertyInstance property, final Object current, final String path, final Object[] stack) {
				printer.println("'" + path + "' = '" + property.getValue() + "'");
				printer.flush();
			}
		};
	}
}
