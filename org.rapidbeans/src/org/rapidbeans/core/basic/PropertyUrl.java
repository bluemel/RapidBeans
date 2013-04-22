/*
 * Rapid Beans Framework: PropertyUrl.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 04/07/2010
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

import java.net.MalformedURLException;
import java.net.URL;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;

/**
 * An <b>URL</b> bean property captures URL values.<br/>
 * <br/>
 * <b>default: default = null</b> specifies the default value<br/>
 * 
 * @author Martin Bluemel
 */
public class PropertyUrl extends Property {

	/**
	 * the property's URL value. !!! do not initialize here because the
	 * superclass does it with the property type's default value
	 */
	private URL value;

	/**
	 * constructor for a new String Property.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyUrl(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the String value of this Property
	 */
	public URL getValue() {
		URL value = this.value;
		if (getBean() instanceof RapidBeanImplSimple) {
			value = (URL) Property.getValueFieldByReflection(getBean(), getName());
		}
		return value;
	}

	/**
	 * String value getter.
	 * 
	 * @return the String representation of the Property's value.<br/>
	 *         For a String this is the value itself.
	 */
	public String toString() {
		final URL value = getValue();
		if (value == null) {
			return null;
		} else {
			return value.toString();
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
					value = (URL) newValue;
				}
			}
		});
	}

	/**
	 * converts different classes to the Property's internal value class.<br/>
	 * For a URL property this means just verifying that the given object is a
	 * non malformed URL.
	 * 
	 * @param argValue
	 *            the value to convert<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>String:</b> the String representation of the URL<br/>
	 *            <b>URL:<b> the URL itself<br/>
	 * 
	 * @return the URL
	 */
	public URL convertValue(final Object argValue) {
		if (argValue == null) {
			return null;
		}
		URL url = null;
		if (argValue instanceof String) {
			try {
				url = new URL((String) argValue);
			} catch (MalformedURLException e) {
				throw new ValidationException("invalid.prop.url.malformed", this,
						"Tried to convert URL value from an invalid or malformed string \"" + argValue + "\".");
			}
		} else if (argValue instanceof URL) {
			url = (URL) argValue;
		} else {
			throw new ValidationException("invalid.prop.url.type", this, "Tried to convert value from a data type \""
					+ argValue.getClass().getName() + "\" different to URL and String.");
		}
		return url;
	}

	/**
	 * generic validation for the Property's value.
	 * 
	 * @param newValue
	 *            the value to validate<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>String:</b> the String representation of the URL<br/>
	 *            <b>URL:<b> the URL itself<br/>
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public URL validate(final Object newValue) {
		final URL newUrlValue = (URL) super.validate(newValue);
		if (!ThreadLocalValidationSettings.getValidation()) {
			return newUrlValue;
		}
		if (newUrlValue == null) {
			return null;
		}
		return newUrlValue;
	}
}
