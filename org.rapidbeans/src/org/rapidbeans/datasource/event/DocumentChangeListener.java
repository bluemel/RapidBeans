/*
 * Rapid Beans Framework: DocumentChangeListener.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 08/12/2006
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

package org.rapidbeans.datasource.event;

/**
 * The document change listener interface.
 *
 * @author Martin Bluemel
 */
public interface DocumentChangeListener {

    /**
     * Signals that a bean is going to be added to a document.
     *
     * @param e the event
     */
    void beanAddPre(AddedEvent e);

    /**
     * Signals that a bean has been added to a document
     *
     * @param e the event
     */
    void beanAdded(AddedEvent e);

    /**
     * Signals that a bean is going to be removed from a document.
     *
     * @param e the event
     */
    void beanRemovePre(RemovedEvent e);

    /**
     * Signals that a bean has been removed from a document
     *
     * @param e the event
     */
    void beanRemoved(RemovedEvent e);

    /**
     * Signals that a bean in a document is going to be changed.
     *
     * @param e the event
     */
    void beanChangePre(ChangedEvent e);

    /**
     * Signals that a bean in a document has been changed.
     *
     * @param e the event
     */
    void beanChanged(ChangedEvent e);

    /**
     * Signals that a document has been saved.
     *
     * @param e the event
     */
    void documentSaved();
}
