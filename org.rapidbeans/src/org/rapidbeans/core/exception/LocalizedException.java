/*
 * Rapid Beans Framework: LocalizedException.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 10/31/2006
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

import java.text.MessageFormat;
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;

/**
 * This exception uses a signature to be localized via resource bundles.
 * In addition you can add an array of argument objects which can be used
 * to add details to the message.
 * 
 * @author Martin Bluemel
 */
public class LocalizedException extends RapidBeansRuntimeException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the unique message signature for a localized exception message.
	 */
	private String signature = null;

	/**
	 * the unique title signature for a localized exception message.
	 */
	private String signatureTitle = "messagedialog.error";

	/**
	 * the object array with message arguments.
	 */
	private Object[] messageArgs = null;

	/**
	 * @return the message signature
	 */
	public String getSignature() {
		return this.signature;
	}

	/**
	 * Constructor for LocalizedException with message.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param message
	 *            the default exception message
	 */
	public LocalizedException(final String sig,
			final String message) {
		super(message);
		this.signature = sig;
	}

	/**
	 * Constructor for LocalizedException with message.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param sigTitle
	 *            a unique signature for a localized exception message title.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param message
	 *            the default exception message
	 */
	public LocalizedException(final String sig, final String sigTitle,
			final String message) {
		super(message);
		this.signature = sig;
		this.signatureTitle = sigTitle;
	}

	/**
	 * Constructor for LocalizedException with message and cause.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param message
	 *            the default exception message
	 * @param cause
	 *            a throwable to nest
	 */
	public LocalizedException(final String sig,
			final String message, final Throwable cause) {
		super(message, cause);
		this.signature = sig;
	}

	/**
	 * Constructor for LocalizedException with message and arguments.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param message
	 *            the default exception message
	 * @param messArgs
	 *            the message arguments
	 */
	public LocalizedException(final String sig,
			final String message, final Object[] messArgs) {
		super(message);
		this.signature = sig;
		this.messageArgs = messArgs;
	}

	/**
	 * Constructor for LocalizedException with message and arguments.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param sigTitle
	 *            a unique signature for a localized exception message title.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param message
	 *            the default exception message
	 * @param messArgs
	 *            the message arguments
	 */
	public LocalizedException(final String sig, final String sigTitle,
			final String message, final Object[] messArgs) {
		super(message);
		this.signature = sig;
		this.signatureTitle = sigTitle;
		this.messageArgs = messArgs;
	}

	/**
	 * Constructor for LocalizedException with message, cause and arguments.
	 * 
	 * @param sig
	 *            a unique signature for a localized exception message.<br/>
	 *            Syntax: &lt;property type&gt;.&lt;reason&gt;<br/>
	 * @param message
	 *            the default exception message
	 * @param cause
	 *            a throwable to nest
	 * @param messArgs
	 *            the message arguments
	 */
	public LocalizedException(final String sig, final String message,
			final Throwable cause, final Object[] messArgs) {
		super(message);
		this.signature = sig;
		this.messageArgs = messArgs;
	}

	/**
	 * present the exception as localized error message.
	 */
	public void present() {
		final Application client = ApplicationManager.getApplication();
		if (client.getTestMode()) {
			throw this;
		}
		final RapidBeansLocale locale = client.getCurrentLocale();
		client.messageError(this.getLocalizedMessage(locale),
				locale.getStringMessage(this.signatureTitle));
	}

	/**
	 * determine a localized message with nothing special to insert.
	 * 
	 * @param locale
	 *            the framework locale
	 * @return the localized message
	 */
	public String getLocalizedMessage(final RapidBeansLocale locale) {
		String message;
		if (this.signature != null) {
			try {
				message = locale.getStringMessage(this.signature);
				if (this.messageArgs != null) {
					Object[] oa = new Object[this.messageArgs.length];
					for (int i = 0; i < this.messageArgs.length; i++) {
						if (this.messageArgs[i] instanceof RapidBean) {
							oa[i] = ((RapidBean) this.messageArgs[i]).toStringGuiId(locale);
						} else if (this.messageArgs[i] instanceof Property) {
							oa[i] = ((Property) this.messageArgs[i]).toStringGui(locale);
						} else {
							oa[i] = this.messageArgs[i];
						}
					}
					message = MessageFormat.format(message, oa);
				}
			} catch (MissingResourceException e) {
				message = this.getClass().getName() + ": " + this.getMessage();
			}
		} else {
			message = this.getClass().getName() + ": " + this.getMessage();
		}
		return message;
	}

	/**
	 * @return the messageArgs
	 */
	public Object[] getMessageArgs() {
		return messageArgs;
	}
}
