/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

/**
 * @author Stewart Bissett
 */
public class PropertyPath {

	private final String path;

	public PropertyPath(final String path) {
		this.path = path;
	}

	public PropertyPath append(final String name) {
		return new PropertyPath(path + "." + name);
	}

	public PropertyPath appendIndex(final String index) {
		return new PropertyPath(path + "[" + index + "]");
	}

	public PropertyPath appendIndex(final int index) {
		return new PropertyPath(path + "[" + index + "]");
	}

	public String noIndexes() {
		return path.replaceAll("\\[\\w*\\]\\.", ".");
	}

	public boolean startsWith(final String prefix) {
		return noIndexes().startsWith(prefix);
	}

	public String fullPath() {
		return path;
	}

	@Override
	public String toString() {
		return "PropertyPath [" + path + "]";
	}
}
