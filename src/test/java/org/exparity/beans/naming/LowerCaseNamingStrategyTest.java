package org.exparity.beans.naming;

import java.util.ArrayList;
import java.util.HashMap;
import org.exparity.beans.core.naming.LowerCaseNamingStrategy;
import org.exparity.beans.testutils.types.AllTypes;
import org.junit.Test;
import static org.exparity.beans.Type.type;
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
	public void canDescribeRoot() {
		assertThat(new LowerCaseNamingStrategy().describeRoot(AllTypes.class), equalTo("alltypes"));
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
		assertThat(new LowerCaseNamingStrategy().describeProperty(type(AllTypes.class).getAccessor("BigDecimalValue"), "get"), equalTo("bigdecimalvalue"));
	}
}
