
package uk.co.it.modular.beans;

/**
 * @author Stewart Bissett
 */
public abstract class BeanFunctions {

	public static BeanPropertyFunction setValue(final Object value) {
		return new BeanPropertyFunction() {

			public void apply(final BeanPropertyInstance property) {
				property.setValue(value);
			}
		};

	}
}
