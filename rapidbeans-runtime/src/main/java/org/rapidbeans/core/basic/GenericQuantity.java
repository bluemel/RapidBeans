/*
 * Rapid Beans Framework: GenericQuantity.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/30/2008
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

import java.math.BigDecimal;

import org.rapidbeans.core.type.TypeRapidQuantity;

/**
 * The generic object for Rapid Quantities.
 * 
 * @author Martin Bluemel
 */
public class GenericQuantity extends RapidQuantity {

	/**
	 * The Rapid Bean type instance. Since we have no class we can't make it a
	 * static member of the class. So every instance has to have a reference to it's
	 * type.
	 */
	private TypeRapidQuantity type = null;

	/**
	 * @return the Rapid Bean type
	 */
	public TypeRapidQuantity getType() {
		return this.type;
	}

	/**
	 * constructor for a generic quantity.
	 * 
	 * @param qtype the quantity type
	 */
	public GenericQuantity(final TypeRapidQuantity qtype) {
		super();
		if (qtype == null) {
			throw new IllegalArgumentException("Can't create a generic quantity without type");
		}
		this.type = qtype;
	}

	/**
	 * constructor for a generic quantity.
	 * 
	 * @param qtype the quantity type
	 * @param value
	 */
	public GenericQuantity(final TypeRapidQuantity qtype, final String value) {
		super(qtype, value);
		if (qtype == null) {
			throw new IllegalArgumentException("Can't create a generic quantity without type");
		}
		this.type = qtype;
	}

	/**
	 * constructor for a generic quantity.
	 * 
	 * @param qtype the quantity type
	 * @param value
	 */
	public GenericQuantity(final TypeRapidQuantity qtype, final BigDecimal argMagnitude, final RapidEnum argUnit) {
		super(argMagnitude, argUnit);
		this.type = qtype;
	}
}
