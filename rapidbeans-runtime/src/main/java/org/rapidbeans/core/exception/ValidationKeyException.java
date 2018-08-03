/*
 * Rapid Beans Framework: ValidationKeyException.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/20/2006
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
public class ValidationKeyException extends ValidationMandatoryException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with message.
	 * 
	 * @param sig     a unique signature for a localized exception message.<br/>
	 *                Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param source  the exception source
	 * @param message the default exception message
	 */
	public ValidationKeyException(final String sig, final Object source, final String message) {
		super(sig, source, message);
	}

	/**
	 * Constructor with message and cause.
	 * 
	 * @param sig     a unique signature for a localized exception message.<br/>
	 *                Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param source  the exception source
	 * @param message the default exception message
	 * @param cause   a Throwable to nest
	 */
	public ValidationKeyException(final String sig, final Object source, final String message, final Throwable cause) {
		super(sig, source, message, cause);
	}

	/**
	 * Constructor with message and arguments.
	 * 
	 * @param sig      a unique signature for a localized exception message.<br/>
	 *                 Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param source   the exception source
	 * @param message  the default exception message
	 * @param messArgs the message arguments
	 */
	public ValidationKeyException(final String sig, final Object source, final String message,
			final Object[] messArgs) {
		super(sig, source, message, messArgs);
	}
}
