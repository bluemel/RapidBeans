/*
 * Rapid Beans Framework: TypePropertyBoolean.java
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

package org.rapidbeans.core.type;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.util.XmlNode;

/**
 * the bean Property type class for Boolean properties.
 * 
 * @author Martin Bluemel
 */
public final class TypePropertyBoolean extends TypeProperty {

	@Override
	public Class<?> getValuetype() {
		return Boolean.class;
	}

	/**
	 * Constructor for TypePropertyInteger.
	 * 
	 * @param xmlNode
	 *            the XML DOM node describing the property type
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyBoolean(final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType) {
		super("Boolean", xmlNodes, parentBeanType);

		final String defaultValueString = xmlNodes[0].getAttributeValue("@default");
		if (defaultValueString != null) {
			validateBooleanString(defaultValueString);
			this.setDefaultValue(Boolean.parseBoolean(defaultValueString));
		}
	}

	/**
	 * Checks if "true" or "false" are specified.
	 * 
	 * @param sBool
	 *            the boolean String to validate.
	 */
	public void validateBooleanString(final String sBool) {
		if (!sBool.equalsIgnoreCase("false") && !sBool.equalsIgnoreCase("true")) {
			throw new ValidationException("invalid.prop.boolean.string",
					this,
					"Property \"" + this.getPropName() + "\": invalid string \""
							+ sBool + "\".\nOnly \"false\" and \"true\" are valid values.");
		}
	}

	/**
	 * @return the property type enumeration
	 */
	public PropertyType getProptype() {
		return PropertyType.bool;
	}

	/**
	 * converter.
	 * 
	 * @param newValue
	 *            the object to convert.
	 *            It must be an instance of the following classes: <li><b>Boolean:</b> the boolean value itself</li> <li><b>String:</b> the boolean as string { 'false' | 'true' }</li>
	 * 
	 * @return the converted value
	 */
	public Boolean convertValue(final Object newValue) {
		if (newValue == null) {
			return null;
		}
		Boolean booleanValue = null;
		if (newValue instanceof Boolean) {
			booleanValue = (Boolean) newValue;
		} else if (newValue instanceof String) {
			final String s = (String) newValue;
			this.validateBooleanString(s);
			booleanValue = Boolean.parseBoolean(s);
		} else {
			throw new ValidationException("invalid.prop.boolean.type",
					this,
					"Property \"" + this.getPropName() + "\": "
							+ "invalid data type \"" + newValue.getClass().getName()
							+ "\" for a boolean property.\n"
							+ "Only \"Boolean\" or \"String\" are valid data types.");
		}
		return booleanValue;
	}
}
