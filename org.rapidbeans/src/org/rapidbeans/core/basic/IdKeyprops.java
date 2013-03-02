/*
 * Rapid Beans Framework: IdKeyprops.java
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

import java.util.ArrayList;
import java.util.List;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * @author Martin Bluemel
 */
public class IdKeyprops extends Id {

	/**
	 * empty key property array to avoid unnecessary instantiations.
	 */
	public static final Property[] EMPTY_KEYPROP_ARRAY = new Property[0];

	/**
	 * Key properties.
	 */
	private ArrayList<Property> keyprops = null;

	/**
	 * @return the key properties as array.
	 */
	public Property[] getKeyprops() {
		return this.keyprops.toArray(EMPTY_KEYPROP_ARRAY);
	}

	/**
	 * the parent bean.
	 */
	private RapidBean bean = null;

	/**
	 * @return the bean
	 */
	protected RapidBean getBean() {
		return bean;
	}

	/**
	 * String representation.
	 */
	private String idKeyprops = null;

	/**
	 * constructor for serialized IDs.
	 * 
	 * @param bean
	 *            the parent bean
	 * @param s
	 *            not used.
	 */
	public IdKeyprops(final RapidBean bean, final String s) {
		this.bean = bean;
		initKeyprops();
		if (s != null) {
			this.idKeyprops = s;
		}
	}

	/**
	 * String conversion.
	 * 
	 * @return String representation
	 */
	public String toString() {
		return this.idKeyprops;
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
		if (!(o instanceof IdKeyprops)) {
			return false;
		}
		IdKeyprops oId = (IdKeyprops) o;
		return this.idKeyprops.equals(oId.idKeyprops);
	}

	/**
	 * compareTo implementation.
	 * 
	 * @param o
	 *            the object to compare with
	 * @return -1, 0, 1
	 */
	public int compareTo(final Id o) {
		int comp = 0;
		List<Property> oprops = ((IdKeyprops) o).keyprops;
		if (this.keyprops != null && oprops != null) {
			int size = this.keyprops.size();
			Property keyprop, oprop;
			for (int i = 0; i < size; i++) {
				keyprop = this.keyprops.get(i);
				oprop = oprops.get(i);
				if (keyprop != null && oprop != null) {
					comp = keyprop.compareTo(oprop);
					if (comp != 0) {
						break;
					}
				}
			}
		} else {
			comp = super.compareTo(o);
		}
		return comp;
	}

	/**
	 * lazy initialize the Keyprops. Used during initialization.
	 */
	protected void initKeyprops() {
		final StringBuffer sb = new StringBuffer();
		final List<Property> proplist = this.bean.getPropertyList();
		this.keyprops = new ArrayList<Property>();
		int i = 0;
		for (Property prop : proplist) {
			if (prop.getType().isKeyCandidate()) {
				this.keyprops.add(prop);
				if (i > 0) {
					sb.append("_");
				}
				sb.append(prop.toString());
				this.idKeyprops = sb.toString();
				i++;
			}
		}
		if (i < 1) {
			throw new RapidBeansRuntimeException("bean type \""
					+ this.bean.getType().getName()
					+ "\" has no key property defined");
		}
	}
}
