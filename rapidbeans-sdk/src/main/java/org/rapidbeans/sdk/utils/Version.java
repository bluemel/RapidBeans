/*
 * Rapid Beans Framework, SDK, Ant Tasks: Version.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/15/2005
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

package org.rapidbeans.sdk.utils;

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
	 * @param index the index of the version component
	 * 
	 * @return a single version component
	 */
	protected final String getComponent(final int index) {
		return this.components.get(index);
	}

	/**
	 * Construct a version out of a version string.
	 * 
	 * @param versionString - the version string. '.' is assumed to be the separator
	 *                      character.
	 */
	protected Version(final String versionString) {
		this(versionString, ".");
	}

	/**
	 * Construct a version out of a version string and one single delimiter
	 * 
	 * @param versionString  the version string
	 * @param separatorChars the separator character or characters to use
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
				componentNumThis = Integer.parseInt(componentStringThis);
			} catch (NumberFormatException e) {
				componentNumThis = null;
			}
			try {
				componentNumThat = Integer.parseInt(componentStringThat);
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
}
