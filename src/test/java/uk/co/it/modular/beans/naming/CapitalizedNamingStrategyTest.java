/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import uk.co.it.modular.beans.naming.CapitalizedNamingStrategy;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static uk.co.it.modular.beans.MethodUtils.getMethod;

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
