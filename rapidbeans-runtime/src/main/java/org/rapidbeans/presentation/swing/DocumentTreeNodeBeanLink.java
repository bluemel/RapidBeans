/*
 * Rapid Beans Framework: DocumentTreeNodeBeanLink.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 03/09/2007
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

package org.rapidbeans.presentation.swing;

import org.rapidbeans.core.basic.RapidBean;

/**
 * A wrapper class for bean link.
 * 
 * @author Martin Bluemel
 */
public class DocumentTreeNodeBeanLink {

	/**
	 * the Bean linked.
	 */
	private RapidBean linkedBean = null;

	/**
	 * @return the original bean
	 */
	public RapidBean getLinkedBean() {
		return this.linkedBean;
	}

	/**
	 * constructor.
	 * 
	 * @param bean the bean linked.
	 */
	public DocumentTreeNodeBeanLink(final RapidBean bean) {
		this.linkedBean = bean;
	}

	// /**
	// * Delegates equals to the bean's implementation.
	// *
	// * @param o the other node
	// *
	// * @return if this node equals or not
	// */
	// public boolean equals(final DocumentTreeNodeBeanLink o) {
	// return this.linkedBean.equals(o.linkedBean);
	// }
	//
	// /**
	// * @return the hash code of the encapsulated bean.
	// */
	// public int hashCode() {
	// return this.linkedBean.hashCode();
	// }
}
