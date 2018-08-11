/*
 * Rapid Beans Framework, SDK, Ant Tasks: XXslt.java
 * 
 * Copyright (C) 2018 Martin Bluemel
 * 
 * Creation Date: 08/10/2018
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

package org.rapidbeans.sdk.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.rapidbeans.sdk.merge.CodeFileDest;
import org.rapidbeans.sdk.merge.CodeFileGen;
import org.rapidbeans.sdk.merge.CodeFileMan;
import org.rapidbeans.sdk.merge.MergeProperties;

import com.google.common.io.Files;

/**
 * This class combines XSL transformation with merge of protected regions that
 * allow generated files to be mixed with manually written parts.
 * 
 * @author Martin Bluemel
 */
public final class XXslt {

	/**
	 * The input XML file of the XSL transformation.
	 */
	private File in = null;

	/**
	 * The file that will contain the output of the XSL transformation.
	 */
	private File out = null;

	/**
	 * the style sheet or template file.
	 */
	private File style = null;

	/**
	 * If set to false, the transformation will be executed only if the in file has
	 * been modified after the out file has been generated the last time. If set to
	 * true the transformation will be done in every case.
	 */
	private boolean force = false;

	/**
	 * Switches on or off merge of protected regions of previous version of
	 * generated output file.
	 */
	private boolean merge = false;

	/**
	 * One line comment mark like "//" or "#"
	 */
	private String oneLineComment = "//";

	/**
	 * Comment text to to mark the begin of a protected region or section.
	 */
	private String sectionBegin = "BEGIN manual code section";

	/**
	 * Comment text to mark the end of a protected region or section.
	 */
	private String sectionEnd = "END manual code section";

	/**
	 * Text line to mark the begin of an unmatched protected region or section.
	 */
	private String sectionUnmatchedBegin = ">>> BEGIN unmatched code section";

	/**
	 * Text line to mark the end of an unmatched protected region or section.
	 */
	private String sectionUnmatchedEnd = ">>> END unmatched code section";

	/**
	 * the path mode type.
	 */
	private Map<String, String> parameters = null;

	private final SdkLogger logger;

	public XXslt(final SdkLogger logger) {
		this.logger = logger;
	}

	/**
	 * Execute XSLT transformation and sections merge.
	 */
	public void execute(final XslTransformer xslTransformer) {
		if (notForceAndOutUpToDate(this.force, this.in.lastModified(), this.style.lastModified(),
				this.out.lastModified())) {
			this.logger.info(String.format("XSLT out file %s is up to date", out.getAbsolutePath()));
			this.logger.debug(String.format("Neither model file %s nor style sheet %s is newer than output file %s.",
					this.in.getAbsolutePath(), this.style.getAbsolutePath(), this.out.getAbsolutePath()));
			return;
		}
		if (this.merge) {
			merge(xslTransformer);
		} else {
			xslt(this.in, this.style, this.out, this.parameters, xslTransformer);
		}
	}

	private void merge(final XslTransformer xslTransformer) {
		final File out1 = new File(this.out.getAbsolutePath() + ".tmp1");
		final File out2 = new File(this.out.getAbsolutePath() + ".tmp2");
		try {
			if (this.out.exists()) {
				xslt(this.in, this.style, out1, this.parameters, xslTransformer);
				this.logger.debug(
						String.format("merging generated file with previous version %s", this.out.getAbsolutePath()));
				mergeSections(out1, this.out, out2, new MergeProperties(this.oneLineComment, this.sectionBegin,
						this.sectionEnd, this.sectionUnmatchedBegin, this.sectionUnmatchedEnd));
				Files.copy(out2, this.out);
			} else {
				xslt(this.in, this.style, this.out, this.parameters, xslTransformer);
				this.logger.debug(String.format("Merge not neccessary. No previous version of file %s found.",
						this.out.getAbsolutePath()));
			}
		} catch (IOException e) {
			throw new BuildException(e);
		} finally {
			if (out1.exists()) {
				out1.delete();
			}
			if (out2.exists()) {
				out2.delete();
			}
		}
	}

	private void xslt(final File in, final File style, final File out, final Map<String, String> parameters,
			final XslTransformer xslTransformer) {
		this.logger.debug("starting XSL Transformation");
		this.logger.debug("  in = " + in.getAbsolutePath());
		this.logger.debug("  style = " + style.getAbsolutePath());
		this.logger.debug("  out = " + out.getAbsolutePath());
		xslTransformer.transform(in, style, out, parameters);
	}

	private void mergeSections(final File srcgen, final File srcman, final File dest,
			final MergeProperties mergeProperties) throws IOException {
		final CodeFileGen srcfilegen = new CodeFileGen(srcgen);
		final CodeFileMan srcfileman = new CodeFileMan(srcman);
		final CodeFileDest destfile = new CodeFileDest(dest);
		srcfilegen.parse(this.oneLineComment, this.sectionBegin, this.sectionEnd);
		srcfileman.parse(this.oneLineComment, this.sectionBegin, this.sectionEnd);
		destfile.merge(srcfilegen, srcfileman, this.sectionUnmatchedBegin, this.sectionUnmatchedEnd);
		destfile.write();
	}

	public static boolean notForceAndOutUpToDate(final boolean isForced, final long inLastModified,
			final long styleLastModified, final long outLastModified) {
		return !isForced && (inLastModified <= outLastModified && styleLastModified <= outLastModified);
	}

	public void setIn(File in) {
		this.in = in;
	}

	public void setOut(File out) {
		this.out = out;
	}

	public void setStyle(File style) {
		this.style = style;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public void setOneLineComment(String oneLineComment) {
		this.oneLineComment = oneLineComment;
	}

	public void setSectionBegin(String sectionBegin) {
		this.sectionBegin = sectionBegin;
	}

	public void setSectionEnd(String sectionEnd) {
		this.sectionEnd = sectionEnd;
	}

	public void setSectionUnmatchedBegin(String sectionUnmatchedBegin) {
		this.sectionUnmatchedBegin = sectionUnmatchedBegin;
	}

	public void setSectionUnmatchedEnd(String sectionUnmatchedEnd) {
		this.sectionUnmatchedEnd = sectionUnmatchedEnd;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}
