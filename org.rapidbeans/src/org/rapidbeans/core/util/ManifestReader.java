/*
 * Rapid Beans Framework: ManifestReader.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 09/06/2007
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

package org.rapidbeans.core.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Manifest;

import org.rapidbeans.core.exception.UtilException;

/**
 * A Utility Class for reading a manifest.
 * 
 * @author Martin Bluemel
 */
public final class ManifestReader {

	/**
	 * prevent default constructor from being used.
	 */
	private ManifestReader() {
	}

	/**
	 * Read a manifest from the same jar file or class folder hierarchy of the
	 * given class.
	 * 
	 * @param clazz
	 *            the class that lies in the jar file ore the same class folder
	 *            hierarchy.
	 * @return the manifest
	 */
	public static Manifest readManifestFromJarOfClass(final Class<?> clazz) {
		final String className = clazz.getSimpleName();
		final String classFileName = className + ".class";
		final String classFilePath = clazz.getPackage().toString().replace('.', '/') + "/" + className;
		final String pathToThisClass = clazz.getResource(classFileName).toString();
		final String pathToManifest = pathToThisClass.toString().substring(0,
				pathToThisClass.length() + 2 - ("/" + classFilePath).length())
				+ "/META-INF/MANIFEST.MF";
		Manifest manifest;
		try {
			manifest = new Manifest(new URL(pathToManifest).openStream());
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		} catch (IOException e) {
			throw new UtilException(e);
		}
		return manifest;
	}
}
