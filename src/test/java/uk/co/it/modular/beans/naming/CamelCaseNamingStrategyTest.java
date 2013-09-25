/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import static uk.co.it.modular.beans.MethodUtils.getMethod;
import uk.co.it.modular.beans.naming.CamelCaseNamingStrategy;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Stewart Bissett
 */
public class CamelCaseNamingStrategyTest {

	@Test
	public void canDescribeAType() {
		assertThat(new CamelCaseNamingStrategy().describeType(AllTypes.class), equalTo("allTypes"));
	}

	@Test
	public void canDescribeAnArrayType() {
		assertThat(new CamelCaseNamingStrategy().describeType(AllTypes[].class), equalTo("allTypes"));
	}

	@Test
	public void canDescribeAMapType() {
		assertThat(new CamelCaseNamingStrategy().describeType(HashMap.class), equalTo("map"));
	}

	@Test
	public void canDescribeACollectionType() {
		assertThat(new CamelCaseNamingStrategy().describeType(ArrayList.class), equalTo("collection"));
	}

	@Test
	public void canDescribeAMethod() {
		assertThat(new CamelCaseNamingStrategy().describeProperty(getMethod(AllTypes.class, "getBigDecimalValue"), "get"), equalTo("bigDecimalValue"));
	}
}
