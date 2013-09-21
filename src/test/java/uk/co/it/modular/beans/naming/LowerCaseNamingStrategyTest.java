/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans.naming;

import org.junit.Test;
import static uk.co.it.modular.beans.MethodUtils.getMethod;
import uk.co.it.modular.beans.naming.LowerCaseNamingStrategy;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Stewart Bissett
 */
public class LowerCaseNamingStrategyTest {

	@Test
	public void canDescribeAType() {
		assertThat(new LowerCaseNamingStrategy().describeType(AllTypes.class), equalTo("alltypes"));
	}

	@Test
	public void canDescribeAMethod() {
		assertThat(new LowerCaseNamingStrategy().describeProperty(getMethod(AllTypes.class, "getBigDecimalValue"), "get"), equalTo("bigdecimalvalue"));
	}
}
