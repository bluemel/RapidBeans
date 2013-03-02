/*
 * Rapid Beans Framework, SDK, Ant Tasks: TaskMergeSections.java
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
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * This Ant task supports incremental code generation by merging the content of
 * specially marked sections with manual code from one file (sercfileman) to
 * another file (srcfilegen).
 * 
 * Example:
 * 
 * <p>
 * 1) srcfileman:<br/>
 * <code>   ...<br/>
 *    // BEGIN manual code section    </code>&lt;-- section begin comment
 * <code><br/>
 *    // TestClass.doSomething()      </code>&lt;-- section signature<code><br/>
 *    this.xxx = YYY;<br/>
 *    // END manual code section      </code>&lt;-- section end comment
 * <code><br/>
 *    ...</code><br/>
 * </p>
 * <p>
 * 2) srcfilegen:<br/>
 * <code>   ...<br/>
 *    // BEGIN manual code section<br/>
 *    // TestClass.doSomething()<br/>
 *    // END manual code section<br/>
 *    ...</code>
 * </p>
 * 
 * <p>
 * merged file<br/>
 * <code>   ...<br/>
 *    // BEGIN manual code section<br/>
 *    // TestClass.doSomething()<br/>
 *    this.xxx = YYY;<br/>
 *    // END manual code section<br/>
 *    ...</code><br/>
 * </p>
 * 
 * As you see you need three lines to mark a section that should be merged:
 * 
 * <p>
 * <b>- section begin comment:</b> a fix one line comment to mark the begin of a
 * section.
 * </p>
 * <p>
 * <b>- section signature:</b> a one line comment with a string that uniqely
 * identifies the section within one code file. The section signature always
 * directly follows the section begin comment.
 * </p>
 * <p>
 * <b>- section end comment:</b> a fix one line comment to mark the begin of a
 * section.
 * </p>
 * 
 * @author Martin.Bluemel
 */
public final class TaskMergeSections extends Task {

	/**
	 * marks a one line comment.
	 */
	private String oneLineComment = "//";

	/**
	 * set the one line comment characters.
	 * 
	 * @param s
	 *            the one line comment characters
	 */
	public void setOneLineComment(final String s) {
		this.oneLineComment = s;
	}

	/**
	 * begin comment.
	 */
	private String sectionBegin = "BEGIN manual code section";

	/**
	 * set the string to mark the begin of a code section.
	 * 
	 * @param s
	 *            the string to mark the begin of a code section
	 */
	public void setSectionBegin(final String s) {
		this.sectionBegin = s;
	}

	/**
	 * end comment.
	 */
	private String sectionEnd = "END manual code section";

	/**
	 * set the string to mark the end of a code section.
	 * 
	 * @param s
	 *            the string to mark the end of a code section
	 */
	public void setSectionEnd(final String s) {
		this.sectionEnd = s;
	}

	/**
	 * unmatched section begin marker.
	 */
	private String sectionUnmatchedBegin = ">>> BEGIN unmatched code section";

	/**
	 * set the string to mark the begin of an unmatched code section.
	 * 
	 * @param s
	 *            the string to mark the begin of an unmatched code section. If
	 *            you take some characters that guarantee the compilation to
	 *            fail then you instantly are aware of unmatched sections.
	 */
	public void setSectionUnmatchedBegin(final String s) {
		this.sectionUnmatchedBegin = s;
	}

	/**
	 * unmatched section end marker.
	 */
	private String sectionUnmatchedEnd = ">>> END unmatched code section";

	/**
	 * set the string to mark the end of an unmatched code section.
	 * 
	 * @param s
	 *            the string to mark the end of an unmatched code section. If
	 *            you take some characters that guarantee the compilation to
	 *            fail then you instantly are aware of unmatched sections.
	 */
	public void setSectionUnmatchedEnd(final String s) {
		this.sectionUnmatchedEnd = s;
	}

