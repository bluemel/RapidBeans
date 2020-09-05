/*
 * Rapid Beans Framework: ModelComboBoxEnum.java
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

import javax.swing.DefaultComboBoxModel;

import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypePropertyQuantity;
import org.rapidbeans.core.type.TypeRapidEnum;

/**
 * the combo box model for RapidEnums.
 * 
 * @author Martin Bluemel
 */
public final class ModelComboBoxEnum<E> extends DefaultComboBoxModel<Object> {

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * flag if empty list entry should be provided.
	 */
	private boolean provideEmpty = true;

	/**
	 * the enum type.
	 */
	private TypeRapidEnum enumtype = null;

	/**
	 * constructor.
	 * 
	 * @param quantitytype the quantity property type
	 */
	public ModelComboBoxEnum(final TypePropertyQuantity quantitytype) {
		this.enumtype = quantitytype.getQuantitytype().getUnitInfo();
		this.provideEmpty = false;
	}

	/**
	 * constructor.
	 * 
	 * @param choicetype the choice property type
	 */
	public ModelComboBoxEnum(final TypePropertyChoice choicetype) {
		this.enumtype = choicetype.getEnumType();
		if (choicetype.getMandatory()) {
			this.provideEmpty = false;
		}
	}

	/**
	 * @return the number of enum elements
	 */
	public int getSize() {
		int size = this.enumtype.getElements().size();
		if (this.provideEmpty) {
			size++;
		}
		return size;
	}

	/**
	 * @param index the index
	 * @return the enum of index
	 */
	public Object getElementAt(final int index) {
		if (this.provideEmpty) {
			if (index == 0) {
				return null;
			} else {
				return this.enumtype.getElements().get(index - 1);
			}
		} else {
			return this.enumtype.getElements().get(index);
		}
	}
}
