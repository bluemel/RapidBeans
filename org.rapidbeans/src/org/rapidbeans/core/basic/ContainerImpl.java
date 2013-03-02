/*
 * Rapid Beans Framework: ContainerImpl.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/31/2006
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
 * An abstract implementation to hide special methods from the Container
 * interface.
 * 
 * @author Martin Bluemel
 */
public abstract class ContainerImpl implements Container {
	/**
	 * insert (create) a new bean in the DB.
	 * 
	 * @param bean
	 *            the bean to insert
	 * @param implicitly
	 *            special for documents. Usually you do not explicitly insert
	 *            beans into a document. Instead insert them implicitly by
	 *            adding them to a parent bean. If you anyway try to insert
	 *            explicitly the document tries to find an appropriate location
	 *            according to the following strategy. <li>
	 *            Find all composition collection properties that have the type
	 *            of the bean to insert as target type.</li><li>
	 *            If there is exactly one add the bean there. Otherwise throw an
	 *            appropriate exception.</li>
	 */
	public abstract void insert(final RapidBean bean, final boolean implicitly);
}
