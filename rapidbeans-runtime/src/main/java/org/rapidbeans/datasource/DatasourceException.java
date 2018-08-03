/*
 * Rapid Beans Framework: DatasourceException.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/31/2006
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

package org.rapidbeans.datasource;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * The parent class for all datasource related runtime exceptions.
 * 
 * @author Martin Bluemel
 */
public class DatasourceException extends RapidBeansRuntimeException {

	/**
	 * the serial veriosn UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for BBExPropValueException.
	 */
	public DatasourceException() {
		super();
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param message the message
	 */
	public DatasourceException(final String message) {
		super(message);
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param message         the message
	 * @param nestedException the exception to nest
	 */
	public DatasourceException(final String message, final Throwable nestedException) {
		super(message, nestedException);
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param nestedException the exception to nest
	 */
	public DatasourceException(final Throwable nestedException) {
		super(nestedException);
	}
}
