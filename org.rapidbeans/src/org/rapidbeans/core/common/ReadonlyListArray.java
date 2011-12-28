/*
 * Rapid Beans Framework: ReadonlyListArray.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 06/02/2006
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.rapidbeans.core.exception.ImmutableCollectionException;

/**
 * An immutable Collection.
 *
 * @author Martin Bluemel
 */
public class ReadonlyListArray<T> implements List<T> {

    /**
     * the array for indexed access.
     */
    private T[] array = null;

    /**
     * @param ary the collection to encapsulate.
     */
    public ReadonlyListArray(final T[] ary) {
        this.array = ary;
    }

    /**
     * @return the size of the collection
     */
    public int size() {
        return this.array.length;
    }

    /**
     * @return if the collection is empty
     */
    public boolean isEmpty() {
        return this.array.length == 0;
    }

    /**
     * @param o the object to query
     * @return if the collection contains a given instance.
     */
    public boolean contains(final Object o) {
        boolean contains = false;
        for (int i = 0; i < this.array.length; i++) {
            if (this.array[i].equals(o)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    /**
     * @TODO xxx
     * @return a readonly form of the Iterator.
     */
    public Iterator<T> iterator() {
        return new ReadonlyIteratorArray<T>(this.array);
    }

    /**
     * returns the array.
     * @return the array
     */
    public Object[] toArray() {
        return this.array;
    }

    /**
     * returns the array.
     * @param a the pattern array
     * @return the array
     */
    @SuppressWarnings("unchecked")
    public Object[] toArray(final Object[] a) {
        return this.array;
    }

    /**
     * adding an object to an immutable collection is not allowed.
     *
     * @param o the object to add
     * @return no return this will throw a ImmutableCollectionException
     */
    public boolean add(final Object o) {
        throw new ImmutableCollectionException();
    }

    /**
     * removing an object of an immutable collection is not allowed.
     *
     * @param o the object to remove
     * @return no return this will throw a ImmutableCollectionException
     */
    public boolean remove(final Object o) {
        throw new ImmutableCollectionException();
    }

    /**
     * @param c the collection to test against
     *
     * @return if this collection contains all instances of the given collection
     */
    public boolean containsAll(final Collection<?> c) {
        boolean containsAll = true;
        int i = 0;
        for (Object o : c) {
            if (!(this.array[i].equals(o))) {
                containsAll = false;
                break;
            }
            i++;
        }
        return containsAll;
    }

    /**
     * adding an object to an immutable collection is not allowed.
     *
     * @param c the collection with instances to add
     * @return no return this will throw a ImmutableCollectionException
     */
    @SuppressWarnings("rawtypes")
    public boolean addAll(final Collection c) {
        throw new ImmutableCollectionException();
    }

    /**
     * removing an instance from an immutable collection is not allowed.
     *
     * @param c the collection with instances to remove
     * @return no return this will throw a ImmutableCollectionException
     */
    public boolean removeAll(final Collection<?> c) {
        throw new ImmutableCollectionException();
    }

    /**
     * removing an instance from an immutable collection is not allowed.
     *
     * @param c the collection with instances not to remove
     * @return no return this will throw a ImmutableCollectionException
     */
    public boolean retainAll(final Collection<?> c) {
        throw new ImmutableCollectionException();
    }

    /**
     * removing an instance from an immutable collection is not allowed.
     */
    public void clear() {
        throw new ImmutableCollectionException();
    }

    /**
     * returns the element at the specified position in the array.
     * @param index the position
     * @return the element at the specified position in the array.
     */
    public T get(final int index) {
        return this.array[index];
    }

    /**
     * returns the first index of the element with the given reference.
     * @param o the object reference tha specifies the element to search for
     * @return the last index of the element with the given reference or -1 if not found
     */
    public int indexOf(final Object o) {
        int index = -1;
        for (int i = 0; i < this.array.length; i++) {
            if (this.array[i].equals(o)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * returns the last index of the element with the given reference.
     * @param o the object reference tha specifies the element to search for
     * @return the last index of the element with the given reference or -1 if not found
     */
    public int lastIndexOf(final Object o) {
        int index = -1;
        for (int i = this.array.length - 1; i >= 0; i--) {
            if (this.array[i].equals(o)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * @return a list iterator starting at the begin of this list
     */
    public ListIterator<T> listIterator() {
        return null;
    }

    /**
     * @param index the index to start iterating
     * @return a list iterator starting at the given index
     */
    public ListIterator<T> listIterator(final int index) {
        return null;
    }

    /**
     * @param fromIndex the index of the first element to include
     * @param toIndex the index of the last element to include
     * @return a list with part of the elements of this list
     */
    public List<T> subList(final int fromIndex, final int toIndex) {
        return null;
    }

    /**
     * set an object of an immutable list is not allowed.
     *
     * @param index the position where to set the element
     * @param element the object to set
     * @return no return this will throw a ImmutableCollectionException
     */
    public T set(final int index, final T element) {
        throw new ImmutableCollectionException();
    }

    /**
     * adding an object to an immutable list is not allowed.
     *
     * @param index the position where to add the element
     * @param element the object to add
     */
    public void add(final int index, final Object element) {
        throw new ImmutableCollectionException();
    }

    /**
     * set an object of an immutable list is not allowed.
     *
     * @param index the position where to add the elements
     * @param c the collection with elements to add
     * @return no return this will throw a ImmutableCollectionException
     */
    @SuppressWarnings("rawtypes")
    public boolean addAll(final int index, final Collection c) {
        throw new ImmutableCollectionException();
    }

    /**
     * remove an object of an immutable list is not allowed.
     *
     * @param index the position where to remove the element
     * @return no return this will throw a ImmutableCollectionException
     */
    public T remove(final int index) {
        throw new ImmutableCollectionException();
    }

    /**
     * implementation of equals method.
     *
     * @param o the object to compare
     *
     * @return if the collection equals or not.
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Collection<?>)) {
            return false;
        }
        Collection<?> col = (Collection<?>) o;
        if (this.size() != col.size()) {
            return false;
        }
        boolean equals = true;
        final Iterator<?> i1 = this.iterator();
        final Iterator<?> i2 = col.iterator();
        Object o1, o2;
        while (i1.hasNext()) {
            o1 = i1.next();
            o2 = i2.next();
            if (!o1.equals(o2)) {
                equals = false;
                break;
            }
        }
        return equals;
    }

    /**
     * implementation of hashCode().
     *
     * @return the hash code.
     */
    public int hashCode() {
        return super.hashCode();
    }
}
