/*
 * Copyright (c) Modular IT Limited.
 */

package org.exparity.beans.naming;

import java.util.ArrayList;
import java.util.HashMap;

import org.exparity.beans.naming.CamelCaseNamingStrategy;
import org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes;
import org.junit.Test;
import static org.exparity.beans.MethodUtils.getMethod;
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
