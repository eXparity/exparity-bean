/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Factory for instances of commonly used {@link BeanVisitor} implementations
 * 
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public abstract class BeanVisitors {

	/**
	 * Print all the properties visited to {@link System#out}
	 */
	public static BeanVisitor printProperties() {
		return printPropeties(new OutputStreamWriter(System.out));
	}

	/**
	 * Print all the properties visited to the {@link Writer}
	 */
	public static BeanVisitor printPropeties(final Writer writer) {
		final PrintWriter printer = new PrintWriter(writer);
		return new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				printer.println(path + " (" + property.getTypeCanonicalName() + ")");
				printer.flush();
			}
		};
	}

	/**
	 * Print all the properties visited to {@link System#out}
	 */
	public static BeanVisitor printPropertyValues() {
		return printPropertyValues(new OutputStreamWriter(System.out));
	}

	/**
	 * Print all the properties visited to the {@link Writer}
	 */
	public static BeanVisitor printPropertyValues(final Writer writer) {
		final PrintWriter printer = new PrintWriter(writer);
		return new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				printer.println(path + " (" + property.getTypeCanonicalName() + ":" + property.getValue(current) + ")");
				printer.flush();
			}
		};
	}

}
