/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.co.it.modular.beans.BeanBuilder.aRandomInstanceOf;
import static uk.co.it.modular.beans.BeanUtils.visitGraph;
import org.hamcrest.Matchers;
import org.junit.Test;
import uk.co.it.modular.beans.BeanProperty;
import uk.co.it.modular.beans.BeanVisitor;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.AllTypes;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Car;

/**
 * @author Stewart.Bissett
 */
public class BeanBuilderTest {

	@Test
	public void canRandomlyFillAGraph() {
		Car car = aRandomInstanceOf(Car.class).build();
		assertThat(car.getEngine(), Matchers.notNullValue());
		assertThat(car.getEngine().getCapacity(), Matchers.notNullValue());
		assertThat(car.getWheels().size(), Matchers.greaterThan(0));
		assertThat(car.getWheels().get(0).getDiameter(), Matchers.notNullValue());
	}

	@Test
	public void canRandomlyFillASimpleObject() {
		AllTypes allTypes = aRandomInstanceOf(AllTypes.class).build();
		visitGraph(allTypes, new BeanVisitor() {

			public void visit(final BeanProperty property, final Object current, final String path, final Object[] stack) {
				assertThat("Expected " + property + " to not be null", property.getValue(), Matchers.notNullValue());
			}
		});
	}

}
