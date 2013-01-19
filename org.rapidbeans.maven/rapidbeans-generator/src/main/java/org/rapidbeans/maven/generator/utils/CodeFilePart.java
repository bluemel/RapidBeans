/*
 * Rapid Beans Framework, SDK, Ant Tasks: CodeFilePart.java
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

/**
 * abstract class for every part of the code file.
 * 
 * @author Martin Bluemel
 */
public abstract class CodeFilePart {

    /**
     * Default constructor.
     */
    public CodeFilePart() {
        super();
    }

    /**
     * @return text as String.
     */
    public abstract String getText();
}
