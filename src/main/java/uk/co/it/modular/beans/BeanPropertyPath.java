
package uk.co.it.modular.beans;

import static org.apache.commons.lang.StringUtils.countMatches;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import org.apache.commons.lang.StringUtils;

/**
 * @author Stewart Bissett
 */
public class BeanPropertyPath {

	private static final String INDEX_PATTERN = "\\[\\w*\\]\\.";
	private static final String PATH_SEPERATOR = ".";
	private final String path;

	public BeanPropertyPath(final String path) {
		this.path = path;
	}

	/**
	 * Append the supplied path to this path to form a new instance of {@link PropertyPath}.This instance is not mutated
	 */
	public BeanPropertyPath append(final String name) {
		return StringUtils.isBlank(path) ? new BeanPropertyPath(name) : new BeanPropertyPath(path + PATH_SEPERATOR + name);
	}

	/**
	 * Append the supplied index to this path to form a new instance of {@link PropertyPath}.This instance is not mutated
	 */
	public BeanPropertyPath appendIndex(final String index) {
		return new BeanPropertyPath(path + "[" + index + "]");
	}

	/**
	 * Append the supplied index to this path to form a new instance of {@link PropertyPath}.This instance is not mutated
	 */
	public BeanPropertyPath appendIndex(final int index) {
		return new BeanPropertyPath(path + "[" + index + "]");
	}

	/**
	 * Return the path with all array indexes removed e.g. x.y.z[0] would be returned as x.y.z
	 */
	public String fullPathWithNoIndexes() {
		return path.replaceAll(INDEX_PATTERN, PATH_SEPERATOR);
	}

	/**
	 * Return <code>true</code> if this path starts with the supplied prefix
	 */
	public boolean startsWith(final String prefix) {
		return fullPathWithNoIndexes().startsWith(prefix);
	}

	/**
	 * Return the full path including indexes if present
	 */
	public String fullPath() {
		return path;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(path);
	}

	/**
	 * Return the depth of this path starting from 0 at root level. For example, if the path was x.y.z then the depth would be 2.
	 */
	public Integer depth() {
		return countMatches(path, PATH_SEPERATOR);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BeanPropertyPath)) {
			return false;
		}
		return equalsIgnoreCase(((BeanPropertyPath) obj).path, path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return "PropertyPath [" + path + "]";
	}
}
