/*
 * Rapid Beans Framework: PropertyBoolean.java
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

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyBoolean;

/**
 * A <b>Boolean</b> bean property captures Boolean values.<br/>
 * In addition enforces validation.<br/>
 * 
 * @author Martin Bluemel
 */
public class PropertyBoolean extends Property {

	/**
	 * The Boolean value. !!! do not initialize here because the superclass does
	 * it with the property type's default value
	 */
	private Boolean value;

	/**
	 * constructor for a new Boolean Property.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyBoolean(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the value of this Property as java.lang.Integer
	 */
	public Boolean getValue() {
		Boolean val = null;
		if (getBean() instanceof RapidBeanImplSimple) {
			val = (Boolean) Property.getValueByReflection(getBean(), getName());
		} else {
			val = this.value;
		}
		return val;
	}

	/**
	 * convenience value getter.
	 * 
	 * @return the value of this Property as primitve boolean type
	 */
	public boolean getValueBoolean() {
		if (this.value == null) {
			throw new RapidBeansRuntimeException("value for property not defined");
		}
		return this.value;
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
		return Boolean.toString(this.value);
	}

	/**
	 * generic value setter. Accepts the following data types:<br/>
	 * 
	 * @param newValue
	 *            the new value for this property.<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>Boolean:</b> the boolean value itself<br/>
	 *            <b>String:</b> the boolean as string { 'false' | 'true' }<br/>
	 */
	public void setValue(final Object newValue) {
		super.setValueWithEvents(this.value, newValue, new PropertyValueSetter() {
			public void setValue(final Object newValue) {
				if (getBean() instanceof RapidBeanImplSimple) {
					Property.setValueByReflection(getBean(), getName(), newValue);
				} else {
					value = (Boolean) newValue;
				}
			}
		});
	}

	/**
	 * converter.
	 * 
	 * @param booleanValue
	 *            the object to convert. It must be an instance of the following
	 *            classes: <il> <li><b>Boolean:</b> the boolean value itself<br/>
	 *            </li> <li><b>String:</b> the boolean as string { 'false' | 'true' }</li> </il>
	 * 
	 * @return the converted value
	 */
	public Boolean convertValue(final Object booleanValue) {
		return ((TypePropertyBoolean) this.getType()).convertValue(booleanValue);
	}

	/**
	 * generic validation for the Property's value.
	 * 
	 * @param newValue
	 *            the value to validate<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>Boolean:</b> the boolean value itself<br/>
	 *            <b>String:</b> the boolean as string { 'false' | 'true' }<br/>
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public Boolean validate(final Object newValue) {
		return (Boolean) super.validate(newValue);
	}
}
