/*
 * Rapid Beans Framework: XmlAttribute.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 10/21/2007
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

package org.rapidbeans.core.util;

import org.w3c.dom.Node;

/**
 * Utility class for primitive XML parsing using good old JAXP DOM parser
 * technology.
 * 
 * @author Martin Bluemel
 */
public final class XmlAttribute {

	/**
	 * the node encapsulated.
	 */
	private Node node = null;

	/**
	 * @return the attribute's name
	 */
	public String getName() {
		return this.node.getNodeName();
	}

	/**
	 * @return the attribute's name
	 */
	public String getValue() {
		return this.node.getNodeValue();
	}

	/**
	 * constructor.
	 * 
	 * @param argNode
	 *            the node to encupsulate
	 */
	public XmlAttribute(final Node argNode) {
		this.node = argNode;
	}
}
