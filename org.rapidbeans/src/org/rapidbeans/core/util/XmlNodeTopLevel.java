/*
 * Rapid Beans Framework: XmlNodeTopLevel.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 10/21/2007
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

package org.rapidbeans.core.util;

import org.w3c.dom.Node;

/**
 * @author Martin Bluemel
 *
 * Utility class for primitive XML parsing using
 * good old JAXP DOM parser technology.
 */
public class XmlNodeTopLevel extends XmlNode {

    /**
     * the XML document's encoding.
     */
    private String encoding = null;

    /**
     * @return the XML document's encoding
     */
    public String getEncoding() {
        return this.encoding;
    }

    /**
     * constructor.
     * @param argNode the node to encupsulate
     * @param enc the XML document's encoding
     */
    public XmlNodeTopLevel(final Node argNode, final String enc) {
        super(argNode);
        this.encoding = enc;
    }
}
