/*
 * Rapid Beans Framework, SDK, Ant Tasks: MergeProperties.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 07/01/2005
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

/**
 * Encapsulates all Merge properties to avoid too much params.
 * 
 * @author Martin Bluemel
 */
public class MergeProperties {

	/**
	 * the oneLineComment.
	 */
	private String oneLineComment = null;

	/**
	 * the marker for the begin of a section.
	 */
	private String sectionBegin = null;

	/**
	 * the marker for the end of a section.
	 */
	private String sectionEnd = null;

	/**
	 * the marker for the begin of an unmatched section.
	 */
	private String sectionUnmatchedBegin = null;

	/**
	 * the marker for the end of an unmatched section.
	 */
	private String sectionUnmatchedEnd = null;

	/**
	 * the default constructor.
	 */
	public MergeProperties() {
	}

	/**
	 * @param argOneLineComment
	 *            the string to mark the begin of a one line comment
	 * @param argSectionBegin
	 *            the string to mark the begin of a code section
	 * @param argSectionEnd
	 *            the string to mark the end of a code section
	 * @param argSectionUnmatchedBegin
	 *            the string to mark the begin of an unmatched code section
	 * @param argSectionUnmatchedEnd
	 *            the string to mark the end of an unmatched code section
	 * 
	 */
	public MergeProperties(final String argOneLineComment,
			final String argSectionBegin, final String argSectionEnd,
			final String argSectionUnmatchedBegin,
			final String argSectionUnmatchedEnd) {
		this.oneLineComment = argOneLineComment;
		this.sectionBegin = argSectionBegin;
		this.sectionEnd = argSectionEnd;
		this.sectionUnmatchedBegin = argSectionUnmatchedBegin;
		this.sectionUnmatchedEnd = argSectionUnmatchedEnd;
	}

	/**
	 * @return Returns the oneLineComment.
	 */
	public final String getOneLineComment() {
		return this.oneLineComment;
	}

	/**
	 * @return Returns the sectionBegin.
	 */
	public final String getSectionBegin() {
		return this.sectionBegin;
	}

	/**
	 * @return Returns the sectionEnd.
	 */
	public final String getSectionEnd() {
		return this.sectionEnd;
	}

	/**
	 * @return Returns the sectionUnmatchedBegin.
	 */
	public final String getSectionUnmatchedBegin() {
		return this.sectionUnmatchedBegin;
	}

	/**
	 * @return Returns the sectionUnmatchedEnd.
	 */
	public final String getSectionUnmatchedEnd() {
		return this.sectionUnmatchedEnd;
	}
}
