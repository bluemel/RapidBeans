/*
 * Rapid Beans Framework: TypeNotFoundException.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/04/2005
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

package org.rapidbeans.core.exception;

/**
 * @author Martin Bluemel
 */
public class TypeNotFoundException extends RapidBeansRuntimeException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for BBExPropValueException.
	 */
	public TypeNotFoundException() {
		super();
	}

	/**
	 * Constructor for BBExPropValueException with message.
	 * 
	 * @param message the exception message
	 */
	public TypeNotFoundException(final String message) {
		super(message);
	}

	/**
	 * Constructor for nested BBExPropValueException.
	 * 
	 * @param exception the nested exception
	 */
	public TypeNotFoundException(final Throwable exception) {
		super(exception);
	}

	/**
	 * Constructor for nested BBExPropValueException with message.
	 * 
	 * @param message   the message
	 * @param exception the nested exception
	 */
	public TypeNotFoundException(final String message, final Throwable exception) {
		super(message, exception);
	}
}
