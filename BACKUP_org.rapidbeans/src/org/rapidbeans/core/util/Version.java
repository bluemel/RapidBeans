/*
 * Rapid Beans Framework: Version.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/15/2007
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

import java.util.Collection;
import java.util.List;

/**
 * Implements version arithmetics by simply splitting a version string into
 * components using a given delimiter.
 * 
 * @author Martin Bluemel
 */
public class Version {

	/**
	 * the version string.
	 */
	private String string = null;

	/**
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return this.string;
	}

	/**
	 * The list with the version component strings.
	 */
	private List<String> components = null;

	/**
	 * @return a list with the version component strings
	 */
	public final List<String> getComponents() {
		return this.components;
	}

	/**
	 * @param index
	 *            the index of the version component
	 * 
	 * @return a single version component
	 */
	protected final String getComponent(final int index) {
		return this.components.get(index);
	}

	/**
	 * Construct a version out of a version string.
	 * 
	 * @param versionString
	 *            - the version string. '.' is assumed to be the separator
	 *            character.
	 */
	public Version(final String versionString) {
		this(versionString, ".");
	}

	/**
	 * Construct a version out of a version string and one single delimiter
	 * 
	 * @param versionString
	 *            the version string
	 * @param separatorChars
	 *            the separator character or characters to use
	 */
	protected Version(final String versionString, final String separatorChars) {
		this.string = versionString;
		this.components = StringHelper.split(versionString, separatorChars);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object o) {
		Version v = (Version) o;
		if (this.components.size() != v.components.size()) {
			return false;
		}
		for (int i = 0; i < this.components.size(); i++) {
			if (!this.components.get(i).equals(v.components.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.string.hashCode();
	}

	/**
	 * @see java.lang.Object#compareTo()
	 */
	public int compareTo(final Object o) {

		final Version that = (Version) o;
		final int componentsSizeThis = this.components.size();
		final int componentsSizeThat = that.components.size();
		Integer componentNumThis, componentNumThat;

		for (int i = 0; i < componentsSizeThis; i++) {

			if (i >= that.components.size()) {
				// return greater if "that" as no components to compare anymore
				return 1;
			}

			final String componentStringThis = this.components.get(i);
			final String componentStringThat = that.components.get(i);

			// try to convert both components to an integer number
			try {
				componentNumThis = new Integer(componentStringThis);
			} catch (NumberFormatException e) {
				componentNumThis = null;
			}
			try {
				componentNumThat = new Integer(componentStringThat);
			} catch (NumberFormatException e) {
				componentNumThat = null;
			}

			if (componentNumThis != null && componentNumThat != null) {
				// if both components are numbers perform an Integer compareTo()
				final int c = componentNumThis.compareTo(componentNumThat);
				if (c != 0) {
					return c;
				}
			} else if (componentNumThis == null && componentNumThat == null) {
				// if both components are no numbers compare them alphabetical
				final int c = componentStringThis.compareTo(componentStringThat);
				if (c > 0) {
					return 1;
				}
				if (c < 0) {
					return -1;
				}
				// pure numbers will always be estimated to be a greater version
				// than other strings
			} else if (componentNumThis != null && componentNumThat == null) {
				return 1;
			} else if (componentNumThis == null && componentNumThat != null) {
				return -1;
			}
		}

		// return less if that still has components left
		if (componentsSizeThat > componentsSizeThis) {
			return -1;
		}
		return 0;
	}

	public double weight() {
		final int componentsSizeThis = this.components.size();
		double weight = 0;
		double fac1 = 1;
		for (int i = 0; i < componentsSizeThis; i++) {
			final String compstring = this.components.get(i);
			double compnum = 0;
			try {
				compnum = Double.parseDouble(compstring);
			} catch (NumberFormatException e) {
				final int len = compstring.length();
				char c;
				double fac2 = 0.01;
				for (int j = 0; j < len; j++) {
					c = compstring.charAt(j);
					compnum += c * fac2;
					fac2 /= 100;
				}
			}
			weight += fac1 * compnum;
			fac1 /= 10;
		}
		return weight;
	}

	/**
	 * Determine the nearest version out of a given version collection.
	 * 
	 * @param versions
	 *            the given version collection
	 * 
	 * @return the nearest version
	 */
	public Version getNearest(final Collection<Version> versions) {
		return getNearest(versions.toArray(VA));
	}

	private static final Version[] VA = new Version[0];

	/**
	 * Determine the nearest version out of a given version array.
	 * 
	 * @param versions
	 *            the given version array
	 * 
	 * @return the nearest version
	 */
	public Version getNearest(final Version[] versions) {
		Version nearest = null;
		double minDist = Double.MAX_VALUE;
		final double weight = this.weight();
		for (final Version version : versions) {
			final double dist = Math.abs(version.weight() - weight);
			if (dist == 0) {
				return version;
			}
			if (dist < minDist) {
				nearest = version;
				minDist = dist;
			}
		}
		return nearest;
	}
}
