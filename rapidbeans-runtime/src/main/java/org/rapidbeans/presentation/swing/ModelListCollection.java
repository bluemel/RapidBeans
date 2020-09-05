/*
 * Rapid Beans Framework: ModelListCollection.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/30/2007
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

import javax.swing.DefaultListModel;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.datasource.Document;

/**
 * The combo box model for bean collections.
 * 
 * @author Martin Bluemel
 */
public final class ModelListCollection extends DefaultListModel<Object> {

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the bean type.
	 */
	private PropertyCollection colProp = null;

	/**
	 * the bean list.
	 */
	private ReadonlyListCollection<RapidBean> list = null;

	/**
	 * constructor.
	 * 
	 * @param prop the collection property
	 * @param doc  the document
	 */
	@SuppressWarnings("unchecked")
	public ModelListCollection(final PropertyCollection prop, final Document doc) {
		this.colProp = prop;
		if (colProp.getValue() != null) {
			this.list = (ReadonlyListCollection<RapidBean>) colProp.getValue();
			// this.list = new ReadonlyListCollectionCached(colProp,
			// colProp.getValue());
		} else {
			this.list = null;
		}
	}

	// /**
	// * release the model.
	// */
	// public void release() {
	// if (this.list != null) {
	// this.list.release();
	// }
	// }

	/**
	 * @return the number of enum elements
	 */
	public int getSize() {
		if (this.list == null) {
			return 0;
		} else {
			return this.list.size();
		}
	}

	/**
	 * @param index the index
	 * @return the enum of index
	 */
	public Object getElementAt(final int index) {
		if (this.list == null) {
			return null;
		} else {
			return this.list.get(index);
		}
	}

	/**
	 * fire.
	 * 
	 * @param bean the bean added
	 */
	public void fireBeanAdded(final RapidBean bean) {
		if (this.list == null) {
			this.fireContentsChanged(this, 0, 0);
		} else {
			this.fireContentsChanged(this, 0, this.list.size());
		}
	}

	/**
	 * fire.
	 * 
	 * @param bean the bean removed
	 */
	public void fireBeanRemoved(final RapidBean bean) {
		if (this.list == null) {
			this.fireContentsChanged(this, 0, 0);
		} else {
			this.fireContentsChanged(this, 0, this.list.size());
		}
	}

	/**
	 * fire.
	 * 
	 * @param prop the collection property changed
	 */
	@SuppressWarnings("unchecked")
	public void fireColPropChanged(final PropertyCollection prop) {
		if (prop.getValue() == null) {
			this.list = null;
		} else {
			if (this.list == null || (!this.list.isSameCollection((Collection<RapidBean>) prop.getValue()))) {
				this.list = new ReadonlyListCollection<RapidBean>((Collection<RapidBean>) prop.getValue(),
						(TypePropertyCollection) prop.getType());
			}
			this.fireContentsChanged(this, 0, this.list.size());
		}
	}
}
