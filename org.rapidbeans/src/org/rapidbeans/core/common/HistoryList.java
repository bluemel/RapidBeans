/*
 * Rapid Beans Framework: HistoryList.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/11/2009
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

package org.rapidbeans.core.common;

import java.util.ArrayList;

/**
 * A simple list container for any histories.
 * 
 * @author Martin Bluemel
 */
public class HistoryList<T> {

	public static final int DEFAULT_MAX = 10;

	/**
	 * The maximum number of elements contained in this list.
	 */
	private int max = -1;

	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @param newmax
	 *            the new max value to set
	 */
	public void setMax(final int newmax) {
		if (max < 0) {
			throw new IllegalArgumentException("Negative max = "
					+ Integer.toString(max) + " does not make any sense");
		}
		for (int i = this.list.size() - 1; i >= newmax; i--) {
			this.list.remove(i);
		}
		this.max = newmax;
	}

	/**
	 * The collection to store the URLs.
	 */
	private ArrayList<T> list = new ArrayList<T>();

	/**
	 * Constructor.
	 * 
	 * @param max
	 *            the maximum number of URL entries contained in this list.
	 */
	public HistoryList(final int max) {
		if (max < 0) {
			throw new IllegalArgumentException("Negative max = "
					+ Integer.toString(max) + " does not make any sense");
		}
		this.max = max;
	}

	/**
	 * Default Constructor.
	 */
	public HistoryList() {
		this(DEFAULT_MAX);
	}

	/**
	 * Add an element to the history list.
	 * 
	 * @param object
	 *            the object to add
	 */
	public void add(final T object) {
		if (this.list.contains(object)) {
			this.list.remove(object);
		}
		if (this.list.size() == 0) {
			this.list.add(object);
		} else {
			if (this.list.size() < this.max) {
				this.list.add(this.list.get(this.list.size() - 1));
			}
		}
		for (int i = this.list.size() - 2; i >= 0; i--) {
			this.list.set(i + 1, this.list.get(i));
		}
		if (this.list.size() > 0) {
			this.list.set(0, object);
		}
	}

	/**
	 * @param index
	 *            the index to get the entry from
	 * 
	 * @return the element at the specified index
	 */
	public T get(final int index) {
		return this.list.get(index);
	}

	/**
	 * @return the size of this history list
	 */
	public int size() {
		return this.list.size();
	}

	/**
	 * @return a read only iterator for the history list
	 */
	public ReadonlyIteratorCollection<T> iterator() {
		return new ReadonlyIteratorCollection<T>(this.list.iterator());
	}
}