	/**
	 * source file 1, the generated class frame.
	 */
	private CodeFileGen srcfilegen = null;

	/**
	 * setter for source file 1, the generated class frame.
	 * 
	 * @param f
	 *            the new value for this property.
	 */
	public void setSrcfilegen(final File f) {
		this.srcfilegen = new CodeFileGen(f);
	}

	/**
	 * source file 2, the class with manually coded method bodies.
	 */
	private CodeFileMan srcfileman = null;

	/**
	 * setter for source file 2, the class with manually coded method bodies.
	 * 
	 * @param f
	 *            the new value for this property.
	 */
	public void setSrcfileman(final File f) {
		if (f != null) {
			this.srcfileman = new CodeFileMan(f);
		}
	}

	/**
	 * destination file with the merged methods.
	 */
	private CodeFileDest destfile = null;

	/**
	 * setter for the destination file with the merged methods.
	 * 
	 * @param f
	 *            the new value for this property.
	 */
	public void setDestfile(final File f) {
		this.destfile = new CodeFileDest(f, this.antGateway);
	}

	/**
	 * the Ant gateway for convenient use of Ant tesks.
	 */
	private AntGateway antGateway = new AntGateway(new Project(), "");

	/**
	 * The execute method has to be implemented from every Ant task.
	 */
	public void execute() {
		try {
			validateProperties();
			this.log("srcfilegen = \""
					+ this.srcfilegen.getFile().getAbsolutePath(),
					Project.MSG_VERBOSE);
			if (this.srcfileman != null) {
				this.log("srcfileman = \""
						+ this.srcfileman.getFile().getAbsolutePath(),
						Project.MSG_VERBOSE);
			}
			if (this.destfile == null) {
				this.destfile = new CodeFileDest(this.srcfileman.getFile(),
						this.antGateway);
			} else {
				this.log("destfile = \""
						+ this.destfile.getFile().getAbsolutePath(),
						Project.MSG_VERBOSE);
			}
			this.srcfilegen.parse(this.oneLineComment, this.sectionBegin,
					this.sectionEnd);
			if (this.srcfileman != null && this.srcfileman.getFile().exists()) {
				this.srcfileman.parse(this.oneLineComment, this.sectionBegin,
						this.sectionEnd);
			}
			if (this.srcfilegen.getFile().lastModified() > this.destfile
					.getFile().lastModified()
					|| this.srcfileman.getFile().lastModified() > this.destfile
							.getFile().lastModified()) {
				this.log("merging files", Project.MSG_VERBOSE);
				if (this.srcfileman != null
						&& this.srcfileman.getFile().exists()) {
					this.log(
							"merging new file\n"
									+ this.srcfilegen.getFile()
											.getAbsolutePath()
									+ "\nwith bodies of file\n"
									+ this.srcfileman.getFile()
											.getAbsolutePath() + "\n to file\n"
									+ this.destfile.getFile().getAbsolutePath()
									+ "...", Project.MSG_VERBOSE);
				} else {
					this.log(
							"copying new file\n"
									+ this.srcfilegen.getFile()
											.getAbsolutePath() + "\n to file\n"
									+ this.destfile.getFile().getAbsolutePath()
									+ "...", Project.MSG_VERBOSE);
				}
				this.destfile.merge(this.srcfilegen, this.srcfileman,
						this.sectionUnmatchedBegin, this.sectionUnmatchedEnd);
				this.destfile.write();
			} else {
				this.log("nothing to merge", Project.MSG_VERBOSE);
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * the propertie's validation method.
	 */
	protected void validateProperties() {
		if (this.srcfilegen == null) {
			throw new BuildException(
					"No value defined for mandatory attribute \"srcfilegen\".");
		}

		if (this.srcfileman == null && this.destfile == null) {
			throw new BuildException(
					"No values defined for attributes \"srcfileman\" and "
							+ "\"destfile\". Please ether define srcfileman or destfile.");
		}
	}
}
