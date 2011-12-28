/*
 * Rapid Beans Framework: TreeChangeInfo.java
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

package org.rapidbeans.presentation.swing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import javax.swing.tree.TreePath;

import org.rapidbeans.core.basic.RapidBean;


/**
 * Tree Change Information to fire a TreeChangeEvent to the tree model.
 *
 * @author Martin Bluemel
 */
class TreeChangeInfo {

    /**
     * the parent path.
     */
    private TreePath parentPath;

    /**
     * the indices of the beans.
     */
    private TreeSet<Integer> indices = new TreeSet<Integer>();

    /**
     * small optimization.
     */
    private int[] indexArray = null;

    /**
     * the beans that a subject of change.
     */
    private Collection<RapidBean> bizBeans = new ArrayList<RapidBean>();

    /**
     * the constructor.
     *
     * @param argParentPath the parent path
     */
    public TreeChangeInfo(final TreePath argParentPath) {
        this.parentPath = argParentPath;
    }

    /**
     * add a bean to the TreeChangeInfo.
     *
     * @param bean the bean to add
     */
    public void addBean(final RapidBean bean) {
        this.bizBeans.add(bean);
    }

    /**
     * a bean array pattern.
     */
    private static final RapidBean[] BA = new RapidBean[0];

    /**
     * @return Returns the bizBeans.
     */
    @SuppressWarnings("synthetic-access")
    public RapidBean[] getBeans() {
        return this.bizBeans.toArray(BA);
    }

    /**
     * add an index to the TreeCHangeInfo.
     *
     * @param index the index to add
     */
    public void addIndex(final int index) {
        this.indices.add(new Integer(index));
    }

    /**
     * @return Returns the indices.
     */
    public int[] getIndices() {
        final int is = this.indices.size();
        if (this.indexArray == null || this.indexArray.length != is) {
            this.indexArray = new int[is];
            int i = 0;
            for (Integer index : this.indices) {
               this.indexArray[i++] = index;
            }
        }
        return this.indexArray;
    }

    /**
     * @return Returns the parentPath.
     */
    public TreePath getParentPath() {
        return this.parentPath;
    }
}
