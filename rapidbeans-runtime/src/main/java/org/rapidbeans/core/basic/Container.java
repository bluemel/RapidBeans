/*
 * Rapid Beans Framework: Container.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/31/2006
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

import java.util.Collection;
import java.util.List;

import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.datasource.query.Query;

/**
 * A very general interface for common properties of a document and a database.
 * 
 * @author Martin Bluemel
 */
public interface Container {

	/**
	 * @return the document's name
	 */
	String getName();

	/**
	 * @return the document's configuration name
	 */
	String getConfigName();

	/**
	 * @return the document's configuration name or name in special cases.
	 */
	String getConfigNameOrName();

	/**
	 * @return if the container may not be written
	 */
	boolean getReadonly();

	/**
	 * @param readonly the flag to lock the container against being written
	 */
	public void setReadonly(final boolean readonly);

	// creation and destruction of beans

	/**
	 * insert (create) a new bean in the DB.
	 * 
	 * @param bean the bean to insert
	 */
	void insert(RapidBean bean);

	/**
	 * delete a bean from the DB.
	 * 
	 * @param bean the bean to insert
	 */
	void delete(RapidBean bean);

	// finders (queries)

	/**
	 * general query for existence of a bean by type and ID.
	 * 
	 * @param typename the bean's type
	 * @param id       the bean's ID
	 * 
	 * @return true if found and false if not found
	 */
	boolean contains(String typename, String id);

	/**
	 * general query for existence of a bean by type and ID.
	 * 
	 * @param bean the bean
	 * 
	 * @return true if found and false if not found
	 */
	boolean contains(final RapidBean bean);

	/**
	 * general query for a bean by type and ID.
	 * 
	 * @param typename the bean's type
	 * @param id       the bean's ID
	 * 
	 * @return the bean's reference or null if not found
	 */
	RapidBean findBean(String typename, String id);

	/**
	 * find types of all beans stored in this DB.
	 * 
	 * @return a list of strings with the typenames
	 */
	Collection<String> findAllTypenames();

	/**
	 * query for all beans of a type.
	 * 
	 * @param typename the name of the bean type for which you want to find
	 *                 instances.
	 * 
	 * @return a list with all found beans
	 */
	List<RapidBean> findBeansByType(String typename);

	/**
	 * find a set of beans by query.
	 * 
	 * @param query the query string.
	 * 
	 * @return a list with all found beans
	 */
	List<RapidBean> findBeansByQuery(String query);

	/**
	 * find a set of beans by query.
	 * 
	 * @param query the query.
	 * 
	 * @return a list with all found beans
	 */
	List<RapidBean> findBeansByQuery(Query query);

	/**
	 * find a single bean by query.
	 * 
	 * @param query the query string.
	 * 
	 * @return the bean found or null
	 */
	RapidBean findBeanByQuery(String query);

	/**
	 * find a single bean by query.
	 * 
	 * @param query the query.
	 * 
	 * @return the bean found or null
	 */
	RapidBean findBeanByQuery(Query query);

	// fire change events

	/**
	 * fire the bean pre add event.
	 * 
	 * @param bean the bean that was added (has become element of this document).
	 */
	void fireBeanAddPre(final RapidBean bean);

	/**
	 * fire the bean added event.
	 * 
	 * @param bean the bean that was added (has become element of this document).
	 */
	void fireBeanAdded(final RapidBean bean);

	/**
	 * fire the bean pre remove event.
	 * 
	 * @param bean the bean that is going to be removed (has become element of this
	 *             document).
	 */
	void fireBeanRemovePre(final RapidBean bean);

	/**
	 * fire the bean removed event.
	 * 
	 * @param bean the bean that was removed (has become element of this document).
	 */
	void fireBeanRemoved(final RapidBean bean);

	/**
	 * fire the bean change pre event.
	 * 
	 * @param propEvent the property change event
	 */
	void fireBeanChangePre(final PropertyChangeEvent propEvent);

	/**
	 * fire the bean changed event.
	 * 
	 * @param propEvent the property change event
	 */
	void fireBeanChanged(final PropertyChangeEvent propEvent);
}
