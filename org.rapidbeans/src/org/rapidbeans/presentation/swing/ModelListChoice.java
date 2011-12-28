/*
 * Rapid Beans Framework: ModelListChoice.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/22/2006
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

import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListModel;

import org.rapidbeans.core.basic.PropertyChoice;
import org.rapidbeans.core.basic.RapidBean;


/**
 * the combo box model for BBEnums.
 *
 * @author Martin Bluemel
 */
public final class ModelListChoice extends DefaultListModel {

    /**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * the enum type.
     */
    private PropertyChoice choiceProp = null;

    /**
     * constructor.
     * @param etype the RapidEnum type
     */
    public ModelListChoice(final PropertyChoice prop) {
        this.choiceProp = prop;
    }

    /**
     * @return the number of enum elements
     */
    public int getSize() {
        final Collection<?> chosen = this.choiceProp.getValue();
        if (chosen == null) {
            return 0;
        } else {
            return chosen.size();
        }
    }

    /**
     * @param index the index
     * @return the enum of index
     */
    public Object getElementAt(final int index) {
        final List<?> chosen = this.choiceProp.getValue();
        if (chosen == null) {
            return null;
        } else {
            return chosen.get(index);
        }
    }

    /**
     * fire.
     *
     * @param bean the bean added
     */
    public void fireBeanAdded(final RapidBean bean) {
        if (this.choiceProp.getValue() == null) {
            this.fireContentsChanged(this, 0, 0);
        } else {
            this.fireContentsChanged(this, 0, this.choiceProp.getValue().size());
        }
    }

    /**
     * fire.
     *
     * @param bean the bean removed
     */
    public void fireBeanRemoved(final RapidBean bean) {
        if (this.choiceProp.getValue() == null) {
            this.fireContentsChanged(this, 0, 0);
        } else {
            this.fireContentsChanged(this, 0, this.choiceProp.getValue().size());
        }
    }

    /**
     * fire.
     *
     * @param prop the choice property changed
     */
    public void fireChoicePropChanged(final PropertyChoice prop) {
        if (prop.getValue() == null) {
            this.fireContentsChanged(this, 0, 0);
        } else {
            this.fireContentsChanged(this, 0, prop.getValue().size());
        }
    }
}
