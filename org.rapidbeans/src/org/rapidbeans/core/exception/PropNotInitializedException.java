/*
 * Rapid Beans Framework: PropNotInitializedException.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 11/22/2006
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

package org.rapidbeans.core.exception;

/**
 * Indicates that a prop has been accessed too early in a
 * state when it was not completely initialized.
 *
 * @author Martin Bluemel
 */
public class PropNotInitializedException extends RapidBeansRuntimeException {

    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * the one and only constructor.
     *
     * @param msg the messae
     */
    public PropNotInitializedException(final String msg) {
        super(msg);
    }
}
