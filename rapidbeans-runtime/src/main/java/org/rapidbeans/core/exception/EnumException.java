/*
 * Rapid Beans Framework: EnumException.java
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
 * Indicates a trial to access an invalid enum element.
 * 
 * @author Martin Bluemel
 */
public class EnumException extends RapidBeansRuntimeException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public EnumException() {
		super();
	}

	/**
	 * Constructor with message.
	 * 
	 * @param message the exception message
	 */
	public EnumException(final String message) {
		super(message);
	}

	/**
	 * Constructor with nested exception.
	 * 
	 * @param exception the nested exception
	 */
	public EnumException(final Throwable exception) {
		super(exception);
	}

	/**
	 * Constructor with message and nested exception.
	 * 
	 * @param message   the message
	 * @param exception the nested exception
	 */
	public EnumException(final String message, final Throwable exception) {
		super(message, exception);
	}
}
