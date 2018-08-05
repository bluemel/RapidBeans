/*
 * Rapid Beans Framework, SDK, Ant Tasks: CodeFileGen.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 10/29/2005
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

import java.io.File;

import org.apache.tools.ant.BuildException;

/**
 * the source file with the generated code.
 * 
 * @author Martin Bluemel
 * 
 */
public class CodeFileGen extends CodeFile {

	/**
	 * constructor.
	 * 
	 * @param argFile
	 *            the file
	 */
	// * @throws IOException I/O problem
	public CodeFileGen(final File argFile) {
		// throws IOException {
		super(argFile);
	}

	/**
	 * validation.
	 */
	public final void validate() {
		if (this.getFile() == null) {
			throw new BuildException("No value defined for mandatory attribute \"srcfilegen\".");
		}
		if (!this.getFile().exists()) {
			throw new BuildException("invalid path given for attribute \"srcfilegen\"." + "Can't find file \""
					+ this.getFile().getAbsolutePath() + "\".");
		}
		if (!this.getFile().canRead()) {
			throw new BuildException("invalid file given for attribute \"srcfilegen\"." + "Can't read file \""
					+ this.getFile().getAbsolutePath() + "\".");
		}
		if (this.getFile().isDirectory()) {
			throw new BuildException("invalid file given for attribute \"srcfilegen\"." + "File \""
					+ this.getFile().getAbsolutePath() + "\" is a directory. Expected a file.");
		}
	}
}
