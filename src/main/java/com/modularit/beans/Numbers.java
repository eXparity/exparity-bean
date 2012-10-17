
package com.modularit.beans;

import static com.modularit.beans.Iterables.nonnulls;
import static java.util.Arrays.asList;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Helper methods for dealing with collections of numbers or performing operations on numberinstances
 * 
 * @author Stewart Bissett
 */
public abstract class Numbers {

	/**
	 * Sum all numbers in a collection of Integers
	 */
	public static Number sum(final Number... numbers) {
		return sum(asList(numbers), new NumberOperator());
	}

	/**
	 * Find the avaerage of all non-null numbers in a collection of Integers or return 0 if there are no non-null numbers
	 */
	public static Double average(final Number... numbers) {
		return average(asList(numbers), new NumberOperator());
	}

	/**
	 * Find the max of all non-null numbers in a collection of Numbers
	 */
	public static Number max(final Number... numbers) {
		Number max = null;
		for (Number number : numbers) {
			if (max == null) {
				max = number;
			} else if (number != null && number.doubleValue() > max.doubleValue()) {
				max = number;
			}
		}
		return max;
	}

	/**
	 * Find the min of all non-null numbers in a collection of Integers
	 */
	public static Number min(final Number... numbers) {
		Number min = null;
		for (Number number : numbers) {
			if (min == null) {
				min = number;
			} else if (number != null && number.doubleValue() < min.doubleValue()) {
				min = number;
			}
		}
		return min;
	}

	/**
	 * Sum all numbers in a collection of Integers
	 */
	public static Integer sum(final Integer... numbers) {
		return sum(asList(numbers), new IntegerOperator());
	}

	/**
	 * Find the avaerage of all non-null numbers in a collection of Integers or return 0 if there are no non-null numbers
	 */
	public static Double average(final Integer... numbers) {
		return average(asList(numbers), new IntegerOperator());
	}

	/**
	 * Find the max of all non-null numbers in a collection of Integers
	 */
	public static Integer max(final Integer... numbers) {
		return Iterables.max(asList(numbers));
	}

	/**
	 * Find the min of all non-null numbers in a collection of Integers
	 */
	public static Integer min(final Integer... numbers) {
		return Iterables.min(asList(numbers));
	}

	/**
	 * Sum all numbers in a collection of Longs
	 */
	public static Long sum(final Long... numbers) {
		return sum(asList(numbers), new LongOperator());
	}

	/**
	 * Find the avaerage of all non-null numbers in a collection of Longs or return 0 if there are no non-null numbers
	 */
	public static Double average(final Long... numbers) {
		return average(asList(numbers), new LongOperator());
	}

	/**
	 * Find the max of all non-null numbers in a collection of Longs
	 */
	public static Long max(final Long... numbers) {
		return Iterables.max(asList(numbers));
	}

	/**
	 * Find the min of all non-null numbers in a collection of Longs
	 */
	public static Long min(final Long... numbers) {
		return Iterables.min(asList(numbers));
	}

	/**
	 * Sum all numbers in a collection of Doubles
	 */
	public static Double sum(final Double... numbers) {
		return sum(asList(numbers), new DoubleOperator());
	}

	/**
	 * Find the avaerage of all non-null numbers in a collection of Doubles or return 0 if there are no non-null numbers
	 */
	public static Double average(final Double... numbers) {
		return average(asList(numbers), new DoubleOperator());
	}

	/**
	 * Find the max of all non-null numbers in a collection of Doubles
	 */
	public static Double max(final Double... numbers) {
		return Iterables.max(asList(numbers));
	}

	/**
	 * Find the min of all non-null numbers in a collection of Doubles
	 */
	public static Double min(final Double... numbers) {
		return Iterables.min(asList(numbers));
	}

	/**
	 * Sum all numbers in a collection of BigDecimals
	 */
	public static BigDecimal sum(final BigDecimal... numbers) {
		return sum(asList(numbers), new BigDecimalOperator());
	}

	/**
	 * Find the avaerage of all non-null numbers in a collection of BigDecimals or return 0 if there are no non-null numbers
	 */
	public static BigDecimal average(final BigDecimal... numbers) {
		return average(asList(numbers), new BigDecimalOperator());
	}

	/**
	 * Find the max of all non-null numbers in a collection of BigDecimals
	 */
	public static BigDecimal max(final BigDecimal... numbers) {
		return Iterables.max(asList(numbers));
	}

	/**
	 * Find the min of all non-null numbers in a collection of BigDecimals
	 */
	public static BigDecimal min(final BigDecimal... numbers) {
		return Iterables.min(asList(numbers));
	}

	/**
	 * Interface to allow encapsulation of the operators applicable to a type
	 */
	public interface NumericalOperator<N extends Number, D extends Number> {

		/**
		 * Add 2 instances of T together
		 */
		public N add(final N lhs, final N rhs);

		/**
		 * Subtract one instance of T from another
		 */
		public N subtract(final N lhs, final N rhs);

		/**
		 * Multiply 2 instances of T together
		 */
		public N multiply(final N lhs, final N rhs);

		/**
		 * Divide an instance of T by another instance of T
		 */
		public D divide(final N lhs, final N rhs);

		/**
		 * Divide an instance of T by an integer
		 */
		public D divide(final N lhs, final int rhs);
	}

	/**
	 * Generic summing algorithm
	 */
	public static <T extends Number> T sum(final Collection<T> numbers, final NumericalOperator<T, ?> operator) {
		if (numbers == null) {
			throw new IllegalArgumentException("Expected a collection of numbers, but was supplied null;");
		} else {
			T total = null;
			for (T number : numbers) {
				total = number != null ? total != null ? operator.add(number, total) : number : total;
			}
			return total;
		}
	}

