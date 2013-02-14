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

package org.rapidbeans.presentation.settings.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;

import org.rapidbeans.core.basic.GenericEnum;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.type.TypeRapidEnum;

/**
 * Handwritten generic enum that provides all character sets available in
 * Java as enum. Some important character sets are provided as constants.
 */
public final class ApplicationLnfTypeSwing
		extends GenericEnum {

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
	 * @param lnfName
	 *            the character set's name
	 * 
	 * @return the enumeration element
	 */
	public static ApplicationLnfTypeSwing getInstance(final String lnfName) {
		return (ApplicationLnfTypeSwing) GenericEnum.valueOf(ApplicationLnfTypeSwing.class.getName(),
				lnfName);
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
	 * the look and feel class.
	 */
	private Class<?> lnfClass = null;

	/**
	 * @return the look and feel class
	 */
	public Class<?> getLnfClass() {
		return lnfClass;
	}

	/**
	 * the original look and feel name (not lower cased).
	 */
	private String lnfName = null;

	/**
	 * @return the lnfName
	 */
	public String getLnfName() {
		return lnfName;
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
	private ApplicationLnfTypeSwing(final String argName, final int argOrdinal) {
		super(type, argName, argOrdinal);
	}

	/**
	 * static initializer.
	 */
	static {
		final List<RapidEnum> elements = new ArrayList<RapidEnum>();
		int order = 0;
		elements.add(new ApplicationLnfTypeSwing("system", order++));
		Class<?> lafClass;
		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			final ApplicationLnfTypeSwing laf = new ApplicationLnfTypeSwing(
					info.getName().toLowerCase(), order++);
			try {
				lafClass = Class.forName(info.getClassName());
			} catch (ClassNotFoundException e) {
				continue;
			}
			laf.lnfClass = lafClass;
			laf.lnfName = info.getName();
			elements.add(laf);
		}
		type = initType(ApplicationLnfTypeSwing.class, elements);
	}

	/**
	 * Retrieve the localized name of the enum element
	 * 
	 * @param locale
	 *            the locale
	 * 
	 * @return a localized string for this enum element
	 */
	public String toStringGui(final RapidBeansLocale locale) {
		return this.lnfName;
	}
}
