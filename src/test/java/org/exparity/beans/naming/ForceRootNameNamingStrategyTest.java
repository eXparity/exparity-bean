
package org.exparity.beans.naming;

import java.lang.reflect.Method;
import org.exparity.beans.core.BeanNamingStrategy;
import org.exparity.beans.core.naming.ForceRootNameNamingStrategy;
import org.exparity.beans.testutils.types.AllTypes;
import org.exparity.beans.testutils.types.Car;
import org.junit.Test;
import static org.exparity.beans.Type.type;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for the {@link ForceRootNameNamingStrategy}
 * 
 * @author Stewart Bissett
 */
public class ForceRootNameNamingStrategyTest {

	@Test
	public void canDescribeType() {
		BeanNamingStrategy delegate = mock(BeanNamingStrategy.class);
		when(delegate.describeType(Car.class)).thenReturn("car");
		assertThat(new ForceRootNameNamingStrategy(delegate, "root").describeType(Car.class), equalTo("car"));
	}

	@Test
	public void canDescribeProperty() {
		BeanNamingStrategy delegate = mock(BeanNamingStrategy.class);
		Method method = type(AllTypes.class).getAccessor("BigDecimalValue");
		when(delegate.describeProperty(method, "car")).thenReturn("car.engine");
		assertThat(new ForceRootNameNamingStrategy(delegate, "root").describeProperty(method, "car"), equalTo("car.engine"));
	}

	@Test
	public void canDescribeRoot() {
		assertThat(new ForceRootNameNamingStrategy(null, "root").describeRoot(Car.class), equalTo("root"));
	}

}
