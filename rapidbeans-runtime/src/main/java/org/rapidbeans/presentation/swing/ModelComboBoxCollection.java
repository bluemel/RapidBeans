/*
 * Rapid Beans Framework: ModelComboBoxCollection.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/24/2006
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

import javax.swing.DefaultComboBoxModel;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.Document;

/**
 * The combo box model for bean collections.
 * 
 * @author Martin Bluemel
 */
public class ModelComboBoxCollection<E> extends DefaultComboBoxModel<RapidBean> {

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * flag if empty list entry should be provided.
	 */
	private boolean provideEmpty = false;

	/**
	 * the bean list.
	 */
	private List<RapidBean> list = null;

	/**
	 * getter.
	 * 
	 * @return the bean list.
	 */
	protected List<RapidBean> getList() {
		return this.list;
	}

	/**
	 * constructor of a model that provides a list of all instances of the given
	 * collection propertie's target type in the given document.
	 * 
	 * @param propType the collection property type
	 * @param doc      the document
	 */
	public ModelComboBoxCollection(final PropertyCollection colProp, final Document doc) {
		final TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();
		if (colPropType.getMinmult() == 0 && (!colPropType.getMandatory())) {
			this.provideEmpty = true;
		}
		this.list = new ArrayList<RapidBean>();
		final TypeRapidBean targetType = colPropType.getTargetType();
		TypePropertyCollection inverseColPropType = null;
		if (colPropType.getInverse() != null) {
			inverseColPropType = (TypePropertyCollection) targetType.getPropertyType(colPropType.getInverse());
		}
		for (final RapidBean bean : doc.findBeansByType(targetType.getName())) {
			if (colPropType.getMaxmult() != 1 || (inverseColPropType != null && inverseColPropType.getMaxmult() != 1)
					|| (bean == null) || (bean.getProperty(colPropType.getInverse()) == null)
					|| (bean.getProperty(colPropType.getInverse()).getValue() == null)
					|| (((Collection<?>) ((PropertyCollection) bean.getProperty(colPropType.getInverse())).getValue())
							.size() == 0)
					|| (((Collection<?>) ((PropertyCollection) bean.getProperty(colPropType.getInverse())).getValue())
							.iterator().next() == colProp.getBean())) {
				this.list.add(bean);
			}
		}
	}

	/**
	 * constructor of a model that provides a list of all instances of the given
	 * collection propertie's target type in the given document.
	 * 
	 * @param propType the collection property type
	 * @param doc      the document
	 */
	public ModelComboBoxCollection(final TypePropertyCollection colPropType, final Collection<RapidBean> col) {
		if (colPropType.getMinmult() == 0 && (!colPropType.getMandatory())) {
			this.provideEmpty = true;
		}
		this.list = new ReadonlyListCollection<RapidBean>(col, colPropType);
	}

	/**
	 * detect model changes while delivering the size.
	 * 
	 * @return the number of elements
	 */
	public int getSize() {
		int size = this.list.size();
		if (this.provideEmpty) {
			size++;
		}
		return size;
	}

	/**
	 * @param index the index
	 * @return the element of index
	 */
	public RapidBean getElementAt(final int index) {
		if (this.provideEmpty) {
			if (index == 0) {
				return null;
			} else {
				return this.list.get(index - 1);
			}
		} else {
			return this.list.get(index);
		}
	}
}
