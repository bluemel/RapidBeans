/*
 * Rapid Beans Framework: CharsetsAvailable.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 10/23/2006
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

package org.rapidbeans.datasource;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.rapidbeans.core.basic.GenericEnum;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.type.TypeRapidEnum;

/**
 * Handwritten generic enum that provides all character sets available in
 * Java as enum. Some important character sets are provided as constants.
 */
public final class CharsetsAvailable
		extends GenericEnum {

	// ------------------------------------------------------------------------
	// Some important enum elements
	// -----------------------------------------------------------------------

	/**
	 * Character set UTF-8
	 */
	public static final CharsetsAvailable UTF_8;

	/**
	 * Character set UTF-16
	 */
	public static final CharsetsAvailable UTF_16;

	/**
	 * Character set ISO-8859-1
	 */
	public static final CharsetsAvailable ISO_8859_1;

	// ------------------------------------------------------------------------
	// fixed set of helper methods
	// -----------------------------------------------------------------------

	/**
	 * get the type object that describes the enum's metadata (like a Class object).
	 * 
	 * @return the type object
	 **/
	public TypeRapidEnum getType() {
		return type;
	}

	/**
	 * get the type object that describes the enum's metadata (like a Class object).
	 * 
	 * @return the type object
	 **/
	public static TypeRapidEnum getEnumType() {
		return type;
	}

	/**
	 * Specific method to get a distinct character set.
	 * 
	 * @param charsetName
	 *            the character set's name
	 * 
	 * @return the enumeration element
	 */
	public static CharsetsAvailable getInstance(final String charsetName) {
		return (CharsetsAvailable) GenericEnum.valueOf(CharsetsAvailable.class.getName(),
				charsetName);
	}

	/**
	 * set the type object that describes the enum's metadata (like a Class object).
	 * 
	 * @param argType
	 *            the type object
	 **/
	public void setType(final TypeRapidEnum argType) {
		type = argType;
	}

	/**
	 * internal enum initialization method.
	 **/
	protected static TypeRapidEnum type = null;

	/**
	 * The constructor for enum elements.
	 * Since all enum elements are pre instantiated before the first use
	 * of this enum class this constructor exclusively is used internally.
	 * 
	 * @param argName
	 *            the enum element name
	 * @param argCharsetName
	 *            the value for enum column charsetName
	 * @param argOrdinal
	 *            the enum element's ordinal.
	 */
	private CharsetsAvailable(final String argName, final int argOrdinal) {
		super(type, argName, argOrdinal);
	}

	/**
	 * static initializer.
	 */
	static {
		final List<RapidEnum> elements = new ArrayList<RapidEnum>();
		int order = 0;
		for (String charset : Charset.availableCharsets().keySet()) {
			elements.add(new CharsetsAvailable(charset, order++));
		}
		type = initType(CharsetsAvailable.class, elements);
		UTF_8 = (CharsetsAvailable) type.elementOf("UTF-8");
		UTF_16 = (CharsetsAvailable) type.elementOf("UTF-16");
		ISO_8859_1 = (CharsetsAvailable) type.elementOf("ISO-8859-1");
	}
}
