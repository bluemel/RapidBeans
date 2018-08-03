/*
 * Rapid Beans Framework: EscapeMap.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 01/28/2010
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

/**
 * 
 */
package org.rapidbeans.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Bluemel
 */
public class EscapeMap {

	/**
	 * @return the escMap
	 */
	public Map<String, String> getEscMap() {
		return escMap;
	}

	/**
	 * @return the uescMap
	 */
	public Map<String, String> getUescMap() {
		return uescMap;
	}

	final Map<String, String> escMap = new HashMap<String, String>();

	final Map<String, String> uescMap = new HashMap<String, String>();

	/**
	 * Constructor.
	 * 
	 * @param mapping the mapping as sequence of pairs of strings
	 */
	public EscapeMap(final List<String> mapping) {
		final int size = mapping.size();
		if (size < 2) {
			throw new IllegalArgumentException("less than two mapping arguments specified");
		}
		if (size % 2 != 0) {
			throw new IllegalArgumentException("odd mapping arguments count");
		}
		for (int i = 0; i < size; i += 2) {
			this.escMap.put(mapping.get(i), mapping.get(i + 1));
			this.uescMap.put(mapping.get(i + 1), mapping.get(i));
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param mapping the mapping as sequence of pairs of strings
	 */
	public EscapeMap(final String[] mapping) {
		if (mapping.length < 2) {
			throw new IllegalArgumentException("less than two mapping arguments specified");
		}
		if (mapping.length % 2 != 0) {
			throw new IllegalArgumentException("odd mapping arguments count");
		}
		for (int i = 0; i < mapping.length; i += 2) {
			this.escMap.put(mapping[i], mapping[i + 1]);
			this.uescMap.put(mapping[i + 1], mapping[i]);
		}
	}
}
