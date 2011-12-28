/*
 * Rapid Beans Framework, SDK, Ant Tasks: CodeFilePartBody.java
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

import org.apache.tools.ant.BuildException;


/**
 * code body.
 *
 * @author Martin Bluemel
 */
public final class CodeFilePartBody extends CodeFilePart {

    /**
     * the signature.
     */
    private String signature = null;

    /**
     * @return the signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * @param s new signature
     */
    public void setSignature(final String s) {
        this.signature = s;
    }

    /**
     * the begin comment.
     */
    private String beginComment = null;

    /**
     * the begin comment's signature.
     */
    private String beginCommentSignature = null;

    /**
     * the body's content.
     */
    private StringBuffer lines = null;

    /**
     * the begin comment.
     */
    private String endComment = null;

    /**
     * @param s begin comment
     */
    public void setBeginComment(final String s) {
        this.beginComment = s;
    }

    /**
     * @param s begin comment, second line with signature
     */
    public void setBeginCommentSignature(final String s) {
        this.beginCommentSignature = s;
    }

    /**
     * @param s end comment
     */
    public void setEndComment(final String s) {
        this.endComment = s;
    }

    /**
     * merged flag.
     */
    private boolean merged = false;

    /**
     * getter.
     * @return if the body already is merged
     */
    public boolean getMerged() {
        return this.merged;
    }

    /**
     * setter.
     * @param b if the body already is merged
     */
    public void setMerged(final boolean b) {
        this.merged = b;
    }

    /**
     * constructor.
     *
     * @param arg the first comment line
     */
    public CodeFilePartBody(final String arg) {
        if (arg == null) {
            throw new BuildException("Null signature is not allowed");
        }
        this.beginComment = arg;
        this.lines = new StringBuffer();
    }

    /**
     * @return if the content is just whitespace chars or not
     */
    public boolean isEmpty() {
        boolean isEmpty = true;
        int length = this.lines.length();
        char c;
        for (int i = 0; i < length; i++) {
            c = this.lines.charAt(i);
            if (c != ' ' && c != '\n' && c != '\t') {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    /**
     * @param line the new line to append.
     */
    public void appendLine(final String line) {
        this.lines.append(line);
        this.lines.append(PlatformHelper.getLineFeed());
    }

    /**
     * @return text as String.
     */
    public String getText() {
        return this.beginComment + PlatformHelper.getLineFeed()
            + this.beginCommentSignature + PlatformHelper.getLineFeed()
            + this.lines.toString()
            + this.endComment + PlatformHelper.getLineFeed();
    }
}
