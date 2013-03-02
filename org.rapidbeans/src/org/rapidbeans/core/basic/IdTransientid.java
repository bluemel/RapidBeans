/*
 * Rapid Beans Framework: IdTransientid.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/21/2005
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
public final class IdTransientid extends Id {

	/**
	 * String cache.
	 */
	private Object reference = null;

	/**
	 * constructor for transient IDs.
	 * 
	 * @param bean
	 *            the reference
	 * @param s
	 *            not used for transient IDs.
	 */
	public IdTransientid(final RapidBean bean, final String s) {
		this.reference = bean;
	}

	/**
	 * String conversion.
	 * 
	 * @return String representation
	 */
	public String toString() {
		return Integer.toHexString(this.reference.hashCode());
	}

	/**
	 * @return the hash code
	 */
	public int hashCode() {
		return 0;
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
		if (!(o instanceof IdTransientid)) {
			return false;
		}
		IdTransientid oId = (IdTransientid) o;
		return (this.reference == oId.reference);
	}

	/**
	 * compareTo implementation.
	 * 
	 * @param id
	 *            the object to comapare with
	 * @return -1, 0, 1
	 */
	public int compareTo(final IdTransientid id) {
		return new Integer(this.reference.hashCode()).compareTo(new Integer(
				id.reference.hashCode()));
	}
}
