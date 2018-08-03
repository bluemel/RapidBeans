/*
 * Rapid Beans Framework: DocumentTreeNodePropColLink.java
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

import org.rapidbeans.core.basic.PropertyCollection;

/**
 * The wrapper class for PropertyCollection of other types than composition to
 * avoid side effects during rendering a Swing Tree.
 * 
 * @author Martin Bluemel
 */
public class DocumentTreeNodePropColLink extends DocumentTreeNodePropCol {

	/**
	 * constructor.
	 * 
	 * @param col the Collection Property.
	 */
	public DocumentTreeNodePropColLink(final PropertyCollection col) {
		super(col);
	}
}
