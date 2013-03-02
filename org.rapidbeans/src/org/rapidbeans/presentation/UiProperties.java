/*
 * Rapid Beans Framework: View.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 02/06/2010
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

package org.rapidbeans.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * Language independent UI resource administration
 * 
 * @author Martin Bluemel
 */
public class UiProperties {

	/**
	 * the language independent UI resources of the application.
	 */
	private Properties guiProps = null;

	/**
	 * Retrieve the value of property specified by the key.
	 * 
	 * @param key
	 *            the key that uniquely specifies the property
	 * 
	 * @return the value of property specified by the key
	 */
	public String getPropertyValue(final String key) {
		return this.guiProps.getProperty(key);
	}

	/**
	 * constructor.
	 * 
	 * @param app
	 *            the (parent) application (client)
	 */
	public UiProperties(final Application app) {
		init(app);
	}

	/**
	 * initializes the RapidBeansLocale instance.
	 * 
	 * @param app
	 *            the (parent) application (client)
	 */
	private void init(final Application app) {
		this.guiProps = new Properties();
		InputStream is = null;
		try {
			String path = app.getRootpackage().replace('.', '/')
					+ "/presentation/gui.properties";
			is = ClassLoader.getSystemResourceAsStream(path);
			if (is == null) {
				is = app.getClass().getResourceAsStream(
						"presentation/gui.properties");
			}
			if (is == null) {
				is = app.getClass().getResourceAsStream("gui.properties");
			}
			if (is != null) {
				this.guiProps.load(is);
			}
		} catch (IOException e) {
			throw new RapidBeansRuntimeException("Error");
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				throw new RapidBeansRuntimeException(e);
			}
		}
	}
}
