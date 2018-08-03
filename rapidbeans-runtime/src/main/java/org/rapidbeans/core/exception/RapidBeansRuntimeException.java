/*
 * Rapid Beans Framework: RapidBeansRuntimeException.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/04/2005
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
public class RapidBeansRuntimeException extends RuntimeException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for BBRuntimeException.
	 */
	public RapidBeansRuntimeException() {
		super();
	}

	/**
	 * Constructor for BBRuntimeException with message.
	 * 
	 * @param message the message
	 */
	public RapidBeansRuntimeException(final String message) {
		super(message);
	}

	/**
	 * Constructor for nested BBRuntimeException.
	 * 
	 * @param exception the exception to nest
	 */
	public RapidBeansRuntimeException(final Throwable exception) {
		super(exception);
	}

	/**
	 * Constructor for nested BBRuntimeException with message.
	 * 
	 * @param message   the message
	 * @param exception the exception to nest
	 */
	public RapidBeansRuntimeException(final String message, final Throwable exception) {
		super(message, exception);
	}
}
