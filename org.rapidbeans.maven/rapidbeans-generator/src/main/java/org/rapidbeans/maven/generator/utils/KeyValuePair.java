/*
 * Rapid Beans Framework, SDK, Ant Tasks: KeyValuePair.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 10/29/2005
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

package org.rapidbeans.maven.generator.utils;

/**
 * a simple key / value pair container.
 * 
 * @version initial
 * 
 * @author bm092114
 */
public final class KeyValuePair {

    /**
     * the key.
     */
    private String key;

    /**
     * the value.
     */
    private String value;

    /**
     * constructor.
     * 
     * @param argKey
     *            key
     * @param argValue
     *            value
     */
    public KeyValuePair(final String argKey, final String argValue) {
        this.key = argKey;
        this.value = argValue;
    }

    /**
     * @return key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @return value
     */
    public String getValue() {
        return this.value;
    }
}
