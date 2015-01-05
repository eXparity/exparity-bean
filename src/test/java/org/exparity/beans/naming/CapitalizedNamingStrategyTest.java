package org.exparity.beans.naming;

import java.util.ArrayList;
import java.util.HashMap;
import org.exparity.beans.naming.CapitalizedNamingStrategy;
import org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes;
import org.junit.Test;
import static org.exparity.beans.core.MethodUtils.getMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Stewart Bissett
 */
public class CapitalizedNamingStrategyTest {

	@Test
	public void canDescribeAType() {
		assertThat(new CapitalizedNamingStrategy().describeType(AllTypes.class), equalTo("AllTypes"));
	}

	@Test
	public void canDescribeAnArrayType() {
		assertThat(new CapitalizedNamingStrategy().describeType(AllTypes[].class), equalTo("AllTypes"));
	}

	@Test
	public void canDescribeAMapType() {
		assertThat(new CapitalizedNamingStrategy().describeType(HashMap.class), equalTo("Map"));
	}

	@Test
	public void canDescribeACollectionType() {
		assertThat(new CapitalizedNamingStrategy().describeType(ArrayList.class), equalTo("Collection"));
	}

	@Test
	public void canDescribeAMethod() {
		assertThat(new CapitalizedNamingStrategy().describeProperty(getMethod(AllTypes.class, "getBigDecimalValue"), "get"), equalTo("BigDecimalValue"));
	}
}