	/**
	 * Generic averaging algorithm
	 */
	public static <N extends Number, D extends Number> D average(final Collection<N> numbers, final NumericalOperator<N, D> operator) {
		if (numbers == null) {
			throw new IllegalArgumentException("Expected a collection of numbers, but was supplied null;");
		} else {
			List<N> nonnulls = nonnulls(numbers);
			if (nonnulls.isEmpty()) {
				return null;
			} else {
				return operator.divide(sum(nonnulls, operator), nonnulls.size());
			}
		}
	}

	/**
	 * Implementation of {@link NumericalOperator} to support Integers
	 */
	public static class IntegerOperator implements NumericalOperator<Integer, Double> {

		public Integer add(final Integer lhs, final Integer rhs) {
			checkForNull(lhs, rhs);
			return lhs + rhs;
		}

		public Integer subtract(final Integer lhs, final Integer rhs) {
			checkForNull(lhs, rhs);
			return lhs - rhs;
		}

		public Integer multiply(final Integer lhs, final Integer rhs) {
			checkForNull(lhs, rhs);
			return lhs * rhs;
		}

		public Double divide(final Integer lhs, final Integer rhs) {
			checkForNull(lhs, rhs);
			return lhs.doubleValue() / rhs;
		}

		public Double divide(final Integer lhs, final int rhs) {
			return divide(lhs, rhs);
		}
	}

	/**
	 * Implementation of {@link NumericalOperator} to support Longs
	 */
	public static class LongOperator implements NumericalOperator<Long, Double> {

		public Long add(final Long lhs, final Long rhs) {
			checkForNull(lhs, rhs);
			return lhs + rhs;
		}

		public Long subtract(final Long lhs, final Long rhs) {
			checkForNull(lhs, rhs);
			return lhs - rhs;
		}

		public Long multiply(final Long lhs, final Long rhs) {
			checkForNull(lhs, rhs);
			return lhs * rhs;
		}

		public Double divide(final Long lhs, final Long rhs) {
			checkForNull(lhs, rhs);
			return lhs.doubleValue() / rhs;
		}

		public Double divide(final Long lhs, final int rhs) {
			return divide(lhs, rhs);
		}
	}

	/**
	 * Implementation of {@link NumericalOperator} to support Doubles
	 */
	public static class DoubleOperator implements NumericalOperator<Double, Double> {

		public Double add(final Double lhs, final Double rhs) {
			checkForNull(lhs, rhs);
			return lhs + rhs;
		}

		public Double subtract(final Double lhs, final Double rhs) {
			checkForNull(lhs, rhs);
			return lhs - rhs;
		}

		public Double multiply(final Double lhs, final Double rhs) {
			checkForNull(lhs, rhs);
			return lhs * rhs;
		}

		public Double divide(final Double lhs, final Double rhs) {
			checkForNull(lhs, rhs);
			return lhs / rhs;
		}

		public Double divide(final Double lhs, final int rhs) {
			return divide(lhs, rhs);
		}
	}

	/**
	 * Implementation of {@link NumericalOperator} to support BigDecimals
	 */
	public static class BigDecimalOperator implements NumericalOperator<BigDecimal, BigDecimal> {

		public BigDecimal add(final BigDecimal lhs, final BigDecimal rhs) {
			checkForNull(lhs, rhs);
			return lhs.add(rhs);
		}

		public BigDecimal subtract(final BigDecimal lhs, final BigDecimal rhs) {
			checkForNull(lhs, rhs);
			return lhs.subtract(rhs);
		}

		public BigDecimal multiply(final BigDecimal lhs, final BigDecimal rhs) {
			checkForNull(lhs, rhs);
			return lhs.multiply(rhs);
		}

		public BigDecimal divide(final BigDecimal lhs, final BigDecimal rhs) {
			checkForNull(lhs, rhs);
			return lhs.divide(rhs);
		}

		public BigDecimal divide(final BigDecimal lhs, final int rhs) {
			return divide(lhs, rhs);
		}
	}

	/**
	 * Implementation of {@link NumericalOperator} to support Numbers.</p>
	 * 
	 * <em>Warning: This method will use the double value from the Number opening the possibility of rounding errors</em></p>
	 */
	public static class NumberOperator implements NumericalOperator<Number, Double> {

		public Number add(final Number lhs, final Number rhs) {
			checkForNull(lhs, rhs);
			return lhs.doubleValue() + rhs.doubleValue();
		}

		public Number subtract(final Number lhs, final Number rhs) {
			checkForNull(lhs, rhs);
			return lhs.doubleValue() - rhs.doubleValue();
		}

		public Number multiply(final Number lhs, final Number rhs) {
			checkForNull(lhs, rhs);
			return lhs.doubleValue() * rhs.doubleValue();
		}

		public Double divide(final Number lhs, final Number rhs) {
			checkForNull(lhs, rhs);
			return lhs.doubleValue() / rhs.doubleValue();
		}

		public Double divide(final Number lhs, final int rhs) {
			checkForNull(lhs, rhs);
			return lhs.doubleValue() / rhs;
		}
	}

	private static <T> void checkForNull(final T lhs, final T rhs) {
		if (lhs == null) {
			throw new IllegalArgumentException("Expected a non-null lhs argument");
		} else if (rhs == null) {
			throw new IllegalArgumentException("Expected a non-null lhs argument");
		}
	}

}
