/*
 * Rapid Beans Framework, SDK, Ant Tasks: CodeFile.java
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

package org.rapidbeans.maven.generator.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * code file.
 * 
 * @version initial
 * 
 * @author Martin Bluemel
 */
public abstract class CodeFile {

    /**
     * handle for the code file's physical file.
     */
    private File file = null;

    /**
     * getter for the file.
     * 
     * @return the file
     */
    public final File getFile() {
        return this.file;
    }

    /**
     * the code file's parts.
     */
    private List<CodeFilePart> parts = null;

    /**
     * getter.
     * 
     * @return the parts list
     */
    public final List<CodeFilePart> getParts() {
        return this.parts;
    }

    /**
     * the code bodie's map.
     */
    private HashMap<String, CodeFilePartBody> bodies = null;

    /**
     * retrieves the body with the appropriate signature.
     * 
     * @param signature
     *            the signature
     * @return the body part
     */
    public final CodeFilePartBody getBody(final String signature) {
        return this.bodies.get(signature);
    }

    /**
     * append a part.
     * 
     * @param part
     *            the part
     */
    public final void appendPart(final CodeFilePart part) {
        this.parts.add(part);
    }

    /**
     * append a part.
     * 
     * @param part
     *            the part
     */
    public final void appendBodyPart(final CodeFilePartBody part) {
        this.parts.add(part);
        this.bodies.put(part.getSignature(), part);
    }

    /**
     * parser stae: basic.
     */
    private static final int STATE_BASIC = 0;

    /**
     * parser state: at the begin of a code body.
     */
    private static final int STATE_BEGIN_MANCODE_BODY = 1;

    /**
     * parser stae: within code body.
     */
    private static final int STATE_MANCODE_BODY = 2;

    /**
     * validation.
     */
    protected abstract void validate();

    /**
     * parse the file.
     * 
     * @param oneLineComment
     *            combination of character to mark a one line comment
     * @param beginSection
     *            text of line (comment) that marks the begin of a manually
     *            coded section.
     * @param endSection
     *            text of line (comment) that marks the end of a manually coded
     *            section
     * @throws IOException
     *             I/O problem
     */
    public final void parse(final String oneLineComment, final String beginSection, final String endSection)
            throws IOException {
        final String beginSectionComment = oneLineComment + " " + beginSection;
        final String endSectionComment = oneLineComment + " " + endSection;
        final LineNumberReader reader = new LineNumberReader(new FileReader(this.getFile()));
        String line, trimmedLine, signature;
        CodeFilePartBody body = null;
        int state = 0;
        while ((line = reader.readLine()) != null) {
            trimmedLine = line.trim();
            switch (state) {
            case STATE_BASIC:
                if (trimmedLine.startsWith(beginSectionComment)) {
                    body = new CodeFilePartBody(line);
                    state = STATE_BEGIN_MANCODE_BODY;
                } else {
                    appendPart(new CodeFilePartLine(line));
                    state = STATE_BASIC;
                }
                break;
            case STATE_BEGIN_MANCODE_BODY:
                signature = stripOneLineComment(trimmedLine);
                body.setBeginCommentSignature(line);
                body.setSignature(signature);
                state = STATE_MANCODE_BODY;
                break;
            case STATE_MANCODE_BODY:
                if (trimmedLine.startsWith(endSectionComment)) {
                    body.setEndComment(line);
                    this.appendBodyPart(body);
                    state = STATE_BASIC;
                } else {
                    body.appendLine(line);
                    state = STATE_MANCODE_BODY;
                }
                break;
            default:
                throw new BuildException("Parser error: wrong state: " + Integer.toString(state));
            }
        }
        reader.close();
    }

    /**
     * constructor.
     * 
     * @param argFile
     *            the file
     */
    public CodeFile(final File argFile) {
        this.file = argFile;
        this.validate();
        this.parts = new ArrayList<CodeFilePart>();
        this.bodies = new HashMap<String, CodeFilePartBody>();
    }

    /**
     * default strip characters (whitespace).
     */
    private static final char[] STRIP_CHARS_ONE_LINE_COMMENT = { ' ', '\t', '/' };

    /**
     * strips "   // xxx yyy" to "xxx yyy".
     * 
     * @param s
     *            the input string
     * @return the stripped string
     */
    private static String stripOneLineComment(final String s) {
        return StringHelper.strip(s, STRIP_CHARS_ONE_LINE_COMMENT, StripMode.leading);
    }
}
