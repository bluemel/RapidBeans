/*
 * Rapid Beans Framework: IdNumeric.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/13/2006
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

/**
 * @author bluemel
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class IdNumeric extends Id {

	/**
	 * the numeric ID.
	 */
	private int number = 0;

	/**
	 * @return the integer ID
	 */
	public int getNumber() {
		return this.number;
	}

	/**
	 * constructor numeric IDs with given number.
	 * 
	 * @param bean
	 *            not used for numeric ids
	 * @param argNumber
	 *            the String with the number.
	 */
	public IdNumeric(final RapidBean bean, final String argNumber) {
		IdGeneratorNumeric generator = (IdGeneratorNumeric) bean.getType().getIdGenerator();
		if (generator == null) {
			// lazy initialization of numeric ID generator in case
			// no ID generator is set otherwise
			generator = new IdGeneratorNumeric();
			generator.setMode(IdGeneratorNumeric.GENERATION_STRATEGY_COMPACT);
			bean.getType().setIdGenerator(generator);
		}
		if (argNumber == null) {
			this.number = generator.generateIdValue().intValue();
		} else {
			this.number = Integer.parseInt(argNumber);
			generator.notifiyIdExisists(this.number);
		}
	}

	/**
	 * String conversion.
	 * 
	 * @return String representation
	 */
	public String toString() {
		return Long.toString(this.number);
	}

	/**
	 * @return the hash code
	 */
	public int hashCode() {
		return this.number;
	}

	/**
	 * compare two numeric ids.
	 * 
	 * @param o
	 *            the object to compare
	 * 
	 * @return if equals or not
	 */
	public boolean equals(final Object o) {
		if (!(o instanceof IdNumeric)) {
			return false;
		}
		IdNumeric id = (IdNumeric) o;
		return (id.number == this.number);
	}

	/**
	 * compareTo implementation.
	 * 
	 * @param id
	 *            the object to compare with
	 * @return -1, 0, 1
	 */
	public int compareTo(final IdNumeric id) {
		if (this.number < id.number) {
			return -1;
		} else if (this.number == id.number) {
			return 0;
		} else {
			return 1;
		}
	}
}
