
package com.modularit.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Stewart Bissett
 */
public abstract class Iterables {

	public static <P extends Comparable<? super P>> P min(final Collection<P> comparables) {
		P min = null;
		for (P comparable : comparables) {
			if (min == null) {
				min = comparable;
			} else if (comparable != null && comparable.compareTo(min) < 0) {
				min = comparable;
			}
		}
		return min;
	}

	public static <P extends Comparable<? super P>> P max(final Collection<P> comparables) {
		P max = null;
		for (P comparable : comparables) {
			if (max == null) {
				max = comparable;
			} else if (comparable != null && comparable.compareTo(max) > 0) {
				max = comparable;
			}
		}
		return max;
	}

	public static <P> List<P> nonnulls(final Collection<P> nullables) {
		List<P> nonnulls = new ArrayList<P>(nullables.size());
		for (P nullable : nullables) {
			if (nullable != null) {
				nonnulls.add(nullable);
			}
		}
		return nonnulls;
	}

}
