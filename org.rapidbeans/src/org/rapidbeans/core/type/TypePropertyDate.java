/*
 * Rapid Beans Framework: TypePropertyDate.java
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

import java.util.Date;

import org.rapidbeans.core.basic.PropertyDate;
import org.rapidbeans.core.common.PrecisionDate;
import org.rapidbeans.core.util.XmlNode;

/**
 * a Date property stores a certain point of time with an arbitrary precision.
 * 
 * @author Martin Bluemel
 */
public class TypePropertyDate extends TypeProperty {

	@Override
	public Class<?> getValuetype() {
		return Date.class;
	}

	/**
	 * defines the precision of the Date.
	 */
	private PrecisionDate precision = PrecisionDate.day;

	/**
	 * the maximal boundary for the date.
	 */
	private long maxVal = Long.MAX_VALUE;

	/**
	 * the minimal boundary for the date.
	 */
	private long minVal = Long.MIN_VALUE;

	/**
	 * construct the type out of an XML description.
	 * 
	 * @param propertyNode
	 *            the XML document root node
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyDate(final XmlNode[] propertyNodes,
			final TypeRapidBean parentBeanType) {
		super("Date", propertyNodes, parentBeanType);

		String s = propertyNodes[0].getAttributeValue("@default");
		if (s != null) {
			this.setDefaultValue(PropertyDate.parse(s));
		}

		s = propertyNodes[0].getAttributeValue("@precision");
		if (s != null) {
			this.precision = (PrecisionDate) PrecisionDate.day.getType()
					.elementOf(s);
		}

		s = propertyNodes[0].getAttributeValue("@maxval");
		if (s != null) {
			this.maxVal = PropertyDate.parse(s).getTime();
		}

		s = propertyNodes[0].getAttributeValue("@minval");
		if (s != null) {
			this.minVal = PropertyDate.parse(s).getTime();
		}
	}

	/**
	 * @return the precision
	 */
	public PrecisionDate getPrecision() {
		return this.precision;
	}

	/**
	 * @return the maximum boundary
	 */
	public long getMaxVal() {
		return this.maxVal;
	}

	/**
	 * @return the minimum boundary
	 */
	public long getMinVal() {
		return this.minVal;
	}

	/**
	 * @return the property type enumeration
	 */
	public PropertyType getProptype() {
		return PropertyType.date;
	}
}
