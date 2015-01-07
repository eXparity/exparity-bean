package org.exparity.beans.core.visitors;

import java.io.PrintWriter;
import java.io.Writer;
import org.exparity.beans.core.BeanProperty;
import org.exparity.beans.core.BeanPropertyPath;
import org.exparity.beans.core.BeanVisitor;

/**
 * @author Stewart Bissett
 */
public class Print implements BeanVisitor {

	private final PrintWriter printer;

	public Print(final Writer writer) {
		printer = new PrintWriter(writer);
	}

	public void visit(final BeanProperty property, final Object current, final BeanPropertyPath path, final Object[] stack) {
		printer.println("'" + path + "' = '" + property.getValue() + "'");
		printer.flush();
	}
}