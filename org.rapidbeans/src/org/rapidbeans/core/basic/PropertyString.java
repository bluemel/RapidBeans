/*
 * Rapid Beans Framework: PropertyString.java
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

import java.util.regex.Pattern;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyString;
import org.rapidbeans.core.util.EscapeMap;
import org.rapidbeans.core.util.StringHelper;

/**
 * A <b>String</b> bean property captures simple String values.<br/>
 * In addition it optionally enforces validation of:<br/>
 * - minimal length<br/>
 * - maximal length<br/>
 * - Java regular expression<br/>
 * Attributes<br/>
 * <b>maxlen: default = 2 147 483 647 = 2 ^32 - 1</b> specifies the maximal
 * length of the String<br/>
 * <b>minlen: default = 0</b> specifies the minimal length of the String<br/>
 * <b>pattern: default = null</b> specifies a Java regular expression that the
 * String must match<br/>
 * <b>default: default = null</b> specifies the default value<br/>
 * 
 * @author Martin Bluemel
 */
public class PropertyString extends Property {

	/**
	 * the property's String value. !!! do not initialize here because the
	 * superclass does it with the property type's default value
	 */
	private String value;

	/**
	 * constructor for a new String Property.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyString(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the String value of this Property
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * String value getter.
	 * 
	 * @return the String representation of the Property's value.<br/>
	 *         For a String this is the value itself.
	 */
	public String toString() {
		if (this.value == null) {
			return null;
		}
		final EscapeMap escapeMap = ((TypePropertyString) this.getType()).getEscapeMap();
		if (escapeMap != null) {
			return StringHelper.escape(this.value, escapeMap);
		} else {
			return this.value;
		}
	}

	/**
	 * generic value setter.
	 * 
	 * @param newValue
	 *            the new value for this property.<br/>
	 *            Must be an instance of the following class:<br/>
	 *            <b>String:</b> the String<br/>
	 */
	public void setValue(final Object newValue) {
		super.setValueWithEvents(this.value, newValue, new PropertyValueSetter() {
			public void setValue(final Object newValue) {
				if (getBean() instanceof RapidBeanImplSimple) {
					Property.setValueByReflection(getBean(), getName(), newValue);
				} else {
					value = (String) newValue;
				}
			}
		});
	}

	/**
	 * converts different classes to the Property's internal value class.<br/>
	 * For a String property this means just verifying that the given object is
	 * a String.
	 * 
	 * @param argValue
	 *            the value to convert<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>String:</b> the String value<br/>
	 * 
	 * @return a String
	 */
	public String convertValue(final Object argValue) {
		if (argValue == null) {
			return null;
		}
		String s = null;
		if (argValue instanceof String) {
			final EscapeMap escMap = ((TypePropertyString) this.getType()).getEscapeMap();
			if (escMap == null) {
				s = (String) argValue;
			} else {
				s = StringHelper.unescape((String) argValue, escMap);
			}
		} else {
			throw new ValidationException("invalid.prop.string.type", this,
					"Tried to convert value from a data type \"" + argValue.getClass().getName()
							+ "\" different to String.");
		}
		return s;
	}

	/**
	 * generic validation for the Property's value.
	 * 
	 * @param newValue
	 *            the value to validate<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>String:</b> the String to validate<br/>
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public String validate(final Object newValue) {
		final String newStringValue = (String) super.validate(newValue);
		if (!ThreadLocalValidationSettings.getValidation()) {
			return newStringValue;
		}
		if (newStringValue == null) {
			return null;
		}
		final TypePropertyString type = (TypePropertyString) this.getType();
		if ((!type.getEmptyValid()) && newStringValue.equals("")) {
			throw new ValidationException("invalid.prop.string.empty", this, "invalid empty value");
		}

		if ((!type.getEmptyValid()) || (!newStringValue.equals(""))) {

			// check against max length
			final int maxLength = type.getMaxLength();

			if ((maxLength != TypePropertyString.LENGTH_UNLIMITED) && (newStringValue.length() > maxLength)) {
				if (this.getBean() != null) {
					throw new ValidationException("invalid.prop.string.maxlen", this, "Bean \""
							+ this.getBean().getType().getName() + "::" + this.getBean().toString() + ", "
							+ "Property \"" + this.getType().getPropName() + "\": " + "value longer than max length "
							+ maxLength, new String[] { newStringValue, Integer.toString(maxLength) });
				} else {
					throw new ValidationException("invalid.prop.string.maxlen", this, "Property \""
							+ this.getType().getPropName() + "\": " + "value longer than max length " + maxLength,
							new String[] { newStringValue, Integer.toString(maxLength) });
				}
			}

			// check against min length
			final int minLength = type.getMinLength();

			if ((minLength > 0) && (newStringValue.length() < minLength)) {
				throw new ValidationException("invalid.prop.string.minlen", this, "new value shorter than"
						+ " minimal length " + minLength, new String[] { newStringValue, Integer.toString(minLength) });
			}

			// check multiple lines
			if (!type.getMultiline() && newStringValue.contains("\n")) {
				throw new ValidationException("invalid.prop.string.multiline", this,
						"String with line break is invalid since property \"" + this.getName()
								+ "\" is not multi lined.");
			}

			// check against pattern
			final Pattern pattern = type.getPattern();

			if ((pattern != null) && (newStringValue != null) && !pattern.matcher(newStringValue).matches()) {
				final Object[] oa = { newValue, pattern.toString() };
				throw new ValidationException("invalid.prop.string.pattern", this, "value does not match pattern \""
						+ pattern.pattern() + "\".", oa);
			}
		}
		return newStringValue;
	}
}
