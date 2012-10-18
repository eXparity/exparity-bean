
package com.modularit.beans;

import static com.modularit.beans.BeanFunctions.*;
import static com.modularit.beans.BeanPredicates.hasValue;
import static com.modularit.beans.BeanProperties.intProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Test;
import com.modularit.beans.testutils.BeanUtilTestFixture.Car;
import com.modularit.beans.testutils.BeanUtilTestFixture.Wheel;

/**
 * @author Stewart Bissett
 */
public class BeanFunctionsTest {

	@Test
	public void canSum() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(sum(car, intProperty("diameter")).longValue(), equalTo(6L));
	}

	@Test
	public void canAverage() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(average(car, intProperty("diameter")).longValue(), equalTo(3L));
	}

	@Test
	public void canCount() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(count(car, intProperty("diameter")), equalTo(2));
	}

	@Test
	public void canMin() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(min(car, intProperty("diameter")), equalTo(2));
	}

	@Test
	public void canMax() {
		Car car = new Car(null, Arrays.asList(new Wheel(2), new Wheel(4)));
		assertThat(max(car, intProperty("diameter")), equalTo(4));
	}

	@Test
	public void canFirst() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(first(car, intProperty("diameter")).getInstance(), equalTo((Object) first));
	}

	@Test
	public void canFirstWithPredicate() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(first(car, intProperty("diameter"), hasValue(4)).getInstance(), equalTo((Object) last));
	}

	@Test
	public void canFirstNothing() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(first(car, intProperty("diameter"), hasValue(100)), Matchers.nullValue());
	}

	@Test
	public void canLast() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(last(car, intProperty("diameter")).getInstance(), equalTo((Object) last));
	}

	@Test
	public void canLastWithPredicate() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(last(car, intProperty("diameter"), hasValue(2)).getInstance(), equalTo((Object) first));
	}

	@Test
	public void canLastNothing() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		assertThat(last(car, intProperty("diameter"), hasValue(100)), Matchers.nullValue());
	}

	@Test
	public void canFind() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanProperty> found = find(car, hasValue(4));
		assertThat(found, hasSize(1));
		assertThat(found.get(0).getInstance(), equalTo((Object) last));
	}

	@Test
	public void canFindProperty() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanProperty> found = find(car, intProperty("diameter"));
		assertThat(found, hasSize(2));
		assertThat(found.get(0).getInstance(), equalTo((Object) first));
		assertThat(found.get(1).getInstance(), equalTo((Object) last));
	}

	@Test
	public void canFindWithPredicate() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanProperty> found = find(car, intProperty("diameter"), hasValue(4));
		assertThat(found, hasSize(1));
		assertThat(found.get(0).getInstance(), equalTo((Object) last));
	}

	@Test
	public void canFindNothing() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<BeanProperty> found = find(car, hasValue(100));
		assertThat(found, hasSize(0));
	}

	@Test
	public void canCollectProperty() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<Integer> found = collect(car, intProperty("diameter"));
		assertThat(found, hasItem(first.getDiameter()));
		assertThat(found, hasItem(last.getDiameter()));
	}

	@Test
	public void canCollectWithPredicate() {
		Wheel first = new Wheel(2), last = new Wheel(4);
		Car car = new Car(null, Arrays.asList(first, last));
		List<Integer> found = collect(car, intProperty("diameter"), hasValue(4));
		assertThat(found, hasItem(last.getDiameter()));
	}

}
