/*
 * Rapid Beans Framework: IdKeypropswithparentscope.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/13/2007
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
 */
public final class IdKeypropswithparentscope extends IdKeyprops {

	/**
	 * the parent beans.
	 */
	private RapidBean[] parentBeans;

	/**
	 * the full id string.
	 * Do not init with null here!
	 */
	private String idString;

	/**
	 * constructor for serialized IDs.
	 * 
	 * @param bean
	 *            the parent bean
	 * @param s
	 *            not used.
	 */
	public IdKeypropswithparentscope(final RapidBean bean, final String s) {
		super(bean, s);
		if (s != null) {
			this.idString = s;
		}
	}

	/**
	 * String conversion.
	 * 
	 * @return String representation
	 */
	public String toString() {
		return this.idString;
	}

	/**
	 * @return the hash code
	 */
	public int hashCode() {
		return 0;
	}

	/**
	 * compare two keyprops ids.
	 * 
	 * @param o
	 *            the object to compare
	 * 
	 * @return if equals or not
	 */
	public boolean equals(final Object o) {
		if (!(o instanceof IdKeypropswithparentscope)) {
			return false;
		}
		IdKeypropswithparentscope oId = (IdKeypropswithparentscope) o;
		return this.idString.equals(oId.idString);
	}

	/**
	 * compareTo implementation.
	 * 
	 * @param o
	 *            the object to comapare with
	 * @return -1, 0, 1
	 */
	public int compareTo(final Id o) {
		int comp = 0;
		final IdKeypropswithparentscope oId = (IdKeypropswithparentscope) o;
		if (this.parentBeans != null) {
			final int start = getStartIndex();
			for (int i = start; i < this.parentBeans.length; i++) {
				// opimization: don't need to compare same parents
				if (this.parentBeans[i] == oId.parentBeans[i]) {
					continue;
				}
				comp = this.parentBeans[i].getId().compareTo(oId.parentBeans[i].getId());
				if (comp != 0) {
					break;
				}
			}
		}
		if (comp == 0) {
			comp = super.compareTo(o);
		}
		return comp;
	}

	/**
	 * lazy initialize the Keyprops.
	 * Used during initialization.
	 */
	protected void initKeyprops() {
		super.initKeyprops();
		StringBuffer sb = new StringBuffer();
		final RapidBean bean = getBean();
		this.parentBeans = bean.getParentBeans();
		final int start = getStartIndex();
		for (int i = start; i < this.parentBeans.length; i++) {
			switch (this.parentBeans[i].getType().getIdtype()) {
			case keypropswithparentscope:
				sb.append(((IdKeypropswithparentscope)
						this.parentBeans[i].getId()).getIdStringKeyprops());
				break;
			default:
				sb.append(this.parentBeans[i].getIdString());
				break;
			}
			sb.append('/');
		}
		this.idString = sb.toString() + super.toString();
	}

	/**
	 * little helper for determining the start index for iterating over the parents.
	 * 
	 * @return the start index
	 */
	private int getStartIndex() {
		int start = 0;
		if (this.getBean().getType().getIdtypeParentScopeDepth() > 0
				&& this.parentBeans.length - this.getBean().getType().getIdtypeParentScopeDepth() > 0) {
			start = this.parentBeans.length - this.getBean().getType().getIdtypeParentScopeDepth();
		}
		return start;
	}

	/**
	 * @return the keyprops part of the id string.
	 */
	private String getIdStringKeyprops() {
		return super.toString();
	}
}
