/*
 * Rapid Beans Framework: ModelListCollectionAllWithout.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/19/2009
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
import java.util.List;

import javax.swing.DefaultListModel;

import org.rapidbeans.core.basic.PropertyChoice;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.type.TypePropertyChoice;

/**
 * The list model for bean choices' complements (all what is not in the choice).
 * 
 * @author Martin Bluemel
 */
public final class ModelListChoiceWithout extends DefaultListModel {

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the bean type.
	 */
	private PropertyChoice choiceProp = null;

	/**
	 * the bean listAllEnums.
	 */
	private List<RapidEnum> listAllEnums = null;

	/**
	 * the choice property's type
	 */
	private TypePropertyChoice choicePropType = null;

	/**
	 * constructor.
	 * 
	 * @param prop
	 *            the collection property
	 * @param type
	 *            the type
	 * @param doc
	 *            the document
	 */
	public ModelListChoiceWithout(final PropertyChoice prop, final TypePropertyChoice type) {
		this.choiceProp = prop;
		this.choicePropType = type;
		updateList();
	}

	/**
	 * @return the number of enum elements
	 */
	public int getSize() {
		if (this.listAllEnums == null) {
			return 0;
		} else {
			return this.listAllEnums.size();
		}
	}

	/**
	 * @param index
	 *            the index
	 * @return the enum of index
	 */
	public Object getElementAt(final int index) {
		if (this.listAllEnums == null) {
			return null;
		} else {
			return this.listAllEnums.get(index);
		}
	}

	/**
	 * update the listAllEnums.
	 */
	protected void updateList() {
		final ReadonlyListCollection<RapidEnum> allEnums = new ReadonlyListCollection<RapidEnum>(this.choicePropType
				.getEnumType().getElements(), this.choicePropType);
		this.listAllEnums = new ArrayList<RapidEnum>();
		final ReadonlyListCollection<?> currentPropValue = this.choiceProp.getValue();
		for (RapidEnum singleEnum : allEnums) {
			if (currentPropValue == null || (!currentPropValue.contains(singleEnum))) {
				this.listAllEnums.add(singleEnum);
			}
		}
	}

	/**
	 * fire.
	 * 
	 * @param prop
	 *            the collection property changed
	 */
	public void fireChoicePropChanged(final PropertyChoice prop) {
		this.updateList();
		this.fireContentsChanged(this, 0, this.listAllEnums.size());
	}
}
