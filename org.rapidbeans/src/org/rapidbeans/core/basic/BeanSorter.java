/*
 * Rapid Beans Framework: BeanSorter.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/25/2006
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

package org.rapidbeans.core.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.rapidbeans.core.type.TypeProperty;

/**
 * a thread local sorter for a list of Rapid Beans.
 * 
 * @author Martin Bluemel
 */
public class BeanSorter {

	/**
	 * Thread local property sorter.
	 */
	private static ThreadLocal<TypeProperty[]> propertySortOrderTL = new ThreadLocal<TypeProperty[]>() {
		protected synchronized TypeProperty[] initialValue() {
			return null;
		}
	};

	/**
	 * @return the thread local property sorter
	 */
	public static TypeProperty[] get() {
		return propertySortOrderTL.get();
	}

	/**
	 * @param props
	 *            the thread local property sorter to apply
	 */
	public static void set(final TypeProperty[] props) {
		propertySortOrderTL.set(props);
	}

	/**
	 * this property list defines the sort ordering.
	 */
	private ArrayList<TypeProperty> propertySortOrder = null;

	/**
	 * creates a sorter.
	 * 
	 * @param proptypes
	 *            the property types in sort order
	 */
	public BeanSorter() {
		this.propertySortOrder = new ArrayList<TypeProperty>();
	}

	/**
	 * creates a sorter.
	 * 
	 * @param proptypes
	 *            the property types in sort order
	 */
	public BeanSorter(final TypeProperty[] proptypes) {
		this.propertySortOrder = new ArrayList<TypeProperty>();
		if (proptypes != null) {
			for (TypeProperty proptype : proptypes) {
				this.propertySortOrder.add(proptype);
			}
		}
	}

	/**
	 * Add a further sort criteria.
	 * 
	 * @param proptype
	 *            the type of the property to take as sorting criteria
	 */
	public void addSortCriteria(final TypeProperty proptype) {
		this.propertySortOrder.add(proptype);
	}

	/**
	 * sorts a collection of Rapid Beans.
	 * 
	 * @param beans
	 *            the collection to sort
	 * 
	 * @return the sorted list
	 */
	public List<RapidBean> sort(final Collection<RapidBean> beans) {

		// sort that collection
		// set the property sort order thread local
		set(this.propertySortOrder
				.toArray(new TypeProperty[this.propertySortOrder.size()]));
		SortedSet<RapidBean> sortedBeans = new TreeSet<RapidBean>();
		for (RapidBean bean : beans) {
			sortedBeans.add(bean);
		}
		set(null);

		// convert the result to an array list
		List<RapidBean> sortedList = new ArrayList<RapidBean>();
		for (RapidBean bean : sortedBeans) {
			sortedList.add(bean);
		}
		return sortedList;
	}
}
