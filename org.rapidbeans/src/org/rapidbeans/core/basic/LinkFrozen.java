/*
 * Rapid Beans Framework: LinkFrozen.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/02/2006
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
 * @author Martin Bluemel
 * 
 *         <P>
 *         A frozen bean link (<B>LinkFrozen</B> just repesents an id to another bean in the form of a string.<br>
 *         </P>
 *         <P>
 *         It once can be resolved and from this moment is change to a concrete bean reference.
 *         </P>
 */
public class LinkFrozen implements Link, Cloneable, Comparable<LinkFrozen> {

	/**
	 * the ID string.
	 */
	private String idString;

	/**
	 * constructor.
	 * 
	 * @param beanRef
	 *            the bean reference to freeze
	 */
	public LinkFrozen(final RapidBean beanRef) {
		this(beanRef.getIdString());
	}

	/**
	 * constructor.
	 * 
	 * @param argIdString
	 *            the id string
	 */
	public LinkFrozen(final String argIdString) {
		this.idString = argIdString;
	}

	/**
	 * @return the ID string
	 */
	public final String getIdString() {
		return this.idString;
	}

	/**
	 * comparison for natural order.
	 * 
	 * @param o
	 *            the object to compare with
	 * @return -1, 0 1
	 */
	public int compareTo(final LinkFrozen lf) {
		Link l = (Link) lf;
		return this.idString.compareTo(l.getIdString());
	}

	/**
	 * @return a new LinkFrozen instance containing the same id string.
	 */
	public LinkFrozen clone() {
		return new LinkFrozen(this.idString);
	}
}
