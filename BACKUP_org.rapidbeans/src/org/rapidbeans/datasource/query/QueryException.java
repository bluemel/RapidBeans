/*
 * Rapid Beans Framework: QueryException.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/09/2006
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

package org.rapidbeans.datasource.query;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * @author Martin Bluemel
 */
public class QueryException extends RapidBeansRuntimeException {

	/**
	 * serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for BBExPropValueException.
	 */
	public QueryException() {
		super();
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param arg0
	 *            message
	 */
	public QueryException(final String arg0) {
		super(arg0);
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param arg0
	 *            message
	 * @param arg1
	 *            throwable to nest
	 */
	public QueryException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor for BBExPropValueException.
	 * 
	 * @param arg0
	 *            throwable to nest
	 */
	public QueryException(final Throwable arg0) {
		super(arg0);
	}

}
