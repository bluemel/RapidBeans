/*
 * Rapid Beans Framework: TypePropertyVersion.java
 *
 * Copyright (C) 2010 Martin Bluemel
 *
 * Creation Date: 07/04/2010
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

package org.rapidbeans.core.type;

import org.rapidbeans.core.util.XmlNode;


/**
 * the type class for String properties.
 *
 * @author Martin Bluemel
 */
public class TypePropertyVersion extends TypeProperty {

    /**
     * Constructor.
     *
     * @param propertyNodes XML DOM nodes with the property type description
     * @param parentBeanType the parent bean type
     */
    public TypePropertyVersion(final XmlNode[] propertyNodes,
            final TypeRapidBean parentBeanType) {
        super("Version", propertyNodes, parentBeanType);
        String s = propertyNodes[0].getAttributeValue("@default");
        if (s != null) {
            setDefaultValue(s);
        }
    }

    /**
     * @return the property type enumeration
     */
    public PropertyType getProptype() {
        return PropertyType.version;
    }
}
