
package com.modularit.beans;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.math.RandomUtils.*;
import static org.apache.commons.lang.time.DateUtils.addSeconds;
import java.math.BigDecimal;
import java.util.Date;
import com.modularit.beans.BeanBuilder.BeanPropertyValue;

/**
 * Implemenation of {@link com.modularit.beans.BeanBuilder.BeanPropertyValue} which returns random values for each type
 * 
 * @author Stewart Bissett
 */
public class RandomBeanPropertyValue implements BeanPropertyValue {

	private static final int MAX_STRING_LENGTH = 50;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int HOURS_PER_DAY = 24;
	private static final int DAYS_PER_YEAR = 365;
	private static final int SECONDS_IN_A_YEAR = MINUTES_PER_HOUR * HOURS_PER_DAY * DAYS_PER_YEAR;

	public String stringValue() {
		return randomAlphanumeric(MAX_STRING_LENGTH);
	}

	public Integer intValue() {
		return Integer.valueOf(nextInt());
	}

	public Short shortValue() {
		return Short.valueOf((short) nextInt(Short.MAX_VALUE));
	}

	public Long longValue() {
		return Long.valueOf(nextLong());
	}

	public Double doubleValue() {
		return Double.valueOf(nextDouble());
	}

	public Float floatValue() {
		return Float.valueOf(nextFloat());
	}

	public Boolean booleanValue() {
		return Boolean.valueOf(nextBoolean());
	}

	public Date dateValue() {
		return addSeconds(new Date(), nextInt(SECONDS_IN_A_YEAR));
	}

	public BigDecimal bigDecimalValue() {
		return new BigDecimal(doubleValue());
	}

	public Byte byteValue() {
		return (byte) nextInt(Byte.MAX_VALUE);
	}

	public Character charValue() {
		return randomAlphabetic(1).charAt(0);
	}

}
