/*
 * Rapid Beans Framework, SDK, Ant Tasks: XmlErrorHandler.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 01/01/2005
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

package org.rapidbeans.ant;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The XML parser's error handler.
 * 
 * @author Martin Bluemel
 */
final class EasybizAntTasksXmlErrorHandler implements ErrorHandler {

	/**
	 * the default constructor.
	 */
	public EasybizAntTasksXmlErrorHandler() {
	}

	/**
	 * warning.
	 * 
	 * @param e
	 *            the exception
	 * @throws SAXException
	 *             the SAX exception that is a warning
	 */
	public void warning(final SAXParseException e) throws SAXException {
		throw e;
	}

	/**
	 * error.
	 * 
	 * @param e
	 *            the exception
	 * @throws SAXException
	 *             the SAX exception that is an error
	 */
	public void error(final SAXParseException e) throws SAXException {
		throw e;
	}

	/**
	 * fatal error.
	 * 
	 * @param e
	 *            the exception
	 * @throws SAXException
	 *             the SAX exception that is a fatal error
	 */
	public void fatalError(final SAXParseException e) throws SAXException {
		throw e;
	}
}
