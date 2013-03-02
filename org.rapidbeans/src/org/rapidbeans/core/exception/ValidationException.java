/*
 * Rapid Beans Framework: ValidationException.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/01/2006
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
public class ValidationException extends LocalizedException {

	/**
	 * The exceptoin source
	 */
	private Object source = null;

	/**
	 * @return the exception source. Could be a Property, a Bean, or a different
	 *         Object.
	 */
	public Object getSource() {
		return this.source;
	}

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with message.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param source
	 *            the exception source
	 * @param message
	 *            the default exception message
	 */
	public ValidationException(final String sig, final Object source,
			final String message) {
		super(sig, message);
		this.source = source;
	}

	/**
	 * Constructor with message and cause.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param source
	 *            the exception source
	 * @param message
	 *            the default exception message
	 * @param cause
	 *            a Throwable to nest
	 */
	public ValidationException(final String sig, final Object source,
			final String message, final Throwable cause) {
		super(sig, message, cause);
		this.source = source;
	}

	/**
	 * Constructor with message and arguments.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param source
	 *            the exception source
	 * @param message
	 *            the default exception message
	 * @param messArgs
	 *            the message arguments
	 */
	public ValidationException(final String sig, final Object source,
			final String message, final Object[] messArgs) {
		super(sig, message, messArgs);
		this.source = source;
	}
}
