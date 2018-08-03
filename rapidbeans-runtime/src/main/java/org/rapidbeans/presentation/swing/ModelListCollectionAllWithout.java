/*
 * Rapid Beans Framework: ModelListCollectionAllWithout.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListModel;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.Document;

/**
 * The combo box model for bean collections.
 * 
 * @author Martin Bluemel
 */
public final class ModelListCollectionAllWithout extends DefaultListModel {

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the bean type.
	 */
	private PropertyCollection colProp = null;

	/**
	 * the bean listAllBeans.
	 */
	private List<RapidBean> listAllBeans = null;

	/**
	 * the Property's listAllBeans.
	 */
	private ReadonlyListCollection<RapidBean> listProp = null;

	/**
	 * the document.
	 */
	private Document document = null;

	/**
	 * the typename.
	 */
	private String typename = null;

	/**
	 * Flag that indicates to provide only valid values
	 */
	private boolean showOnlyValid = false;

	/**
	 * constructor.
	 * 
	 * @param prop the collection property
	 * @param type the type
	 * @param doc  the document
	 */
	@SuppressWarnings("unchecked")
	public ModelListCollectionAllWithout(final PropertyCollection prop, final TypeRapidBean type, final Document doc,
			final boolean showOnlyValid) {
		this.colProp = prop;
		this.typename = type.getName();
		this.document = doc;
		this.listProp = (ReadonlyListCollection<RapidBean>) colProp.getValue();
		this.showOnlyValid = showOnlyValid;
		updateList();
	}

	/**
	 * @return the number of enum elements
	 */
	public int getSize() {
		if (this.listAllBeans == null) {
			return 0;
		} else {
			return this.listAllBeans.size();
		}
	}

	/**
	 * @param index the index
	 * @return the enum of index
	 */
	public Object getElementAt(final int index) {
		if (this.listAllBeans == null) {
			return null;
		} else {
			return this.listAllBeans.get(index);
		}
	}

	/**
	 * update the listAllBeans.
	 */
	protected void updateList() {
		List<RapidBean> allBeans = this.document.findBeansByType(this.typename);
		if (this.listProp == null) {
			this.listAllBeans = allBeans;
		} else {
			this.listAllBeans = new ArrayList<RapidBean>();
			for (RapidBean bean : (List<RapidBean>) allBeans) {
				if (!this.listProp.contains(bean)) {
					if (this.showOnlyValid) {
						boolean valid = false;
						try {
							this.colProp.validate(bean);
							valid = true;
						} catch (ValidationException e) {
						}
						if (valid) {
							this.listAllBeans.add(bean);
						}
					} else {
						this.listAllBeans.add(bean);
					}
				}
			}
		}
	}

	/**
	 * fire.
	 * 
	 * @param bean the bean added
	 */
	public void fireBeanAdded(final RapidBean bean) {
		this.updateList();
		this.fireContentsChanged(this, 0, this.listAllBeans.size());
	}

	/**
	 * fire.
	 * 
	 * @param bean the bean removed
	 */
	public void fireBeanRemoved(final RapidBean bean) {
		this.updateList();
		this.fireContentsChanged(this, 0, this.listAllBeans.size());
	}

	/**
	 * fire.
	 * 
	 * @param prop the collection property changed
	 */
	@SuppressWarnings("unchecked")
	public void fireColPropChanged(final PropertyCollection prop) {
		if (prop.getValue() != null && this.listAllBeans != null && (this.listProp == null
				|| (!this.listProp.isSameCollection((Collection<RapidBean>) prop.getValue())))) {
			this.listProp = (ReadonlyListCollection<RapidBean>) prop.getValue();
			this.updateList();
			this.fireContentsChanged(this, 0, this.listAllBeans.size());
		}
	}
}
