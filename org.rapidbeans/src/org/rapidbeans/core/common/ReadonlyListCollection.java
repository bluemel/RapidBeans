/*
 * Rapid Beans Framework: ReadonlyListCollection.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.rapidbeans.core.basic.BeanSorter;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.exception.ImmutableCollectionException;
import org.rapidbeans.core.type.TypeProperty;

/**
 * Encapsulates any collection to make it
 * 1) immutable (read only). Hence if you try to use a changing
 *    method like add you get a RuntimeException
 * 2) a List. If the collection is a List the reading List methods
 *    use the List directly.
 * 3) If the collection is not a List the reading List methods work
 *    on an array (internally made out of it)
 *
 * @author Martin Bluemel
 */
public class ReadonlyListCollection<T> implements List<T> {

    /**
     * the encapsulated collection.
     */
    private Collection<T> collection;

    private TypeProperty proptype = null;

    /**
     * @return the collection
     */
    protected Collection<T> getCollection() {
        return collection;
    }

    /**
     * setter.
     *
     * @param col the new collection
     */
    protected void setCollection(final Collection<T> col) {
        this.collection = col;
    }

    /**
     * @param col the collection to encapsulate.
     */
	public ReadonlyListCollection(final Collection<T> col,
	        final TypeProperty propertyType) {
        this.proptype = propertyType;
        if (col instanceof ReadonlyListCollection) {
            this.collection = ((ReadonlyListCollection<T>) col).collection;
        } else {
            this.collection = col;
        }
    }

    /**
     * @return the size of the collection
     */
    public int size() {
        return this.collection.size();
    }

    /**
     * @return if the collection is empty
     */
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    /**
     * @param o the object to query
     * @return if the collection contains a given instance.
     */
    public boolean contains(final Object o) {
        boolean contains;
        final TypeProperty[] propsbefore = BeanSorter.get();
        try {
            PropertyCollection.prepareSorting(this.proptype);
            contains = this.collection.contains(o);
        } finally {
            PropertyCollection.cleanupSorting(this.proptype, propsbefore);
        }
        return contains;
    }

    /**
     * @return a read only form of the Iterator.
     */
	public ReadonlyIteratorCollection<T> iterator() {
        return new ReadonlyIteratorCollection<T>(this.collection.iterator());
    }

    /**
     * returns the collection converted to an array of Objects.
     * Since an array is immutable this is not a problem.
     *
     * @return the collection converted to an array of Objects
     */
    public Object[] toArray() {
        Object[] array;
        final TypeProperty[] propsbefore = BeanSorter.get();
        try {
            PropertyCollection.prepareSorting(this.proptype);
            array = this.collection.toArray();
        } finally {
            PropertyCollection.cleanupSorting(this.proptype, propsbefore);
        }
        return array; 
    }

    /**
     * returns the collection converted to a typed array.
     * Since an array is immutable this is not a problem.
     *
     * @param a the pattern array
     *
     * @return the collection converted to an array
     *
     * @throws ArrayStoreException in case the Object[] given
     *         has an incorrect type
     */
    @SuppressWarnings("unchecked")
	public T[] toArray(final Object[] a) {
        T[] array;
        final TypeProperty[] propsbefore = BeanSorter.get();
        try {
            PropertyCollection.prepareSorting(this.proptype);
            array = (T[]) this.collection.toArray(a);
        } finally {
            PropertyCollection.cleanupSorting(this.proptype, propsbefore);
        }
        return array;
    }

    /**
     * adding an object to an immutable collection is not allowed.
     *
     * @param o the object to add
     *
     * @return no return this will throw a ImmutableCollectionException
     */
    public boolean add(final Object o) {
        throw new ImmutableCollectionException();
    }

    /**
     * removing an object of an immutable collection is not allowed.
     *
     * @param o the object to remove
     *
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
        boolean contains;
        final TypeProperty[] propsbefore = BeanSorter.get();
        try {
            PropertyCollection.prepareSorting(this.proptype);
            contains = this.collection.containsAll(c);
        } finally {
            PropertyCollection.cleanupSorting(this.proptype, propsbefore);
        }
        return contains;
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
     * returns the element at the specified position in the list.
     * Caution: Collections that are no Lists will be converted
     *          to arrays which will cause performance problems in case
     *          a big collections.
     * @param index the position
     * @return the element at the specified position in the list.
     */
    @SuppressWarnings("unchecked")
    public T get(final int index) {
        if (this.collection instanceof List) {
            return ((List<T>) this.collection).get(index);
        } else {
            return (T) this.toArray()[index];
        }
    }

    /**
     * returns the first index of the element with the given reference.
     * @param o the object reference tha specifies the element to search for
     * @return the last index of the element with the given reference or -1 if not found
     */
    public int indexOf(final Object o) {
        if (this.collection instanceof List) {
            return ((List<?>) this.collection).indexOf(o);
        } else {
            int i = 0;
            for (Object o1 : this.collection) {
                if (o1.equals(o)) {
                    return i;
                }
                i++;
            }
            return -1;
        }
    }

    /**
     * returns the last index of the element with the given reference.
     * @param o the object reference that specifies the element to search for
     * @return the last index of the element with the given reference or -1 if not found
     */
    public int lastIndexOf(final Object o) {
        if (this.collection instanceof List) {
            return ((List<?>) this.collection).lastIndexOf(o);
        } else {
            Object[] oa = this.toArray();
            for (int i = oa.length - 1; i >= 0; i--) {
                if (oa[i].equals(o)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * @return a list iterator starting at the begin of this list
     */
    public ListIterator<T> listIterator() {
        if (this.collection instanceof List<?>) {
            return new ReadonlyIteratorCollection<T>(((List<T>) this.collection).listIterator());
        } else {
            return  new ReadonlyIteratorCollection<T>(new ArrayList<T>(this.collection).listIterator());
        }
    }

    /**
     * @param index the index to start iterating
     *
     * @return a list iterator starting at the given index
     */
    public ListIterator<T> listIterator(final int index) {
        if (this.collection instanceof List<?>) {
            return new ReadonlyIteratorCollection<T>(((List<T>) this.collection).listIterator(index));
        } else {
            return new ReadonlyIteratorCollection<T>(this.toArrayList().listIterator(index));
        }
    }

    /**
     * @param fromIndex the index of the first element to include
     * @param toIndex the index of the last element to include
     * @return a list with part of the elements of this list
     */
    public List<T> subList(final int fromIndex, final int toIndex) {
        if (this.collection instanceof List<?>) {
            return new ReadonlyListCollection<T>(((List<T>) this.collection).subList(fromIndex, toIndex),
                    this.proptype);
        } else {
            return new ReadonlyListCollection<T>(this.toArrayList().subList(fromIndex, toIndex),
                    this.proptype);
        }
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
        final Collection<?> col = (Collection<?>) o;
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

    /**
     * checks for identity (same instance) with another collection.
     *
     * @param col the collection to check
     *
     * @return if identical or not
     */
    public boolean isSameCollection(final Collection<?> col) {
        return this.collection == col;
    }

    /**
     * returns the collection converted to an array list.
     * Since an array list is not immutable this method is private.
     *
     * @return the collection converted to an array
     */
    private ArrayList<T> toArrayList() {
        return new ArrayList<T>(this.collection);
    }
}
