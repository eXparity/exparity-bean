/*
 * Copyright (c) Modular IT Limited.
 */
package uk.co.it.modular.beans;

/**
 * List of properties which can control the {@link BeanInspector}
 * 
 * @author Stewart Bissett
 */
public enum BeanInspectorProperty {

	/**
	 * Prevent the inspection raising a {@link StackOverflowError} if an infinite loop is found between assosciated types
	 */
	STOP_OVERFLOW,

	/**
	 * Instruct the inspection to follow assosciations
	 */
	INSPECT_CHILDREN
}