/*
 * Rapid Beans Framework: PropertyNumber.java
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

import org.rapidbeans.core.type.TypeProperty;

/**
 * A <b>Number</b> is the common base class for all number Properties.<br/>
 * 
 * @author Martin Bluemel
 */
public abstract class PropertyNumber extends Property {

	/**
	 * @return the number value.
	 */
	public abstract Number getValue();

	/**
	 * common constructor for Number Properties.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyNumber(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}
}
