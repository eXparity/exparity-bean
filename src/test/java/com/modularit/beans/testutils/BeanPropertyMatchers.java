/*
 * Copyright (c) Modular IT Limited.
 */

package com.modularit.beans.testutils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import com.modularit.beans.BeanProperty;

/**
 * A Static factory to produce hamcrest {@link org.hamcrest.Matcher} instance for testing {@link BeanProperty}
 * 
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public abstract class BeanPropertyMatchers {

	/**
	 * Construct a Hamcrest matcher to match a {@link BeanProperty} with the expected property name and property type
	 */
	public static BeanPropertyMatcher aBeanProperty(final String propertyName, final Class<?> propertyType) {
		return new BeanPropertyMatcher(propertyType, propertyName);
	}

	/**
	 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
	 */
	private static final class BeanPropertyMatcher extends TypeSafeDiagnosingMatcher<BeanProperty> {

		private final Class<?> propertyType;
		private final String propertyName;

		private BeanPropertyMatcher(final Class<?> propertyType, final String propertyName) {
			this.propertyType = propertyType;
			this.propertyName = propertyName;
		}

		public void describeTo(final Description description) {
			description.appendText(createDescription(propertyName, propertyType));
		}

		@Override
		protected boolean matchesSafely(final BeanProperty item, final Description mismatchDescription) {
			if (!item.getName().equals(propertyName)) {
				mismatchDescription.appendText(createDescription(item.getName(), item.getType()));
				return false;
			} else if (!item.isType(propertyType)) {
				mismatchDescription.appendText(createDescription(item.getName(), item.getType()));
				return false;
			} else {
				return true;
			}
		}

		private String createDescription(final String propertyName, final Class<?> propertyType) {
			return "Bean Property '" + propertyName + "' of type '" + propertyType.getCanonicalName() + "'";
		}
	}

}
