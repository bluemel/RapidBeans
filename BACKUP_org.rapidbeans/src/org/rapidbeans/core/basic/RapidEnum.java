/*
 * Rapid Beans Framework: RapidEnum.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/27/2008
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

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.type.TypeRapidEnum;

/**
 * Base interface for Rapid Beans enum elements (potentially generic). Although
 * it is nearly the same be careful not to mix up with java.lang.Enum.
 * 
 * @author Martin Bluemel
 */
public interface RapidEnum {

	/**
	 * the type accessing method.
	 * 
	 * @return the type instance
	 */
	public abstract TypeRapidEnum getType();

	/**
	 * @return the enum element's name
	 */
	public abstract String name();

	/**
	 * @return the enum element's order
	 */
	public abstract int ordinal();

	/**
	 * @return the enum element's name.
	 */
	public abstract String toString();

	/**
	 * Retrieve the localized name of the enum element
	 * 
	 * @param locale
	 *            the locale
	 * 
	 * @return a localized string for this enum element
	 */
	public abstract String toStringGui(final RapidBeansLocale locale);

	/**
	 * @param locale
	 *            the Locale
	 * 
	 * @return the short form of a localized string for this enum element
	 */
	public String toStringGuiShort(final RapidBeansLocale locale);

	/**
	 * Get the description from the model (meta information - not UI).
	 * 
	 * @return this enumeration element's description.
	 */
	public String getDescription();
}
