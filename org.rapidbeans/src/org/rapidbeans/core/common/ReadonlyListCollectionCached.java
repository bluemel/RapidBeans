/*
 * Rapid Beans Framework: ReadonlyListCollectionCached.java
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
import java.util.List;
import java.util.TreeSet;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.event.PropertyChangeListener;
import org.rapidbeans.core.type.TypePropertyCollection;


/**
 * Encapsulates any collection to make it
 * 1) immutable (read only). Hence if you try to use a changing
 *    method like add you get a RuntimeException
 * 2) a List. If the collection is a List the reading List methods
 *    use the List directly.
 * 3) If the collection is not a List the reading List methods work
 *    on an array (internally made out of it)
 * in addition caches of the array list or array if they are used
 * The object listens to any changes of this property and invalidates
 * the caches if the collection has changed.
 *
 * Please note that you must release this collection explicitly in order
 * to make it possible that they are garbage collected.
 *
 * @author Martin Bluemel
 */
@SuppressWarnings("unchecked")
public class ReadonlyListCollectionCached<T> extends ReadonlyListCollection<T>
    implements PropertyChangeListener {

    /**
     * cached array.
     */
    private Object[] arrayCache = null;

    /**
     * cached array list.
     */
    private ArrayList<T> arrayListCache = null;

    /**
     * the observed property.
     */
    private PropertyCollection property = null;

    /**
     * if the property is sorted.
     */
    private boolean propertyIsSorted = false;

    /**
     * @param prop the property
     * @param col the collection to encapsulate.
     */
    public ReadonlyListCollectionCached(final PropertyCollection prop, final Collection<T> col) {
        super(col, prop.getType());
        if (col instanceof ReadonlyListCollectionCached) {
            this.setCollection(((ReadonlyListCollectionCached<T>) col).getCollection());
        }
        this.property = prop;
        Class<?> colClass = ((TypePropertyCollection) prop.getType()).getCollectionClass();
        if (colClass != null) {
            if (colClass == TreeSet.class) {
                this.propertyIsSorted = true;
            }
        }
        this.property.getBean().addPropertyChangeListener(this);
    }

    /**
     * It's important to release the object in order
     * to dispose it for garbage collection.
     */
    public void release() {
        this.property.getBean().removePropertyChangeListener(this);
    }

    /**
     * returns the collection converted to an array.
     * Since an array is immutable this is not a problem.
     * All users of to array must be aware that they will
     * get a snapshot of the collection's current state.
     *
     * @return the collection converted to an array
     */
    public Object[] toArray() {
        if (this.arrayCache == null) {
            this.arrayCache = this.getCollection().toArray();
        }
        return this.arrayCache;
    }

    /**
     * returns the collection converted to an array list.
     * Since an arry list is not immutable this method is private.
     * @return the collection converted to an array
     */
    private ArrayList<T> toArrayList() {
        if (this.arrayListCache == null) {
            this.arrayListCache = new ArrayList<T>(this.getCollection());
        }
        return this.arrayListCache;
    }

    /**
     * returns the element at the specified position in the list.
     * Caution: Collections that are no Lists will be converted
     *          to arrays which will cause performance problems in case
     *          a big collections.
     * @param index the position
     * @return the element at the specified position in the list.
     */
    public T get(final int index) {
        if (this.getCollection() instanceof List) {
            return ((List<T>) this.getCollection()).get(index);
        } else {
            return (T) this.toArrayList().get(index);
        }
    }

    /**
     * returns the last index of the element with the given reference.
     * @param o the object reference tha specifies the element to search for
     * @return the last index of the element with the given reference or -1 if not found
     */
    public int lastIndexOf(final Object o) {
        if (this.getCollection() instanceof List) {
            return ((List<Integer>) this.getCollection()).lastIndexOf(o);
        } else {
            return this.toArrayList().lastIndexOf(o);
        }
    }

    /**
     * The before property change event handle method to implement
     * by every listener.
     *
     * @param e the property change event
     */
    public void propertyChangePre(final PropertyChangeEvent e) {
        // do nothing
    }

    /**
     * Implementation of the PropertyChangeListener interface.
     *
     * @param e the event
     */
    public void propertyChanged(final PropertyChangeEvent e) {
        // invalidate the caches
        if (e.getProperty() == this.property) {
            switch (e.getType()) {
            case addlink:
                this.arrayCache = null;
                if (this.arrayListCache != null) {
                    if (e.getLink() != null && (this.propertyIsSorted)) {
                        this.arrayListCache.add((T) e.getLink());
                    } else {
                        this.arrayListCache = null;
                    }
                }
                break;
            case removelink:
                this.arrayCache = null;
                if (this.arrayListCache != null && (!this.propertyIsSorted)) {
                    if (e.getLink() != null) {
                        this.arrayListCache.remove(e.getLink());
                    } else {
                        this.arrayListCache = null;
                    }
                }
                break;
            default:
                this.arrayCache = null;
                this.arrayListCache = null;
                break;
            }
        }
    }
}
