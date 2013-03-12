/*
 * Rapid Beans Framework: PropertyInteger.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/27/2005
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

import java.math.BigInteger;

import org.rapidbeans.core.exception.PropValueNullException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyInteger;

/**
 * An <b>Integer</b> property encapsulates Integer values.<br/>
 * In addition enforces validation of:<br/>
 * - minimal value<br/>
 * - maximal value<br/>
 * 
 * @author Martin Bluemel
 */
public final class PropertyInteger extends PropertyNumber {

	/**
	 * the Integer value (Byte, Short, Integer, Long, BigInteger). !!! do not
	 * initialize here because the superclass does it with the property type's
	 * default value
	 */
	private Number value;

	/**
	 * constructor for a new Integer Property.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyInteger(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the value of this Property as Number (Byte, Short, Integer, Long,
	 *         BigInteger)
	 */
	public Number getValue() {
		return this.value;
	}

	/**
	 * convenience value getter.
	 * 
	 * @return the value of this Property as primitive int type
	 */
	public long getValueLong() {
		if (this.value == null) {
			throw new PropValueNullException("value for property not defined");
		}
		return this.value.longValue();
	}

	/**
	 * convenience value getter.
	 * 
	 * @return the value of this Property as primitive int type
	 */
	public int getValueInt() {
		if (this.value == null) {
			throw new PropValueNullException("value for property not defined");
		}
		return this.value.intValue();
	}

	/**
	 * convenience value getter.
	 * 
	 * @return the value of this Property as primitive short type
	 */
	public short getValueShort() {
		if (this.value == null) {
			throw new PropValueNullException("value for property not defined");
		}
		return this.value.shortValue();
	}

	/**
	 * convenience value getter.
	 * 
	 * @return the value of this Property as primitive byte type
	 */
	public byte getValueByte() {
		if (this.value == null) {
			throw new PropValueNullException("value for property not defined");
		}
		return this.value.byteValue();
	}

	/**
	 * String value getter.
	 * 
	 * @return the String representation of the Property's value. For an Integer
	 *         this is a decimal number
	 */
	public String toString() {
		if (this.value == null) {
			return null;
		}
		return this.value.toString();
	}

	/**
	 * generic value setter. Accepts the following data types:<br/>
	 * 
	 * @param newValue
	 *            the new integer value for this property.<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>Number (Long, Integer, Short, Byte):</b> the integer value
	 *            itself<br/>
	 *            <b>String:</b> the integer as decimal string<br/>
	 */
	public void setValue(final Object newValue) {
		super.setValueWithEvents(this.value, newValue, new PropertyValueSetter() {
			public void setValue(final Object newValue) {
				if (getBean() instanceof RapidBeanImplSimple) {
					Property.setValueByReflection(getBean(), getName(), newValue);
				} else {
					value = (Number) newValue;
				}
			}
		});
	}

	/**
	 * converter.
	 * 
	 * @param integerValue
	 *            the object to convert Must be an instance of the following
	 *            classes:<br/>
	 *            <b>Integer:</b> the integer value itself<br/>
	 *            <b>String:</b> the integer as decimal string<br/>
	 * 
	 * @return the converted value
	 */
	public Number convertValue(final Object integerValue) {
		final TypePropertyInteger type = (TypePropertyInteger) this.getType();
		return type.convertValue(this, integerValue);
	}

	/**
	 * generic validation for the Property's value.
	 * 
	 * @param newValue
	 *            the value to validate<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>Integer:</b> the integer value itself<br/>
	 *            <b>String:</b> the integer as decimal string<br/>
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public Number validate(final Object newValue) {
		final Number newNumberValue = (Number) super.validate(newValue);
		if (!ThreadLocalValidationSettings.getValidation()) {
			return newNumberValue;
		}
		if (newValue == null) {
			return null;
		}
		// check against max boundary
		final Number maxValue = ((TypePropertyInteger) this.getType()).getMaxValue();
		if (maxValue != null) {
			boolean exceeded = false;
			if (newNumberValue instanceof BigInteger) {
				exceeded = ((BigInteger) newNumberValue).compareTo((BigInteger) maxValue) > 0;
			} else if (newNumberValue instanceof Long) {
				exceeded = ((Long) newNumberValue).compareTo((Long) maxValue) > 0;
			} else if (newNumberValue instanceof Integer) {
				exceeded = ((Integer) newNumberValue).compareTo((Integer) maxValue) > 0;
			} else if (newNumberValue instanceof Short) {
				exceeded = ((Short) newNumberValue).compareTo((Short) maxValue) > 0;
			} else if (newNumberValue instanceof Byte) {
				exceeded = ((Byte) newNumberValue).compareTo((Byte) maxValue) > 0;
			}
			if (exceeded) {
				throw new ValidationException("invalid.prop.integer.maxval", this, "invalid integer \""
						+ newNumberValue.toString() + "\" greater than maximal value \"" + maxValue.toString() + "\".",
						new Object[] { newNumberValue, maxValue });
			}
		}

		// check against min boundary
		final Number minValue = ((TypePropertyInteger) this.getType()).getMinValue();
		if (minValue != null) {
			boolean exceeded = false;
			if (newNumberValue instanceof BigInteger) {
				exceeded = ((BigInteger) newNumberValue).compareTo((BigInteger) minValue) < 0;
			} else if (newNumberValue instanceof Long) {
				exceeded = ((Long) newNumberValue).compareTo((Long) minValue) < 0;
			} else if (newNumberValue instanceof Integer) {
				exceeded = ((Integer) newNumberValue).compareTo((Integer) minValue) < 0;
			} else if (newNumberValue instanceof Short) {
				exceeded = ((Short) newNumberValue).compareTo((Short) minValue) < 0;
			} else if (newNumberValue instanceof Byte) {
				exceeded = ((Byte) newNumberValue).compareTo((Byte) minValue) < 0;
			}
			if (exceeded) {
				throw new ValidationException("invalid.prop.integer.minval", this, "invalid integer \""
						+ newNumberValue.toString() + "\" lower than minimal value \"" + minValue.toString() + "\".",
						new Object[] { newNumberValue, minValue });
			}
		}
		return newNumberValue;
	}
}
