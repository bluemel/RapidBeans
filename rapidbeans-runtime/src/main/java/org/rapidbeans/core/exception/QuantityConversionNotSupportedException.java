/*
 * Rapid Beans Framework: QuantityConversionNotSupportedException.java
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
public class QuantityConversionNotSupportedException extends RapidBeansRuntimeException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for BBExPropValueException.
	 */
	public QuantityConversionNotSupportedException() {
		super();
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param message the message
	 */
	public QuantityConversionNotSupportedException(final String message) {
		super(message);
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param message   the message
	 * @param exception the exception
	 */
	public QuantityConversionNotSupportedException(final String message, final Throwable exception) {
		super(message, exception);
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param exception the exception
	 */
	public QuantityConversionNotSupportedException(final Throwable exception) {
		super(exception);
	}
}
