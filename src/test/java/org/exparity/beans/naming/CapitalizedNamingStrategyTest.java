
package org.exparity.beans.naming;

import java.util.ArrayList;
import java.util.HashMap;
import org.exparity.beans.testutils.BeanUtilTestFixture.AllTypes;
import org.junit.Test;
import static org.exparity.beans.Type.type;
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
	public void canDescribeRoot() {
		assertThat(new CapitalizedNamingStrategy().describeRoot(AllTypes.class), equalTo("AllTypes"));
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
		assertThat(new CapitalizedNamingStrategy().describeProperty(type(AllTypes.class).getAccessor("BigDecimalValue"), "get"), equalTo("BigDecimalValue"));
	}
}
