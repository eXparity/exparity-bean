
package com.modularit.beans;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class NumbersTest {

	@Test
	public void canSumNumber() {
		Number min = new AtomicInteger(-1), max = new AtomicInteger(1);
		assertThat(Numbers.sum(max, min), equalTo(0.0));
	}

	@Test
	public void canAverageNumber() {
		Number min = -1, max = 1;
		assertThat(Numbers.average(max, min), equalTo(0.0));
	}

	@Test
	public void canMaxNumber() {
		Number min = -1, max = 1;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canMinNumber() {
		Number min = -1, max = 1;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canSumInteger() {
		Integer min = -1, max = 1;
		assertThat(Numbers.sum(max, min), equalTo(0));
	}

	@Test
	public void canAverageInteger() {
		Integer min = -1, max = 1;
		assertThat(Numbers.average(max, min), equalTo(0.0));
	}

	@Test
	public void canMaxInteger() {
		Integer min = -1, max = 1;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canMinInteger() {
		Integer min = -1, max = 1;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canSumLong() {
		Long min = -1L, max = 1L;
		assertThat(Numbers.sum(max, min), equalTo(0L));
	}

	@Test
	public void canAverageLong() {
		Long min = -1L, max = 1L;
		assertThat(Numbers.average(max, min), equalTo(0.0));
	}

	@Test
	public void canMaxLong() {
		Long min = -1L, max = 1L;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canMinLong() {
		Long min = -1L, max = 1L;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canSumDouble() {
		Double min = -1.0, max = 1.0;
		assertThat(Numbers.sum(max, min), equalTo(0.0));
	}

	@Test
	public void canAverageDouble() {
		Double min = -1.0, max = 1.0;
		assertThat(Numbers.average(max, min), equalTo(0.0));
	}

	@Test
	public void canMaxDouble() {
		Double min = -1.0, max = 1.0;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canMinDouble() {
		Double min = -1.0, max = 1.0;
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canSumBigDecimal() {
		BigDecimal min = new BigDecimal(-1.0), max = new BigDecimal(1.0);
		assertThat(Numbers.average(max, min), comparesEqualTo(new BigDecimal(0.0)));
	}

	@Test
	public void canAverageBigDecimal() {
		BigDecimal min = new BigDecimal(-1.0), max = new BigDecimal(1.0);
		assertThat(Numbers.average(max, min), comparesEqualTo(new BigDecimal(0.0)));
	}

	@Test
	public void canMaxBigDecimal() {
		BigDecimal min = new BigDecimal(-1.0), max = new BigDecimal(1.0);
		assertThat(Numbers.max(max, min), equalTo(max));
	}

	@Test
	public void canMinBigDecimal() {
		BigDecimal min = new BigDecimal(-1.0), max = new BigDecimal(1.0);
		assertThat(Numbers.min(max, min), equalTo(min));
	}

}
