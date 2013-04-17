/*
 * Rapid Beans Framework: PropertyQuantity.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/22/2005
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.core.basic;

import java.lang.reflect.InvocationTargetException;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyQuantity;

/**
 * A <b>RapidQuantity</b> bean property stores a quantity of a certain type.<br/>
 * Attributes<br/>
 * <b>quantity: (mandatory)</b> specifies the bean RapidQuantity type (class)<br/>
 * <b>minval:</b> specifies the lower boundary for valid values<br/>
 * <b>maxval:</b> specifies the upper boundary for valid values<br/>
 * 
 * @author Martin Bluemel
 */
public class PropertyQuantity extends Property {

	/**
	 * the encapsulated RapidQuantity value. !!! do not initialize here because
	 * the superclass does it with the property type's default value
	 */
	private RapidQuantity value;

	/**
	 * constructor for a new RapidQuantity Property.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyQuantity(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the value of this Property as RapidQuantity
	 */
	public RapidQuantity getValue() {
		RapidQuantity value = this.value;
		if (getBean() instanceof RapidBeanImplSimple) {
			value = (RapidQuantity) Property.getValueByReflection(getBean(), getName());
		}
		return value;
	}

	/**
	 * String value getter.
	 * 
	 * @return the String representation of the Property's value.<br/>
	 *         For a RapidQuantity this is a combination of a decimal number and
	 *         an RapidEnum name.
	 */
	public String toString() {
		final RapidQuantity value = getValue();
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}

	/**
	 * Generic value setter.
	 * 
	 * @param quantityValue
	 *            the new value for this property.<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>RapidQuantity:</b> the quantity<br/>
	 *            <b>String:</b> the quantity as String: &lt;number&gt;
	 *            &lt;enum&gt;<br/>
	 */
	public void setValue(final Object newValue) {
		super.setValueWithEvents(this.value, newValue, new PropertyValueSetter() {
			public void setValue(final Object newValue) {
				if (getBean() instanceof RapidBeanImplSimple) {
					Property.setValueByReflection(getBean(), getName(), newValue);
				} else {
					value = (RapidQuantity) newValue;
				}
			}
		});
	}

	/**
	 * converts different classes to the Property's internal value class.<br/>
	 * 
	 * @param argValue
	 *            the value to convert<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>RapidQuantity:</b> the quantity<br/>
	 *            <b>String:</b> the quantity as String: &lt;number&gt;
	 *            &lt;enum&gt;<br/>
	 * 
	 * @return a RapidQuantity
	 */
	public RapidQuantity convertValue(final Object argValue) {

		if (argValue == null) {
			return null;
		}

		RapidQuantity quantity = null;

		if (argValue instanceof RapidQuantity) {
			quantity = (RapidQuantity) argValue;
		} else if (argValue instanceof String) {
			try {
				quantity = RapidQuantity.createInstance(((TypePropertyQuantity) this.getType()).getQuantitytype()
						.getName(), (String) argValue);
			} catch (RapidBeansRuntimeException e) {
				Throwable t1 = e.getCause();
				if (t1 != null && t1 instanceof InvocationTargetException) {
					Throwable t2 = t1.getCause();
					if (t2 != null && t2 instanceof ValidationException) {
						throw (ValidationException) t2;
					}
				}
			}
		} else {
			throw new ValidationException("invalid.prop.quantity.type", this, "invalid data type \""
					+ argValue.getClass().getName() + "\".\nOnly \"PropertyQuantity\" and \"String\" are valid types.");
		}

		return quantity;
	}

	/**
	 * generic validation for the Property's value.
	 * 
	 * @param quantityValue
	 *            the value to validate<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>RapidQuantity:</b> the quantity<br/>
	 *            <b>String:</b> the quantity as String: &lt;number&gt;
	 *            &lt;enum&gt;<br/>
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public RapidQuantity validate(final Object quantityValue) {

		final RapidQuantity quantity = (RapidQuantity) super.validate(quantityValue);
		if (!ThreadLocalValidationSettings.getValidation()) {
			return quantity;
		}
		if (quantityValue == null) {
			return null;
		}

		final TypePropertyQuantity quantPropType = (TypePropertyQuantity) this.getType();
		// check RapidQuantity Type
		if (quantity.getType() != quantPropType.getQuantitytype()) {
			final String[] sa = { quantity.getType().getName(), quantity.toString(),
					quantPropType.getQuantitytype().getName() };
			throw new ValidationException("invalid.prop.quantity.quantitytype", this, "Wrong type \""
					+ quantity.getType().getName() + "\" for new quantity \"" + quantity.toString() + "\"\n"
					+ "Expected quantity type \"" + quantPropType.getQuantitytype().getName() + "\"", sa);
		}

		// check maximal value
		if (quantPropType.getMaxVal() != null) {
			final String[] sa = { quantity.toString(), quantPropType.getMaxVal().toString() };
			if (quantity.compareTo(quantPropType.getMaxVal()) == 1) {
				throw new ValidationException("invalid.prop.quantity.greater", this, "invalid quantity \"" + quantity
						+ " greater than maximal value \"" + quantPropType.getMaxVal().toString() + "\".", sa);
			}
		}

		// check minimal value
		if (quantPropType.getMinVal() != null) {
			final String[] sa = { quantity.toString(), quantPropType.getMinVal().toString() };
			if (quantity.compareTo(quantPropType.getMinVal()) == -1) {
				throw new ValidationException("invalid.prop.quantity.greater", this, "invalid quantity \"" + quantity
						+ " lower than minimal value \"" + quantPropType.getMinVal().toString() + "\".", sa);
			}
		}
		return quantity;
	}
}
