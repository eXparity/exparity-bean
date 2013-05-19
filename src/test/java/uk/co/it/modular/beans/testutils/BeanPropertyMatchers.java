/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.testutils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import uk.co.it.modular.beans.BeanProperty;
import uk.co.it.modular.beans.BeanPropertyInstance;

/**
 * A Static factory to produce hamcrest {@link org.hamcrest.Matcher} instance for testing {@link BeanProperty}
 * 
 * @author Stewart.Bissett
 */
public abstract class BeanPropertyMatchers {

	/**
	 * Construct a Hamcrest matcher to match a {@link BeanProperty} with the expected property name and property type
	 */
	public static BeanPropertyMatcher<BeanProperty> aBeanProperty(final String propertyName, final Class<?> propertyType) {
		return new BeanPropertyMatcher<BeanProperty>(propertyType, propertyName);
	}

	/**
	 * Construct a Hamcrest matcher to match a {@link BeanProperty} with the expected property name and property type
	 */
	public static BeanPropertyMatcher<BeanPropertyInstance> aBeanPropertyInstance(final String propertyName, final Class<?> propertyType) {
		return new BeanPropertyMatcher<BeanPropertyInstance>(propertyType, propertyName);
	}

	/**
	 * @author Stewart.Bissett
	 */
	private static final class BeanPropertyMatcher<T extends BeanProperty> extends TypeSafeDiagnosingMatcher<T> {

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
		protected boolean matchesSafely(final T item, final Description mismatchDescription) {
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
