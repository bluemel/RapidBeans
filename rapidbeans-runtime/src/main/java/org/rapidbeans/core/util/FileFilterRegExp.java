/*
 * Rapid Beans Framework: FileFilterRegExp.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/09/2005
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

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * A file filter using a regular expression.
 * 
 * @author Martin Bluemel
 */
public final class FileFilterRegExp implements FileFilter {

	/**
	 * the Java regular expression to match.
	 */
	private Pattern regExpPattern = null;

	/**
	 * Construct a a file filter using a regular expression.
	 * 
	 * @param pattern the regular expression to match. Basically this is a Java
	 *                regular expression but to be more compatible with usual file
	 *                regular expressions '*' is translated to '.*'. So for example
	 *                you just have to write *.xml instead of .*.xml.
	 * 
	 */
	public FileFilterRegExp(final String pattern) {
		final int len = pattern.length();
		final StringBuffer buffer = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			final char c = pattern.charAt(i);
			switch (c) {
			case '*':
				buffer.append(".*");
				break;
			default:
				buffer.append(c);
				break;
			}
		}
		this.regExpPattern = Pattern.compile(buffer.toString());
	}

	/**
	 * @param file the File to test.
	 * 
	 * @return if the file matches the pattern or not.
	 */
	public boolean accept(final File file) {
		return this.regExpPattern.matcher(file.getName()).matches();
	}
}
