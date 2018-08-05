/*
 * Rapid Beans Framework: ClassHelper.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/09/2005
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

/**
 * A Utility Class for handling of classes.
 * 
 * @author Martin Bluemel
 */
public final class ClassHelper {

	/**
	 * prevent default constructor from being used.
	 */
	private ClassHelper() {
	}

	/**
	 * Works like "instance of" but with classes.
	 * 
	 * @param superclassOrInterface
	 *            the class or interface to check against.
	 * @param classToCheck
	 *            the class to check
	 * 
	 * @return if class to check is a subclass of or implements the given class.
	 */
	public static boolean classOf(final Class<?> superclassOrInterface, final Class<?> classToCheck) {
		if (classToCheck == superclassOrInterface) {
			return true;
		} else {
			if (classToCheck.getSuperclass() != null) {
				if (classOf(superclassOrInterface, classToCheck.getSuperclass())) {
					return true;
				}
			}
			if (superclassOrInterface.isInterface()) {
				for (final Class<?> interfaceClass : classToCheck.getInterfaces()) {
					if (classOf(superclassOrInterface, interfaceClass)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
