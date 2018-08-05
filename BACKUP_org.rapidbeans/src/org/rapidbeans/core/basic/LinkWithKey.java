/*
 * Rapid Beans Framework: LinkWithKey.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/18/2009
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
 * A simple key value pair for generic value setting of map properties.
 * 
 * @author Martin Bluemel
 */
public class LinkWithKey {

	private String key = null;

	private Link link = null;

	public LinkWithKey(final String key, final Link link) {
		this.key = key;
		this.link = link;
	}

	/**
	 * @return the key
	 */
	protected String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	protected void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the link
	 */
	protected Link getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	protected void setLink(Link link) {
		this.link = link;
	}
}
