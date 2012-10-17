
package com.modularit.beans;

import static com.modularit.beans.BeanFunctions.*;
import static com.modularit.beans.BeanProperties.intProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import java.util.Arrays;
import org.junit.Test;
import com.modularit.beans.BeanUtilTestFixture.Car;
import com.modularit.beans.BeanUtilTestFixture.Wheel;

/**
 * @author Stewart Bissett
 */
public class BeanFunctionsTest {

	@Test
	public void canSumProperties() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(sum(car, intProperty("diameter")).longValue(), equalTo(6L));
	}

	@Test
	public void canAverageProperties() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(average(car, intProperty("diameter")).longValue(), equalTo(3L));
	}

	@Test
	public void canCountProperties() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(count(car, intProperty("diameter")), equalTo(2));
	}

	@Test
	public void canFindMinValueOfProperty() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(min(car, intProperty("diameter")), equalTo(2));
	}

	@Test
	public void canFindMaxValueOfProperty() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(max(car, intProperty("diameter")), equalTo(4));
	}

	// @Test
	// public void canFindFirstValueMatchingProperty() {
	// Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
	// assertThat(first(car, intProperty("diameter"), hasValue(2)), equalTo(2)));
	// }
	//
	// @Test
	// public void canFindLastValueMatchingProperty() {
	// Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
	// assertThat(last(car, intProperty("diameter"), hasValue(2)), equalTo(2)));
	// }
	//
	// @Test
	// public void canFindProperties() {
	// Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
	// assertThat(sum(car, longProperty("diameter")), equalTo(3L));
	// assertThat(find(car, "diameter"), hasItems(2));
	// }

}
