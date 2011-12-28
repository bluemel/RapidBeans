/*
 * Rapid Beans Framework: DocumentTreeViewListener.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/14/2006
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

package org.rapidbeans.presentation;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;

/**
 * listener interface for bean document tree views.
 *
 * @author Martin Bluemel
 */
interface DocumentTreeViewListener {

    /**
     * @param keys the tree path
     * @param beans the bean recently selected.
     *
     * @return the bean editor
     */
    EditorBean editBeans(Object[] keys, RapidBean[] beans);

    /**
     * create a bean of the given type.
     * @param key the tree path of the parent collection property
     * @param parentBeanColProp the parent bean Collection Property
     *                          of the new bean
     *
     * @return the bean editor
     */
    EditorBean createBean(Object key, PropertyCollection parentBeanColProp);
}
