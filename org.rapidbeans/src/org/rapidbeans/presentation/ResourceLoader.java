/*
 * Rapid Beans Framework: ResourceLoader.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 05/05/2010
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The ResourceLoader tries to load resources.
 * 
 * 1) by a number of given classes from the jars where these classes
 * come from relatively to their package which is fine for
 * remote applications in form of Java WebStart apps or Java Applets.
 * 
 * 2) by the ClassLoader as system resource which is fine for simple
 * local Java applications.
 */
public class ResourceLoader {

	private List<Class<?>> rootPackageClasses = new ArrayList<Class<?>>();

	/**
	 * Default constructor. We don't need another one.
	 */
	public ResourceLoader() {
	}

	/**
	 * Adds a root package class.
	 * 
	 * @param rootPackageClass
	 *            the new root package class to add.
	 */
	public void addRootPackageClass(final Class<?> rootPackageClass) {
		this.rootPackageClasses.add(rootPackageClass);
	}

	/**
	 * Load a resource and open an input stream.
	 * 
	 * @param path
	 *            the resource's path
	 * 
	 * @return the input stream containing the resource's data
	 */
	public InputStream getResourceAsStream(final String path) {
		InputStream is = null;
		for (final Class<?> rootPackageClass : this.rootPackageClasses) {
			is = rootPackageClass.getResourceAsStream(path);
			if (is != null) {
				break;
			}
		}
		if (is == null) {
			is = ClassLoader.getSystemResourceAsStream(path);
		}
		return is;
	}

	/**
	 * Determine the URL of a resource.
	 * 
	 * @param path
	 *            the resource's path
	 * 
	 * @return the URL of the resource
	 */
	public URL getResource(final String path) {
		URL url = null;
		for (final Class<?> rootPackageClass : this.rootPackageClasses) {
			url = rootPackageClass.getResource(path);
			if (url != null) {
				break;
			}
		}
		if (url == null) {
			url = ClassLoader.getSystemResource(path);
		}
		return url;
	}
}
