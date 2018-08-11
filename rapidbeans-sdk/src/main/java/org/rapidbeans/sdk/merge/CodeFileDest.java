/*
 * Rapid Beans Framework, SDK, Ant Tasks: CodeFileDest.java
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

package org.rapidbeans.sdk.merge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tools.ant.BuildException;

/**
 * the source file with the generated code.
 * 
 * @author Martin Bluemel
 */
public class CodeFileDest extends CodeFile {

	/**
	 * constructor.
	 * 
	 * @param argFile the file
	 */
	public CodeFileDest(final File argFile) {
		super(argFile);
	}

	/**
	 * validation.
	 */
	public final void validate() {
		if (this.getFile() == null) {
			throw new BuildException("No value defined for mandatory attribute \"destfile\".");
		}
		if (!this.getFile().getParentFile().exists()) {
			this.getFile().getParentFile().mkdirs();
		}
		if (!this.getFile().getParentFile().exists()) {
			throw new BuildException("invalid file given for attribute \"destfile\"."
					+ "Can't nether find nor create parent directory for" + " file \""
					+ this.getFile().getAbsolutePath() + "\".");
		}
		if (this.getFile().exists() && (!this.getFile().canWrite())) {
			throw new BuildException("invalid file given for attribute \"destfile\"." + "Can't write file \""
					+ this.getFile().getAbsolutePath() + "\".");
		}
		if (this.getFile().exists() && this.getFile().isDirectory()) {
			throw new BuildException("invalid file given for attribute \"destfile\"." + "File \""
					+ this.getFile().getAbsolutePath() + "\" is a directory. Expected a file.");
		}
	}

	/**
	 * the merge.
	 * 
	 * @param srcfilegen            gen
	 * @param srcfileman            man
	 * @param beginUnmatchedSection marker string
	 * @param endUnmatchedSection   marker string
	 */
	public final void merge(final CodeFileGen srcfilegen, final CodeFileMan srcfileman,
			final String beginUnmatchedSection, final String endUnmatchedSection) {

		// step 1: merge all bodies from srcfileman over the corresponding
		// bodies in srcfilegen
		for (final CodeFilePart part : srcfilegen.getParts()) {
			if (part instanceof CodeFilePartBody && srcfileman != null) {
				final CodeFilePartBody bodygen = (CodeFilePartBody) part;
				final String signature = bodygen.getSignature();
				final CodeFilePartBody bodyman = srcfileman.getBody(signature);
				if (bodyman != null) {
					if (bodyman.getMerged()) {
						throw new BuildException("Body \"" + signature + "\"" + "is already merged.");
					}
					this.appendBodyPart(bodyman);
					bodyman.setMerged(true);
				} else {
					this.appendBodyPart(bodygen);
				}
			} else {
				this.appendPart(part);
			}
		}

		// step 2: append all unmatched bodies (bodies from srcfileman without
		// a corresponding body signature in srgfilegen
		if (srcfileman != null) {
			for (final CodeFilePart part2 : srcfileman.getParts()) {
				if (part2 instanceof CodeFilePartBody) {
					final CodeFilePartBody bodyman = (CodeFilePartBody) part2;
					if (!bodyman.getMerged() && !bodyman.isEmpty()) {
						this.appendPart(new CodeFilePartLine(""));
						this.appendPart(new CodeFilePartLine(beginUnmatchedSection));
						this.appendBodyPart(bodyman);
						this.appendPart(new CodeFilePartLine(endUnmatchedSection));
					}
				}
			}
		}
	}

	/**
	 * the write.
	 * 
	 * @throws IOException IO problem
	 */
	public final void write() throws IOException {
		try (final FileWriter wr = new FileWriter(this.getFile())) {
			for (final CodeFilePart part : this.getParts()) {
				wr.write(part.getText());
			}
		}
	}
}
