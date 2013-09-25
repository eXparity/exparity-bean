/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static uk.co.it.modular.beans.MethodUtils.getMethod;

/**
 * @author Stewart Bissett
 */
public class LowerCaseNamingStrategyTest {

	@Test
	public void canDescribeAType() {
		assertThat(new LowerCaseNamingStrategy().describeType(AllTypes.class), equalTo("alltypes"));
	}

	@Test
	public void canDescribeAnArrayType() {
		assertThat(new LowerCaseNamingStrategy().describeType(AllTypes[].class), equalTo("alltypes"));
	}

	@Test
	public void canDescribeAMapType() {
		assertThat(new LowerCaseNamingStrategy().describeType(HashMap.class), equalTo("map"));
	}

	@Test
	public void canDescribeACollectionType() {
		assertThat(new LowerCaseNamingStrategy().describeType(ArrayList.class), equalTo("collection"));
	}

	@Test
	public void canDescribeAMethod() {
		assertThat(new LowerCaseNamingStrategy().describeProperty(getMethod(AllTypes.class, "getBigDecimalValue"), "get"), equalTo("bigdecimalvalue"));
	}
}
