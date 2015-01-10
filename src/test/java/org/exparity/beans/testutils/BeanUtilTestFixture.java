
package org.exparity.beans.testutils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import org.exparity.beans.testutils.types.AllTypes;
import org.exparity.beans.testutils.types.AllTypes.EnumValues;
import org.exparity.beans.testutils.types.Car;
import org.exparity.beans.testutils.types.Engine;
import org.exparity.beans.testutils.types.Person;
import org.exparity.beans.testutils.types.Wheel;
import static java.util.Arrays.asList;

/**
 * @author Stewart.Bissett
 */
public class BeanUtilTestFixture {

	public static Person aPopulatedPerson() {
		Person bob = new Person("Bob", "Onion"), tina = new Person("Tina", "Melon"), geoff = new Person("Geoff", "Carrot");
		bob.setSiblings(Arrays.asList(tina, geoff));
		return bob;
	}

	public static Car aPopulatedCar() {
		return new Car(new Engine(new BigDecimal("3.8")), asList(new Wheel(5), new Wheel(5), new Wheel(5), new Wheel(5)));
	}

	public static AllTypes anEmptyAllTypes() {
		AllTypes a = new AllTypes();
		a.setArray(new int[0]);
		a.setCollection(new ArrayList<String>());
		a.setList(new ArrayList<String>());
		a.setMap(new HashMap<Long, String>());
		a.setSet(new HashSet<String>());
		return a;
	}

	public static AllTypes aPopulatedAllTypes() {
		AllTypes a = new AllTypes();
		a.setArray(new int[] {
				1, 2, 3
		});
		a.setBigDecimalValue(new BigDecimal("12345.67890"));
		a.setBooleanObjectValue(Boolean.TRUE);
		a.setBooleanValue(true);
		a.setByteObjectValue(new Byte((byte) 0xFF));
		a.setByteValue((byte) 0xFF);
		a.setCharObjectValue(new Character('a'));
		a.setCharValue('a');
		a.setCollection(Arrays.asList("This", "is", "a", "list"));
		a.setDateValue(new Date());
		a.setDoubleObjectValue(new Double(12345.67890));
		a.setDoubleValue(12345.67890);
		a.setEnumValue(EnumValues.VALUE_1);
		a.setFloatObjectValue(new Float(12345.67890));
		a.setFloatValue(12345.67890F);
		a.setIntegerObjectValue(new Integer(1));
		a.setIntegerValue(1);
		a.setList(asList("This", "is", "a", "list"));
		a.setLongObjectValue(new Long(1));
		a.setLongValue(1L);
		a.setMap(Collections.singletonMap(1L, "Map Entry"));
		a.setSet(new HashSet<String>(asList("This", "is", "a", "list")));
		a.setShortObjectValue(new Short((short) 1));
		a.setShortValue((short) 1);
		a.setStringValue("A String");
		return a;
	}
}
