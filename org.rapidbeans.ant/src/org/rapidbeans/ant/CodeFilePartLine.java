/*
 * Rapid Beans Framework, SDK, Ant Tasks: CodeFilePartLine.java
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
 * Simple line.
 *
 * @version initial
 *
 * @author Martin Bluemel
 */
public final class CodeFilePartLine extends CodeFilePart {

    /**
     * the line.
     */
    private String line = null;

    /**
     * constructor.
     *
     * @param argLine the line
     */
    public CodeFilePartLine(final String argLine) {
        if (argLine == null) {
            throw new BuildException("Null line is not allowed");
        }
        this.line = argLine;
    }

    /**
     * @return text as String.
     */
    public String getText() {
        return this.line + PlatformHelper.getLineFeed();
    }
}
