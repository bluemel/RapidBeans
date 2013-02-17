/*
 * Rapid Beans Framework: TypePropertyQuantity.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/01/2005
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

import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.basic.RapidQuantity;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.core.util.XmlNode;

/**
 * a Date property stores a RapidQuantity.
 * 
 * @author Martin Bluemel
 */
public class TypePropertyQuantity extends TypeProperty {

	@Override
	public Class<?> getValuetype() {
		return RapidQuantity.class;
	}

	/**
	 * defines the precision of the Date.
	 */
	private TypeRapidQuantity quantitytype = null;

	/**
	 * the maximal boundary for the date.
	 */
	private RapidQuantity maxVal = null;

	/**
	 * the default unit.
	 */
	private RapidEnum defaultUnit = null;

	/**
	 * the minimal boundary for the date.
	 */
	private RapidQuantity minVal = null;

	/**
	 * construct the type out of an XML description.
	 * 
	 * @param propertyNodes
	 *            the XML document root nodes
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyQuantity(final XmlNode[] propertyNodes,
			final TypeRapidBean parentBeanType) {
		super("Quantity", propertyNodes, parentBeanType);

		String s = propertyNodes[0].getAttributeValue("@quantity");
		if (s == null) {
			throw new RapidBeansRuntimeException("Mandatory property \"quantity\" not defined");
		}
		if (s != null) {
			if (!s.contains(".") && parentBeanType.getPackageName() != null) {
				s = parentBeanType.getPackageName() + '.' + s;
			}
			try {
				this.quantitytype = TypeRapidQuantity.forName(s);
			} catch (TypeNotFoundException e) {
				if (!s.contains(".")) {
					this.quantitytype = TypeRapidQuantity.forName(parentBeanType.getPackageName()
							+ '.' + s);
				}
			}
		}
		s = propertyNodes[0].getAttributeValue("@maxval");
		if (s != null) {
			this.maxVal = RapidQuantity.createInstance(this.quantitytype.getName(), s);
		}

		s = propertyNodes[0].getAttributeValue("@minval");
		if (s != null) {
			this.minVal = RapidQuantity.createInstance(this.quantitytype.getName(), s);
		}

		s = propertyNodes[0].getAttributeValue("@defaultunit");
		if (s != null) {
			this.defaultUnit = this.quantitytype.getUnitInfo().elementOf(s);
		}

		s = propertyNodes[0].getAttributeValue("@default");
		if (s != null) {
			this.setDefaultValue(RapidQuantity.createInstance(this.quantitytype.getName(), s));
		}
	}

	/**
	 * @return the type of the RapidQuantity of this Property
	 */
	public TypeRapidQuantity getQuantitytype() {
		return this.quantitytype;
	}

	/**
	 * @return the maximum boundary
	 */
	public RapidQuantity getMaxVal() {
		return this.maxVal;
	}

	/**
	 * @return the minimum boundary
	 */
	public RapidQuantity getMinVal() {
		return this.minVal;
	}

	/**
	 * @return the property type enumeration
	 */
	public PropertyType getProptype() {
		return PropertyType.quantity;
	}

	/**
	 * @return the defaultUnit
	 */
	public RapidEnum getDefaultUnit() {
		return defaultUnit;
	}
}
