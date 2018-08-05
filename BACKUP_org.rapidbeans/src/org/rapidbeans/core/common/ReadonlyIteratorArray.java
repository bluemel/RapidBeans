/*
 * Rapid Beans Framework: ReadonlyIteratorArray.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 03/31/2006
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

import java.util.ListIterator;

import org.rapidbeans.core.exception.ImmutableCollectionException;

/**
 * An iteratator for an immutable Collection with potential List features.
 * 
 * @author Martin Bluemel
 */
public final class ReadonlyIteratorArray<T> implements ListIterator<T> {

	/**
	 * the encapsulated iterator.
	 */
	private T[] array = null;

	/**
	 * the index.
	 */
	private int index = 0;

	/**
	 * @param ar
	 *            the arry to iterate over.
	 */
	public ReadonlyIteratorArray(final T[] ar) {
		this.array = ar;
	}

	/**
	 * @return if there is a next element.
	 */
	public boolean hasNext() {
		return this.index < this.array.length;
	}

	/**
	 * @return the next element of the collection to iterate over
	 */
	public T next() {
		if (this.index < this.array.length) {
			return (T) this.array[this.index++];
		} else {
			return null;
		}
	}

	/**
	 * @return if there is a previous element
	 */
	public boolean hasPrevious() {
		return this.index >= 0;
	}

	/**
	 * @return the previous element of the colletion to iterate over
	 */
	public T previous() {
		if (this.index >= 0) {
			return (T) this.array[this.index--];
		} else {
			return null;
		}
	}

	/**
	 * @return the next index
	 */
	public int nextIndex() {
		return this.index + 1;
	}

	/**
	 * @return the previous index
	 */
	public int previousIndex() {
		return this.index - 1;
	}

	/**
	 * it ist not allowed to mute the collection.
	 */
	public void remove() {
		throw new ImmutableCollectionException();
	}

	/**
	 * it ist not allowed to mute the collection.
	 * 
	 * @param o
	 *            the element to set
	 */
	public void set(final Object o) {
		throw new ImmutableCollectionException();
	}

	/**
	 * it ist not allowed to mute the collection.
	 * 
	 * @param o
	 *            the element to add
	 */
	public void add(final Object o) {
		throw new ImmutableCollectionException();
	}
}
